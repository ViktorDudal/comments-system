package com.gmail.viktordudal

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import javax.persistence.Cacheable
import javax.persistence.Entity

@Entity
@Cacheable
class Comment : PanacheEntity {
    companion object: PanacheCompanion<Comment>
    lateinit var postId: String
    lateinit var commentMessage: String

    constructor()

    constructor(postId: String, commentMessage: String) {
        this.postId = postId
        this.commentMessage = commentMessage
    }
}