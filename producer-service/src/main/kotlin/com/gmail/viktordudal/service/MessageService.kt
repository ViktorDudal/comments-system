package com.gmail.viktordudal.service

import com.gmail.viktordudal.model.Comment
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class MessageService(
    private val messageSender : KafkaMessageProducer
) {
    fun sendMessage(comment: Comment) = messageSender.sendMessage(comment)
}