package me.blogSpringBoot.springbootdeveloper;

import me.blogSpringBoot.springbootdeveloper.test.Member;
import me.blogSpringBoot.springbootdeveloper.test.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 테스트용 어플리케이션 컨텍스트임을 명시
@SpringBootTest
/// MockMvc를 생성하고 자동으로 구성 / 서버에 배포하지 않고도 테스트용 mvc 환경을 만들어 요청, 전송, 응답 기능 제공
@AutoConfigureMockMvc
class TestControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MemberRepository memberRepository;

    // 테스트 실행 전 실행하는 메서드
    @BeforeEach
    public void mockMvcSetUp() {
        // MockMvc 설정
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    // 테스트 실행 후 실행하는 메서드
    @AfterEach
    public void cleanUp() {
        // 데이터 모두 삭제
        memberRepository.deleteAll();
    }

    @DisplayName("getAllMemvers: 아티클 조회에 성공한다")
    @Test
    public void getAllMembers() throws Exception {
        // given (테스트 실행을 준비하는 단계 / 저장할 새로운 정보를 입력)
        final String url = "/test";
        Member savedMember = memberRepository.save(new Member(1L, "홍길동"));

        // when (테스트를 진행하는 단계 / 저장할 새로운 정보 저장)
        final ResultActions result = mockMvc.perform(get(url) // perform : 요청을 전송하는 역할
                .accept(MediaType.APPLICATION_JSON)); // accept : 요청을 보낼 때 무슨 타입으로 응답을 받을지 결정

        // then (테스트 결과를 검증하는 단계 / 저장된 정보가 GIVEN절에서 준비한 정보와 같은지 검증)
        result.andExpect(status().isOk()) // andExpect : 응답을 검증 (isOk 메서드를 사용해 응답 코드가 200인지 확인)
                // 응답의 0번째 값이 DB에 저장한 값과 같은지 확인
                .andExpect(jsonPath("$[0].id").value(savedMember.getId())) // jsonPath("$[0]${필드명})은 json 응답값의 값을 가져오는 역할 (0번쨰 뱌열에 들어있는 객체의 필드값 가져오고 저장된 값과 같은지 확인)
                .andExpect(jsonPath("$[0].name").value(savedMember.getName()));
    }

}