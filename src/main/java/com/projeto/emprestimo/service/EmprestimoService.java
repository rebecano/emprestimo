package com.projeto.emprestimo.service;

import com.projeto.emprestimo.model.Emprestimo;
import com.projeto.emprestimo.model.Equipamento;
import com.projeto.emprestimo.repository.ColaboradorRepository;
import com.projeto.emprestimo.repository.EmprestimoRepository;
import com.projeto.emprestimo.repository.EquipamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    public List<Emprestimo> listar(Boolean atrasados) {
        if (Boolean.TRUE.equals(atrasados)) {
            return repositoryEmprestimo.findByDataDevolucaoIsNullAndDataPrevistaDevolucaoBefore(LocalDate.now());
        }
        return repositoryEmprestimo.findAll();
    }

    public Emprestimo buscar(Long id) {
        return repositoryEmprestimo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado"));
    }

    @Transactional
    public Emprestimo emprestar(Long equipamentoId, Long colaboradorId) {
        Equipamento equipamento = repositoryEquipamento.findById(equipamentoId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID de equipamento não encontrado"));

        if (!repositoryColaborador.existsById(colaboradorId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID de colaborador não encontrado");
        }

        equipamento.emprestar();
        repositoryEquipamento.save(equipamento);

        Emprestimo emprestimo = new Emprestimo(equipamentoId, colaboradorId, LocalDate.now());

        return repositoryEmprestimo.save(emprestimo);
    }

    @Transactional
    public Emprestimo devolver(Long id) {
        Emprestimo emprestimo = buscar(id);
        emprestimo.devolver(LocalDate.now());

        Equipamento equipamento = repositoryEquipamento.findById(emprestimo.getEquipamentoId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID de equipamento não encontrado"));
        equipamento.devolver();

        repositoryEquipamento.save(equipamento);

        return repositoryEmprestimo.save(emprestimo);
    }

    public List<Emprestimo> listarDoColaborador(Long colaboradorId) {
        if (!repositoryColaborador.existsById(colaboradorId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID de colaborador não encontrado");
        }
        return repositoryEmprestimo.findByColaboradorId(colaboradorId);
    }

}
