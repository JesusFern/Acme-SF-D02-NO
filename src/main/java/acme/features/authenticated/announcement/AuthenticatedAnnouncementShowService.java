/*
 * AuthenticatedAnnouncementShowService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.announcement;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.announcements.Announcement;

@Service
public class AuthenticatedAnnouncementShowService extends AbstractService<Authenticated, Announcement> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedAnnouncementRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		Announcement announcement;
		Date deadline;

		id = super.getRequest().getData("id", int.class);
		announcement = this.repository.findOneAnnouncementById(id);
		deadline = MomentHelper.deltaFromCurrentMoment(-30, ChronoUnit.DAYS);
		status = MomentHelper.isAfter(announcement.getMoment(), deadline);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Announcement object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneAnnouncementById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Announcement object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "title", "moment", "status", "text", "moreInfo");

		super.getResponse().addData(dataset);
	}

}
