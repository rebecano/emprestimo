package com.projeto.emprestimo.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmprestimoTest {

    @Test
    void verificarPrazoDe14Dias() {
        Emprestimo emprestimo = new Emprestimo(1L, 1L, LocalDate.of(2026, 11, 14));

        assertEquals(LocalDate.of(2026, 11, 14), emprestimo.getDataEmprestimo());
        assertEquals(LocalDate.of(2026, 11, 28), emprestimo.getDataPrevistaDevolucao());
    }

    @Test
    void conferirSeRetornaADataDeDevolucao() {
        Emprestimo emprestimo = new Emprestimo(1L, 1L, LocalDate.of(2026, 11, 14));

        emprestimo.devolver(LocalDate.of(2026, 11, 20));

        assertEquals(LocalDate.of(2026, 11, 20), emprestimo.getDataDevolucao());
    }

    @Test
    void naoDevolverMaisDeUmaVez() {
        Emprestimo emprestimo = new Emprestimo();

        emprestimo.devolver(LocalDate.of(2026, 11, 20));

        assertThrows(IllegalStateException.class, () -> emprestimo.devolver(LocalDate.of(2026, 11, 21)));
    }

}