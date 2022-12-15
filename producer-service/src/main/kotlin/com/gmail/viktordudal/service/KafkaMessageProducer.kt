package com.gmail.viktordudal.service

import com.gmail.viktordudal.model.Comment
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata
import mu.KotlinLogging
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import org.eclipse.microprofile.reactive.messaging.Message
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class KafkaMessageProducer(
    @Channel(value = "comments_requests")
    private val emitter: Emitter<String>
) {

    fun sendMessage(comment: Comment) {
        emitter.send(createCommentMessage(comment))
    }

    private fun createCommentMessage(comment: Comment): Message<String?> {
        return  Message.of(comment.commentMessage)
            .addMetadata(
                OutgoingKafkaRecordMetadata.OutgoingKafkaRecordMetadataBuilder<String>()
                    .withKey(comment.postId).build()
            )
    }

}