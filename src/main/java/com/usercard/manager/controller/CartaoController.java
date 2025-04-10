package com.usercard.manager.controller;

import com.usercard.manager.DTO.CartaoDTO;
import com.usercard.manager.model.Cartao;
import com.usercard.manager.model.CartaoPage;
import com.usercard.manager.service.CartaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/cartao")
@Tag(name = "Cartão")
public class CartaoController {
    private final CartaoService cartaoService;

    Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    public CartaoController(CartaoService cartaoService){
        this.cartaoService = cartaoService;
    }

    @Operation(summary = "Adiciona um cartão a um usuário do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cartão cadastrado corretamente",
                    content = { @Content(mediaType = "text/plain",
                            schema = @Schema(type = "string", example = "123456789034"))}),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Cadastro não efetuado: Erro interno no sistema",
                    content = { @Content(mediaType = "text/plain",
                            schema = @Schema(type = "string", example = "java.lang.IllegalArgumentException"))})})
    @PostMapping("/cadastrar/{usuarioId}")
    public ResponseEntity<?> cadastraCartao(@PathVariable Long usuarioId, @RequestBody CartaoDTO cartaoDTO){
        try{
            var cartao = cartaoService.cadastraCartao(usuarioId, cartaoDTO);
            return new ResponseEntity<>(cartao.getNumeroCartao(), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error(e.getMessage());
            if(e.getClass().getName().equals("jakarta.persistence.EntityNotFoundException")) {
                return new ResponseEntity<>(e.getMessage().getBytes(StandardCharsets.UTF_8), HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>("Internal Server Error: " + e.getClass().getName(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Operation(summary = "Consulta uma lista de cartões")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cartões encontrados",
                    content = { @Content(mediaType = "application/multilevel+json", schema = @Schema(implementation = CartaoPage.class))}),
            @ApiResponse(responseCode = "500", description = "Cartões não encontrados: Erro interno do servidor",
                    content = @Content) })
    @GetMapping("/listar")
    public ResponseEntity<?> listarCartoes(@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "5") int pageSize){
        try{
            var cartoes = cartaoService.listaCartoes(pageNo, pageSize);
            return new ResponseEntity<>(cartoes,HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("Internal Server Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Inativa/Ativa um cartão")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cartão alterado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cartao.class)) }),
            @ApiResponse(responseCode = "404", description = "Cartão não encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Cartão não encontrado: Erro interno do servidor",
                    content = @Content) })
    @PutMapping("/mudarStatus/{id}")
    public ResponseEntity<?> mudaStatus(@PathVariable Long id, @RequestParam Boolean status){
        try{
            var cartao = cartaoService.updateCartao(id, status);
            return new ResponseEntity<>(cartao,HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error(e.getMessage());
            if(e.getClass().getName().equals("jakarta.persistence.EntityNotFoundException")) {
                return new ResponseEntity<>(e.getMessage().getBytes(StandardCharsets.UTF_8), HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>("Internal Server Error: " + e.getClass().getName(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Operation(summary = "Deleta um cartão")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cartão deletado",
                    content = { @Content }),
            @ApiResponse(responseCode = "404", description = "Cartão não encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Cartão não encontrado: Erro interno do servidor",
                    content = @Content) })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> removerCartao(@PathVariable Long id){
        try{
            cartaoService.deletaCartao(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error(e.getMessage());
            if(e.getClass().getName().equals("jakarta.persistence.EntityNotFoundException")) {
                return new ResponseEntity<>(e.getMessage().getBytes(StandardCharsets.UTF_8), HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>("Internal Server Error: " + e.getClass().getName(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
