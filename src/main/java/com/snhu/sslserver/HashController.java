package com.snhu.sslserver;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Controller
public class HashController {

    private static final String USER = "Erik"; // Hardcoded username
    private static final int MAX_LENGTH = 50; // Max length constant
    private static final String INDEX = "index"; // Placeholder for index

    // Renders the welcome page
    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("user", USER);
        return INDEX;
    }

    // Test route for GlobalExceptionHandler
    @GetMapping("/test-error")
    public String testError() {
        throw new TestRouteException("Testing the GlobalExceptionHandler");
    }

    // Route mapping to return hashed value of data string
    @PostMapping("/hash")
    public String getHash(@RequestParam(required = false) String data, Model model) { // required = false to do validation manually
        model.addAttribute("user", USER);

        // Input validation on null data parameter
        if (data == null) {
            model.addAttribute("errorMessage", "Please enter a valid string.");
            return INDEX;
        }

        // Input validation on data parameter length
        if (data.length() > MAX_LENGTH) {
            model.addAttribute("errorMessage", "String exceeds maximum length of " + MAX_LENGTH);
            return INDEX;
        }

        // Also needs rate limiting to prevent DoS attacks

        model.addAttribute("originalData", data);
        model.addAttribute("hash", computeHash(data));
        return INDEX;
    }

    private String computeHash(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new HashingException("Algorithm not found", e);
        }
    }
}
