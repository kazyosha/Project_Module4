package com.codegym.demo.controllers;

import com.codegym.demo.dto.CreateUserDTO;
import com.codegym.demo.dto.DepartmentDTO;
import com.codegym.demo.dto.EditUserDTO;
import com.codegym.demo.dto.UserDTO;
import com.codegym.demo.repositories.response.ListUserResponse;
import com.codegym.demo.services.DepartmentService;
import com.codegym.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String listUsers(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
                            Model model) {
        if (page < 1) page = 1;
        int zeroBasedPage = page - 1;

        ListUserResponse listUserResponse = userService.getAllUsers(zeroBasedPage, size);
        List<UserDTO> users = listUserResponse.getUsers();
        // Logic to list users
        model.addAttribute("totalPages", listUserResponse.getTotalPage());
        model.addAttribute("currentPage", page);
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
    public String storeUser(@Valid @ModelAttribute("user") CreateUserDTO createUserDTO,
                                    BindingResult result, Model model) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "admin/create-user";
        }
        userService.storeUser(createUserDTO);
        return "redirect:/admin";
    }

    @GetMapping("/user/{id}/edit")
    public String showFormEdit(@PathVariable("id") int id, Model model) {
        UserDTO user = userService.getUserById(id);
        System.out.println(user);
        if (user == null) {
            return "redirect:/admin";
        }

        EditUserDTO editUserDTO = new EditUserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone()
        );
        editUserDTO.setDepartmentId(user.getDepartmentId());

        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        model.addAttribute("user", editUserDTO);
        model.addAttribute("departments", departments);

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
