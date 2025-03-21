package com.gschool.controller.ui;

import com.gschool.entities.Filiere;
import com.gschool.entities.Student;
import com.gschool.entities.User;
import com.gschool.service.FiliereService;
import com.gschool.service.StudentService;
import com.gschool.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UiDashboardController {

    private final StudentService etudiantService;
    private final FiliereService filiereService;
    private final UserService userService;

    // Inject EtudiantService via constructor
    public UiDashboardController(StudentService etudiantService , FiliereService filiereService , UserService userService) {
        this.etudiantService = etudiantService;
        this.filiereService = filiereService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public ModelAndView dashboard(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return new ModelAndView("redirect:/login"); // Redirect if not logged in
        }

        ModelAndView modelAndView = new ModelAndView("dashboard");
        modelAndView.addObject("user", user);
        modelAndView.addObject("section", "accueil"); // Pass the section name

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
    public ModelAndView filieres(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        // Redirect to login if the user is not logged in
        if (user == null) {
            return new ModelAndView("redirect:/login");
        }

        // Fetch filières from the database
        List<Filiere> filieres = filiereService.getAllFilieres();

        // Add filières to the model
        model.addAttribute("filieres", filieres);

        // Create ModelAndView and add necessary objects
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
        modelAndView.addObject("section", "etudiants"); // Pass the section name
        return modelAndView;
    }

    @GetMapping("/dashboard/utilisateurs")
    public ModelAndView utilisateurs(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return new ModelAndView("redirect:/login"); // Redirect if not logged in
        }

        // Fetch users from the database
        List<User> utilisateurs = userService.getAllUsers();

        // Add users to the model
        model.addAttribute("utilisateurs", utilisateurs);

        // Create ModelAndView and add necessary objects
        ModelAndView modelAndView = new ModelAndView("dashboard");
        modelAndView.addObject("user", user);
        modelAndView.addObject("section", "utilisateurs"); // Pass the section name
        return modelAndView;
    }
}
