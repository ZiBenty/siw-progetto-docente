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
public class Piatto {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String nome;
	
	private String description;
	
	@ManyToMany(mappedBy = "piatti")
	private Set<Buffet> buffets = new HashSet<Buffet>();
	
	//@Size(min = 1)
	@ManyToMany
	private Set<Ingrediente> ingredienti = new HashSet<Ingrediente>();

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

	public Set<Buffet> getBuffets() {
		return buffets;
	}

	public void setBuffets(Set<Buffet> buffets) {
		this.buffets = buffets;
	}

	public Set<Ingrediente> getIngredienti() {
		return ingredienti;
	}

	public void setIngredienti(Set<Ingrediente> ingredienti) {
		this.ingredienti = ingredienti;
	}
	
	public void removeFromBuffets() {
		for (Buffet buffet : buffets) {
			buffet.getPiatti().remove(this);
		}
	}
	
	public void addIngrediente(Ingrediente ingrediente) {
		this.ingredienti.add(ingrediente);
		ingrediente.getPiatti().add(this);
	}
	
	public void removeIngrediente(Ingrediente ingrediente) {
		this.ingredienti.remove(ingrediente);
		ingrediente.getPiatti().remove(this);
	}
}
