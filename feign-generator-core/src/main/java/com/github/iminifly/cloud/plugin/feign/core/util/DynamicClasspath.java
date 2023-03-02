package com.github.iminifly.cloud.plugin.feign.core.util;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * DynamicClasspath
 *
 * @author XGF
 * @date 2020/11/17 9:22
 */
public class DynamicClasspath {

	private final static DynamicClasspath dynamicClasspath = new DynamicClasspath();

	public static DynamicClasspath getInstance() {
		return dynamicClasspath;
	}

	private final List<URL> urlList = new ArrayList<>();

	private URLClassLoader urlClassLoader;

	private DynamicClasspath() {

	}

	public void loadJar(String jar) throws MalformedURLException {
		this.urlList.add(new URL("file:///" + jar.replace("\\", "/")));
	}

	public void loadLib(String libDir) throws MalformedURLException {

		File lib = new File(libDir);

		File[] files = lib.listFiles();

		if (files == null) {
			throw new RuntimeException("未找到lib目录[" + lib.getName() + "]");
		}

		for (File file : files) {
			if (file.getName().endsWith("jar")) {
				this.urlList.add(file.toURI().toURL());
			}
		}
	}

	public void loadDir(String dir) throws MalformedURLException {
		File programRootDir = new File(dir);
		this.urlList.add(programRootDir.toURI().toURL());
	}

	public synchronized URLClassLoader getClassLoader() {
		if (this.urlClassLoader == null) {
			URL[] urls = new URL[this.urlList.size()];
			for (int i = 0; i < this.urlList.size(); i++) {
				urls[i] = this.urlList.get(i);
			}
			this.urlClassLoader = new URLClassLoader(urls);
		}
		return this.urlClassLoader;
	}

	public Class<?> loadClazz(String className) {
		try {
			return Class.forName(className, false, this.urlClassLoader);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public Class<Annotation> loadAnnotationClazz(String className) {
		try {
			return (Class<Annotation>) Class.forName(className, false, this.urlClassLoader);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
