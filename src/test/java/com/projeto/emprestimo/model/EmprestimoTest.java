package com.projeto.emprestimo.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmprestimoTest {

    @Test
    void criarEmprestimoDefineDevolucaoPrevistaEm14Dias() {
        Emprestimo emprestimo = new Emprestimo(1L, 1L, LocalDate.of(2026, 11, 14));

        assertEquals(LocalDate.of(2026, 11, 14), emprestimo.getDataEmprestimo());
        assertEquals(LocalDate.of(2026, 11, 28), emprestimo.getDataPrevistaDevolucao());
    }

    @Test
    void devolverEmprestimoAbertoRegistraADataDeDevolucao() {
        Emprestimo emprestimo = new Emprestimo(1L, 1L, LocalDate.of(2026, 11, 14));

        emprestimo.devolver(LocalDate.of(2026, 11, 20));

        assertEquals(LocalDate.of(2026, 11, 20), emprestimo.getDataDevolucao());
    }

    @Test
    void naoDevolveEmprestimoJaDevolvido() {
        Emprestimo emprestimo = new Emprestimo(1L, 1L, LocalDate.of(2026, 11, 14));

        emprestimo.devolver(LocalDate.of(2026, 11, 20));

        assertThrows(IllegalStateException.class, () -> emprestimo.devolver(LocalDate.of(2026, 11, 21)));
    }

    @Test
    void emprestimoNaoDevolvidoComPrazoVencidoEstaAtrasado() {
        Emprestimo emprestimo = new Emprestimo(1L, 1L, LocalDate.of(2026, 10, 27));

        assertTrue(emprestimo.estaAtrasadoEm(LocalDate.of(2026, 11, 14)));
    }

    @Test
    void emprestimoNaoDevolvidoDentroDoPrazoNaoEstaAtrasado() {
        Emprestimo emprestimo = new Emprestimo(1L, 1L, LocalDate.of(2026, 11, 6));

        assertFalse(emprestimo.estaAtrasadoEm(LocalDate.of(2026, 11, 14)));
    }

    @Test
    void emprestimoDevolvidoNaoEstaAtrasadoMesmoComPrazoVencido() {
        Emprestimo emprestimo = new Emprestimo(1L, 1L, LocalDate.of(2026, 10, 27));

        emprestimo.devolver(LocalDate.of(2026, 11, 12));

        assertFalse(emprestimo.estaAtrasadoEm(LocalDate.of(2026, 11, 14)));
    }

    @Test
    void emprestimoNaoEstaAtrasadoNoDiaDoPrazo() {
        Emprestimo emprestimo = new Emprestimo(1L, 1L, LocalDate.of(2026, 10, 31));

        assertFalse(emprestimo.estaAtrasadoEm(LocalDate.of(2026, 11, 14)));
    }

}