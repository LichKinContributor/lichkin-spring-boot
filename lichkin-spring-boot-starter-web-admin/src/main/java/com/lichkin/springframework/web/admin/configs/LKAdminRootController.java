package com.lichkin.springframework.web.admin.configs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lichkin.springframework.controllers.LKPagesController;
import com.lichkin.springframework.web.LKSession;
import com.lichkin.springframework.web.beans.LKPage;

@Controller
@RequestMapping("/admin")
public class LKAdminRootController extends LKPagesController {

	@GetMapping(value = "/index" + MAPPING)
	public LKPage toIndex() {
		return null;
	}


	@GetMapping(value = "/home" + MAPPING)
	public LKPage toHome() {
		return null;
	}


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
