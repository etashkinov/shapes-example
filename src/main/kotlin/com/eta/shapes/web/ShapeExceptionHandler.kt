package com.eta.shapes.web

import com.eta.shapes.api.exception.IntersectionException
import com.eta.shapes.api.exception.NameAlreadyExistsException
import com.fasterxml.jackson.databind.exc.ValueInstantiationException
import org.apache.commons.logging.LogFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ShapeExceptionHandler : ResponseEntityExceptionHandler() {

    private val logger = LogFactory.getLog(javaClass)

    @ExceptionHandler(IntersectionException::class)
    fun handleConflict(ex: IntersectionException, request: WebRequest) =
        ResponseEntity
            .status(HttpStatus.CONFLICT)
            // types could be extracted to constants, but imho as long as the string is used just once in the code,
            // it's ok to keep it inline for simplicity.
            // The very same moment we use it twice - we should extract it
            .body(ErrorResponse("INTERSECTION", getMessage(ex), ex.intersections))

    @ExceptionHandler(NameAlreadyExistsException::class)
    fun handleConflict(ex: NameAlreadyExistsException, request: WebRequest) =
        ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ErrorResponse("NAME_CONFLICT", getMessage(ex), ex.name))

    /**
     * Strictly speaking this is a bit dangerous, as not only our shape checks may throw this exception.
     * But added here for simplicity atm
     */
    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> =
        // Check if geometry [require] checks failed
        if (ex.cause is ValueInstantiationException && ex.cause?.cause is IllegalArgumentException)
            ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse("BAD_GEOMETRY", ex.cause?.cause?.message!!, null))
        else super.handleHttpMessageNotReadable(ex, headers, status, request)

    override fun handleExceptionInternal(
        ex: Exception,
        body: Any?,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val error = ErrorResponse(ex::class.simpleName!!.uppercase(), getMessage(ex), body)
        return super.handleExceptionInternal(ex, error, headers, status, request)
    }

    /**
     * By default Spring exposes too much internal error information,
     * which is a security concern, so hiding it is generally a good idea
     */
    @ExceptionHandler(Exception::class)
    fun handleInternalException(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponse<Unit>> {
        logger.error("Internal exception for " + request.contextPath, ex)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse("INTERNAL_ERROR", "Something went wrong", null))
    }

    private fun getMessage(ex: Exception) = ex.message ?: "No message"
}
