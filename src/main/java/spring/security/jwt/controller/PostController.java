package spring.security.jwt.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spring.security.jwt.model.dto.post.PostRequest;
import spring.security.jwt.model.dto.post.PostDto;
import spring.security.jwt.model.IamResponse;
import spring.security.jwt.service.PostService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.LinkedList;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/${end.points.posts}")
public class PostController {
    private final PostService postService;

    @GetMapping("${end.points.id}")
    @Operation(summary = "Get post by Id", description = "Ready to use")
    public ResponseEntity<IamResponse<PostDto>> getPostById(
            @PathVariable(name = "id") Integer userId) {
        IamResponse<PostDto> response = postService.getById(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("${end.points.create}")
    @Operation(summary = "Create new Post", description = "Ready to use")
    public ResponseEntity<IamResponse<PostDto>> createPost(
            @RequestBody @Valid PostRequest postRequest, Principal principal) {
        IamResponse<PostDto> createdPost = postService.createPost(postRequest, principal.getName());
        return ResponseEntity.ok(createdPost);
    }

    @GetMapping
    @Operation(summary = "Get all posts by user")
    public ResponseEntity<IamResponse<LinkedList<PostDto>>> getAllPostsByUser() {
        IamResponse<LinkedList<PostDto>> response = postService.findAllPostsByUser();
        return ResponseEntity.ok(response);
    }

}
