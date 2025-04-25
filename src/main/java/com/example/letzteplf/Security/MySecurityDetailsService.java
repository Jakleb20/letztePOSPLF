package com.example.letzteplf.Security;

import com.example.letzteplf.models.Student;
import com.example.letzteplf.repos.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MySecurityDetailsService implements UserDetailsService {
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = studentRepository.findStudentByName(username);

        if (student == null) {
            return null;
        }

        String role = "USER";

        return User.builder()
                .password(student.getPassword())
                .username(username)
                .roles(role)
                .build();
    }
}
