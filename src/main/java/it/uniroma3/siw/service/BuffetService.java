package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Buffet;
import it.uniroma3.siw.model.Piatto;
import it.uniroma3.siw.repository.BuffetRepository;

@Service
public class BuffetService {
	@Autowired
	private BuffetRepository buffetRepository;
	
	//salva un buffet nel repository
	@Transactional
	public void save(Buffet buffet) {
		buffetRepository.save(buffet);
	}
	
	//elimina un buffet del repository
	@Transactional
	public void deleteById(Long id) {
		Buffet buffet = buffetRepository.findById(id).get();
		buffetRepository.delete(buffet);
	}
	
	//ritorna buffet dato id
	public Buffet findById(Long id) {
		return buffetRepository.findById(id).get();
	}
	
	//ritorna la lista di tutti i buffet
	public List<Buffet> findAll(){
		List<Buffet> buffets = new ArrayList<Buffet>();
		for(Buffet b:buffetRepository.findAll()) {
			buffets.add(b);
		}
		return buffets;
	}
	
	//ritorna la lista di piatti associati ad un buffet
	public List<Piatto> findAllPiatti(Buffet buffet){
		return buffet.getPiatti();
	}
	
}
