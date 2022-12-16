package com.gmail.viktordudal

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.apache.commons.lang3.RandomStringUtils
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test

@QuarkusTest
class CommentResourceTest {

    @Test
    fun testSuccessCommentMessage() {
        given()
            .body("{\"postId\": \"postId\", \"commentMessage\": \"Correct comment message\"}")
            .header("Content-Type", "application/json")
        .`when`()
            .post("/comments")
        .then()
            .statusCode(204)
            .contentType(ContentType.JSON)
            .body("postId", `is`("postId"), "commentMessage", `is`("Correct comment message"))
    }

    @Test
    fun testPostIdNullCommentMessage() {
        given()
            .body("{\"postId\": null, \"commentMessage\": \"Correct comment message!\"}")
            .header("Content-Type", "application/json")
        .`when`()
            .post("/comments")
        .then()
            .statusCode(400)
            .body(
                "violations.field[0]", `is`("postComment.comment.postId"),
                "violations.message[0]", `is`("Field 'postId' must not be null nor empty!")
            )
    }

    @Test
    fun testPostIdEmptyCommentMessage() {
        given()
            .body("{\"postId\": \"\", \"commentMessage\": \"Correct comment message!\"}")
            .header("Content-Type", "application/json")
        .`when`()
            .post("/comments")
        .then()
            .statusCode(400)
            .body(
                "violations.field[0]", `is`("postComment.comment.postId"),
                "violations.message[0]", `is`("Field 'postId' must not be null nor empty!")
            )
    }

    @Test
    fun testNullCommentMessage() {
        given()
            .body("{\"postId\": \"postId\", \"commentMessage\": null}")
            .header("Content-Type", "application/json")
        .`when`()
            .post("/comments")
        .then()
            .statusCode(400)
            .body(
                "violations.field[0]", `is`("postComment.comment.commentMessage"),
                "violations.message[0]", `is`("Field 'commentMessage' must not be null!")
            )
    }

    @Test
    fun testToShortCommentMessage() {
        given()
            .body("{\"postId\": \"postId\", \"commentMessage\": \"Short\"}")
            .header("Content-Type", "application/json")
        .`when`()
            .post("/comments")
        .then()
            .statusCode(400)
            .body(
                "violations.field[0]", `is`("postComment.comment.commentMessage"),
                "violations.message[0]", `is`("Comment message length must be from 10 to 1024 characters")
            )
    }

    @Test
    fun testToLongCommentMessage() {
        given()
            .body("{\"postId\": \"postId\", \"commentMessage\": \"${RandomStringUtils.random(1025)}\"}")
            .header("Content-Type", "application/json")
        .`when`()
            .post("/comments")
        .then()
            .statusCode(400)
            .contentType(ContentType.JSON)
            .body(
                "violations.field[0]", `is`("postComment.comment.commentMessage"),
                "violations.message[0]", `is`("Comment message length must be from 10 to 1024 characters")
            )
    }

    @Test
    fun testNullBothPostIdAndCommentMessage() {
        given()
            .body("{\"postId\": null, \"commentMessage\": null}")
            .header("Content-Type", "application/json")
        .`when`()
            .post("/comments")
        .then()
            .statusCode(400)
            .body(
                "violations.field[0]", `is`("postComment.comment.postId"),
                "violations.message[0]", `is`("Field 'postId' must not be null nor empty!"),
                "violations.field[1]", `is`("postComment.comment.commentMessage"),
                "violations.message[1]", `is`("Field 'commentMessage' must not be null!"),
            )
    }

}