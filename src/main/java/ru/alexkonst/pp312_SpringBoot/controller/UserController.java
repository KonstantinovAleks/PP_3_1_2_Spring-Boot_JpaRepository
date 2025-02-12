package ru.alexkonst.pp312_SpringBoot.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.alexkonst.pp312_SpringBoot.model.User;
import ru.alexkonst.pp312_SpringBoot.services.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String showAllUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "showAllUsers";
    }

    @GetMapping("/{id}")
    public String showUser(HttpServletRequest request, Model model) {
        String id = request.getRequestURI().split("/")[2];
        model.addAttribute("user", userService.findOne(Integer.parseInt(id)));
        return "showUserById";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "newUser";
    }

    @PostMapping
    public String createUser(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "newUser";

        userService.save(user);
        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String editUser(HttpServletRequest request, Model model) {
        String id = request.getRequestURI().split("/")[2];
        model.addAttribute("user", userService.findOne(Integer.parseInt(id)));
        return "editUser";
    }

    @PostMapping("/{id}")
    public String updateUser(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult,
                             HttpServletRequest request) {
        if (bindingResult.hasErrors())
            return "editUser";

        String id = request.getRequestURI().split("/")[2];
        userService.update(Integer.parseInt(id), user);
        return "redirect:/users";
    }

    @PostMapping(value = "/{id}", params = "action=del")
    public String deleteUser(HttpServletRequest request) {
        String id = request.getRequestURI().split("/")[2];
        userService.delete(Integer.parseInt(id));
        return "redirect:/users";
    }
}
