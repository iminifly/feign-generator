package com.github.iminifly.cloud.feign.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * IndexController
 *
 * @author xgf
 * @date 2020/12/4 9:23
 */
@Controller
@RequestMapping("/")
public class IndexController {

	@RequestMapping(value = { "", "/index" }, method = RequestMethod.GET)
	public String index() {
		return "index";
	}
}
