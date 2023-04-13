package com.hyukolog.api.repository;

import com.hyukolog.api.domain.Post;
import com.hyukolog.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
