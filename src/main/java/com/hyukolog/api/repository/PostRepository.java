package com.hyukolog.api.repository;

import com.hyukolog.api.domain.Post;
import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post, Long> {

}
