package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.repository.IngredienteRepository;

@Service
public class IngredienteService {
	@Autowired
	private IngredienteRepository ingredienteRepository;
	
	//salva un ingrediente nel repository
	@Transactional
	public void save(Ingrediente ingrediente) {
		ingredienteRepository.save(ingrediente);
	}
	
	//elimina un ingrediente del repository
	//WARNING: elimina anche i buffet a lui collegati
	@Transactional
	public void deleteById(Long id) {
		Ingrediente ingrediente = ingredienteRepository.findById(id).get();
		ingredienteRepository.delete(ingrediente);
	}
	
	//ritorna ingrediente dato id
	public Ingrediente findById(Long id) {
		return ingredienteRepository.findById(id).get();
	}
	
	//ritorna la lista di tutti gli ingredienti
	public List<Ingrediente> findAll(){
		List<Ingrediente> ingredienti = new ArrayList<Ingrediente>();
		for(Ingrediente i:ingredienteRepository.findAll()) {
			ingredienti.add(i);
		}
		return ingredienti;
	}
	
	//ritorna true se esiste un ingrediente con lo stesso nome ed origine in un piatto
	public boolean existsByNomeAndOriginePiatto(Ingrediente ingrediente, Set<Ingrediente> piatto) {
		boolean check = false;
		for (Ingrediente i : piatto) {
			if (i.getNome().equals(ingrediente.getNome()) && i.getOrigine().equals(ingrediente.getOrigine())) {
				check = true;
				break;
			}
		}
		return check;
	}
}
