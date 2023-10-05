package com.sauvageboris.dao;

import com.sauvageboris.dao.entity.Post;

import java.util.List;

public interface PostDao {
    void create(Post post);

    Post findById(Long postId);

    List<Post> findAll();
}
