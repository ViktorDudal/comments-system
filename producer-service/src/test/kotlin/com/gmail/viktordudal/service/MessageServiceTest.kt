package com.gmail.viktordudal.service

import com.gmail.viktordudal.model.Comment
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class MessageServiceTest {

    @MockK
    private lateinit var messageSender : KafkaMessageProducer

    @InjectMockKs
    private lateinit var messageService: MessageService

    @Test
    fun sendMessageTest() {
        val comment = Comment("postId", "Correct comment message!")
        every {messageSender.sendMessage(comment)} returns Unit
        messageService.sendMessage(comment)
        verify { messageSender.sendMessage(comment) }
    }

}