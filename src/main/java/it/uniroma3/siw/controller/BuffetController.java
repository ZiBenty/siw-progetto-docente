package it.uniroma3.siw.controller;

import java.util.ArrayList;
import java.util.List;

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
	    public String newBuffet(@Valid @ModelAttribute("buffet") Buffet buffet, 
	    		BindingResult buffetBindingResult, Model model) {
			this.buffetValidator.validate(buffet, buffetBindingResult);
			if (!buffetBindingResult.hasErrors()){ // se i dati sono corretti // trova lo chef dall'id
			  /*try {
				  List<Buffet> buffets = chefService.getAllBuffet(buffet.getChef());
				  buffets.add(buffet);
				  buffet.getChef().setBuffets(buffets); // aggiunge il buffet allo chef
			  }catch (NullPointerException e) {
				  buffet.getChef().setBuffets(new ArrayList<Buffet>());
				  buffet.getChef().getBuffets().add(buffet);
			  }*/
		      this.buffetService.save(buffet); // salvo un oggetto Buffet
		      model.addAttribute("buffet", buffet);
		      return "buffet.html";// presenta un pagina con il buffet salvato
		    } else
		      return "buffetForm.html"; // ci sono errori, torna alla form iniziale
		}
	
	//richiede la form per inserire un buffet
	@GetMapping("/admin/buffetForm")
	public String getBuffetForm(Model model) {
		model.addAttribute("buffet", new Buffet());
		model.addAttribute("listChef", chefService.findAll());
		return "buffetForm.html";
	}
	
	//chiede buffet con specifico id che viene dal path
	@GetMapping("/buffet/{id}")
	public String getBuffet(@PathVariable("id") Long id, Model model) {
		model.addAttribute("buffet", this.buffetService.findById(id));
		return "chef.html";
	}
	
	//richiede la lista dei buffet associata ad uno chef
	@GetMapping("buffets/{id}")
	public String getBuffets(@PathVariable("id") Long id, Model model) {
		Chef chef = this.chefService.findById(id);
		model.addAttribute("listBuffet", chef.getBuffets());
		model.addAttribute("chef", chef);
		return "buffets.html";
	}
}
