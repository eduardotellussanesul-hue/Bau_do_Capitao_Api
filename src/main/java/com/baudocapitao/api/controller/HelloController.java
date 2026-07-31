package com.baudocapitao.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Hello", description = "Endpoints de saudação")
public class HelloController {

    @Operation(summary = "Diz olá ao capitão", description = "Retorna uma mensagem de boas-vindas")
    @GetMapping("/hello")
    public String sayHello() {
        return "Olá, Capitão! Bem-vindo ao Bau do Capitão!";
    }
}