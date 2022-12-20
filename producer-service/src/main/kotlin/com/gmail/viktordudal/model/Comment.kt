package com.gmail.viktordudal.model

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class Comment(
    @field:NotEmpty(message = "Field 'postId' must not be null nor empty!")
    val postId: String?,
    @field:NotNull(message = "Field 'commentMessage' must not be null!")
    @field:Size(message = "Comment message length must be from 10 to 1024 characters", min = 10, max = 1024)
    val commentMessage: String?
)