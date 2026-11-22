package com.projeto.emprestimo.controller;

import com.projeto.emprestimo.model.Colaborador;
import com.projeto.emprestimo.model.Emprestimo;
import com.projeto.emprestimo.model.Equipamento;
import com.projeto.emprestimo.model.TipoEquipamento;
import com.projeto.emprestimo.repository.ColaboradorRepository;
import com.projeto.emprestimo.repository.EmprestimoRepository;
import com.projeto.emprestimo.repository.EquipamentoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:testes")
@AutoConfigureMockMvc
class EmprestimoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Test
    void emprestarEquipamentoDisponivelRetorna201() throws Exception {
        Equipamento equipamento = criarEquipamento("NB-002");
        Colaborador colaborador = criarColaborador("ana@empresa.com");

        mockMvc.perform(post(urlEmprestar(equipamento, colaborador)))
                .andExpect(status().isCreated());
    }

    @Test
    void devolverEmprestimoAbertoRetorna200() throws Exception {
        Equipamento equipamento = criarEquipamento("NB-003");
        Colaborador colaborador = criarColaborador("joao@empresa.com");

        equipamento.emprestar();
        equipamentoRepository.save(equipamento);
        Emprestimo emprestimo = emprestimoRepository.save(
                new Emprestimo(equipamento, colaborador, LocalDate.now()));

        mockMvc.perform(put("/emprestimos/" + emprestimo.getId() + "/devolucao"))
                .andExpect(status().isOk());
    }

    @Test
    void emprestarEquipamentoJaEmprestadoRetorna409() throws Exception {
        Equipamento equipamento = criarEquipamento("NB-004");
        Colaborador colaborador = criarColaborador("maria@empresa.com");

        mockMvc.perform(post(urlEmprestar(equipamento, colaborador)))
                .andExpect(status().isCreated());

        mockMvc.perform(post(urlEmprestar(equipamento, colaborador)))
                .andExpect(status().isConflict());
    }

    private Equipamento criarEquipamento(String patrimonio) {
        Equipamento equipamento = new Equipamento();
        equipamento.setPatrimonio(patrimonio);
        equipamento.setNome("Notebook Dell");
        equipamento.setTipo(TipoEquipamento.NOTEBOOK);
        return equipamentoRepository.save(equipamento);
    }

    private Colaborador criarColaborador(String email) {
        Colaborador colaborador = new Colaborador();
        colaborador.setNome("Colaborador Teste");
        colaborador.setEmail(email);
        colaborador.setCargo("Analista");
        return colaboradorRepository.save(colaborador);
    }

    private String urlEmprestar(Equipamento equipamento, Colaborador colaborador) {
        return "/emprestimos?equipamentoId=" + equipamento.getId()
                + "&colaboradorId=" + colaborador.getId();
    }

}