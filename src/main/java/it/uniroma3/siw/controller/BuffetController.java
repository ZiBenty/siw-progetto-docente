package it.uniroma3.siw.controller;

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
    		BindingResult BindingResult, Model model) {
		this.buffetValidator.validate(buffet, BindingResult);
		if (!BindingResult.hasErrors()){ // se i dati sono corretti // trova lo chef dall'id
	        this.buffetService.save(buffet); // salvo un oggetto Buffet
			model.addAttribute("buffet", buffet);
	        return "buffet.html";// presenta un pagina con il buffet salvato
	    } else {
	    	model.addAttribute("listChef", chefService.findAll());
	        return "buffetForm.html"; // ci sono errori, torna alla form iniziale
	    }
    }

	//chiede buffet con specifico id che viene dal path
	@GetMapping("/buffet/{id}")
	public String getBuffet(@PathVariable("id") Long id, Model model) {
		model.addAttribute("buffet", this.buffetService.findById(id));
		return "buffet.html";
	}
	
	//richiede la lista dei buffet associata ad uno chef
	@GetMapping("buffets/{id}")
	public String getBuffets(@PathVariable("id") Long id, Model model) {
		Chef chef = this.chefService.findById(id);
		model.addAttribute("listBuffet", chef.getBuffets());
		model.addAttribute("chef", chef);
		return "buffets.html";
	}
		
	//richiede la form per inserire un buffet
	@GetMapping("/admin/buffet/new")
	public String getBuffetForm(Model model) {
		model.addAttribute("buffet", new Buffet());
		model.addAttribute("listChef", chefService.findAll());
		return "buffetForm.html";
	}
	
	//richiede la form per modificare i valori di un buffet
	@GetMapping("/admin/buffet/edit/{id}")
	public String getEditBuffetForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("buffet", this.buffetService.findById(id));
		model.addAttribute("listChef", chefService.findAll());
		return "buffetForm.html";
	}
	
	//cancella il buffet associato all'id nel path
	@GetMapping("/admin/buffet/delete/{chefId}/{id}")
	public String deleteBuffet(@PathVariable("chefId") Long chefId, @PathVariable("id") Long id, Model model) {
		this.buffetService.deleteById(id);
		return "admin/home";
	}
	
}
