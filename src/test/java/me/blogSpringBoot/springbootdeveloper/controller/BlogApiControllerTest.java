package me.blogSpringBoot.springbootdeveloper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import me.blogSpringBoot.springbootdeveloper.domain.Article;
import me.blogSpringBoot.springbootdeveloper.dto.AddArticleRequest;
import me.blogSpringBoot.springbootdeveloper.dto.UpdateArticleRequest;
import me.blogSpringBoot.springbootdeveloper.repository.BlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper; // 직렬화, 역직렬화를 위한 클래스

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach
    public  void mockMveSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        blogRepository.deleteAll();
    }

    @DisplayName("addArticle: 블로그 글 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception {
        // given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        final String requestBody = objectMapper.writeValueAsString(userRequest);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("addArticle: 아티클 추가할 때 title이 null이면 실패한다.")
    @Test
    public void addArticleNullValidation() throws Exception {
        // given
        final String url = "/api/articles";
        final String title = null;
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        final String requestBody = objectMapper.writeValueAsString(userRequest);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody));

        // then
        result.andExpect(status().isBadRequest());
    }

    @DisplayName("addArticle: 아티클 추가할 때 title이 10자를 넘으면 실패한다.")
    @Test
    public void addArticleSizeValidation() throws Exception {
        // given
        Faker faker = new Faker();

        final String url = "/api/articles";
        final String title = faker.lorem().characters(11);
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        final String requestBody = objectMapper.writeValueAsString(userRequest);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody));

        // then
        result.andExpect(status().isBadRequest());
    }

    @DisplayName("findAllArticles: 블로그 글 목록 조회에 성공한다.")
    @Test
    public void findAllArticles() throws Exception {
        // given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";

        blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        // when
        final ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].title").value(title));
    }


    @DisplayName("findArticle : 블로그 글 조회에 성공하기")
    @Test
    public void findArticle() throws Exception {
        // given
        final String url = "/api/articles/{id}";
        final  String title = "title";
        final String content = "content";
        // 블로그 글 저장
        Article savedArticle = blogRepository.save(Article.builder().title(title).content(content).build());

        // when
        // 글 조회 API 호출
        final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.title").value(title));
    }

    @DisplayName("findArticle : 블로그 글 삭제에 성공하기")
    @Test
    public void deleteArticle() throws Exception {
        // given
        final String url = "/api/articles/{id}";
        final  String title = "title";
        final String content = "content";
        // 블로그 글 저장
        Article savedArticle = blogRepository.save(Article.builder().title(title).content(content).build());

        // when
        // 글 삭제 API 호출
        mockMvc.perform(delete(url, savedArticle.getId()))
                .andExpect(status().isOk());

        // then
        List<Article> articles = blogRepository.findAll();
        assertThat(articles).isEmpty();
    }
    @DisplayName("findArticle : 블로그 글 수정에 성공하기")
    @Test
    public void updateArticle() throws Exception {
        // given
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";
        // 블로그 글 저장
        Article savedArticle = blogRepository.save(Article.builder().title(title).content(content).build());

        final String newTitle = "new title";
        final String newContent = "new content";
        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);

        // when
        // 글 수정 API 호출
        ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isOk());

        Article article = blogRepository.findById(savedArticle.getId()).get();

        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }
}