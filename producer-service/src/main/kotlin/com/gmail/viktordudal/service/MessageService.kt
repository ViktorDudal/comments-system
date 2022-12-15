package com.gmail.viktordudal.service

import com.gmail.viktordudal.model.Comment
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class MessageService(
    private val messageSender : KafkaMessageProducer
) {
    fun sendMessage(comment: Comment) {
        messageSender.sendMessage(comment)
    }
}