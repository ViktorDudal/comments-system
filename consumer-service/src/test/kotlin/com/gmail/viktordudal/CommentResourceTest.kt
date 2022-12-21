package com.gmail.viktordudal

import com.gmail.viktordudal.service.CommentService
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectSpy
import io.restassured.RestAssured.given
import org.junit.jupiter.api.Test
import org.mockito.Mockito

@QuarkusTest
class CommentResourceTest {

    @InjectSpy
    lateinit var commentService : CommentService

    @Test
    fun testGetAllComments() {
        given()
          .`when`().get("/comments")
          .then()
             .statusCode(200)

        Mockito.verify(commentService, Mockito.times(1)).getAllComments();
    }

}