package com.movie.booking.movie_booking_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public String greet(){
        return "Welcome to Movie APIs";
    }
}
