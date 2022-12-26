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

    companion object {
        const val CORRECT_COMMENT_MESSAGE = "Correct comment message"
        const val INCORRECT_COMMENT_MESSAGE = "Comment message with blacklisted word"
        const val POST_ID_FIELD = "postId"
        const val TIMESTAMP_FIELD = "2011-12-03T10:15:30"
        const val URL = "/comments"
    }

    @InjectSpy
    lateinit var commentService : CommentService

    @Test
    fun testGetAllComments() {
        `when`(commentService.getAllComments()).thenReturn(getListOfComments)
        given()
            .`when`().get(URL)
            .then()
            .statusCode(200)
            .body(
                containsString(POST_ID_FIELD),
                containsString(CORRECT_COMMENT_MESSAGE),
                containsString(TIMESTAMP_FIELD)
            )

        assert(getListOfComments.count() == 5)

        verify(commentService, Mockito.times(1)).getAllComments();
    }

    @Test
    fun testGetCommentById() {
        `when`(commentService.getCommentById(1L)).thenReturn(getListOfComments[1])
        given()
            .`when`().get("$URL/1")
            .then()
            .statusCode(200)
            .body(
                containsString(POST_ID_FIELD),
                containsString(CORRECT_COMMENT_MESSAGE),
                containsString(TIMESTAMP_FIELD)
            )

        verify(commentService, Mockito.times(1)).getCommentById(1L);
    }

    @Test
    fun testGetWhitelistedComments() {
        `when`(commentService.getWhitelistedComments()).thenReturn(getWhiteListedComments)
        given()
            .`when`().get("$URL/searchWhitelisted")
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
            .`when`().get("$URL/searchBlacklisted")
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
            POST_ID_FIELD,
            CORRECT_COMMENT_MESSAGE,
            TIMESTAMP_FIELD,
            MessageType.WHITELIST
        ),
        Comment(
            POST_ID_FIELD,
            CORRECT_COMMENT_MESSAGE,
            TIMESTAMP_FIELD,
            MessageType.WHITELIST
        ),
        Comment(
            POST_ID_FIELD,
            "$INCORRECT_COMMENT_MESSAGE - bollocks",
            TIMESTAMP_FIELD,
            MessageType.BLACKLIST
        ),
        Comment(
            POST_ID_FIELD,
            "$INCORRECT_COMMENT_MESSAGE - bullshit",
            TIMESTAMP_FIELD,
            MessageType.BLACKLIST
        ),
        Comment(
            POST_ID_FIELD,
            "$INCORRECT_COMMENT_MESSAGE - trash",
            TIMESTAMP_FIELD,
            MessageType.BLACKLIST
        )
    )

    private val getWhiteListedComments = getListOfComments.filter { it.messageType == MessageType.WHITELIST }

    private val getBlackListedComments = getListOfComments.filter { it.messageType == MessageType.BLACKLIST }

}