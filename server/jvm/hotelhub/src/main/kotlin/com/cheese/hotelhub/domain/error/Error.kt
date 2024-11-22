package com.cheese.hotelhub.domain.error

import org.springframework.http.HttpStatus

data class Error(val message: String, val error: String, val status: HttpStatus)