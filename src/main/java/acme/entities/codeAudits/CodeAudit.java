
package acme.entities.codeAudits;

import java.util.Date;

import javax.persistence.Entity;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CodeAudit extends AbstractEntity {
	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Unique
	@Pattern(regexp = "[A-Z]{1,3}-[0-9]{3}")
	@NotNull
	private String				code;

	@NotNull
	@Past
	private Date				execution;

	@NotNull
	private Type				type;

	@NotNull
	@NotBlank
	@Length(max = 101)
	private String				correctiveActions;

	@NotNull
	@Range(min = 0, max = 100)
	@Digits(integer = 3, fraction = 2)
	private Integer				mark;

	@URL
	@Length(max = 255)
	private String				link;

}
