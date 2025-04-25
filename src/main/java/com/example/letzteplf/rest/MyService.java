package com.example.letzteplf.rest;

import com.example.letzteplf.jwt.JwtUtilities;
import com.example.letzteplf.models.Comment;
import com.example.letzteplf.models.Student;
import com.example.letzteplf.repos.CommentRepository;
import com.example.letzteplf.repos.PostRepository;
import com.example.letzteplf.repos.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JwtUtilities jwtUtilities;
    private final AuthenticationManager authenticationManager;

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;

    public MyService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public String signin(String username, String password) {
        Student student = studentRepository.findStudentByName(username.trim());

        if (student == null) {
            logger.info("--> Student not found");
            return null;
        } else {
             Authentication authentication =
                     authenticationManager.authenticate(
                             new UsernamePasswordAuthenticationToken(username, password)
                     );

            logger.info("--> Auth -> " + student);

            return jwtUtilities.generateToken(username);
        }
    }

    public long getNumberOfPosts() {
        return postRepository.count();
    }

    public Map<String, Double> getAverageRatingOfAllPosts() {
        List<Comment> comments = commentRepository.findAll();

        Map<String, List<Double>> ratingsByTitle = new HashMap<>();

        for (Comment comment : comments) {
            String title = comment.getPost().getTitle();
            double rating;
            try {
                rating = Double.parseDouble(comment.getRating());
            } catch (NumberFormatException e) {
                continue; // ungültige Bewertung überspringen
            }

            ratingsByTitle.computeIfAbsent(title, k -> new ArrayList<>()).add(rating);
        }

        // Durchschnitt berechnen
        Map<String, Double> avgRatings = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : ratingsByTitle.entrySet()) {
            List<Double> values = entry.getValue();
            double avg = values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            avgRatings.put(entry.getKey(), Math.round(avg * 1000.0) / 1000.0); // z.B. 4.667
        }

        return avgRatings;
    }

    public Map<String, Integer> getWhoWroteTheMostComments() {
        List<Comment> comments = commentRepository.findAll();

        // Kommentare zählen pro Student
        Map<String, Integer> commentCountByUser = new HashMap<>();
        for (Comment comment : comments) {
            String name = comment.getStudent().getName();
            commentCountByUser.put(name, commentCountByUser.getOrDefault(name, 0) + 1);
        }

        // Max-Wert finden
        int max = commentCountByUser.values().stream().mapToInt(i -> i).max().orElse(0);

        // Alle mit Max-Wert rausholen
        Map<String, Integer> result = new HashMap<>();
        for (Map.Entry<String, Integer> entry : commentCountByUser.entrySet()) {
            if (entry.getValue() == max) {
                result.put(entry.getKey(), entry.getValue());
            }
        }

        return result;
    }


}
