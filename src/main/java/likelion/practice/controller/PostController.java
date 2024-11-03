package likelion.practice.controller;

import likelion.practice.dto.PostDTO;
import likelion.practice.entity.Post;
import likelion.practice.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시물 작성 API
    @PostMapping("/create")
    public ResponseEntity<Post> createPost(@RequestBody PostDTO postDTO) {
        Post createdPost = postService.createPost(postDTO);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    // 게시물 검색 API
    @GetMapping("/search")
    public ResponseEntity<List<Post>> searchPosts(@RequestParam String keyword) {
        List<Post> posts = postService.searchPosts(keyword);
        return ResponseEntity.ok(posts);
    }

    // 게시물 편집 API
    @PutMapping("/update/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable Long postId, @RequestBody PostDTO updatedPostDTO) {
        Post updatedPost = postService.updatePost(postId, updatedPostDTO);
        return ResponseEntity.ok(updatedPost);
    }
}
