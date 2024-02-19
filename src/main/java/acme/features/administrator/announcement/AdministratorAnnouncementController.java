/*
 * AdministratorAnnouncementController.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.administrator.announcement;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.client.data.accounts.Administrator;
import acme.entities.announcements.Announcement;

@Controller
public class AdministratorAnnouncementController extends AbstractController<Administrator, Announcement> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAnnouncementListAllService	listAllService;

	@Autowired
	private AdministratorCompanyListRecentService	listRecentService;

	@Autowired
	private AdministratorAnnouncementShowService	showService;

	@Autowired
	private AdministratorAnnouncementCreateService	createService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addCustomCommand("list-all", "list", this.listAllService);
		super.addCustomCommand("list-recent", "list", this.listRecentService);
	}

}
