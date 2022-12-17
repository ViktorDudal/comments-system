package com.gmail.viktordudal

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
@Cacheable
class Comment : PanacheEntity {
    companion object: PanacheCompanion<Comment>
    lateinit var postId: String
    lateinit var commentMessage: String
    lateinit var dateTime: String
    @Enumerated(EnumType.STRING)
    lateinit var messageType: MessageType

    constructor()

    constructor(postId: String, commentMessage: String, dateTime: String, messageType: MessageType) {
        this.postId = postId
        this.commentMessage = commentMessage
        this.dateTime = dateTime
        this.messageType = messageType
    }
}