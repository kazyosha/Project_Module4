package com.codegym.demo.services;

import com.codegym.demo.dto.RoleDTO;
import com.codegym.demo.models.Role;
import com.codegym.demo.repositories.IRoleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {

    private IRoleRepository roleRepository;
    public RoleService(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    public List<RoleDTO> getAllRoles(){
        List<Role> roles = roleRepository.findAll();
        List<RoleDTO> list = new ArrayList<>();
        for (Role role : roles) {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(role.getId());
            roleDTO.setName(role.getName());
            list.add(roleDTO);
        }
        return list;
    }
}
