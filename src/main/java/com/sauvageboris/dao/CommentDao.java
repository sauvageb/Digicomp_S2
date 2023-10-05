package com.sauvageboris.dao;

import com.sauvageboris.dao.entity.Comment;

import java.util.List;

public interface CommentDao {

    void create(Comment comment);

    public Comment findById(Long commentId);

    public void update(Comment comment);

    public void delete(Long commentId);

    public List<Comment> findAll();

    List<Comment> findAllByPostId(Long id);
}
