package com.projeto.emprestimo.repository;

import com.projeto.emprestimo.model.Equipamento;
import com.projeto.emprestimo.model.TipoEquipamento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    boolean existsByPatrimonio(String patrimonio);

    List<Equipamento> findByTipo(TipoEquipamento tipo);

    List<Equipamento> findByDisponivel(boolean disponivel);

    List<Equipamento> findByTipoAndDisponivel(TipoEquipamento tipo, boolean disponivel);

}
