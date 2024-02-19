/*
 * AdministratorCompanyListService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.administrator.company;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.companies.Company;

@Service
public class AdministratorCompanyListService extends AbstractService<Administrator, Company> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorCompanyRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Company> objects;

		objects = this.repository.findAllCompanies();

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Company object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "name", "description", "moreInfo");

		super.getResponse().addData(dataset);
	}

}
