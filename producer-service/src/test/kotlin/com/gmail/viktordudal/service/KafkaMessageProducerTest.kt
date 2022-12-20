package com.gmail.viktordudal.service

import com.gmail.viktordudal.model.Comment
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verify
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata
import org.eclipse.microprofile.reactive.messaging.Emitter
import org.eclipse.microprofile.reactive.messaging.Message
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class KafkaMessageProducerTest {

    @MockK
    private lateinit var emitter : Emitter<String>

    @InjectMockKs
    private lateinit var kafkaMessageProducer: KafkaMessageProducer

    @Test
    @Disabled
    fun sendMessageTest() {
        val comment = Comment("id2", "Correct comment message!")
        val message = Message.of(comment.commentMessage)
            .addMetadata(
                OutgoingKafkaRecordMetadata.OutgoingKafkaRecordMetadataBuilder<String>()
                    .withKey(comment.postId).build()
            )
        justRun {emitter.send(message)}
        kafkaMessageProducer.sendMessage(comment)
        verify { emitter.send(message) }
    }

}