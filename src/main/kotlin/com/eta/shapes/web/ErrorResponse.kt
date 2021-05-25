package com.eta.shapes.web

/**
 * Generic POJO for an error response
 *
 * It's good to have a common standard error response across the entire app for consistency
 */
data class ErrorResponse<T>(
    /**
     * Machine error key, useful to handle the error in the code
     */
    val type: String,

    /**
     * Human readable message
     */
    val message: String,

    /**
     * Specific error details
     */
    val details: T?
)
