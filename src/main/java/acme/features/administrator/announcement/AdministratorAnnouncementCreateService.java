/*
 * AdministratorAnnouncementCreateService.java
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

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.announcements.Announcement;
import acme.entities.announcements.AnnouncementStatus;

@Service
public class AdministratorAnnouncementCreateService extends AbstractService<Administrator, Announcement> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAnnouncementRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Announcement object;
		Date moment;

		moment = MomentHelper.getCurrentMoment();

		object = new Announcement();
		object.setTitle("");
		object.setMoment(moment);
		object.setStatus(AnnouncementStatus.INFO);
		object.setText("");
		object.setMoreInfo("");

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Announcement object) {
		assert object != null;

		super.bind(object, "title", "status", "text", "moreInfo");
	}

	@Override
	public void validate(final Announcement object) {
		assert object != null;

		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "javax.validation.constraints.AssertTrue.message");
	}

	@Override
	public void perform(final Announcement object) {
		assert object != null;

		Date moment;

		moment = MomentHelper.getCurrentMoment();
		object.setMoment(moment);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Announcement object) {
		assert object != null;

		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(AnnouncementStatus.class, object.getStatus());

		dataset = super.unbind(object, "title", "status", "text", "moreInfo");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("statuses", choices);

		super.getResponse().addData(dataset);
	}

}
