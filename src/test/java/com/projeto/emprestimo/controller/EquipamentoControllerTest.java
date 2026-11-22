package com.projeto.emprestimo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:testes")
@AutoConfigureMockMvc
class EquipamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void buscarEquipamentoInexistenteRetorna404() throws Exception {
        mockMvc.perform(get("/equipamentos/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void cadastrarEquipamentoValidoRetorna201() throws Exception {
        mockMvc.perform(post("/equipamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"patrimonio": "NB-001", "nome": "Notebook Dell", "tipo": "NOTEBOOK"}
                                """))
                .andExpect(status().isCreated());
    }

}