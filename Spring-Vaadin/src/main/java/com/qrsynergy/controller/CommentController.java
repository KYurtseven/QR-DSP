package com.qrsynergy.controller;

import com.qrsynergy.controller.helper.CommentDTO;
import com.qrsynergy.controller.helper.CommentResponse;
import com.qrsynergy.controller.helper.FailureMessage;
import com.qrsynergy.controller.helper.ResponseStatusType;
import com.qrsynergy.model.Comment;
import com.qrsynergy.model.QR;
import com.qrsynergy.model.User;
import com.qrsynergy.model.helper.CommentEntry;
import com.qrsynergy.model.helper.RightType;
import com.qrsynergy.service.CommentService;
import com.qrsynergy.service.QRService;
import com.qrsynergy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/comment")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CommentController {
    @Autowired
    QRService qrService;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;

    /**
     * REST API: /api/comment/add
     * ex:
     * localhost:8080/api/comment/add
     *
     * Correct input
     * {
     * 	"url" : "dfd76e6b-5b09-4dd6-acd5-1ac14f37dc83",
     * 	"email" : "koray.can.yurtseven@gmail.com",
     * 	"message" : "Here is the comment that I want to add"
     * }
     * @param commentDTO contains information to find comment, user
     * @return success or failure
     */
    @PostMapping("/add")
    public ResponseEntity addComment(@Valid @RequestBody CommentDTO commentDTO){
        CommentResponse commentResponse;
        // Find comment document from database using url
        Comment comment = commentService.findComment(commentDTO.getUrl());
        if(comment == null) {
            commentResponse = new CommentResponse(FailureMessage.QR_NOT_FOUND);
            return ResponseEntity.badRequest().body(commentResponse);
        }
        // Find user document from database using email, to make sure that email exists
        User user = userService.findByEmail(commentDTO.getEmail());
        if(user == null) {
            // Invalid user
            commentResponse = new CommentResponse(FailureMessage.INVALID_EMAIL);
            return ResponseEntity.badRequest().body(commentResponse);
        }
        // TODO
        // Find QR document from the database
        // Normally, when a comment entry is present, we don't need to check this
        // Just to be sure, maybe it will be fixed in the future
        QR qr = qrService.findQRByUrl(commentDTO.getUrl());
        if(qr == null){
            // Invalid url
            commentResponse = new CommentResponse(FailureMessage.QR_NOT_FOUND);
            return ResponseEntity.badRequest().body(commentResponse);
        }
        // Even though qr is expired, you can comment
        // So, no check on expiration date of the QR

        // make sure that this user have rights on the document (owner, edit, view)
        // public and no rights cannot add comment
        // Find QR to find rights
        RightType rightType = qr.findUsersRightInQR(user.getEmail());
        if( rightType == RightType.EDIT ||
            rightType == RightType.VIEW ||
            rightType == RightType.OWNER){

            // Create a commentEntry
            CommentEntry commentEntry = new CommentEntry(user, commentDTO.getMessage());
            // save to the database
            commentService.addCommentEntry(comment, commentEntry);

            commentResponse = new CommentResponse();
            commentResponse.setStatus(ResponseStatusType.SUCCESS);
            return ResponseEntity.badRequest().body(commentResponse);
        }
        else{
            // Invalid rights
            commentResponse = new CommentResponse(FailureMessage.COMMENT_NOT_ENOUGH_RIGHTS);
            return ResponseEntity.badRequest().body(commentResponse);
        }


    }
}
