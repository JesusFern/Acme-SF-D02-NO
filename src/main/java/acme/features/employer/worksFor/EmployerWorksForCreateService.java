/*
 * EmployerWorksForCreateService.java
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

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.companies.Company;
import acme.entities.companies.WorksFor;
import acme.roles.Employer;

@Service
public class EmployerWorksForCreateService extends AbstractService<Employer, WorksFor> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private EmployerWorksForRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int proxyId;
		Employer proxy;
		WorksFor object;

		proxyId = super.getRequest().getPrincipal().getActiveRoleId();
		proxy = this.repository.findOneProxyById(proxyId);

		object = new WorksFor();
		object.setRoles("");
		object.setContractor(null);
		object.setProxy(proxy);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final WorksFor object) {
		assert object != null;

		int proxyId, contractorId;
		Employer proxy;
		Company contractor;

		super.bind(object, "roles");

		proxyId = super.getRequest().getPrincipal().getActiveRoleId();
		proxy = this.repository.findOneProxyById(proxyId);
		object.setProxy(proxy);

		contractorId = super.getRequest().getData("contractor", int.class);
		contractor = this.repository.findOneContractorById(contractorId);
		object.setContractor(contractor);
	}

	@Override
	public void validate(final WorksFor object) {
		assert object != null;
	}

	@Override
	public void perform(final WorksFor object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final WorksFor object) {
		assert object != null;

		int proxyId;
		Collection<Company> companies;
		SelectChoices choices;
		Dataset dataset;

		proxyId = object.getProxy().getId();
		companies = this.repository.findManyAvailableContractorsByProxyId(proxyId);
		choices = SelectChoices.from(companies, "name", object.getContractor());

		dataset = super.unbind(object, "roles");
		dataset.put("contractor", choices.getSelected().getKey());
		dataset.put("contractors", choices);

		super.getResponse().addData(dataset);
	}

}
