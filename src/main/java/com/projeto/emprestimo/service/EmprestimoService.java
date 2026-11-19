package com.projeto.emprestimo.service;

import com.projeto.emprestimo.model.Emprestimo;
import com.projeto.emprestimo.repository.ColaboradorRepository;
import com.projeto.emprestimo.repository.EmprestimoRepository;
import com.projeto.emprestimo.repository.EquipamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {

    private final EmprestimoRepository repositoryEmprestimo;
    private final ColaboradorRepository repositoryColaborador;
    private final EquipamentoRepository repositoryEquipamento;

    public EmprestimoService(EmprestimoRepository repositoryEmprestimo, ColaboradorRepository repositoryColaborador, EquipamentoRepository repositoryEquipamento) {
        this.repositoryEmprestimo = repositoryEmprestimo;
        this.repositoryColaborador = repositoryColaborador;
        this.repositoryEquipamento = repositoryEquipamento;
    }

    public List<Emprestimo> listar() {return repositoryEmprestimo.findAll();}

    public Emprestimo buscar(Long id) {
        return repositoryEmprestimo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado"));
    }

    public Emprestimo emprestar(Long equipamentoId, Long colaboradorId) {
        repositoryEquipamento.findById(equipamentoId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID de equipamento não encontrado"));
        if (!repositoryColaborador.existsById(colaboradorId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID de colaborador não encontrado");
        }

        Emprestimo emprestimo = new Emprestimo(equipamentoId, colaboradorId, LocalDate.now());
        return repositoryEmprestimo.save(emprestimo);
    }

}
