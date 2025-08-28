package com.codegym.demo.exceptionHandler;

import com.codegym.demo.models.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ModelAttribute("currentUser")
    public User addCurrentUser(HttpSession session) {
        return (User) session.getAttribute("currentUser");
    }
}
