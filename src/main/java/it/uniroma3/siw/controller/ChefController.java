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

import it.uniroma3.siw.controller.validator.ChefValidator;
import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.service.ChefService;

@Controller
public class ChefController {
	@Autowired
	private ChefService chefService;
	@Autowired
	private ChefValidator chefValidator;
	
	//salva e ritorna lo chef salvato
	@PostMapping("/admin/chef/save")
    public String newChef(@Valid @ModelAttribute("chef") Chef chef, BindingResult bindingResult, Model model) {
		this.chefValidator.validate(chef, bindingResult);
		if (!bindingResult.hasErrors()){ // se i dati sono corretti
	      this.chefService.save(chef); // salvo un oggetto Persona
	      model.addAttribute("chef", chef);
	      return "chef.html";// presenta un pagina con lo chef appena salvato
	    } else
	      return "chefForm.html"; // ci sono errori, torna alla form iniziale
	}
	
	//richiede tutte gli chefs
	@GetMapping("/chefs")
	public String getChefs(Model model) {
		model.addAttribute("chefs", chefService.findAll());
		return "chefs.html";
	}
	
	//richiede la form per inserire uno chef
	@GetMapping("/admin/chef/new")
	public String getChefForm(Model model) {
		model.addAttribute("chef", new Chef());
		return "chefForm.html";
	}
	
	//richiede la form per modificare i valori di uno chefù
	@GetMapping("/admin/chef/edit/{id}")
	public String getEditChefForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("chef", this.chefService.findById(id));
		return "chefForm.html";
	}
	
	//chiede chef con specifico id che viene dal path
	@GetMapping("/chef/{id}")
	public String getChef(@PathVariable("id") Long id, Model model) {
		model.addAttribute("chef", this.chefService.findById(id));
		return "chef.html";
	}
}
