package com.codegym.demo.controllers;

import com.codegym.demo.dto.department.DepartmentDTO;
import com.codegym.demo.services.DepartmentService;
import com.codegym.demo.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class DepartmentController {

    public UserService userService;
    public DepartmentService departmentService;

    public DepartmentController(UserService userService, DepartmentService departmentService) {
        this.userService = userService;
        this.departmentService = departmentService;
    }

    @GetMapping("/department")
    public String listDepartments(Model model) {
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);
        return "admin/list-department";
    }

    @GetMapping("/department/create")
    public String showPageCreateDepartment(Model model) {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        model.addAttribute("department", departmentDTO);
        return "admin/create-department";
    }
    @PostMapping("/department/create")
    public String createDepartment(@ModelAttribute("department") DepartmentDTO departmentDTO) throws Exception {

        departmentService.saveDepartment(departmentDTO);
        return "redirect:/admin/department";
    }

    @GetMapping("/department/{id}/delete")
    public String deleteDepartment(@PathVariable("id") int id){
        departmentService.deleteById(id);
        return "redirect:/admin/department";
    }

    @GetMapping("/department/{id}/edit")
    public String showFormEdit(@PathVariable("id") int id, Model model) {
        DepartmentDTO department = departmentService.getDepartmentById(id);
        if (department == null) {
            return "redirect:/admin/department?null";
        }
        model.addAttribute("department", department);
        return "admin/edit-department";
    }
    @PostMapping("/department/{id}/edit")
        public String updateDepartment(@PathVariable("id") int id,
                                       @ModelAttribute("department") DepartmentDTO departmentDTO) {
        DepartmentDTO department = departmentService.getDepartmentById(id);
        if (department == null) {
            return "redirect:/admin/department?null";
        }
        departmentService.updateDepartment(id, departmentDTO);
        return "redirect:/admin/department";
        }

}
