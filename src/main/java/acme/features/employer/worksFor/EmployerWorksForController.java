/*
 * EmployerWorksForController.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.employer.worksFor;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.companies.WorksFor;
import acme.roles.Employer;

@Controller
public class EmployerWorksForController extends AbstractController<Employer, WorksFor> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private EmployerWorksForListService		listService;

	@Autowired
	private EmployerWorksForShowService		showService;

	@Autowired
	private EmployerWorksForCreateService	createService;

	@Autowired
	private EmployerWorksForUpdateService	updateService;

	@Autowired
	private EmployerWorksForDeleteService	deleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
	}

}
