package com.projeto.emprestimo.model;

import com.projeto.emprestimo.exception.ConflitoException;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne private Equipamento equipamento;
    @ManyToOne private Colaborador colaborador;

    private LocalDate dataEmprestimo;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucao;

    private static final int DIAS_DE_GARANTIA = 14;

    public Emprestimo() { }

    public Emprestimo(Equipamento equipamento, Colaborador colaborador, LocalDate hoje) {
        this.equipamento = equipamento;
        this.colaborador = colaborador;
        dataEmprestimo = hoje;
        dataPrevistaDevolucao = dataEmprestimo.plusDays(DIAS_DE_GARANTIA);
    }

    public void devolver(LocalDate data) {
        if (dataDevolucao != null) {throw new ConflitoException("Erro: empréstimo já devolvido");}
        dataDevolucao = data;
    }

    public boolean estaAtrasadoEm(LocalDate data) {
        if (dataDevolucao != null) {
            return false;
        }
        return data.isAfter(dataPrevistaDevolucao);
    }

    public boolean isAtrasado() {return estaAtrasadoEm(LocalDate.now());}

    public Long getId() {return id;}

    public Equipamento getEquipamento() {return equipamento;}

    public Colaborador getColaborador() {return colaborador;}

    public LocalDate getDataEmprestimo() {return dataEmprestimo;}

    public LocalDate getDataPrevistaDevolucao() {return dataPrevistaDevolucao;}

    public LocalDate getDataDevolucao() {return dataDevolucao;}

}
