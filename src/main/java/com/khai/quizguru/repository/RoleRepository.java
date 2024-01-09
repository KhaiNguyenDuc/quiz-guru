package com.khai.quizguru.repository;

import com.khai.quizguru.model.User.Role;
import com.khai.quizguru.model.User.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(RoleName roleName);
}
