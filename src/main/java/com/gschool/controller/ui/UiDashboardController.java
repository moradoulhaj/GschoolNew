package com.gschool.controller.ui;

import com.gschool.entities.Student;
import com.gschool.entities.User;
import com.gschool.service.FiliereService;
import com.gschool.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UiDashboardController {

    private final StudentService etudiantService;

    // Inject EtudiantService via constructor
    public UiDashboardController(StudentService etudiantService) {
        this.etudiantService = etudiantService;
    }

    @GetMapping("/dashboard")
    public ModelAndView dashboard(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return new ModelAndView("redirect:/login"); // Redirect if not logged in
        }

        ModelAndView modelAndView = new ModelAndView("dashboard");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @GetMapping("/dashboard/accueil")
    public ModelAndView accueil(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return new ModelAndView("redirect:/login"); // Redirect if not logged in
        }

        ModelAndView modelAndView = new ModelAndView("dashboard");
        modelAndView.addObject("user", user);
        modelAndView.addObject("section", "accueil"); // Pass the section name
        return modelAndView;
    }

    @GetMapping("/dashboard/filieres")
    public ModelAndView filieres(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return new ModelAndView("redirect:/login"); // Redirect if not logged in
        }

        ModelAndView modelAndView = new ModelAndView("dashboard");
        modelAndView.addObject("user", user);
        modelAndView.addObject("section", "filieres"); // Pass the section name
        return modelAndView;
    }

    @GetMapping("/dashboard/etudiant")
    public ModelAndView etudiant(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return new ModelAndView("redirect:/login"); // Redirect if not logged in
        }

        // Fetch students from the database
        List<Student> etudiants = etudiantService.getAllStudents();

        // Add students to the model
        model.addAttribute("etudiants", etudiants);

        ModelAndView modelAndView = new ModelAndView("dashboard");
        modelAndView.addObject("user", user);
        modelAndView.addObject("section", "etudiant"); // Pass the section name
        return modelAndView;
    }

    @GetMapping("/dashboard/utilisateurs")
    public ModelAndView utilisateurs(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return new ModelAndView("redirect:/login"); // Redirect if not logged in
        }

        ModelAndView modelAndView = new ModelAndView("dashboard");
        modelAndView.addObject("user", user);
        modelAndView.addObject("section", "utilisateurs"); // Pass the section name
        return modelAndView;
    }
}