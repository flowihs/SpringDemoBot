package org.springdemobot.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

@Slf4j
@Service
public class ProfanityCheckerService {

    private final Set<String> profanityWords = new HashSet<>();
    private static final Pattern NON_ALPHANUMERIC_PATTERN = Pattern.compile("[^\\p{L}\\p{N}]+");

    @PostConstruct
    public void init() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(getClass()
                        .getClassLoader().getResourceAsStream("profanity_words.txt"))))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    profanityWords.add(line.trim().toLowerCase());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public boolean isProfanity(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }

        String cleanedText = NON_ALPHANUMERIC_PATTERN.matcher(text.toLowerCase()).replaceAll(" ");
        String[] words = cleanedText.split("\\s+");

        for (String word : words) {
            if (profanityWords.contains(word)) {
                return true;
            }
        }
        return false;
    }
}