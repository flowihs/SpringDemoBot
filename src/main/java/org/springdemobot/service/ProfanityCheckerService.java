package org.springdemobot.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ProfanityCheckerService {
    private final List<String> profanityList;

    public ProfanityCheckerService() throws IOException {
        this.profanityList = Files.readAllLines(Paths.get("src/main/resources/profanity_words.txt"));
    }

    public boolean isProfanity(String messageText) {
        String lowerCaseMessageText = messageText.toLowerCase();
        return profanityList.contains(lowerCaseMessageText);
    }
}