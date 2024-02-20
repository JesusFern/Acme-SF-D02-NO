
package acme.entities.risks;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Risk extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Column(unique = true)
	@Pattern(regexp = "R-[0-9]{3}", message = "{validation.risk.reference}")
	@NotBlank
	@NotNull
	private String				reference;

	@NotNull
	@Past
	private Date				identificationDate;

	@NotNull
	@Min(0)
	private Double				impact;

	@NotNull
	@Min(0)
	@Max(100)
	private Double				probability;

	@NotNull
	//Falta especificar como se calcula
	private Double				value;

	@NotNull
	@NotBlank
	@Length(max = 101)
	private String				description;

	@URL
	private String				link;

}
