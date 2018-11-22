package com.lichkin.springframework.controllers;

import static com.lichkin.framework.defines.LKSessionStatics.KEY_COMP;
import static com.lichkin.framework.defines.LKSessionStatics.KEY_DEPT;
import static com.lichkin.framework.defines.LKSessionStatics.KEY_LOGIN;
import static com.lichkin.framework.defines.LKSessionStatics.KEY_MENUS;
import static com.lichkin.framework.defines.LKSessionStatics.KEY_ROLES;
import static com.lichkin.framework.defines.LKSessionStatics.KEY_USER;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lichkin.framework.web.annotations.WithoutLogin;
import com.lichkin.springframework.web.beans.LKPage;

@Controller
@RequestMapping("/")
public class LKRootController extends LKPagesController {

	@WithoutLogin
	@GetMapping(value = "/index" + MAPPING)
	public LKPage toIndex() {
		return null;
	}


	@WithoutLogin
	@GetMapping(value = "/logout" + MAPPING)
	public LKPage toLogout() {
		session.setAttribute(KEY_LOGIN, null);
		session.setAttribute(KEY_MENUS, null);
		session.setAttribute(KEY_ROLES, null);
		session.setAttribute(KEY_USER, null);
		session.setAttribute(KEY_COMP, null);
		session.setAttribute(KEY_DEPT, null);
		session.invalidate();
		return null;
	}

}
