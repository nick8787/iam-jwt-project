package spring.security.jwt.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spring.security.jwt.model.constants.ApiLogMessage;
import spring.security.jwt.model.dto.post.PostDTO;
import spring.security.jwt.model.request.post.PostRequest;
import spring.security.jwt.model.IamResponse;
import spring.security.jwt.service.PostService;
import spring.security.jwt.utils.ApiUtils;

import javax.validation.Valid;
import java.security.Principal;
import java.util.LinkedList;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${end.points.posts}")
public class PostController {
    private final PostService postService;

    @GetMapping("${end.points.id}")
    @Operation(summary = "Get post by Id", description = "Ready to use")
    public ResponseEntity<IamResponse<PostDTO>> getPostById(
            @PathVariable(name = "id") Integer userId) {

        IamResponse<PostDTO> response = postService.getById(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("${end.points.create}")
    @Operation(summary = "Create new Post", description = "Ready to use")
    public ResponseEntity<IamResponse<PostDTO>> createPost(
            @RequestBody @Valid PostRequest postRequest, Principal principal) {

        IamResponse<PostDTO> createdPost = postService.createPost(postRequest, principal.getName());
        return ResponseEntity.ok(createdPost);
    }

    @PutMapping("${end.points.id}")
    @Operation(summary = "Update post by Id", description = "Update an existing post by Id")
    public ResponseEntity<IamResponse<PostDTO>> updatePostById(
            @PathVariable(name = "id") Integer postId,
            @RequestBody @Valid PostRequest postRequest, Principal principal) {

        IamResponse<PostDTO> updatedPost = postService.updatePost(postId, postRequest, principal.getName());
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("${end.points.id}")
    @Operation(summary = "Delete post by id", description = "Ready to use")
    public ResponseEntity<Void> softDeletePost(
            @PathVariable(name = "id") Integer userId, Principal principal
    ) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        postService.softDelete(userId, principal.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "Get all posts by user")
    public ResponseEntity<IamResponse<LinkedList<PostDTO>>> getAllPostsByUser() {

        IamResponse<LinkedList<PostDTO>> response = postService.findAllPostsByUser();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all posts", description = "Get a list of all posts")
    public ResponseEntity<IamResponse<LinkedList<PostDTO>>> getAllPosts() {
        IamResponse<LinkedList<PostDTO>> response = postService.findAllPosts();
        return ResponseEntity.ok(response);
    }

}
