package com.gmail.viktordudal

import com.gmail.viktordudal.model.Comment
import com.gmail.viktordudal.service.MessageService
import io.smallrye.common.annotation.NonBlocking
import javax.validation.Valid
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.core.MediaType

@Path("/send-comment")
class CommentSenderResource(
    private val messageService : MessageService
) {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @NonBlocking
    fun postComment(@Valid comment: Comment) :Comment {
        messageService.sendMessage(comment)
        return comment
    }
}