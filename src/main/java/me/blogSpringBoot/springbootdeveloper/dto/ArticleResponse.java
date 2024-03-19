package me.blogSpringBoot.springbootdeveloper.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.blogSpringBoot.springbootdeveloper.domain.Article;

@Getter
@RequiredArgsConstructor
public class ArticleResponse {

    private final String title;
    private final String content;

    public ArticleResponse (Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}
