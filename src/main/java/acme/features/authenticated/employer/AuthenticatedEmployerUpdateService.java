/*
 * AuthenticatedEmployerUpdateService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.employer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.roles.Employer;

@Service
public class AuthenticatedEmployerUpdateService extends AbstractService<Authenticated, Employer> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedEmployerRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Employer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Employer object;
		Principal principal;
		int userAccountId;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		object = this.repository.findOneEmployerByUserAccountId(userAccountId);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Employer object) {
		assert object != null;

		super.bind(object, "area", "sector");
	}

	@Override
	public void validate(final Employer object) {
		assert object != null;
	}

	@Override
	public void perform(final Employer object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Employer object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "area", "sector");

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
