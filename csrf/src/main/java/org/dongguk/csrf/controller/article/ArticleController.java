package org.dongguk.csrf.controller.article;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.dongguk.csrf.entity.Article;
import org.dongguk.csrf.model.article.CreateArticleDto;
import org.dongguk.csrf.model.auth.LoginDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class ArticleController {
    private final List<Article> posts = new ArrayList<>();

    @GetMapping("/new.html")
    public String newPostForm(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) return "redirect:/login-form";

        String csrfToken = UUID.randomUUID().toString();
        session.setAttribute("csrfToken", csrfToken);
        model.addAttribute("csrfToken", csrfToken);
        return "new";
    }

    @PostMapping("/article")
    public Object createPost(@RequestParam String title,
                             @RequestParam String content,
                             @RequestParam(required = false) String csrfToken,
                             HttpSession session,
                             HttpServletRequest request) {
        String user = (String) session.getAttribute("user");
        if (user == null) return "redirect:/login-form";

        // 1. CSRF 토큰 검증
        String expected = (String) session.getAttribute("csrfToken");
        if (expected == null || !expected.equals(csrfToken)) {
            return ResponseEntity.status(403).body("CSRF Token Invalid");
        }

        // 2. Origin 헤더 검증
        String origin = request.getHeader("Origin");
        if (origin != null && !origin.equals("http://localhost:8080")) {
            return ResponseEntity.status(403).body("Invalid Origin: " + origin);
        }

        Article newPost = new Article(title, content, user);
        posts.add(newPost);
        return "redirect:/posts";
    }

    @GetMapping("/posts/{id}")
    public String viewPost(@PathVariable int id, Model model) {
        if (id >= posts.size()) return "redirect:/posts";
        model.addAttribute("post", posts.get(id));
        return "post";
    }

    @GetMapping("/posts")
    @ResponseBody
    public String allPosts() {
        StringBuilder sb = new StringBuilder("<h1>All Posts</h1>");

        sb.append("<p><strong>제목</strong> | <strong>작성자</strong></p>");
        for (int i = 0; i < posts.size(); i++) {
            sb.append("<p><a href='/posts/").append(i).append("'>")
                    .append(posts.get(i).getTitle()).append(" | ")
                    .append(posts.get(i).getAuthor())
                    .append("</a></p>");
        }

        sb.append("<a href='/new.html'><button>새 글 작성</button></a><br><br>");
        sb.append("<a href='/login-form'><button>로그인</button></a>");

        return sb.toString();
    }

    @GetMapping("/login-form")
    @ResponseBody
    public String loginForm() {
        return """
            <form method='post' action='/login'>
                <input name='username' placeholder='username'>
                <button>Login</button>
            </form>
        """;
    }
}