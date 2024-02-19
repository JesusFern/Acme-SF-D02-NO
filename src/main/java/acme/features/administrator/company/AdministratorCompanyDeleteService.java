/*
 * AdministratorCompanyDeleteService.java
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.companies.Company;

@Service
public class AdministratorCompanyDeleteService extends AbstractService<Administrator, Company> {

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
		Company object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCompanyById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Company object) {
		assert object != null;

		super.bind(object, "name", "description", "moreInfo");
	}

	@Override
	public void validate(final Company object) {
		assert object != null;

		boolean status;
		int id, numberProxies, numberJobs;

		id = super.getRequest().getData("id", int.class);
		numberProxies = this.repository.findNumberProxiesByContractorId(id);
		numberJobs = this.repository.findNumberJobsByContractorId(id);

		status = numberProxies == 0 && numberJobs == 0;

		super.state(status, "*", "administrator.company.delete.company-linked");
	}

	@Override
	public void perform(final Company object) {
		assert object != null;

		this.repository.delete(object);
	}

	@Override
	public void unbind(final Company object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "name", "description", "moreInfo");

		super.getResponse().addData(dataset);
	}

}
