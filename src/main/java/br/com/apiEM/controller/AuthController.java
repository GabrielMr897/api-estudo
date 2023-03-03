package br.com.apiEM.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RequestMapping("/api/authentication")
@Tag(name = "auth", description = "Autenticação")
public class AuthController {
  
}
