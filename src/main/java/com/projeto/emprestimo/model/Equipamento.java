package com.projeto.emprestimo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Equipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Erro: o patrimônio é obrigatório")
    @Column(nullable = false, unique = true)
    private String patrimonio;

    @NotBlank(message = "Erro: o nome é obrigatório")
    @Column(nullable = false)
    private String nome;

    @NotNull(message = "Erro: o tipo é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEquipamento tipo;

    @Column(nullable = false)
    private boolean disponivel = true;

    public Equipamento() { }

    public void emprestar() {
        if (!isDisponivel()) {throw new IllegalStateException("Erro: equipamento indisponível");}
        setDisponivel(false);
    }

    public void devolver() {setDisponivel(true);}

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getPatrimonio() {return patrimonio;}
    public void setPatrimonio(String patrimonio) {this.patrimonio = patrimonio;}

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public TipoEquipamento getTipo() {return tipo;}
    public void setTipo(TipoEquipamento tipo) {this.tipo = tipo;}

    public boolean isDisponivel() {return disponivel;}
    public void setDisponivel(boolean disponivel) {this.disponivel = disponivel;}

}
