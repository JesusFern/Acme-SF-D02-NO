/*
 * EmployerJobShowTest.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.testing.employer.job;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.jobs.Job;
import acme.testing.TestHarness;

public class EmployerJobShowTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	private EmployerJobTestRepository repository;

	// Test data --------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/employer/job/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String reference, final String contractor, final String title, final String deadline, final String salary, final String score, final String moreInfo, final String description) {
		// HINT: this test signs in as an employer, lists all of the jobs, click on  
		// HINT+ one of them, and checks that the form has the expected data.

		super.signIn("employer1", "employer1");

		super.clickOnMenu("Employer", "List all jobs");
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("reference", reference);
		super.checkInputBoxHasValue("contractor", contractor);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("deadline", deadline);
		super.checkInputBoxHasValue("salary", salary);
		super.checkInputBoxHasValue("score", score);
		super.checkInputBoxHasValue("moreInfo", moreInfo);
		super.checkInputBoxHasValue("description", description);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there are not any negative tests for this feature because it is a listing
		// HINT+ that does not involve entering any data in any forms.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to show an unpublished job by someone who is not the principal.

		Collection<Job> jobs;
		String param;

		jobs = this.repository.findManyJobsByEmployerUsername("employer1");
		for (final Job job : jobs)
			if (job.isDraftMode()) {
				param = String.format("id=%d", job.getId());

				super.checkLinkExists("Sign in");
				super.request("/employer/job/show", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/employer/job/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("employer2", "employer2");
				super.request("/employer/job/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("worker1", "worker1");
				super.request("/employer/job/show", param);
				super.checkPanicExists();
				super.signOut();
			}
	}

}
