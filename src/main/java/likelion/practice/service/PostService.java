package likelion.practice.service;

import likelion.practice.dto.PostDTO;
import likelion.practice.entity.Post;
import likelion.practice.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // 게시물 작성
    public Post createPost(PostDTO postDTO) {
        if (postDTO.getImageUrls() == null || postDTO.getImageUrls().size() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미지는 최소 2장 이상 필요합니다.");
        }

        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setImageUrls(postDTO.getImageUrls());

        return postRepository.save(post);
    }

    // 게시물 검색
    public List<Post> searchPosts(String keyword) {
        return postRepository.findByTitleOrContentContaining(keyword);
    }

    // 게시물 편집
    public Post updatePost(Long postId, PostDTO updatedPostDTO) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다."));

        post.setTitle(updatedPostDTO.getTitle());
        post.setContent(updatedPostDTO.getContent());
        post.setImageUrls(updatedPostDTO.getImageUrls());

        // 수정된 게시물 저장 (수정일 자동 반영)
        return postRepository.save(post);
    }
}
