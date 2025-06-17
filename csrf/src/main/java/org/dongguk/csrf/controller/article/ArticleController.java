package org.dongguk.csrf.controller.article;


import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.dongguk.csrf.entity.Article;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class ArticleController {
    private final List<Article> posts = new ArrayList<>();

    @PostConstruct
    public void initDummyPosts() {
        posts.add(new Article("환영합니다!", "이 게시판은 테스트용입니다.", "admin"));
        posts.add(new Article("CSRF란 무엇인가", "<a href='/attacker.html'>이벤트 참여하고 선물 받기 🎁</a>", "attacker"));
    }

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

//      1. CSRF 토큰 검증
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
    @ResponseBody
    public String viewPost(@PathVariable int id) {
        if (id < 0 || id >= posts.size()) {
            return "<p>해당 게시글을 찾을 수 없습니다.</p><a href='/posts'>목록으로</a>";
        }
        Article post = posts.get(id);
        return "<h2>" + post.getTitle() + "</h2>" +
                "<p><b>작성자:</b> " + post.getAuthor() + "</p>" +
                "<p>" + post.getContent() + "</p>" +
                "<button type='submit'><a href='/posts'>목록으로</a></button>";
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
        sb.append("<a href='/login-form'><button>로그아웃/로그인</button></a>");

        return sb.toString();
    }

    @GetMapping("/login-form")
    @ResponseBody
    public String loginForm() {
        return """
            <h2>로그인이 필요합니다</h2>
            <form method='post' action='/login'>
                <label for='username'>사용자 이름:</label>
                <input id='username' name='username' placeholder='username' required>
                <button type='submit'>Login</button>
            </form>
        """;
    }
}