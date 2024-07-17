package me.blogSpringBoot.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.blogSpringBoot.springbootdeveloper.domain.Article;
import me.blogSpringBoot.springbootdeveloper.dto.AddArticleRequest;
import me.blogSpringBoot.springbootdeveloper.dto.ArticleResponse;
import me.blogSpringBoot.springbootdeveloper.dto.UpdateArticleRequest;
import me.blogSpringBoot.springbootdeveloper.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.security.Principal;
@RequiredArgsConstructor
@RestController // HTTP Response Body에 객체 데이터를 JSON 형식으로 반환하는 컨트롤러
public class BlogApiController {

    private final BlogService blogService;

    // HTTP 메서드가 POST일 때 전달받은 URL과 동일하면 메서드로 매핑
//    @PostMapping("/api/articles")
//    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
//        Article savedArticle = blogService.save(request);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
//    }

    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody @Validated AddArticleRequest request, Principal principal) {
        Article savedArticle = blogService.save(request, principal.getName());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle);
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<ArticleResponse> articles = blogService.findAll()
                .stream()
                .map(ArticleResponse::new)
                .toList();

        return ResponseEntity.ok().body(articles);
    }



    // URL에서 {id}에 해당하는 값이 매게변수로 들어옴
    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticles(@PathVariable long id) {
        Article articles = blogService.findById(id);

        return ResponseEntity.ok().body(new ArticleResponse(articles));
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id) {
        blogService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable long id, @RequestBody UpdateArticleRequest request) {
        Article updateArticle = blogService.update(id, request);
        return ResponseEntity.ok().body(updateArticle);
    }

}
