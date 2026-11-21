package com.projeto.emprestimo.service;

import com.projeto.emprestimo.model.Colaborador;
import com.projeto.emprestimo.repository.ColaboradorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ColaboradorService {

    private final ColaboradorRepository repository;

    public ColaboradorService(ColaboradorRepository repository) {this.repository = repository;}

    public List<Colaborador> listar() {
        return repository.findAll();
    }

    public Colaborador buscar(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Erro: colaborador não encontrado"));
    }

    public Colaborador cadastrar(Colaborador colaborador) {
        if (repository.existsByEmail(colaborador.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Erro: email já cadastrado");
        }
        return repository.save(colaborador);
    }

}
