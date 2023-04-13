package com.hyukolog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyukolog.api.domain.Post;
import com.hyukolog.api.repository.PostRepository;
import com.hyukolog.api.request.PostCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// 글 제목
// 글 내용
// 사용자
// id
// user
// level

/***
 *  {
 *      "title": "글제목입니다.",
 *      "content": "글내용입니다.."
 *      "user": {
 *          "id": 1,
 *          "username": "hyukolog",
 *          "level": 1
 *      }
 *  }
 */

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    // 각각의 메소드 테스트 실행전에 실행되는 메소드 @BeforeEach 의 역할
    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청시 Hello World! 를 반환한다. (성공)")
    void test() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("글제목입니다.")
                .content("글내용입니다..")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                ) // application/json
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청시 title 값은 필수다  (성공)")
    void test2() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .content("글내용입니다..")
                .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                ) // application/json
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청시 db에 값이 저장된다  (성공)")
    void test3() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("글제목입니다.")
                .content("글내용입니다..")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                ) // application/json
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("글제목입니다.", post.getTitle());
        assertEquals("글내용입니다..", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        // given
        Post post = Post.builder()
                .title("123456789012345")
                .content("bar")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)) // application/json
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("1234567890"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());


    }

    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("혀코 제목 " + i)
                        .content("롯데타워 " + i)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON)) // application/json
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("혀코 제목 30"))
                .andExpect(jsonPath("$[0].content").value("롯데타워 30"))
                .andDo(print());


    }

    @Test
    @DisplayName("페이지를 0으로 요청하면 첫페이지를 가져온다.")
    void test6() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("혀코 제목 " + i)
                        .content("롯데타워 " + i)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(get("/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON)) // application/json
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("혀코 제목 30"))
                .andExpect(jsonPath("$[0].content").value("롯데타워 30"))
                .andDo(print());


    }
}
