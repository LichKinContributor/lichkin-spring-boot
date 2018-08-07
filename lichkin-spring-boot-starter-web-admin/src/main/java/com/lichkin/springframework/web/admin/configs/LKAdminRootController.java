package com.lichkin.springframework.web.admin.configs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lichkin.framework.web.annotations.WithoutLogin;
import com.lichkin.springframework.controllers.LKPagesController;
import com.lichkin.springframework.web.beans.LKPage;

@Controller
@RequestMapping("/admin")
public class LKAdminRootController extends LKPagesController {

	@WithoutLogin
	@GetMapping(value = "/index" + MAPPING)
	public LKPage toIndex() {
		return null;
	}


	@GetMapping(value = "/home" + MAPPING)
	public LKPage toHome() {
		return null;
	}

}
