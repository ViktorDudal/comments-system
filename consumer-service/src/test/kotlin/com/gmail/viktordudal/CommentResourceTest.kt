package com.gmail.viktordudal

import com.gmail.viktordudal.models.Comment
import com.gmail.viktordudal.models.MessageType
import com.gmail.viktordudal.service.CommentService
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectSpy
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@QuarkusTest
class CommentResourceTest {

    @InjectSpy
    lateinit var commentService : CommentService

    @Test
    fun testGetAllComments() {
        `when`(commentService.getAllComments()).thenReturn(getListOfComments)
        given()
            .`when`().get("/comments")
            .then()
            .statusCode(200)
            .body(
                containsString("postId"),
                containsString("Correct comment message"),
                containsString("2011-12-03T10:15:30")
            )

        assert(getListOfComments.count() == 5)

        verify(commentService, Mockito.times(1)).getAllComments();
    }

    @Test
    fun testGetCommentById() {
        `when`(commentService.getCommentById(1L)).thenReturn(getListOfComments[1])
        given()
            .`when`().get("/comments/1")
            .then()
            .statusCode(200)
            .body(
                containsString("postId"),
                containsString("Another correct comment message"),
                containsString("2011-12-03T10:15:30")
            )
//
//        assert(getListOfComments.count() == 5)

        verify(commentService, Mockito.times(1)).getCommentById(1L);
    }

    @Test
    fun testGetWhitelistedComments() {
        `when`(commentService.getWhitelistedComments()).thenReturn(getWhiteListedComments)
        given()
            .`when`().get("/comments/searchWhitelisted")
            .then()
            .statusCode(200)
            .body(
                containsString("${MessageType.WHITELIST}"),
            )

        assert(getWhiteListedComments.count() == 2)

        verify(commentService, Mockito.times(1)).getWhitelistedComments();
    }

    @Test
    fun testGetBlacklistedComments() {
        `when`(commentService.getBlacklistedComments()).thenReturn(getBlackListedComments)
        given()
            .`when`().get("/comments/searchBlacklisted")
            .then()
            .statusCode(200)
            .body(
                containsString("${MessageType.BLACKLIST}"),
            )

        assert(getBlackListedComments.count() == 3)

        verify(commentService, Mockito.times(1)).getBlacklistedComments();
    }

    private val getListOfComments = listOf(
        Comment(
            "postId",
            "Correct comment message",
            "2011-12-03T10:15:30",
            MessageType.WHITELIST
        ),
        Comment(
            "postId",
            "Another correct comment message",
            "2011-12-03T10:15:30",
            MessageType.WHITELIST
        ),
        Comment(
            "postId",
            "Comment message with blacklisted word - bollocks",
            "2011-12-03T10:15:30",
            MessageType.BLACKLIST
        ),
        Comment(
            "postId",
            "Comment message with blacklisted word - bullshit",
            "2011-12-03T10:15:30",
            MessageType.BLACKLIST
        ),
        Comment(
            "postId",
            "Comment message with blacklisted word - trash",
            "2011-12-03T10:15:30",
            MessageType.BLACKLIST
        )
    )

    private val getWhiteListedComments = getListOfComments.filter { it.messageType == MessageType.WHITELIST }

    private val getBlackListedComments = getListOfComments.filter { it.messageType == MessageType.BLACKLIST }

}