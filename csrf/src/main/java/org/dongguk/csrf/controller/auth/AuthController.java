package org.dongguk.csrf.controller.auth;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    @PostMapping("/login")
    public String login(@RequestParam String username, HttpServletResponse response, HttpSession session) {
        session.setAttribute("user", username);
        response.setHeader("Set-Cookie",
                "JSESSIONID=" + session.getId() + "; HttpOnly; Path=/; SameSite=Strict");
        return "redirect:/posts";
    }
}
