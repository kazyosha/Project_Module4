package com.codegym.demo.services;

import com.codegym.demo.dto.DepartmentDTO;
import com.codegym.demo.models.Department;
import com.codegym.demo.repositories.IDepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService {
    private IDepartmentRepository departmentRepository;

    public DepartmentService(IDepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<DepartmentDTO> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        List<DepartmentDTO> list = new ArrayList<>();
        for (Department department : departments) {
            DepartmentDTO departmentDTO = new DepartmentDTO();
            departmentDTO.setId(department.getId());
            departmentDTO.setName(department.getName());
            list.add(departmentDTO);
        }
        return list;
    }

    public void saveDepartment(DepartmentDTO departmentDTO) {
        Department department = new Department();
        department.setId(departmentDTO.getId());
        department.setName(departmentDTO.getName());
        departmentRepository.save(department);
    }

    public DepartmentDTO getDepartmentById(int id) {
        Department department = departmentRepository.findById((long)id).orElse(null);
        if (department != null) {
            DepartmentDTO departmentDTO = new DepartmentDTO();
            departmentDTO.setId(department.getId());
            departmentDTO.setName(department.getName());
            return departmentDTO;
        }
        return null;
    }
    public void deleteById(int id){
        departmentRepository.deleteById((long)id);
    }
    public void updateDepartment(int id, DepartmentDTO departmentDTO){
        Department department = departmentRepository.findById((long)id).orElse(null);
        if (department != null) {
            department.setName(departmentDTO.getName());
            departmentRepository.save(department);
        }
    }
}
