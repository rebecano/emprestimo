package com.projeto.emprestimo.repository;

import com.projeto.emprestimo.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    List<Emprestimo> findByColaboradorId(Long colaboradorId);

}
