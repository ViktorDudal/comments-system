package com.gmail.viktordudal

import org.jboss.resteasy.reactive.RestPath
import javax.enterprise.context.ApplicationScoped
import javax.transaction.Transactional
import javax.ws.rs.*

@Path("comment")
@ApplicationScoped
@Produces("application/json")
class CommentResource {

    @GET
    @Transactional
    fun getAll() : List<Comment> {
        return Comment.listAll()
    }

    @GET
    @Path("{id}")
    fun getSingle(@RestPath id: Long): Comment {
        return Comment.findById(id)
            ?: throw WebApplicationException("Comment with id of $id does not exist.", 404)
    }

}