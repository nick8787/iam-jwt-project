package spring.security.jwt.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spring.security.jwt.model.IamResponse;
import spring.security.jwt.model.constants.ApiLogMessage;
import spring.security.jwt.model.dto.comment.CommentDTO;
import spring.security.jwt.model.request.comment.CommentRequest;
import spring.security.jwt.service.CommentService;
import spring.security.jwt.utils.ApiUtils;

import javax.validation.Valid;
import java.security.Principal;
import java.util.LinkedList;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${end.points.comments}")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("${end.points.id}")
    @Operation(summary = "Get comment by Id", description = "Ready to use")
    public ResponseEntity<IamResponse<CommentDTO>> getCommentById(
            @PathVariable(name = "id") Integer commentId) {

        IamResponse<CommentDTO> response = commentService.getCommentById(commentId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("${end.points.create}")
    @Operation(summary = "Create new Comment", description = "Ready to use")
    public ResponseEntity<IamResponse<CommentDTO>> createComment(
            @RequestBody @Valid CommentRequest commentRequest, Principal principal) {

        IamResponse<CommentDTO> response = commentService.createComment(commentRequest, principal.getName());
        return ResponseEntity.ok(response);
    }

    @PutMapping("${end.points.id}")
    @Operation(summary = "Update comment by Id", description = "Update an existing comment by Id")
    public ResponseEntity<IamResponse<CommentDTO>> updateCommentById(
            @PathVariable(name = "id") Integer postId,
            @RequestBody @Valid CommentRequest commentRequest, Principal principal) {

        IamResponse<CommentDTO> response = commentService.updateComment(postId, commentRequest, principal.getName());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("${end.points.id}")
    @Operation(summary = "Delete comment by id", description = "Ready to use")
    public ResponseEntity<Void> softDeleteComment(
            @PathVariable(name = "id") Integer userId, Principal principal
    ) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        commentService.softDelete(userId, principal.getName());
        return ResponseEntity.ok().build();
    }

    // New method to get all comments
    @GetMapping("/all")
    @Operation(summary = "Get all comments", description = "Publicly accessible endpoint to view all comments")
    public ResponseEntity<IamResponse<LinkedList<CommentDTO>>> getAllComments() {
        IamResponse<LinkedList<CommentDTO>> response = commentService.findAllComments();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all comments by user")
    public ResponseEntity<IamResponse<LinkedList<CommentDTO>>> getAllCommentsByUser() {

        IamResponse<LinkedList<CommentDTO>> response = commentService.findAllCommentsByUser();
        return ResponseEntity.ok(response);
    }

}
