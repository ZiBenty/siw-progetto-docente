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

import it.uniroma3.siw.controller.validator.IngredienteValidator;
import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.Piatto;
import it.uniroma3.siw.service.IngredienteService;
import it.uniroma3.siw.service.PiattoService;

@Controller
public class IngredienteController {
	@Autowired
	private IngredienteService ingredienteService;
	@Autowired
	private PiattoService piattoService;
	@Autowired
	private IngredienteValidator ingredienteValidator;
	
	//salva l'ingrediente e lo aggiunge al piatto passato nel path, poi ritorna le info sull'ingrediente
	@PostMapping("/admin/ingrediente/{piattoId}")
    public String newPiattoIngrediente(@PathVariable("piattoId") Long piattoId, 
    		@Valid @ModelAttribute("ingrediente") Ingrediente ingrediente, 
    		BindingResult BindingResult, Model model) {
		Piatto piatto = this.piattoService.findById(piattoId);
		this.ingredienteValidator.validate(ingrediente, BindingResult);
		if (!BindingResult.hasErrors() && !this.ingredienteService.existsByNomeAndOriginePiatto(ingrediente, piatto.getIngredienti())){ // se i dati sono corretti
			piatto.addIngrediente(ingrediente);
	        this.ingredienteService.save(ingrediente); // salvo un oggetto Piatto
	        model.addAttribute("listIngrediente", piatto.getIngredienti());
			model.addAttribute("piatto", piatto);
	        return "ingredienti.html";// presenta un pagina con il piatto salvato
	    } else {
	    	model.addAttribute("piatto", this.piattoService.findById(piatto.getId()));
	    	BindingResult.reject("duplicatePiatto");
	        return "admin/ingredienteForm.html"; // ci sono errori, torna alla form iniziale
	    }
    }
	
	//salva e ritorna le info modificate dell'ingrediente
	@PostMapping("/admin/ingrediente")
    public String newIngrediente(@Valid @ModelAttribute("ingrediente") Ingrediente ingrediente, 
    		BindingResult BindingResult, Model model) {
		this.ingredienteValidator.validate(ingrediente, BindingResult);
		if (!BindingResult.hasErrors()){ // se i dati sono corretti
	        this.ingredienteService.save(ingrediente); // salvo un oggetto Piatto
			model.addAttribute("ingrediente", ingrediente);
	        return "ingrediente.html";// presenta un pagina con il piatto salvato
	    } else {
	    	model.addAttribute("ingrediente", ingrediente);
	        return "admin/ingredienteFormEdit.html"; // ci sono errori, torna alla form iniziale
	    }
    }

	//chiede ingrediente con specifico id che viene dal path
	@GetMapping("/ingrediente/{id}")
	public String getIngrediente(@PathVariable("id") Long id, Model model) {
		model.addAttribute("ingrediente", this.ingredienteService.findById(id));
		return "ingrediente.html";
	}
	
	//richiede la lista dei ingredienti associata ad un piatto
	@GetMapping("ingredienti/{id}")
	public String getIngredienti(@PathVariable("id") Long id, Model model) {
		Piatto piatto = this.piattoService.findById(id);
		model.addAttribute("listIngrediente", piatto.getIngredienti());
		model.addAttribute("piatto", piatto);
		return "ingredienti.html";
	}
		
	//richiede la form per inserire un ingrediente
	@GetMapping("/admin/ingrediente/new/{piattoId}")
	public String getIngredienteForm(@PathVariable("piattoId") Long piattoId, Model model) {
		model.addAttribute("ingrediente", new Ingrediente());
		model.addAttribute("piatto", this.piattoService.findById(piattoId));
		return "admin/ingredienteForm.html";
	}
	
	//richiede la form per modificare i valori di un ingrediente
	@GetMapping("/admin/ingrediente/edit/{id}")
	public String getEditIngredienteForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("ingrediente", this.ingredienteService.findById(id));
		return "admin/ingredienteFormEdit.html";
	}
	
	//cancella un ingrediente da tutti i piatto e dal repository
	@GetMapping("/admin/ingrediente/delete/{piattoId}/{id}")
	public String deleteIngrediente(@PathVariable("piattoId") Long piattoId, @PathVariable("id") Long id, Model model) {
		Ingrediente ingrediente = this.ingredienteService.findById(id);
		ingrediente.removeFromPiatti();
		this.ingredienteService.deleteById(id);
		return getIngredienti(piattoId, model);
	}
}
