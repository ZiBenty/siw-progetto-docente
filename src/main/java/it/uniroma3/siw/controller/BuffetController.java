package it.uniroma3.siw.controller;

import java.util.ArrayList;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.validator.BuffetValidator;
import it.uniroma3.siw.model.Buffet;
import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.service.BuffetService;
import it.uniroma3.siw.service.ChefService;

@Controller
public class BuffetController {
	@Autowired
	private BuffetService buffetService;
	@Autowired
	private ChefService chefService;
	@Autowired
	private BuffetValidator buffetValidator;
	
	//salva e ritorna la lista degli chef aggiornata con il buffet salvato
		@PostMapping("/admin/buffet")
	    public String newBuffet(@Valid @ModelAttribute("buffet") Buffet buffet, BindingResult buffetBindingResult,
	    		@Valid @ModelAttribute("chef") Chef chef, BindingResult chefBindingResult, Model model) {
			
			buffet.setChef(chef); // aggiunge lo chef al buffet
			this.buffetValidator.validate(buffet, buffetBindingResult);
			
			if (!buffetBindingResult.hasErrors() && !chefBindingResult.hasErrors()){ // se i dati sono corretti // trova lo chef dall'id
			  try {
				  chef.getBuffets().add(buffet); // aggiunge il buffet allo chef
			  }catch (NullPointerException e) {
				  chef.setBuffets(new ArrayList<Buffet>());
				  chef.getBuffets().add(buffet);
			  }
		      this.buffetService.save(buffet); // salvo un oggetto Buffet
		      model.addAttribute("buffet", buffet);
		      return "buffet.html";// presenta un pagina con il buffet salvato
		    } else
		      return "buffetForm.html"; // ci sono errori, torna alla form iniziale
		}
	
	//richiede la form per inserire un buffet
	@GetMapping("/admin/buffetForm/{id}")
	public String getBuffetForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("buffet", new Buffet());
		model.addAttribute("chef", this.chefService.findById(id));
		return "buffetForm.html";
	}
	
	//chiede buffet con specifico id che viene dal path
	@GetMapping("/buffet/{id}")
	public String getBuffet(@PathVariable("id") Long id, Model model) {
		model.addAttribute("buffet", this.buffetService.findById(id));
		return "chef.html";
	}
}
