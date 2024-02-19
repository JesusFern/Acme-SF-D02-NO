/*
 * Job.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.entities.jobs;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.client.data.datatypes.Money;
import acme.client.helpers.MomentHelper;
import acme.entities.companies.Company;
import acme.roles.Employer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "draftMode, deadline"), //
	@Index(columnList = "employer_id, id")
})
public class Job extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Column(unique = true)
	@NotBlank
	@Length(min = 5, max = 15)
	@Pattern(regexp = "^[\\w\\-]+$", message = "{validation.job.reference}")
	private String				reference;

	@NotBlank
	@Length(max = 100)
	private String				title;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				deadline;

	@NotNull
	@Valid
	private Money				salary;

	@Range(min = 0, max = 100)
	@Digits(integer = 3, fraction = 2)
	private double				score;

	@NotBlank
	@Length(max = 255)
	private String				description;

	@URL
	@Length(max = 255)
	private String				moreInfo;

	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------


	@Transient
	public boolean isAvailable() {
		boolean result;

		result = !this.draftMode && MomentHelper.isFuture(this.deadline);

		return result;
	}

	// Relationships ----------------------------------------------------------


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Employer	employer;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Company		contractor;

}
