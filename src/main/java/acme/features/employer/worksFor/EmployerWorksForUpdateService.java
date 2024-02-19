/*
 * EmployerWorksForUpdateService.java
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
public class EmployerWorksForUpdateService extends AbstractService<Employer, WorksFor> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private EmployerWorksForRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		WorksFor worksFor;

		id = super.getRequest().getData("id", int.class);
		worksFor = this.repository.findOneWorksForById(id);
		status = worksFor != null && super.getRequest().getPrincipal().hasRole(worksFor.getProxy());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		WorksFor object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneWorksForById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final WorksFor object) {
		assert object != null;

		int contractorId;
		Company contractor;

		contractorId = super.getRequest().getData("contractor", int.class);
		contractor = this.repository.findOneContractorById(contractorId);

		super.bind(object, "roles");
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

		Collection<Company> contractors;
		SelectChoices choices;
		Dataset dataset;

		contractors = this.repository.findAllContractors();
		choices = SelectChoices.from(contractors, "name", object.getContractor());

		dataset = super.unbind(object, "roles");
		dataset.put("contractor", choices.getSelected().getKey());
		dataset.put("contractors", choices);

		super.getResponse().addData(dataset);
	}

}
