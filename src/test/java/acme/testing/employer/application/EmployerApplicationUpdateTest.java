/*
 * EmployerApplicationUpdateTest.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.testing.employer.application;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.jobs.Application;
import acme.entities.jobs.ApplicationStatus;
import acme.testing.TestHarness;

public class EmployerApplicationUpdateTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	private EmployerApplicationTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/employer/application/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String reference, final String moment, final String title, final String statement, final String skills, final String qualifications, final String status) {
		// HINT: this test authenticates as an employer, lists his or her applications,
		// HINT+ changes their status and checks that it is been updated.

		super.signIn("employer1", "employer1");

		super.clickOnMenu("Employer", "List my applications");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(recordIndex, 0, reference);

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.fillInputBoxIn("status", status);
		super.clickOnSubmit("Update");

		super.clickOnMenu("Employer", "List my applications");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, reference);
		super.checkColumnHasValue(recordIndex, 1, status);
		super.checkColumnHasValue(recordIndex, 2, title);

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("reference", reference);
		super.checkInputBoxHasValue("moment", moment);
		super.checkInputBoxHasValue("statement", statement);
		super.checkInputBoxHasValue("skills", skills);
		super.checkInputBoxHasValue("qualifications", qualifications);
		super.checkInputBoxHasValue("status", status);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: updating an application means changing its status using a
		// HINT+ select input box.  There is no means to enter invalid data here.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to update an application that is managed
		// HINT+ by a different employer.

		Collection<Application> applications;
		String param;

		applications = this.repository.findManyApplicationsByEmployerUsername("employer1");
		for (final Application application : applications) {
			param = String.format("id=%d", application.getId());
			super.request("/employer/application/update", param);
			super.checkPanicExists();
		}
	}

	@Test
	public void test301Hacking() {
		// HINT: this test tries to update an application that was previously
		// HINT+ accepted or rejected.

		Collection<Application> applications;
		String param;

		super.signIn("employer1", "employer1");
		applications = this.repository.findManyApplicationsByEmployerUsername("employer1");
		for (final Application application : applications)
			if (!application.getStatus().equals(ApplicationStatus.PENDING)) {
				param = String.format("id=%d", application.getId());
				super.request("/employer/application/update", param);
				super.checkPanicExists();
			}
		super.signOut();
	}

}
