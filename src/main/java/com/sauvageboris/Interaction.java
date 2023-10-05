package com.sauvageboris;

import com.sauvageboris.dao.CommentDao;
import com.sauvageboris.dao.CommentDaoImpl;
import com.sauvageboris.dao.PostDao;
import com.sauvageboris.dao.entity.Comment;
import com.sauvageboris.dao.entity.Post;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Interaction {

    private final CommentDao commentDao;
    private final PostDao postDao;
    private final Scanner scanner;

    public Interaction() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        this.commentDao = new CommentDaoImpl(emf);
        this.postDao = new PostDaoImpl(emf);
        this.scanner = new Scanner(System.in);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println();
            System.out.println("Fermeture du programme.");
            emf.close();
        }));

    }

    public void start() {
        boolean isContinue = true;
        while (isContinue) {
            System.out.println();
            System.out.println("Menu interactif :");
            System.out.println("1. Afficher tous les posts");
            System.out.println("2. Ajouter un post");
            System.out.println("3. Ajouter un commentaire");
            System.out.println("4. Quitter");
            System.out.print("Choisissez une option : ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    displayAllPosts();
                    break;
                case 2:
                    addPost();
                    break;
                case 3:
                    addComment();
                    break;
                case 4:
                    isContinue = false;
                    break;
                default:
                    System.out.println("Option invalide.");
            }
        }
    }

    private void addPost() {
        System.out.print("Quel est la description de votre article : ");
        String description = scanner.nextLine();

        Post newPost = new Post(description, LocalDateTime.now());
        postDao.create(newPost);
        System.out.println("Article créé avec succès");
    }


    private void addComment() {
        displayAllPosts();

        System.out.println("Sur quel post souhaitez-vous ajouter un commentaire ?");
        Long postId = scanner.nextLong();
        scanner.nextLine();

        Post selectedPost = postDao.findById(postId);
        if (selectedPost != null) {
            System.out.println("Post sélectionné : " + selectedPost.getDescription());

            System.out.print("Entrez le contenu du commentaire : ");
            String commentContent = scanner.nextLine();

            Comment newComment = new Comment(commentContent, LocalDateTime.now(), selectedPost);
            commentDao.create(newComment);
            System.out.println("Commentaire ajouté avec succès !");
        } else {
            System.out.println("Aucun post trouvé avec cet ID.");
        }

    }


    private void displayAllPosts() {
        System.out.println("Liste des posts :");
        List<Post> postList = this.postDao.findAll();
        if (postList.isEmpty()) {
            System.out.println("Aucun post dans la base de données.");
        }
        for (Post item : postList) {
            System.out.println("\n");
            System.out.println("──────────────────────────────      " + item.getId() + "        ──────────────────────────────");
            System.out.println("|       Description : " + item.getDescription());
            System.out.println("|       Date de création : " + item.getCreatedAt());
            System.out.println("───────────────────────────────────────────────────────────────────────────");
            List<Comment> comments = commentDao.findAllByPostId(item.getId());
            if (comments.isEmpty()) {
                System.out.println("|   Commentaires :       Aucun");
            } else {
                System.out.println("|   Commentaires :       Ci-dessous");
            }
            for (Comment comment : comments) {
                System.out.println(">>>> " + comment.getMessage());
            }
            System.out.println("──────────────────────────────────────────────────────────────────────────");
            System.out.println();
        }
    }
}
