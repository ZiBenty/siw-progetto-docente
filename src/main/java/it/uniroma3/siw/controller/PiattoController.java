package it.uniroma3.siw.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.validator.PiattoValidator;
import it.uniroma3.siw.model.Buffet;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Piatto;
import it.uniroma3.siw.service.BuffetService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.PiattoService;

@Controller
public class PiattoController {
	@Autowired
	private BuffetService buffetService;
	@Autowired
	private PiattoService piattoService;
	@Autowired
	private PiattoValidator piattoValidator;
	@Autowired 
	private CredentialsService credentialsService;
	
	//salva il piatto e lo aggiunge al buffet passato nel path, poi ritorna le info sul piatto
	@PostMapping("/admin/piatto/{buffetId}")
    public String newBuffetPiatto(@PathVariable("buffetId") Long buffetId, @Valid @ModelAttribute("piatto") Piatto piatto, 
    		BindingResult BindingResult, Model model) {
		Buffet buffet = this.buffetService.findById(buffetId);
		this.piattoValidator.validate(piatto, BindingResult);
		if (!BindingResult.hasErrors() && !this.piattoService.existsByNomeBuffet(piatto, buffet.getPiatti())){ // se i dati sono corretti
			buffet.addPiatto(piatto);
	        this.piattoService.save(piatto); // salvo un oggetto Piatto
	        model.addAttribute("listPiatti", buffet.getPiatti());
			model.addAttribute("buffet", buffet);
	        return "piatti.html";// presenta un pagina con il piatto salvato
	    } else {
	    	model.addAttribute("buffet", this.buffetService.findById(buffet.getId()));
	    	BindingResult.reject("duplicateBuffet");
	        return "admin/piattoForm.html"; // ci sono errori, torna alla form iniziale
	    }
    }
	
	//salva e ritorna le info modificate del piatto
	@PostMapping("/admin/piatto")
    public String newPiatto(@Valid @ModelAttribute("piatto") Piatto piatto, 
    		BindingResult BindingResult, Model model) {
		this.piattoValidator.validate(piatto, BindingResult);
		if (!BindingResult.hasErrors()){ // se i dati sono corretti
	        this.piattoService.save(piatto); // salvo un oggetto Piatto
			model.addAttribute("piatto", piatto);
	        return "piatto.html";// presenta un pagina con il piatto salvato
	    } else {
	    	model.addAttribute("piatto", piatto);
	        return "admin/piattoFormEdit.html"; // ci sono errori, torna alla form iniziale
	    }
    }

	//chiede piatto con specifico id che viene dal path
	@GetMapping("/piatto/{id}")
	public String getPiatto(@PathVariable("id") Long id, Model model) {
		model.addAttribute("piatto", this.piattoService.findById(id));
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			return "admin/piatto.html";
		} else {
			return "piatto.html";
		}
	}
	
	//richiede la lista dei piatti associata ad un buffet
	@GetMapping("piatti/{id}")
	public String getPiatti(@PathVariable("id") Long id, Model model) {
		Buffet buffet = this.buffetService.findById(id);
		model.addAttribute("listPiatti", buffet.getPiatti());
		model.addAttribute("buffet", buffet);
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			return "admin/piatti.html";
		} else {
			return "piatti.html";
		}
	}
		
	//richiede la form per inserire un piatto
	@GetMapping("/admin/piatto/new/{buffetId}")
	public String getPiattoForm(@PathVariable("buffetId") Long buffetId, Model model) {
		model.addAttribute("piatto", new Piatto());
		model.addAttribute("buffet", this.buffetService.findById(buffetId));
		return "admin/piattoForm.html";
	}
	
	//richiede la form per modificare i valori di un piatto
	@GetMapping("/admin/piatto/edit/{id}")
	public String getEditPiattoForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("piatto", this.piattoService.findById(id));
		return "admin/piattoFormEdit.html";
	}
	
	//cancella un piatto da tutti i buffet e dal repository
	@GetMapping("/admin/piatto/delete/{buffetId}/{id}")
	public String deletePiatto(@PathVariable("buffetId") Long buffetId, @PathVariable("id") Long id, Model model) {
		Piatto piatto = this.piattoService.findById(id);
		piatto.removeFromBuffets();
		this.piattoService.deleteById(id);
		return getPiatti(buffetId, model);
	}
}
