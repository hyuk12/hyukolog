package com.hyukolog.api.service;

import com.hyukolog.api.domain.Post;
import com.hyukolog.api.exception.PostNotFound;
import com.hyukolog.api.repository.PostRepository;
import com.hyukolog.api.request.PostCreate;
import com.hyukolog.api.request.PostEdit;
import com.hyukolog.api.request.PostSearch;
import com.hyukolog.api.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성 테스트")
    void test1() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("글제목입니다.")
                .content("글내용입니다.")
                .build();

        // when
        postService.write(postCreate);

        // then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals(postCreate.getTitle(), post.getTitle());
        assertEquals(postCreate.getContent(), post.getContent());

    }

    @Test
    @DisplayName("글 1개 조회 테스트")
    void test2() {
        // given
        Post requestPost = Post.builder()
                .title("11")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        // when
        PostResponse response = postService.get(requestPost.getId());

        // then
        assertNotNull(response);
        assertEquals(1L, postRepository.count());
        assertEquals("11", response.getTitle());
        assertEquals("bar", response.getContent());
    }

    @Test
    @DisplayName("글 1 page 조회")
    void test3() {
        // given

        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("혀코 제목 " + i)
                        .content("롯데타워 " + i)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();

        // when
        List<PostResponse> posts = postService.getList(postSearch);

        // then

        assertEquals(10L, posts.size());
        assertEquals("혀코 제목 30", posts.get(0).getTitle());
        assertEquals("혀코 제목 26", posts.get(4).getTitle());
    }

    @Test
    @DisplayName("글 내용 수정")
    void test4() {
        // given

        Post post = Post.builder()
                .title("혀코 제목")
                .content("롯데타워")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title(null)
                .content("삼정타워")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changePost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다." + post.getId()));
        assertEquals("혀코 제목", changePost.getTitle());
        assertEquals("삼정타워", changePost.getContent());
    }

    @Test
    @DisplayName("글 삭제")
    void test5() {
        // given
        Post post = Post.builder()
                .title("혀코 제목")
                .content("롯데타워")
                .build();

        postRepository.save(post);

        // when
        postService.delete(post.getId());

        // then
        assertEquals(0L, postRepository.count());
    }

    @Test
    @DisplayName("글 1개 조회 실패 테스트")
    void test6() {
        // given
        Post post = Post.builder()
                .title("11")
                .content("bar")
                .build();
        postRepository.save(post);

        // excepted
        PostNotFound e = assertThrows(PostNotFound.class, () -> {
            postService.get(post.getId() + 1L);
        });

        assertEquals("해당 게시글을 찾을 수 없습니다.", e.getMessage());

    }

    @Test
    @DisplayName("존재하지 않는 글 내용 수정")
    void test7() {
        // given

        Post post = Post.builder()
                .title("혀코 제목")
                .content("롯데타워")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title(null)
                .content("삼정타워")
                .build();

        // when
        // excepted
        PostNotFound e = assertThrows(PostNotFound.class, () -> {
            postService.get(post.getId() + 1L);
        });

        assertEquals("해당 게시글을 찾을 수 없습니다.", e.getMessage());
    }

    @Test
    @DisplayName("없는 게시물 글 삭제")
    void test8() {
        // given
        Post post = Post.builder()
                .title("혀코 제목")
                .content("롯데타워")
                .build();

        postRepository.save(post);

        // excepted
        PostNotFound e = assertThrows(PostNotFound.class, () -> {
            postService.get(post.getId() + 1L);
        });

        assertEquals("해당 게시글을 찾을 수 없습니다.", e.getMessage());
    }
}

