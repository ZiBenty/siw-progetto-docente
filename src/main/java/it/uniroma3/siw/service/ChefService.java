package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Buffet;
import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.repository.ChefRepository;

@Service
public class ChefService {
	@Autowired
	private ChefRepository chefRepository;
	
	//salva uno chef nel repository
	@Transactional
	public void save(Chef chef) {
		chefRepository.save(chef);
	}
	
	//elimina uno chef del repository
	//WARNING: elimina anche i buffet a lui collegati
	@Transactional
	public void deleteById(Long id) {
		Chef chef = chefRepository.findById(id).get();
		chefRepository.delete(chef);
	}
	
	//ritorna chef dato id
	public Chef findById(Long id) {
		return chefRepository.findById(id).get();
	}
	
	//ritorna la lista di tutti gli chef
	public List<Chef> findAll(){
		List<Chef> chefs = new ArrayList<Chef>();
		for(Chef c:chefRepository.findAll()) {
			chefs.add(c);
		}
		return chefs;
	}
	
	//ritorna la lista di tutti i buffet associati ad uno chef
		public List<Buffet> getAllBuffet(Chef chef){
			return chef.getBuffets();
		}
	
	//controlla se esista già uno chef con gli stessi parametri
	public boolean alreadyExists(Chef chef) {
		return chefRepository.existsByNomeAndCognomeAndNazionalita(chef.getNome(), chef.getCognome(), chef.getNazionalita());
	}
}
