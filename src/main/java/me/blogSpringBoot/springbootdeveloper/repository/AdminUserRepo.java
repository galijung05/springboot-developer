package me.blogSpringBoot.springbootdeveloper.repository;

import me.blogSpringBoot.springbootdeveloper.domain.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminUserRepo extends JpaRepository<AdminUser, Long> {

    Optional<AdminUser> findByUsername(String username);
}
