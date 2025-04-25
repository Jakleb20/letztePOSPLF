package com.example.letzteplf.db;

import com.example.letzteplf.models.Comment;
import com.example.letzteplf.models.Post;
import com.example.letzteplf.models.Student;
import com.example.letzteplf.repos.CommentRepository;
import com.example.letzteplf.repos.PostRepository;
import com.example.letzteplf.repos.StudentRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Slf4j
public class DBInit implements ApplicationRunner {

    @Value("classpath:Student.json")
    private Resource studentResource;

    @Value("classpath:PostComment.json")
    private Resource postCommentResource;

    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    private StudentRepository studentRepository;
    private CommentRepository commentRepository;
    private PostRepository postRepository;

    public DBInit(StudentRepository studentRepository,
                  CommentRepository commentRepository,
                  PostRepository postRepository,
                  ObjectMapper objectMapper,
                  PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Studenten aus der Datei lesen
        List<Student> students = objectMapper.readValue(studentResource.getFile(), new TypeReference<List<Student>>() {});

        // Alle Studenten mit verschlüsseltem Passwort speichern
        for (Student student : students) {
            student.setPassword(passwordEncoder.encode(student.getPassword()));
        }
        log.info("Studenten wurden erfolgreich in die Datenbank gespeichert.");

        // Posts und Kommentare aus der Datei lesen
        List<Post> posts = objectMapper.readValue(postCommentResource.getFile(), new TypeReference<List<Post>>() {});

        for (Post post : posts) {
            if (post.getComment() != null) {
                for (Comment comment : post.getComment()) {
                    if (comment.getStudent() != null) {
                        Student commentStudent = comment.getStudent();
                        // Student speichern, falls er noch nicht in der DB ist
                        commentStudent.setPassword(passwordEncoder.encode(commentStudent.getPassword()));
                        studentRepository.save(commentStudent);
                        comment.setStudent(commentStudent);
                    }
                    comment.setPost(post);
                }
            }

            postRepository.save(post);
            if (post.getComment() != null) {
                commentRepository.saveAll(post.getComment());
            }
        }

        log.info("Posts und Kommentare wurden erfolgreich in die Datenbank gespeichert.");
    }




}
