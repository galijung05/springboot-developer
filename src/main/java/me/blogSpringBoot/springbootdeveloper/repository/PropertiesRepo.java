package me.blogSpringBoot.springbootdeveloper.repository;

import me.blogSpringBoot.springbootdeveloper.domain.AdminProperties;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertiesRepo  extends JpaRepository<AdminProperties, Long> {
}
