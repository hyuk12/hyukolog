package com.hyukolog.api.controller;

import com.hyukolog.api.domain.Post;
import com.hyukolog.api.exception.InvalidRequest;
import com.hyukolog.api.request.PostCreate;
import com.hyukolog.api.request.PostEdit;
import com.hyukolog.api.request.PostSearch;
import com.hyukolog.api.response.PostResponse;
import com.hyukolog.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// ssr -> jsp, thymeleaf, mustache, freemarker
// spa -> react, vue, angular
// vue -> vue + SSR = nuxt.js
// react -> react + SSR = next.js

// 데이터를 검증하는 이유?

// 1. client 개발자가 깜빡할 수 있다. 실수로 값을 안보낼 수 있다.
// 2. client bug 로 값이 누락될 수 있다.
// 3. 외부에 나쁜 사람이 값을 임의로 조작해서 보낼수 있다.
// 4. DB에 값을 저장할 때 의도치 않은 오류가 발생할 수 있다.
// 5. 서버 개발자의 편안함을 위해서

// 개발 팁!
// 1. 무언가 3번이상 반복작업을 할 때 내가 뭔가 잘못하고 있는건 아닐지 의심한다.


@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    // 글 등록
    // POST Method
    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request, @RequestHeader String authorization){
        // Case1. 저장한 데이터 Entity -> response 로 응답하기
        // Case2. 저장한 데이터의 primary_id -> response 로 응답하기
        // Case3. 응답 필요 없음 -> 클라이언트에서 모든 POST(글) 데이터 context 를 관리함
        if(authorization.equals("hyuko")){
            request.validate();
            postService.write(request);
        }

    }

    /**
     *
     *  /posts/{postId} -> 글 1개 조회
     */

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
    }

    /**
     *  조회 API
     *  여러개 글을 조회하는 API
     *  /posts -> 글 전체 조회(검색 + 페이징)
     */

    @GetMapping("/posts")
    public List<PostResponse> getList(PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit postEdit){
        postService.edit(postId, postEdit);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId){
        postService.delete(postId);
    }

}
