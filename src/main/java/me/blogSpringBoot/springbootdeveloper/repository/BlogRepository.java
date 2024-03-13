package me.blogSpringBoot.springbootdeveloper.repository;

import me.blogSpringBoot.springbootdeveloper.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {
}
