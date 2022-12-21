package com.gmail.viktordudal.service

import com.gmail.viktordudal.models.Comment
import com.gmail.viktordudal.models.MessageType
import org.jboss.resteasy.reactive.RestPath
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CommentService {

    fun getAllComments() = Comment.listAll()

    fun getSingleComment(@RestPath id: Long)= Comment.findById(id)

    fun getWhitelistedComments() = Comment.list("messageType", MessageType.WHITELIST)

    fun getBlacklistedComments() = Comment.list("messageType", MessageType.BLACKLIST)

}