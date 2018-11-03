package com.example.springsecuritydemo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("shelf2")
public class SecondShelfController {

    @GetMapping("book1")
    public ResponseEntity<?> occupy1() {
        return new ResponseEntity<>("You can take book1.",HttpStatus.OK);
    }

    @GetMapping("book2")
    public ResponseEntity<?> occupy2() {
        return new ResponseEntity<>("You can take book2.", HttpStatus.OK);
    }
}
