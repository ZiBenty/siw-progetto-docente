package it.uniroma3.siw.controller.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Buffet;
import it.uniroma3.siw.service.BuffetService;
import it.uniroma3.siw.service.ChefService;

@Component
public class BuffetValidator implements Validator{
	@Autowired
	private BuffetService buffetService;
	@Autowired
	private ChefService chefService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Buffet.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Buffet buffet = (Buffet)target;
		List<Buffet> buffets = chefService.getAllBuffet(buffet.getChef());
		for(Buffet b:buffets) {
			if (b.getNome().equals(buffet.getNome())) //se lo chef ha già un buffet con lo stesso nome
				errors.reject("duplicate");
		}
	}
	
}
