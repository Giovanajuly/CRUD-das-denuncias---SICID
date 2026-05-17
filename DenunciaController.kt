package com.sicid.projeto.controller

import com.sicid.projeto.dto.denuncia.CreateDenunciaFormRequest
import com.sicid.projeto.dto.denuncia.CreateDenunciaRequest
import com.sicid.projeto.dto.denuncia.DenunciaResponse
import com.sicid.projeto.dto.denuncia.UpdateStatusRequest
import com.sicid.projeto.service.DenunciaService
import com.sicid.projeto.service.PermissionService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/denuncias")
class DenunciaController(
    private val denunciaService: DenunciaService,
    private val permissionService: PermissionService
) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun criarJson(
        @Valid @RequestBody request: CreateDenunciaRequest,
        authentication: Authentication?
    ): DenunciaResponse =
        denunciaService.criar(authentication, request)

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun criarMultipart(
        @Valid @ModelAttribute request: CreateDenunciaFormRequest,
        authentication: Authentication?
    ): DenunciaResponse =
        denunciaService.criar(authentication, request)

    @GetMapping("/minhas")
    fun listarMinhas(authentication: Authentication): List<DenunciaResponse> =
        denunciaService.listarMinhas(permissionService.currentUserId(authentication)!!)

    @GetMapping
    fun listar(): List<DenunciaResponse> =
        denunciaService.listarPublicas()

    @GetMapping("/publicas")
    fun listarPublicas(): List<DenunciaResponse> =
        denunciaService.listarPublicas()

    @GetMapping("/{id}")
    fun buscar(@PathVariable id: Long): DenunciaResponse =
        denunciaService.buscar(id)

    @PatchMapping("/{id}/status")
    fun atualizarStatus(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateStatusRequest
    ): DenunciaResponse =
        denunciaService.atualizarStatus(id, request.status)
}
