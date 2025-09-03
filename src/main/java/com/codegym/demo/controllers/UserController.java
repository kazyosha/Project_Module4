package com.codegym.demo.controllers;

import com.codegym.demo.dto.department.DepartmentDTO;
import com.codegym.demo.dto.user.CreateUserDTO;
import com.codegym.demo.dto.user.EditUserDTO;
import com.codegym.demo.dto.user.RoleDTO;
import com.codegym.demo.dto.user.UserDTO;
import com.codegym.demo.models.User;
import com.codegym.demo.repositories.response.ListUserResponse;
import com.codegym.demo.services.DepartmentService;
import com.codegym.demo.services.RoleService;
import com.codegym.demo.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class UserController {
    private final UserService userService;
    private final DepartmentService departmentService;
    private final RoleService roleService;

    public UserController(UserService userService, DepartmentService departmentService, RoleService roleService) {
        this.userService = userService;
        this.departmentService = departmentService;
        this.roleService = roleService;
    }

    @GetMapping
    public String listUsers(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "departmentId", required = false) String departmentIdStr,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam Map<String, String> params,
            HttpSession session,
            Model model) {

        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        if (!"ADMIN".equalsIgnoreCase(currentUser.getRole().getName())) {
            return "redirect:/home";
        }
        model.addAttribute("user", currentUser);
        int size = 5;
        page = Math.max(page, 1);
        int zeroBasedPage = page - 1;

        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);

        Long departmentId = parseDepartmentId(departmentIdStr);

        ListUserResponse listUserResponse = fetchUsersByFilter(keyword, departmentId, zeroBasedPage, size);

        params.remove("page");
        String queryString = buildQueryString(params);

        String baseUrl = buildBaseUrl(departmentId);

        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("queryParams", queryString);
        model.addAttribute("totalPages", listUserResponse.getTotalPage());
        model.addAttribute("currentPage", page);
        model.addAttribute("users", listUserResponse.getUsers());
        model.addAttribute("selectedDepartmentId", departmentId);
        model.addAttribute("keyword", keyword);

        return "admin/list-user";
    }

    private Long parseDepartmentId(String departmentIdStr) {
        if (departmentIdStr != null && !departmentIdStr.isBlank()) {
            try {
                return Long.parseLong(departmentIdStr);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private ListUserResponse fetchUsersByFilter(String keyword, Long departmentId, int page, int size) {
        if (keyword != null && !keyword.isBlank() && departmentId != null) {
            return userService.searchUsersByNameAndDepartment(keyword, departmentId, page, size);
        } else if (keyword != null && !keyword.isBlank()) {
            return userService.searchUsersByName(keyword, page, size);
        } else if (departmentId != null) {
            return userService.getUsersByDepartment(departmentId, page, size);
        } else {
            return userService.getAllUsers(page, size);
        }
    }

    private String buildQueryString(Map<String, String> params) {
        return params.entrySet().stream()
                .filter(e -> e.getValue() != null && !e.getValue().isBlank())
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
    }

    private String buildBaseUrl(Long departmentId) {
        String baseUrl = "/admin";
        if (departmentId != null) {
            baseUrl += "?departmentId=" + departmentId;
        }
        return baseUrl;
    }

    @GetMapping("/user/create")
    public String createUser(Model model) {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        List<RoleDTO> roles = roleService.getAllRoles();
        model.addAttribute("roles", roles);
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
    public String showFormEdit(@Valid @PathVariable("id") int id, Model model) {
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
        editUserDTO.setDepartmentId(user.getDepartmentId());
        editUserDTO.setRoleId(user.getRoleId());

        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        List<RoleDTO> roles = roleService.getAllRoles();
        model.addAttribute("user", editUserDTO);
        model.addAttribute("departments", departments);
        model.addAttribute("roles", roles);

        return "admin/edit-user";
    }

    @PostMapping("/user/{id}/edit")
    public String updateUser(@PathVariable("id") int id,
                             @Valid @ModelAttribute("user") EditUserDTO editUserDTO,
                             BindingResult result, Model model) {
        UserDTO user = userService.getUserById(id);
        if (user == null) {
            return "redirect:/admin";
        }
        if (result.hasErrors()) {
            List<DepartmentDTO> departments = departmentService.getAllDepartments();
            List<RoleDTO> roles = roleService.getAllRoles();
            model.addAttribute("roles", roles);
            model.addAttribute("departments", departments);
            return "admin/edit-user";
        }
        userService.updateUser(id, editUserDTO);
        return "redirect:/admin";
    }
}
