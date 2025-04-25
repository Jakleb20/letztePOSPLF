package com.example.letzteplf.rest;

import com.example.letzteplf.jwt.JwtAuthenticationResponse;
import com.example.letzteplf.models.MyUserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1")
public class MyRestController {

    private final MyService service;

    public MyRestController(MyService service) {
        this.service = service;
    }


    @PostMapping("public/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody MyUserDTO user) {
        String token = service.signin(user.getUsername(), user.getPassword());
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(token);
        return ResponseEntity.ok(jwtAuthenticationResponse);
    }


    @GetMapping("user/getNumberOfPosts")
    public ResponseEntity<Map<String, Long>> getNumberOfPosts() {
        long count = service.getNumberOfPosts();
        return ResponseEntity.ok(Map.of("number", count));
    }


    @GetMapping("user/getAverageRatingOfAllPosts")
    public ResponseEntity<Map<String, Double>> getAverageRatingOfAllPosts() {
        Map<String, Double> result = service.getAverageRatingOfAllPosts();
        return ResponseEntity.ok(result);
    }


    @GetMapping("user/getWhoWroteTheMostComments")
    public ResponseEntity<Map<String, Integer>> getWhoWroteTheMostComments() {
        Map<String, Integer> result = service.getWhoWroteTheMostComments();
        return ResponseEntity.ok(result);
    }

}
