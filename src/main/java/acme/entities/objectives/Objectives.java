
package acme.entities.objectives;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Objectives extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Past
	@NotNull
	@NotBlank
	@Temporal(TemporalType.TIMESTAMP)
	private Date				instantiationMoment;

	@NotBlank
	@NotNull
	@Length(max = 76)
	private String				title;

	@NotBlank
	@NotNull
	@Length(max = 101)
	private String				description;

	@NotNull
	private Priority			priority;

	@NotNull
	private Boolean				status;

	@NotNull

	@Temporal(TemporalType.TIMESTAMP)
	private Date				duration;

	@URL
	@Length(max = 255)
	private String				link;

}
