package com.codegym.demo.services;

import com.codegym.demo.dto.CreateUserDTO;
import com.codegym.demo.dto.EditUserDTO;
import com.codegym.demo.dto.UserDTO;
import com.codegym.demo.models.Department;
import com.codegym.demo.models.User;
import com.codegym.demo.repositories.IDepartmentRepository;
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
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final String uploadDir = "F:/uploads/";
    private final IUserRepository userRepository;
    private final IDepartmentRepository departmentRepository;
    private final FileManager fileManager;

    public UserService(IUserRepository userRepository, IDepartmentRepository departmentRepository, FileManager fileManager) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.fileManager = fileManager;
    }

    public ListUserResponse getAllUsers(int pageNumber, int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").ascending());
        Page<User> data = userRepository.findAll(pageable);

        List<User> users = data.getContent();

        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId().intValue());
            userDTO.setUsername(user.getName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPhone(user.getPhone());
            userDTO.setImageUrl(user.getImageUrl());

            String nameDepartment = user.getDepartment() != null ? user.getDepartment().getName() : "No Department";
            userDTO.setDepartmentName(nameDepartment);

            userDTOs.add(userDTO);

        }

        ListUserResponse listUserResponse = new ListUserResponse();
        listUserResponse.setTotalPage(data.getTotalPages());
        listUserResponse.setCurrentPage(data.getNumber() + 1);
        listUserResponse.setUsers(userDTOs);

        return listUserResponse;
    }

    public void deleteById(int id) {
        Optional<User> user = userRepository.findById((long) (id));
        if (user.isPresent()) {
            User currentUser = user.get();
            fileManager.deleteFile(uploadDir + "/" + currentUser.getImageUrl());
            userRepository.delete(currentUser);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    public void storeUser(CreateUserDTO createUserDTO) throws IOException {
        String username = createUserDTO.getUsername();
        String password = createUserDTO.getPassword();
        String email = createUserDTO.getEmail();
        String phone = createUserDTO.getPhone();

        User newUser = new User();
        newUser.setName(username);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setPhone(phone);

        Long departmentId = createUserDTO.getDepartmentId();
        MultipartFile file = createUserDTO.getImage();

        if (!file.isEmpty()) {
            String fileName = fileManager.uploadFile(uploadDir, file);
            System.out.println("Saved file at: " + uploadDir + "/" + fileName);
            newUser.setImageUrl(fileName);
        }

        if (departmentId != null) {
            Department department = departmentRepository.findById(departmentId).orElse(null);
            if (department != null) {
                newUser.setDepartment(department);
            }
        }
        userRepository.save(newUser);
    }

    //
    public UserDTO getUserById(int id) {
        Optional<User> user = userRepository.findById((long) id);
        if (user.isPresent()) {
            User currentUser = user.get();
            UserDTO userDTO = new UserDTO();
            userDTO.setId(currentUser.getId().intValue());
            userDTO.setUsername(currentUser.getName());
            userDTO.setEmail(currentUser.getEmail());
            userDTO.setPhone(currentUser.getPhone());
            userDTO.setImageUrl(currentUser.getImageUrl());
            userDTO.setDepartmentId(currentUser.getDepartment() != null ? currentUser.getDepartment().getId() : null);
            return userDTO;
        }
        return null;
    }

    //
    public void updateUser(int id, EditUserDTO editUserDTO) {
        Optional<User> user = userRepository.findById((long) id);
        if (user.isPresent()) {
            User currentUser = user.get();
            currentUser.setName(editUserDTO.getUsername());
            currentUser.setEmail(editUserDTO.getEmail());
            currentUser.setPhone(editUserDTO.getPhone());

            Long departmentId = editUserDTO.getDepartmentId();
            if (departmentId != null) {
                Department department = departmentRepository.findById(departmentId).orElse(null);
                if (department != null) {
                    currentUser.setDepartment(department);
                }
            }
            MultipartFile file = editUserDTO.getImage();
            if (!file.isEmpty()) {
                fileManager.deleteFile(uploadDir + "/" + currentUser.getImageUrl());
                String fileName = fileManager.uploadFile(uploadDir, file);
                currentUser.setImageUrl(fileName);
            }
            userRepository.save(currentUser);
        }
    }
}
