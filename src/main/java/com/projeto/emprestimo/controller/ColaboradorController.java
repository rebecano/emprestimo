package com.projeto.emprestimo.controller;

import com.projeto.emprestimo.model.Colaborador;
import com.projeto.emprestimo.service.ColaboradorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colaboradores")
public class ColaboradorController {

    private final ColaboradorService service;

    public ColaboradorController(ColaboradorService service) {this.service = service;}

    @GetMapping
    public List<Colaborador> listar() {return service.listar();}

    @GetMapping("/{id}")
    public Colaborador buscar(@PathVariable Long id) {return service.buscar(id);}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Colaborador cadastrar(@RequestBody Colaborador colaborador) {return service.cadastrar(colaborador);}

}
