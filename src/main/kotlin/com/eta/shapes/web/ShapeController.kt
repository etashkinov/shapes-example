package com.eta.shapes.web

import com.eta.shapes.api.ShapeService
import com.eta.shapes.api.geometry.Circle
import com.eta.shapes.api.geometry.Point
import com.eta.shapes.api.geometry.Polygon
import com.eta.shapes.api.geometry.Rectangle
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

/**
 * Main idea of the post methods here is in splitting the geometry and the name.
 *
 * Considering that for complex shapes geometry could be quite heavy (can even imagine providing it as a file),
 * it makes sense to leave the entire post request body to the geometry, and keep the name as a query parameter
 *
 * Also, having each geometry under its own endpoint, helps us
 * to do the strict geometry type conversion as soon as possible, and not do it manually in the code
 */
@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class ShapeController(private val service: ShapeService) {

    @ApiOperation(
        value = "Finds all shapes",
        notes = "Response is paginated. Default page size: 20",
    )
    @GetMapping
    fun getShapes(pageable: Pageable) = service.getShapes(null /* not yet implemented */, pageable)

    @ApiOperation(
        value = "Add new rectangle",
        notes = """
            Shape name must be unique. 
            Shape should not intersect with any other existing shapes.
            Coordinates should be ordered:
             x1 < x2
             y1 < y2
        """,
    )
    @ApiResponses(
        value =
        [
            ApiResponse(code = 201, message = "Shape successfully created with a provided id"),
            ApiResponse(code = 400, message = "Invalid geometry coordinates provided"),
            ApiResponse(code = 409, message = "Non unique name provided, or shape intersects with the others"),
        ]
    )
    @PostMapping("/rectangles")
    @ResponseStatus(HttpStatus.CREATED)
    fun addRectangle(
        @RequestParam name: String,
        @RequestBody rectangle: Rectangle
    ) =
        service.addShape(name, rectangle)

    @ApiOperation(value = "Add new circle", notes = "Not yet fully implemented")
    @PostMapping("/circles")
    @ResponseStatus(HttpStatus.CREATED)
    fun addCircle(@RequestParam name: String, @RequestBody circle: Circle) =
        service.addShape(name, circle)

    @ApiOperation(value = "Add new polygon", notes = "Not yet fully implemented")
    @PostMapping("/polygons")
    @ResponseStatus(HttpStatus.CREATED)
    fun addPolygon(@RequestParam name: String, @RequestBody points: List<Point>) =
        service.addShape(name, Polygon(points))
}
