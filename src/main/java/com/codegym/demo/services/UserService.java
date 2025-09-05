package com.codegym.demo.services;

import com.codegym.demo.dto.user.CreateUserDTO;
import com.codegym.demo.dto.user.EditUserDTO;
import com.codegym.demo.dto.user.UserDTO;
import com.codegym.demo.models.Department;
import com.codegym.demo.models.Role;
import com.codegym.demo.models.User;
import com.codegym.demo.repositories.IDepartmentRepository;
import com.codegym.demo.repositories.IRoleRepository;
import com.codegym.demo.repositories.IUserRepository;
import com.codegym.demo.repositories.response.ListUserResponse;
import com.codegym.demo.untils.FileManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final String uploadDir = "F:/uploads/";
    private final IUserRepository userRepository;
    private final IDepartmentRepository departmentRepository;
    private final FileManager fileManager;
    private final IRoleRepository roleRepository;

    public UserService(IUserRepository userRepository, IDepartmentRepository departmentRepository, FileManager fileManager, IRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.fileManager = fileManager;
        this.roleRepository = roleRepository;
    }

    private String getRoleNames(User user) {
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            return user.getRoles()
                    .stream()
                    .map(Role::getName)
                    .collect(Collectors.joining(", "));
        }
        return "No Role";
    }

    private UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId().intValue());
        dto.setUsername(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setImageUrl(user.getImageUrl());
        dto.setDepartmentName(user.getDepartment() != null ? user.getDepartment().getName() : "No Department");
        dto.setRoleName(Collections.singletonList(getRoleNames(user)));
        return dto;
    }

    public ListUserResponse getAllUsers(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").ascending());
        Page<User> data = userRepository.findAll(pageable);

        List<UserDTO> userDTOs = data.getContent()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        ListUserResponse response = new ListUserResponse();
        response.setTotalPage(data.getTotalPages());
        response.setCurrentPage(data.getNumber() + 1);
        response.setUsers(userDTOs);

        return response;
    }

    public void deleteById(int id) {
        Optional<User> userOpt = userRepository.findById((long) id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            fileManager.deleteFile(uploadDir + "/" + user.getImageUrl());
            userRepository.delete(user);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    public void storeUser(CreateUserDTO createUserDTO) throws IOException {
        User newUser = new User();
        newUser.setName(createUserDTO.getUsername());
        newUser.setEmail(createUserDTO.getEmail());
        newUser.setPassword(createUserDTO.getPassword());
        newUser.setPhone(createUserDTO.getPhone());

        // Upload ảnh
        MultipartFile file = createUserDTO.getImage();
        if (!file.isEmpty()) {
            String fileName = fileManager.uploadFile(uploadDir, file);
            newUser.setImageUrl(fileName);
        }

        // Gán Department
        if (createUserDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(createUserDTO.getDepartmentId()).orElse(null);
            newUser.setDepartment(department);
        }

        // Gán Roles (n-n)
        if (createUserDTO.getRoleId() != null && !createUserDTO.getRoleId().isEmpty()) {
            List<Role> roles = roleRepository.findAllById(createUserDTO.getRoleId());
            newUser.setRoles(roles);
        }

        userRepository.save(newUser);
    }

    public UserDTO getUserById(int id) {
        Optional<User> userOpt = userRepository.findById((long) id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            UserDTO dto = new UserDTO();
            dto.setId(user.getId().intValue());
            dto.setUsername(user.getName());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setImageUrl(user.getImageUrl());

            // map department
            if (user.getDepartment() != null) {
                dto.setDepartmentId(user.getDepartment().getId());
                dto.setDepartmentName(user.getDepartment().getName());
            }

            // map roles
            if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                List<Long> roleIds = user.getRoles()
                        .stream()
                        .map(Role::getId)
                        .toList();
                dto.setRoleId(roleIds);

                // nếu cần cả tên
                String roleNames = user.getRoles()
                        .stream()
                        .map(Role::getName)
                        .collect(Collectors.joining(", "));
                dto.setRoleName(Collections.singletonList(roleNames));
            }

            return dto;
        }
        return null;
    }

    public void updateUser(int id, EditUserDTO editUserDTO) throws IOException {
        Optional<User> userOpt = userRepository.findById((long) id);
        if (userOpt.isPresent()) {
            User currentUser = userOpt.get();
            currentUser.setName(editUserDTO.getUsername());
            currentUser.setEmail(editUserDTO.getEmail());
            currentUser.setPhone(editUserDTO.getPhone());

            // Cập nhật department
            if (editUserDTO.getDepartmentId() != null) {
                Department department = departmentRepository.findById(editUserDTO.getDepartmentId()).orElse(null);
                currentUser.setDepartment(department);
            }

            // Cập nhật roles (n-n)
            if (editUserDTO.getRoleId() != null && !editUserDTO.getRoleId().isEmpty()) {
                List<Role> roles = roleRepository.findAllById(editUserDTO.getRoleId());
                currentUser.setRoles(roles);
            }

            // Cập nhật ảnh
            MultipartFile file = editUserDTO.getImage();
            if (file != null && !file.isEmpty()) {
                fileManager.deleteFile(uploadDir + "/" + currentUser.getImageUrl());
                String fileName = fileManager.uploadFile(uploadDir, file);
                currentUser.setImageUrl(fileName);
            }

            userRepository.save(currentUser);
        }
    }

    public ListUserResponse getUsersByDepartment(Long departmentId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").ascending());
        Page<User> data = userRepository.findByDepartmentId(departmentId, pageable);

        List<UserDTO> userDTOs = data.getContent()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        ListUserResponse response = new ListUserResponse();
        response.setTotalPage(data.getTotalPages());
        response.setCurrentPage(data.getNumber() + 1);
        response.setUsers(userDTOs);

        return response;
    }

    public ListUserResponse searchUsersByName(String keyword, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").ascending());
        Page<User> data = userRepository.findByNameContainingIgnoreCase(keyword, pageable);

        List<UserDTO> userDTOs = data.getContent()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        ListUserResponse response = new ListUserResponse();
        response.setTotalPage(data.getTotalPages());
        response.setCurrentPage(data.getNumber() + 1);
        response.setUsers(userDTOs);

        return response;
    }

    public ListUserResponse searchUsersByNameAndDepartment(String keyword, Long departmentId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").ascending());
        Page<User> data = userRepository.findByNameContainingIgnoreCaseAndDepartmentId(keyword, departmentId, pageable);

        List<UserDTO> userDTOs = data.getContent()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        ListUserResponse response = new ListUserResponse();
        response.setTotalPage(data.getTotalPages());
        response.setCurrentPage(data.getNumber() + 1);
        response.setUsers(userDTOs);

        return response;
    }
}
