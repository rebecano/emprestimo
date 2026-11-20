package com.projeto.emprestimo.controller;

import com.projeto.emprestimo.model.Colaborador;
import com.projeto.emprestimo.model.Emprestimo;
import com.projeto.emprestimo.service.ColaboradorService;
import com.projeto.emprestimo.service.EmprestimoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colaboradores")
public class ColaboradorController {

    private final ColaboradorService serviceColaborador;
    private final EmprestimoService serviceEmprestimo;

    public ColaboradorController(ColaboradorService serviceColaborador, EmprestimoService serviceEmprestimo) {
        this.serviceColaborador = serviceColaborador;
        this.serviceEmprestimo = serviceEmprestimo;}

    @GetMapping
    public List<Colaborador> listar() {return serviceColaborador.listar();}

    @GetMapping("/{id}")
    public Colaborador buscar(@PathVariable Long id) {return serviceColaborador.buscar(id);}

    @GetMapping("/{id}/emprestimos")
    public List<Emprestimo> listarDoColaborador(@PathVariable Long id) {
        return serviceEmprestimo.listarDoColaborador(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Colaborador cadastrar(@RequestBody Colaborador colaborador) {
        return serviceColaborador.cadastrar(colaborador);
    }

}
