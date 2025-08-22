package com.codegym.demo.repositories;

import com.codegym.demo.models.Department;
import com.codegym.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDepartmentRepository extends JpaRepository<Department, Long> {

}
