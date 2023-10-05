package com.sauvageboris.dao;

import com.sauvageboris.dao.entity.Comment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentDaoImpl implements CommentDao {

    private EntityManagerFactory emf;

    public CommentDaoImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void create(Comment comment) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(comment);
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

    public Comment findById(Long commentId) {
        EntityManager em = emf.createEntityManager();
        Comment comment = null;

        try {
            comment = em.find(Comment.class, commentId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        return comment;
    }

    public List<Comment> filterCriteria() {
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();

        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
        Date threeDaysAgoDate = Date.from(threeDaysAgo.atZone(ZoneId.systemDefault()).toInstant());

        CriteriaQuery<Comment> cq = cb.createQuery(Comment.class);
        Root<Comment> commentRoot = cq.from(Comment.class);
        Predicate datePredicate = cb
                .greaterThanOrEqualTo(
                        commentRoot.get("createdAt"),
                        threeDaysAgoDate);
        cq.select(commentRoot).where(datePredicate);
        return em.createQuery(cq).getResultList();
    }


    public void update(Comment comment) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();

        try {
            et.begin();
            em.merge(comment);
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

    public void delete(Long commentId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();

        try {
            et.begin();
            Comment comment = em.find(Comment.class, commentId);
            if (comment != null) {
                em.remove(comment);
            }
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
    public List<Comment> findAllByPostId(Long postId) {
        EntityManager em = emf.createEntityManager();
        List<Comment> comments = new ArrayList<>();
        try {
            TypedQuery<Comment> query = em.createQuery("SELECT c FROM Comment c WHERE c.post.id = :postId", Comment.class);
            query.setParameter("postId", postId);
            comments = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        return comments;
    }

    public List<Comment> findAll() {
        EntityManager em = emf.createEntityManager();
        List<Comment> comments = null;

        try {
            comments = em.createQuery("SELECT c FROM Comment c", Comment.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        return comments;
    }
}
