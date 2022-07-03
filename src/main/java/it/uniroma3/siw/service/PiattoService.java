package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.Piatto;
import it.uniroma3.siw.repository.PiattoRepository;

@Service
public class PiattoService {
	@Autowired
	private PiattoRepository piattoRepository;
	
	//salva un piatto nel repository
	@Transactional
	public void save(Piatto piatto) {
		piattoRepository.save(piatto);
	}
	
	//elimina un piatto del repository
	//WARNING: elimina anche i buffet a lui collegati
	@Transactional
	public void deleteById(Long id) {
		Piatto piatto = piattoRepository.findById(id).get();
		piattoRepository.delete(piatto);
	}
	
	//ritorna piatto dato id
	public Piatto findById(Long id) {
		return piattoRepository.findById(id).get();
	}
	
	//ritorna la lista di tutti i piatto
	public List<Piatto> findAll(){
		List<Piatto> piatti = new ArrayList<Piatto>();
		for(Piatto p:piattoRepository.findAll()) {
			piatti.add(p);
		}
		return piatti;
	}
	
	//ritorna lista degli ingredienti del piatto
	public List<Ingrediente> getIngredienti(Piatto piatto){
		return piatto.getIngredienti();
	}
}
