package com.projeto.emprestimo.service;

import com.projeto.emprestimo.model.Emprestimo;
import com.projeto.emprestimo.model.Equipamento;
import com.projeto.emprestimo.model.TipoEquipamento;
import com.projeto.emprestimo.repository.EmprestimoRepository;
import com.projeto.emprestimo.repository.EquipamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EquipamentoService {

    private final EquipamentoRepository repositoryEquipamento;
    private final EmprestimoRepository repositoryEmprestimo;

    public EquipamentoService(EquipamentoRepository repositoryEquipamento, EmprestimoRepository repositoryEmprestimo) {
        this.repositoryEquipamento = repositoryEquipamento;
        this.repositoryEmprestimo = repositoryEmprestimo;
    }

    public List<Equipamento> listar(TipoEquipamento tipo, Boolean disponivel) {
        if (tipo == null && disponivel == null) {return repositoryEquipamento.findAll();}

        if (tipo == null) {return repositoryEquipamento.findByDisponivel(disponivel);}

        if (disponivel == null) {return repositoryEquipamento.findByTipo(tipo);}

        return repositoryEquipamento.findByTipoAndDisponivel(tipo, disponivel);
    }

    public Equipamento buscar(Long id) {
        return repositoryEquipamento.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento não encontrado"));
    }

    public Equipamento cadastrar(Equipamento equipamento) {
        if (repositoryEquipamento.existsByPatrimonio(equipamento.getPatrimonio())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Patrimônio já cadastrado");
        }
        equipamento.setDisponivel(true);
        return repositoryEquipamento.save(equipamento);
    }

    public Equipamento atualizar(Long id, Equipamento dados) {
        Equipamento equipamento = buscar(id);
        equipamento.setNome(dados.getNome());
        equipamento.setTipo(dados.getTipo());
        return repositoryEquipamento.save(equipamento);
    }

    public void remover(Long id) {
        buscar(id);

        if (repositoryEmprestimo.existsByEquipamentoId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Equipamento tem histórico");
        }

        repositoryEquipamento.deleteById(id);
    }

}
