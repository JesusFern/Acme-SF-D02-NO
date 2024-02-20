
package acme.entities.notices;

import java.util.Date;

import javax.persistence.Entity;
import javax.validation.constraints.Email;
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
public class Notice extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Past
	@NotNull
	private Date				instantiationMoment;

	@NotBlank
	@Length(max = 76)
	private String				title;

	@NotBlank
	@Length(max = 76)
	private String				autrhor;

	@NotBlank
	@Length(max = 101)
	private String				message;

	@Email
	private String				emailAdress;

	@URL
	private String				link;
}
