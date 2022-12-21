package com.gmail.viktordudal.service

import com.gmail.viktordudal.models.Comment
import com.gmail.viktordudal.models.MessageType
import io.smallrye.reactive.messaging.annotations.Blocking
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.eclipse.microprofile.reactive.messaging.Incoming
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
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
        val comment = Comment(record.key(), record.value(), getDateTime(record.timestamp()), MessageType.WHITELIST)
        comment.persistAndFlush()
    }

    @Incoming("comments_blacklist")
    @Blocking
    @Transactional
    fun retrieveBlackListComment(record: ConsumerRecord<String, String>) {
        val comment = Comment(record.key(), record.value(), getDateTime(record.timestamp()), MessageType.BLACKLIST)
        comment.persistAndFlush()
    }

    private fun getDateTime(time: Long) = Instant.ofEpochMilli(time)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
        .format(DateTimeFormatter.ISO_DATE_TIME)

}