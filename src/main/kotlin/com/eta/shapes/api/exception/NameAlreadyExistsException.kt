package com.eta.shapes.api.exception

/**
 * Thrown when trying to add a shape with the non-unique name
 *
 * @param name non-unique name
 */
class NameAlreadyExistsException(val name: String) : Exception("Shape with name '$name' already exists")
