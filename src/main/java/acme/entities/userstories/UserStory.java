
package acme.entities.userstories;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.entities.projects.Project;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserStory extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotNull
	@NotBlank
	@Length(max = 76)
	private String				title;

	@NotNull
	@NotBlank
	@Length(max = 101)
	private String				description;

	@NotNull
	@NotBlank
	@Length(max = 101)
	private String				acceptanceCriteria;

	@NotNull
	@Min(0)
	private Integer				cost;

	@NotNull
	private Priority			priority;

	@URL
	private String				link;

	@NotNull
	@Valid
	@ManyToOne
	private Project				project;
}
