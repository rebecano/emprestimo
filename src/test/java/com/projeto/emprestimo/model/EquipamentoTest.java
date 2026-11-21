package com.projeto.emprestimo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EquipamentoTest {

    @Test
    void emprestarEquipamentoDisponivelTornaOEquipamentoIndisponivel() {
        Equipamento equipamento = new Equipamento();

        equipamento.emprestar();

        assertFalse(equipamento.isDisponivel());
    }

    @Test
    void naoEmprestaEquipamentoIndisponivel() {
        Equipamento equipamento = new Equipamento();

        equipamento.emprestar();

        assertThrows(IllegalStateException.class, () -> equipamento.emprestar());
    }

    @Test
    void devolverEquipamentoEmprestadoODeixaDisponivel() {
        Equipamento equipamento = new Equipamento();

        equipamento.emprestar();
        equipamento.devolver();

        assertTrue(equipamento.isDisponivel());
    }

}