package com.codegym.demo.repositories;

import com.codegym.demo.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends PagingAndSortingRepository<User , Long> {
    Optional<User> findById(Long id);
    void delete(User user);
    void save(User user);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    Page<User> findByDepartmentId(Long departmentId, Pageable pageable);
    Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<User> findByNameContainingIgnoreCaseAndDepartmentId(String name, Long departmentId, Pageable pageable);
}
