package com.projeto.emprestimo.model;

import com.projeto.emprestimo.exception.ConflitoException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmprestimoTest {

    @Test
    void criarEmprestimoDefineDevolucaoPrevistaEm14Dias() {
        Equipamento equipamento = new Equipamento();
        Colaborador colaborador = new Colaborador();

        Emprestimo emprestimo = new Emprestimo(equipamento, colaborador, LocalDate.of(2026, 11, 14));

        assertEquals(LocalDate.of(2026, 11, 14), emprestimo.getDataEmprestimo());
        assertEquals(LocalDate.of(2026, 11, 28), emprestimo.getDataPrevistaDevolucao());
    }

    @Test
    void devolverEmprestimoAbertoRegistraADataDeDevolucao() {
        Equipamento equipamento = new Equipamento();
        Colaborador colaborador = new Colaborador();

        Emprestimo emprestimo = new Emprestimo(equipamento, colaborador, LocalDate.of(2026, 11, 14));

        emprestimo.devolver(LocalDate.of(2026, 11, 20));

        assertEquals(LocalDate.of(2026, 11, 20), emprestimo.getDataDevolucao());
    }

    @Test
    void naoDevolveEmprestimoJaDevolvido() {
        Equipamento equipamento = new Equipamento();
        Colaborador colaborador = new Colaborador();

        Emprestimo emprestimo = new Emprestimo(equipamento, colaborador, LocalDate.of(2026, 11, 14));

        emprestimo.devolver(LocalDate.of(2026, 11, 20));

        assertThrows(ConflitoException.class, () -> emprestimo.devolver(LocalDate.of(2026, 11, 21)));
    }

    @Test
    void emprestimoNaoDevolvidoComPrazoVencidoEstaAtrasado() {
        Equipamento equipamento = new Equipamento();
        Colaborador colaborador = new Colaborador();

        Emprestimo emprestimo = new Emprestimo(equipamento, colaborador, LocalDate.of(2026, 10, 27));

        assertTrue(emprestimo.estaAtrasadoEm(LocalDate.of(2026, 11, 14)));
    }

    @Test
    void emprestimoNaoDevolvidoDentroDoPrazoNaoEstaAtrasado() {
        Equipamento equipamento = new Equipamento();
        Colaborador colaborador = new Colaborador();

        Emprestimo emprestimo = new Emprestimo(equipamento, colaborador, LocalDate.of(2026, 11, 6));

        assertFalse(emprestimo.estaAtrasadoEm(LocalDate.of(2026, 11, 14)));
    }

    @Test
    void emprestimoDevolvidoNaoEstaAtrasadoMesmoComPrazoVencido() {
        Equipamento equipamento = new Equipamento();
        Colaborador colaborador = new Colaborador();

        Emprestimo emprestimo = new Emprestimo(equipamento, colaborador, LocalDate.of(2026, 10, 27));

        emprestimo.devolver(LocalDate.of(2026, 11, 12));

        assertFalse(emprestimo.estaAtrasadoEm(LocalDate.of(2026, 11, 14)));
    }

    @Test
    void emprestimoNaoEstaAtrasadoNoDiaDoPrazo() {
        Equipamento equipamento = new Equipamento();
        Colaborador colaborador = new Colaborador();

        Emprestimo emprestimo = new Emprestimo(equipamento, colaborador, LocalDate.of(2026, 10, 31));

        assertFalse(emprestimo.estaAtrasadoEm(LocalDate.of(2026, 11, 14)));
    }

}