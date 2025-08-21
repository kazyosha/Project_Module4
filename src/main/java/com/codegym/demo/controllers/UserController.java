package com.codegym.demo.controllers;

import com.codegym.demo.dto.CreateUserDTO;
import com.codegym.demo.dto.DepartmentDTO;
import com.codegym.demo.dto.EditUserDTO;
import com.codegym.demo.dto.UserDTO;
import com.codegym.demo.services.DepartmentService;
import com.codegym.demo.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class UserController {
    private final UserService userService;
    private final DepartmentService departmentService;

    public UserController(UserService userService, DepartmentService departmentService) {
        this.userService = userService;
        this.departmentService = departmentService;
    }

    @GetMapping
    public String listUsers(Model model) {
        List<UserDTO> users = userService.getAllUsers();
        // Logic to list users
        model.addAttribute("users", users);
        return "admin/list-user";
    }

    @GetMapping("/user/create")
    public String createUser(Model model) {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);
        model.addAttribute("user", createUserDTO);

        return "admin/create-user";
    }

    @GetMapping("/user/{id}/detail")
    public String userDetail(@PathVariable("id") int id,
                             Model model) {
        UserDTO user = userService.getUserById(id);
        if (user == null) {
            return "redirect:/admin";
        }
        model.addAttribute("user", user);
        return "admin/detail-user";
    }

    @GetMapping("/user/{id}/delete")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

    @PostMapping("/user/create")
    public String storeUser(@ModelAttribute("user") CreateUserDTO
                                        createUserDTO) throws IOException {
        userService.storeUser(createUserDTO);
        return "redirect:/admin";
    }

    @GetMapping("/user/{id}/edit")
    public String showFormEdit(@PathVariable("id") int id, Model model) {
        UserDTO user = userService.getUserById(id);
        if (user == null) {
            return "redirect:/admin";
        }

        EditUserDTO editUserDTO = new EditUserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone()
        );

        model.addAttribute("user", editUserDTO);

        return "admin/edit-user";
    }

    @PostMapping("/user/{id}/edit")
    public String updateUser(@PathVariable("id") int id,
                             @ModelAttribute("user") EditUserDTO editUserDTO) {
        UserDTO user = userService.getUserById(id);
        if (user == null) {
            return "redirect:/admin";
        }
        userService.updateUser(id, editUserDTO);
        return "redirect:/admin";
    }
}
