package com.gmail.viktordudal

import com.gmail.viktordudal.models.Comment
import com.gmail.viktordudal.models.MessageType
import org.jboss.resteasy.reactive.RestPath
import javax.enterprise.context.ApplicationScoped
import javax.transaction.Transactional
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Path("comments")
@ApplicationScoped
@Produces("application/json")
class CommentResource {

    @GET
    @Transactional
    fun getAllComments() = Comment.listAll()

    @GET
    @Path("{id}")
    fun getSingleComment(@RestPath id: Long)= Comment.findById(id)

    @GET
    @Path("searchWhitelisted")
    fun getWhitelistedComments() = Comment.list("messageType", MessageType.WHITELIST)

    @GET
    @Path("searchBlacklisted")
    fun getBlacklistedComments() = Comment.list("messageType", MessageType.BLACKLIST)

}