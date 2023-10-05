package com.sauvageboris;

import com.sauvageboris.dao.PostDao;
import com.sauvageboris.dao.entity.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class PostDaoImpl implements PostDao {

    private EntityManagerFactory emf;

    public PostDaoImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void create(Post post) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(post);
            et.commit();
        } catch (Exception e) {
            if (et.isActive()) {
                et.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public Post findById(Long postId) {
        EntityManager em = emf.createEntityManager();
        Post post = null;

        try {
            post = em.find(Post.class, postId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        return post;
    }

    @Override
    public List<Post> findAll() {
        EntityManager em = emf.createEntityManager();
        List<Post> posts = null;

        try {
            posts = em.createQuery("SELECT p FROM Post p", Post.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        return posts;
    }

}
