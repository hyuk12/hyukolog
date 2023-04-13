package com.hyukolog.api.service;

import com.hyukolog.api.domain.Post;
import com.hyukolog.api.repository.PostRepository;
import com.hyukolog.api.request.PostCreate;
import com.hyukolog.api.request.PostSearch;
import com.hyukolog.api.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        postRepository.save(post);
    }

    public PostResponse get(Long id) {
        Post post =  postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));

        return new PostResponse(post);
    }

    public List<PostResponse> getList(PostSearch postSearch) {
        // web -> page 1 -> 0

        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

}
