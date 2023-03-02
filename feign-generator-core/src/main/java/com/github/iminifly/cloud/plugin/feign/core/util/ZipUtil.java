package com.github.iminifly.cloud.plugin.feign.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.Data;

/**
 * ZipUtil
 *
 * @author XGF
 * @date 2020/11/13 21:27
 */
public class ZipUtil {

	private static void zipFile(ZipOutputStream zipOutputStream, File file, String parentFileName) {
		FileInputStream in = null;
		try {
			ZipEntry zipEntry = new ZipEntry(parentFileName);
			zipOutputStream.putNextEntry(zipEntry);
			in = new FileInputStream(file);
			int len;
			byte[] buf = new byte[8 * 1024];
			while ((len = in.read(buf)) != -1) {
				zipOutputStream.write(buf, 0, len);
			}
			zipOutputStream.closeEntry();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				assert in != null;
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void zipText(ZipOutputStream zipOutputStream, String content, String fileName) {
		InputStream in = null;
		try {
			ZipEntry zipEntry = new ZipEntry(fileName);
			zipOutputStream.putNextEntry(zipEntry);
			in = new ByteArrayInputStream(content.getBytes("UTF-8"));
			int len;
			byte[] buf = new byte[8 * 1024];
			while ((len = in.read(buf)) != -1) {
				zipOutputStream.write(buf, 0, len);
			}
			zipOutputStream.closeEntry();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				assert in != null;
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 递归压缩目录结构
	 */
	private static void directory(ZipOutputStream zipOutputStream, File file, String parentFileName) {
		File[] files = file.listFiles();
		String parentFileNameTemp;
		assert files != null;
		for (File fileTemp : files) {
			parentFileNameTemp = parentFileName == null || parentFileName.length() == 0 ? fileTemp.getName()
					: parentFileName + "/" + fileTemp.getName();
			if (fileTemp.isDirectory()) {
				directory(zipOutputStream, fileTemp, parentFileNameTemp);
			} else {
				zipFile(zipOutputStream, fileTemp, parentFileNameTemp);
			}
		}
	}

	/**
	 * 压缩文件目录
	 *
	 * @param source 源文件目录（单个文件和多层目录）
	 * @param dst    目标目录
	 */
	public static void zipFiles(String source, String dst) {
		File file = new File(source);
		ZipOutputStream zipOutputStream = null;
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(dst);
			zipOutputStream = new ZipOutputStream(fileOutputStream);
			if (file.isDirectory()) {
				directory(zipOutputStream, file, "");
			} else {
				zipFile(zipOutputStream, file, "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				assert zipOutputStream != null;
				zipOutputStream.close();
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * zipTexts
	 *
	 * @param textList text集合
	 * @param handler  输出流处理器
	 * @author xuguofeng
	 * @date 2020/11/16 15:58
	 */
	public static void zipTexts(List<Text> textList, OutputStreamHandler handler) {
		ZipOutputStream zipOutputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
			for (Text textFile : textList) {
				zipText(zipOutputStream, textFile.getText(), textFile.getFileName());
			}
			// 输出流处理器
			zipOutputStream.finish();
			zipOutputStream.flush();
			handler.handle(byteArrayOutputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				assert zipOutputStream != null;
				zipOutputStream.close();
				byteArrayOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * zipTexts
	 *
	 * @param textList text集合
	 * @author xuguofeng
	 * @date 2020/11/16 15:58
	 */
	public static byte[] zipTexts(List<Text> textList) {
		ZipOutputStream zipOutputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
			for (Text textFile : textList) {
				zipText(zipOutputStream, textFile.getText(), textFile.getFileName());
			}
			// 输出流处理器
			zipOutputStream.finish();
			zipOutputStream.flush();
			return byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				assert zipOutputStream != null;
				zipOutputStream.close();
				byteArrayOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public interface OutputStreamHandler {

		/**
		 * 输出流处理器
		 *
		 * @param byteArrayOutputStream 输出流
		 * @author xuguofeng
		 * @date 2020/11/16 16:03
		 */
		void handle(ByteArrayOutputStream byteArrayOutputStream);
	}

	/**
	 * 本地文件输出流处理器
	 *
	 * @author xuguofeng
	 * @date 2020/11/16 16:04
	 */
	public static class LocalFileOutputStreamHandler implements OutputStreamHandler {

		private String filePath;

		public LocalFileOutputStreamHandler(String filePath) {
			this.filePath = filePath;
		}

		@Override
		public void handle(ByteArrayOutputStream byteArrayOutputStream) {
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(filePath);
				fileOutputStream.write(byteArrayOutputStream.toByteArray());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					assert fileOutputStream != null;
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Data
	public static class Text {

		private String fileName;
		private String text;
	}
}
