package org.cheese.hotelhubserver.http.controller

import jakarta.validation.Valid
import org.cheese.hotelhubserver.domain.user.AuthenticatedUser
import org.cheese.hotelhubserver.http.Uris
import org.cheese.hotelhubserver.http.model.critique.CritiqueCreateInputModel
import org.cheese.hotelhubserver.http.model.critique.CritiqueEditInputModel
import org.cheese.hotelhubserver.services.CritiqueServices
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
class CritiqueController(
    private val critiqueServices: CritiqueServices,
) {
    @PostMapping(Uris.Critique.CREATE)
    fun createCritique(
        user: AuthenticatedUser,
        @Valid @RequestBody critique: CritiqueCreateInputModel
    ): ResponseEntity<*> {
        val res = critiqueServices.createCritique(user.user.id, critique.hotelId, critique.stars, critique.description)
        return ResponseEntity.status(CREATED).body(res)
    }

    @DeleteMapping(Uris.Critique.DELETE)
    fun deleteCritique(
        user: AuthenticatedUser,
        @Valid @PathVariable critiqueId: Int
    ): ResponseEntity<*> {
        val res = critiqueServices.deleteCritique(user.user.id, critiqueId)
        return ResponseEntity.status(204).body(res)
    }

    @PutMapping(Uris.Critique.EDIT)
    fun editCritique(
        user: AuthenticatedUser,
        @Valid @RequestBody critique: CritiqueEditInputModel,
        @Valid @PathVariable critiqueId: Int
    ): ResponseEntity<*> {
        critiqueServices.editCritique(user.user.id, critiqueId, critique.critiqueId, critique.stars, critique.description)
        return ResponseEntity.status(204).body(critiqueId)
    }

    @GetMapping(Uris.Critique.GET)
    fun getCritique(
        @Valid @PathVariable critiqueId: Int
    ): ResponseEntity<*> {
        val res = critiqueServices.getCritique(critiqueId)
        return ResponseEntity.status(200).body(res)
    }

    @GetMapping(Uris.Critique.GETLIST)
    fun getCritiques(
        @Valid @PathVariable hotelId: Int
    ): ResponseEntity<*> {
        val res = critiqueServices.getCritiques(hotelId)
        return ResponseEntity.status(200).body(res)
    }
}
