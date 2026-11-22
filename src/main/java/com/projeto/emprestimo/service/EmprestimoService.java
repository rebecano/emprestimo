package com.projeto.emprestimo.service;

import com.projeto.emprestimo.exception.ConflitoException;
import com.projeto.emprestimo.exception.NaoEncontradoException;
import com.projeto.emprestimo.model.Colaborador;
import com.projeto.emprestimo.model.Emprestimo;
import com.projeto.emprestimo.model.Equipamento;
import com.projeto.emprestimo.repository.ColaboradorRepository;
import com.projeto.emprestimo.repository.EmprestimoRepository;
import com.projeto.emprestimo.repository.EquipamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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

    public List<Emprestimo> listar(Boolean atrasados) {
        if (Boolean.TRUE.equals(atrasados)) {
            return repositoryEmprestimo.findByDataDevolucaoIsNullAndDataPrevistaDevolucaoBefore(LocalDate.now());
        }
        return repositoryEmprestimo.findAll();
    }

    public Emprestimo buscar(Long id) {
        return repositoryEmprestimo.findById(id).orElseThrow(
                () -> new NaoEncontradoException("Erro: empréstimo não encontrado"));
    }

    @Transactional
    public Emprestimo emprestar(Long equipamentoId, Long colaboradorId) {
        Equipamento equipamento = repositoryEquipamento.findById(equipamentoId).orElseThrow(
                () -> new NaoEncontradoException("Erro: ID de equipamento não encontrado"));

        Colaborador colaborador = repositoryColaborador.findById(colaboradorId).orElseThrow(
                () -> new NaoEncontradoException("Erro: ID de colaborador não encontrado"));

        if (repositoryEmprestimo.existsByColaboradorIdAndDataDevolucaoIsNullAndDataPrevistaDevolucaoBefore(
                colaboradorId, LocalDate.now())) {
            throw new ConflitoException("Erro: " + colaborador.getNome() + "está com empréstimos em atraso");
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
                () -> new NaoEncontradoException("Erro: ID de equipamento não encontrado"));

        equipamento.devolver();

        repositoryEquipamento.save(equipamento);

        return repositoryEmprestimo.save(emprestimo);
    }

    public List<Emprestimo> listarDoColaborador(Long colaboradorId) {
        if (!repositoryColaborador.existsById(colaboradorId)) {
            throw new NaoEncontradoException("Erro: ID de colaborador não encontrado");
        }
        return repositoryEmprestimo.findByColaboradorId(colaboradorId);
    }

}
