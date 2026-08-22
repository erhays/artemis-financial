package com.snhu.sslserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * This class serves as a catch-all for any unexpected errors that occur within the application.
 * It is not a replacement for the manual validation checks. Instead, it prevents the application
 * from displaying a full stack trace upon encountering an edge-case or unintended exception.
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String USER ="Erik";
    private static final String INDEX = "index";

    @ExceptionHandler(Exception.class)
    public String handleUnexpectedError(Exception ex, Model model) {

        // Logs errors to the console
        logger.error("Unexpected error occurred.", ex);

        // The user only sees a generic error message
        model.addAttribute("user", USER);
        model.addAttribute("errorMessage", "Something went wrong. Please try again.");
        return INDEX;
    }

}

