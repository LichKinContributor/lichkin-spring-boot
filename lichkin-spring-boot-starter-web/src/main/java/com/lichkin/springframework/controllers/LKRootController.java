package com.lichkin.springframework.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lichkin.framework.web.annotations.WithoutLogin;
import com.lichkin.springframework.web.LKSession;
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
		LKSession.setComp(session, null);
		LKSession.setLogin(session, null);
		LKSession.setMenus(session, null);
		LKSession.setRoles(session, null);
		LKSession.setUser(session, null);
		session.invalidate();
		return null;
	}

}
