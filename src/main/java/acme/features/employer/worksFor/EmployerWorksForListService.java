/*
 * EmployerWorksForListService.java
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
import acme.entities.companies.WorksFor;
import acme.roles.Employer;

@Service
public class EmployerWorksForListService extends AbstractService<Employer, WorksFor> {

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
		Collection<WorksFor> objects;

		proxyId = super.getRequest().getPrincipal().getActiveRoleId();
		objects = this.repository.findManyWorksForByProxyId(proxyId);

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final WorksFor object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "roles");
		dataset.put("contractor", object.getContractor().getName());

		super.getResponse().addData(dataset);
	}

}
