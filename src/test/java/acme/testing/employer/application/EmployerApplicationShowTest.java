/*
 * EmployerApplicationShowTest.java
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
import acme.testing.TestHarness;

public class EmployerApplicationShowTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	private EmployerApplicationTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/employer/application/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String reference, final String moment, final String title, final String statement, final String skills, final String qualifications, final String status) {
		// HINT: this test checks that an employer can list and display his or
		// HINT+ her applications.

		super.signIn("employer1", "employer1");

		super.clickOnMenu("Employer", "List my applications");
		super.checkListingExists();
		super.sortListing(0, "asc");
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
		// HINT: this is a listing, which implies that no data must be entered in any forms.
		// HINT+ Then, there are not any negative test cases for this feature.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to show applications that the principal cannot show.

		Collection<Application> applications;
		String param;

		applications = this.repository.findManyApplicationsByEmployerUsername("employer1");
		for (final Application application : applications) {
			param = String.format("id=%d", application.getId());

			super.checkLinkExists("Sign in");
			super.request("/employer/application/show", param);
			super.checkPanicExists();

			super.signIn("employer2", "employer2");
			super.request("/employer/application/show", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("worker2", "worker2");
			super.request("/employer/application/show", param);
			super.checkPanicExists();
			super.signOut();
		}
	}

}
