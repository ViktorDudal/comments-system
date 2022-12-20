package com.gmail.viktordudal

import com.gmail.viktordudal.model.Comment
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.apache.commons.lang3.RandomStringUtils
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test

@QuarkusTest
class CommentSenderResourceTest {

    companion object {
        const val CONTENT_TYPE = "Content-Type"
        const val CONTENT_TYPE_JSON = "application/json"
        const val COMMENT_MESSAGE_FIELD = "postComment.comment.commentMessage"
        const val POST_ID_FIELD = "postComment.comment.postId"
        const val EMPTY_OR_NULL_POST_ID_ERROR_MESSAGE = "Field 'postId' must not be null nor empty!"
        const val NULL_COMMENT_MESSAGE_ERROR = "Field 'commentMessage' must not be null!"
        const val NOT_VALID_COMMENT_MESSAGE_ERROR = "Comment message length must be from 10 to 1024 characters"
        const val URL = "/send-comment"
    }

    @Test
    fun testSuccessCommentMessage() {
        given()
            .body("{\"postId\": \"postId\", \"commentMessage\": \"Correct comment message!\"}")
            .header(CONTENT_TYPE, CONTENT_TYPE_JSON)
        .`when`()
            .post(URL)
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("postId", `is`("postId"), "commentMessage", `is`("Correct comment message!"))
    }

    @Test
    fun testPostIdNullCommentMessage() {
        given()
            .body("{\"postId\": null, \"commentMessage\": \"Correct comment message!\"}")
            .header(CONTENT_TYPE, CONTENT_TYPE_JSON)
        .`when`()
            .post(URL)
        .then()
            .statusCode(400)
            .body(
                "violations.field[0]", `is`(POST_ID_FIELD),
                "violations.message[0]", `is`(EMPTY_OR_NULL_POST_ID_ERROR_MESSAGE)
            )
    }

    @Test
    fun testPostIdEmptyCommentMessage() {
        given()
            .body("{\"postId\": \"\", \"commentMessage\": \"Correct comment message!\"}")
            .header(CONTENT_TYPE, CONTENT_TYPE_JSON)
        .`when`()
            .post(URL)
        .then()
            .statusCode(400)
            .body(
                "violations.field[0]", `is`(POST_ID_FIELD),
                "violations.message[0]", `is`(EMPTY_OR_NULL_POST_ID_ERROR_MESSAGE)
            )
    }

    @Test
    fun testNullCommentMessage() {
        val comment = Comment("postId", null)
        given()
            .body("{\"postId\": \"${comment.postId}\", \"commentMessage\": ${comment.commentMessage}}")
            .header(CONTENT_TYPE, CONTENT_TYPE_JSON)
        .`when`()
            .post(URL)
        .then()
            .statusCode(400)
            .body(
                "violations.field[0]", `is`(COMMENT_MESSAGE_FIELD),
                "violations.message[0]", `is`(NULL_COMMENT_MESSAGE_ERROR)
            )
    }

    @Test
    fun testToShortCommentMessage() {
        given()
            .body("{\"postId\": \"postId\", \"commentMessage\": \"${RandomStringUtils.random(9)}\"}")
            .header(CONTENT_TYPE, CONTENT_TYPE_JSON)
        .`when`()
            .post(URL)
        .then()
            .statusCode(400)
            .body(
                "violations.field[0]", `is`(COMMENT_MESSAGE_FIELD),
                "violations.message[0]", `is`(NOT_VALID_COMMENT_MESSAGE_ERROR)
            )
    }

    @Test
    fun testToLongCommentMessage() {
        given()
            .body("{\"postId\": \"postId\", \"commentMessage\": \"${RandomStringUtils.random(1025)}\"}")
            .header(CONTENT_TYPE, CONTENT_TYPE_JSON)
        .`when`()
            .post(URL)
        .then()
            .statusCode(400)
            .contentType(ContentType.JSON)
            .body(
                "violations.field[0]", `is`(COMMENT_MESSAGE_FIELD),
                "violations.message[0]", `is`(NOT_VALID_COMMENT_MESSAGE_ERROR)
            )
    }

    @Test
    fun testNullBothPostIdAndCommentMessage() {
        val response = given()
            .body("{\"postId\": null, \"commentMessage\": null}")
            .header(CONTENT_TYPE, CONTENT_TYPE_JSON)
        .`when`()
            .post(URL)
        .then()
            .statusCode(400)
            .extract()
            .body()
            .asString()

        assert(response.contains(EMPTY_OR_NULL_POST_ID_ERROR_MESSAGE))
        assert(response.contains(NULL_COMMENT_MESSAGE_ERROR))
    }

    @Test
    fun testEmptyBothPostIdAndCommentMessage() {
        val response = given()
            .body("{\"postId\": \"\", \"commentMessage\": \"\"}")
            .header(CONTENT_TYPE, CONTENT_TYPE_JSON)
        .`when`()
            .post(URL)
        .then()
            .statusCode(400)
            .extract()
            .body()
            .asString()

        assert(response.contains(EMPTY_OR_NULL_POST_ID_ERROR_MESSAGE))
        assert(response.contains(NOT_VALID_COMMENT_MESSAGE_ERROR))
    }

}