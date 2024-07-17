package me.blogSpringBoot.springbootdeveloper.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.blogSpringBoot.springbootdeveloper.domain.Article;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 추가
@Getter
public class AddArticleRequest {

    @NotNull
    @Column(length=10, nullable=false)
    private String title;

    @NotNull
    private String content;

    // 생성자를 사용해 객체 생성 (DTO를 엔티티로 만들어주는 메서드)
    public Article toEntity() {
        return Article.builder()
                .title(title)
                .content(content)
                .build();
    }
}
