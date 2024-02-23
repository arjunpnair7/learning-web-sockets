package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VotingController {

    @GetMapping("/voting")
    public String getVotingController() {
        return "Voting.html";
    }
}
