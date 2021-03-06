package com.rgiaviti.kr.api.handlers

import com.rgiaviti.kr.api.req.movies.MovieReq
import com.rgiaviti.kr.api.res.AppResponse
import com.rgiaviti.kr.api.res.MessageRes
import com.rgiaviti.kr.api.res.ResponseStatus
import com.rgiaviti.kr.api.validators.movie.PostMovieValidator
import com.rgiaviti.kr.business.services.MovieService
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class MovieHandler(
        private val postMovieValidator: PostMovieValidator,
        private val movieService: MovieService
) {

    private companion object {
        val LOGGER = KotlinLogging.logger { }
    }

    suspend fun createMovie(request: ServerRequest): ServerResponse {
        val body = request.awaitBody<MovieReq>()
        val validationResult = this.postMovieValidator.validateRequest(body)
        if (validationResult.hasErrors()) {
            return ServerResponse.badRequest().bodyValueAndAwait(validationResult.toAppResponse())
        }

        return this.movieService.createMovie(body)
    }

    suspend fun getMovieById(request: ServerRequest): ServerResponse {
        val movieId = request.pathVariable("id")
        return this.movieService.findById(movieId.toInt())
    }

    suspend fun getMovieByYear(request: ServerRequest): ServerResponse {
        val year = request.pathVariable("year")
        return this.movieService.findByYear(year)
    }

    suspend fun getMovies(request: ServerRequest): ServerResponse {
        return this.movieService.findAll()
    }
}