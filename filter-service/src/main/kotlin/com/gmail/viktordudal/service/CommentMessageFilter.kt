package com.gmail.viktordudal.service

import com.gmail.viktordudal.models.Comment
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.eclipse.microprofile.reactive.messaging.Message
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CommentMessageFilter(
    @Channel("comments")
    private val whiteListEmitter: Emitter<String>,
    @Channel("comments_blacklist")
    private val blackListEmitter: Emitter<String>
) {

    private final val blackList = listOf("rusnia", "gribana")

    @Incoming("comments_requests")
    fun retrieveComment(record: ConsumerRecord<String, String>) {
        val comment = Comment(record.key(), record.value())
        commentMessageVerification(comment)
    }

    private fun commentMessageVerification(comment: Comment) {
        if (blackList.stream().anyMatch { comment.commentMessage.contains(it) } ) {
            sendBadMessage(comment)
        } else {
            sendGoodMessage(comment)
        }
    }

    private fun sendGoodMessage(comment: Comment) = whiteListEmitter.send(createCommentMessage(comment))

    private fun sendBadMessage(comment: Comment) = blackListEmitter.send(createCommentMessage(comment))

    private fun createCommentMessage(comment: Comment) = Message.of(comment.commentMessage)
            .addMetadata(
                OutgoingKafkaRecordMetadata.OutgoingKafkaRecordMetadataBuilder<String>()
                    .withKey(comment.postId).build()
            )

}