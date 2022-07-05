package it.uniroma3.siw.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

@Entity
public class Buffet {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String nome;
	
	private String description;

	@ManyToOne
	private Chef chef;
	
	//@Size(min = 1)
	@ManyToMany
	@Column(unique = true)
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Chef getChef() {
		return chef;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public Set<Piatto> getPiatti() {
		return piatti;
	}

	public void setPiatti(Set<Piatto> piatti) {
		this.piatti = piatti;
	}
	
	public void addPiatto(Piatto piatto) {
		this.piatti.add(piatto);
		piatto.getBuffets().add(this);
	}
	
	public void removePiatto(Piatto piatto) {
		this.piatti.remove(piatto);
		piatto.getBuffets().remove(this);
	}
}
