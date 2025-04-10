package com.usercard.manager.controller;

import com.usercard.manager.DTO.UsuarioDTO;
import com.usercard.manager.model.Usuario;
import com.usercard.manager.model.UsuarioPage;
import com.usercard.manager.service.UsuarioService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/usuario")
@Tag(name = "Usuário")
public class UsuarioController {
    private final UsuarioService usuarioService;

    Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Faz o cadastro de um usuário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado corretamente",
                    content = { @Content(mediaType = "text/plain",
                            schema = @Schema(type = "string", example = "Fulano da Silva"))}),
            @ApiResponse(responseCode = "500", description = "Cadastro não efetuado: Erro interno no sistema",
                    content = { @Content(mediaType = "text/plain",
                            schema = @Schema(type = "string", example = "java.lang.IllegalArgumentException"))})})
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody UsuarioDTO usuarioDTO){
        try{
            var usuario = usuarioService.inserirUsuario(usuarioDTO);
            return new ResponseEntity<>(usuario.getNome(),HttpStatus.CREATED);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("Internal Server Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Operation(summary = "Consulta de um usuário pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class)) }),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Usuário não encontrado: Erro interno do servidor",
                    content = @Content) })
    @GetMapping("/consulta/{id}")
    public ResponseEntity<?> consultaUsuario(@PathVariable Long id){
        try{
            var usuario = usuarioService.consultarUsuario(id);
            return new ResponseEntity<>(usuario,HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            if(e.getClass().getName().equals("jakarta.persistence.EntityNotFoundException")) {
                return new ResponseEntity<>(e.getMessage().getBytes(StandardCharsets.UTF_8), HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>("Internal Server Error: " + e.getClass().getName(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Operation(summary = "Consulta uma lista de usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários encontrados",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Usuario.class))),
                    @Content(mediaType = "application/multilevel+json", schema = @Schema(implementation = UsuarioPage.class))}),
            @ApiResponse(responseCode = "500", description = "Usuários não encontrados: Erro interno do servidor",
                    content = @Content) })
    @GetMapping("/consulta")
    public ResponseEntity<?> listaUsuarios(@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "5") int pageSize,
                                           @RequestParam String listMethod){
        try{
            var usuarios = listMethod.equals("Page") ? usuarioService.listaUsuariosPagina(pageNo, pageSize) : usuarioService.listaUsuarios();
            return new ResponseEntity<>(usuarios,HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("Internal Server Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Operation(summary = "Altera um usuário pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário alterado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class)) }),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Usuário não encontrado: Erro interno do servidor",
                    content = @Content) })
    @PutMapping("/alterar/{id}")
    public ResponseEntity<?> alteraUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO){
        try{
            var usuario = usuarioService.alterarUsuario(id, usuarioDTO);
            return new ResponseEntity<>(usuario,HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error(e.getMessage());
            if(e.getClass().getName().equals("jakarta.persistence.EntityNotFoundException")) {
                return new ResponseEntity<>(e.getMessage().getBytes(StandardCharsets.UTF_8), HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>("Internal Server Error: " + e.getClass().getName(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Operation(summary = "Deleta um usuário pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário deletado",
                    content = { @Content }),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Usuário não encontrado: Erro interno do servidor",
                    content = @Content) })
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> removeUsuario(@PathVariable Long id){
        try{
            usuarioService.deleteUsuario(id);
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
