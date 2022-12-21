package com.gmail.viktordudal

import com.gmail.viktordudal.service.CommentService
import org.jboss.resteasy.reactive.RestPath
import javax.enterprise.context.ApplicationScoped
import javax.transaction.Transactional
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Path("comments")
@ApplicationScoped
@Produces("application/json")
class CommentResource(
    private var commentService: CommentService
)
{

    @GET
    @Transactional
    fun getAll() = commentService.getAllComments()

    @GET
    @Path("{id}")
    fun getSingleComment(@RestPath id: Long)= commentService.getSingleComment(id)

    @GET
    @Path("searchWhitelisted")
    fun getWhitelistedComments() = commentService.getWhitelistedComments()

    @GET
    @Path("searchBlacklisted")
    fun getBlacklistedComments() = commentService.getBlacklistedComments()

}