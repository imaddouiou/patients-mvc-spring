package com.douiou0.patientsmvc.web;

import com.douiou0.patientsmvc.entities.Patient;
import com.douiou0.patientsmvc.repositories.PatientRepository;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
@AllArgsConstructor
public class PatientController {
    private PatientRepository patientRepository ;
    @GetMapping("/user/index")
    public String patients(Model model ,
                           @RequestParam(name="page",defaultValue= "0") int page ,
                           @RequestParam(name="size",defaultValue= "5") int size,
                           @RequestParam(name="keyword",defaultValue= "") String keyword)
    {
//        Page<Patient> pagePatients = patientRepository.findAll(PageRequest.of(page,size));
        Page<Patient> pagePatients=patientRepository.findByNomContains(keyword,PageRequest.of(page,size));
        model.addAttribute("ListePatients",pagePatients.getContent());
        model.addAttribute("pages",new int[pagePatients.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword",keyword);
        return "patients";
//        http://localhost:8082/index?page=4&size=3
    }
    @GetMapping("/admin/delete") //get tres movee pour delete concernemt la securite
    public String delete(Long id,String keyword,int page){
        patientRepository.deleteById(id);
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
    }
    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/user/patients")
    @ResponseBody //reagir comme restcontrolller->json
    public List<Patient> listPatients(){
        return patientRepository.findAll();
    }
    @GetMapping("/admin/formPatients")
    public String formPatient(Model model){
        model.addAttribute("patient", new Patient());
        return "formPatients";
    }
    @PostMapping(path = "/admin/save")
    public String save(Model model, @Valid Patient patient, BindingResult bindingResult,
                       @RequestParam(defaultValue= "0") int page,
                       @RequestParam(defaultValue= "") String keyword){
        if (bindingResult.hasErrors()) {
            return "formPatients"; // Retourne au formulaire pour afficher les erreurs
        }
        patientRepository.save(patient);//pour ajout et aussi a edit si pose meme id
        // ... redirection après succès
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
    }
    @GetMapping("/user/editPatient")
    public String editPatient(Model model,Long id,String keyword,int page){
        Patient patient = patientRepository.findById(id).orElse(null);
        if(patient == null) throw new RuntimeException("Patient introuvable");
        model.addAttribute("patient",patient );
        model.addAttribute("page",page );
        model.addAttribute("keyword",keyword );
        return "editPatient";
    }
}
