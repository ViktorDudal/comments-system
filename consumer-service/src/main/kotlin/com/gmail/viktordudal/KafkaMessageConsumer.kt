package com.gmail.viktordudal

import io.smallrye.reactive.messaging.annotations.Blocking
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.eclipse.microprofile.reactive.messaging.Incoming
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.context.control.ActivateRequestContext
import javax.transaction.Transactional

@ApplicationScoped
@ActivateRequestContext
class KafkaMessageConsumer {

    @Incoming("comments")
    @Blocking
    @Transactional
    fun retrieveComment(record: ConsumerRecord<String, String>) {
        val comment = Comment(record.key(), record.value())
        println("Get next comment: postId = ${comment.postId} with message - ${comment.commentMessage}, time - ${record.timestamp()}")
    }

    @Incoming("comments_blacklist")
    @Blocking
    @Transactional
    fun retrieveBlackListComment(record: ConsumerRecord<String, String>) {
        val comment = Comment(record.key(), record.value())
        println("Get next comment: postId = ${comment.postId} with message - ${comment.commentMessage}, time - ${record.timestamp()}")
    }

}