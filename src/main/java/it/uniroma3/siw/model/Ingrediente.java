package it.uniroma3.siw.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;

@Entity
public class Ingrediente {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String nome;
	
	@NotBlank
	private String origine;
	
	private String description;
	
	@ManyToMany(mappedBy = "ingredienti")
	private Set<Piatto> piatti = new HashSet<Piatto>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getOrigine() {
		return origine;
	}

	public void setOrigine(String origine) {
		this.origine = origine;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Piatto> getPiatti() {
		return piatti;
	}

	public void setPiatti(Set<Piatto> piatti) {
		this.piatti = piatti;
	}
	
	public void removeFromPiatti() {
		for (Piatto piatto : piatti) {
			piatto.getIngredienti().remove(this);
		}
	}
}
