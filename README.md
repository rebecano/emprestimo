# Sistema de Empréstimo de Equipamentos

Projeto 2 do bootcamp #ElasTech Java + IA: uma API REST em Java com Spring Boot para controlar o empréstimo de equipamentos (notebooks, monitores, headsets e outros) a colaboradores de uma empresa.

![CI](https://github.com/rebecano/emprestimo/actions/workflows/ci.yml/badge.svg)

**Autora:** Rebeca
**API no ar (Render):** https://emprestimo-6nu6.onrender.com
**Vídeo de demonstração:** <link do vídeo>

## O que o sistema faz

- Cadastra equipamentos (patrimônio, nome e tipo), com filtro por tipo e por disponibilidade.
- Cadastra colaboradores (nome, e-mail e cargo).
- Registra o empréstimo de um equipamento a um colaborador, com devolução prevista para 14 dias depois.
- Registra a devolução, que libera o equipamento de novo.
- Lista os empréstimos, com filtro para ver só os atrasados, e mostra o histórico de cada colaborador.
- Atualiza (nome e tipo) e remove equipamentos.
- Recusa operações que quebram uma regra de negócio, respondendo com o status HTTP certo e uma mensagem clara.
- Tem uma tela web simples (`/index.html`) que usa a própria API, além da documentação interativa (Swagger).

## Como executar

Requisitos: JDK 25 (a versão indicada no `pom.xml`) e Maven. O projeto também traz o Maven Wrapper (`mvnw`).

```bash
mvn spring-boot:run
```

No Windows, sem o Maven instalado: `mvnw.cmd spring-boot:run`. Também dá para abrir o projeto no IntelliJ e executar a classe `EmprestimoApplication`.

Com a aplicação rodando:

| O quê | Endereço |
|---|---|
| Tela web | http://localhost:8080/index.html |
| Documentação (Swagger) | http://localhost:8080/swagger-ui/index.html |
| Saúde da aplicação (Actuator) | http://localhost:8080/actuator/health |
| Console do banco (H2) | http://localhost:8080/h2-console |

O banco H2 grava os dados na pasta `dados/`, que fica fora do Git. Para recomeçar do zero, pare a aplicação e apague o conteúdo dessa pasta. No console do H2, a URL de conexão é `jdbc:h2:file:./dados/emprestimos`.

Para rodar os testes:

```bash
mvn test
```

## Organização dos pacotes

```
com.projeto
├── EmprestimoApplication
└── emprestimo
    ├── controller   EquipamentoController, ColaboradorController e EmprestimoController
    ├── service      EquipamentoService, ColaboradorService e EmprestimoService
    ├── repository   um repository para cada entidade (Spring Data JPA)
    ├── model        Equipamento, Colaborador, Emprestimo e TipoEquipamento
    ├── exception    NaoEncontradoException, ConflitoException e TratadorDeErros
    └── config       SwaggerConfig
```

A tela web fica em `src/main/resources/static/index.html`, e o Spring a serve junto com a API.

### Divisão de responsabilidades

| Camada | Responsabilidade |
|---|---|
| Controller | Define as rotas, recebe os dados do pedido e devolve o status. Não tem regra de negócio. |
| Service | Aplica as regras que precisam consultar o banco ou juntar vários objetos. |
| Repository | Guarda e busca os dados. Não toma decisões. |
| Model | Descreve cada entidade e guarda as regras que dependem só dela mesma. |
| TratadorDeErros | Transforma cada exceção na resposta HTTP certa. |

## Rotas

| Método | Rota | O que faz | Status de sucesso |
|---|---|---|---|
| POST | `/equipamentos` | Cadastra um equipamento | 201 |
| GET | `/equipamentos` | Lista; aceita `?tipo=` e `?disponivel=` | 200 |
| GET | `/equipamentos/{id}` | Busca um equipamento | 200 |
| PUT | `/equipamentos/{id}` | Atualiza nome e tipo | 200 |
| DELETE | `/equipamentos/{id}` | Remove (se nunca foi emprestado) | 204 |
| POST | `/colaboradores` | Cadastra um colaborador | 201 |
| GET | `/colaboradores` | Lista os colaboradores | 200 |
| GET | `/colaboradores/{id}` | Busca um colaborador | 200 |
| GET | `/colaboradores/{id}/emprestimos` | Histórico de empréstimos da pessoa | 200 |
| POST | `/emprestimos?equipamentoId=1&colaboradorId=2` | Registra um empréstimo | 201 |
| GET | `/emprestimos` | Lista todos; com `?atrasados=true`, só os atrasados | 200 |
| GET | `/emprestimos/{id}` | Busca um empréstimo | 200 |
| PUT | `/emprestimos/{id}/devolucao` | Registra a devolução | 200 |

O tipo do equipamento é um destes valores: `NOTEBOOK`, `MONITOR`, `MOUSE`, `TECLADO`, `HEADSET` ou `OUTRO`.

### Respostas de erro

| Situação | Status | Corpo |
|---|---|---|
| Equipamento, colaborador ou empréstimo que não existe | 404 | `{"mensagem": "..."}` |
| Regra quebrada (patrimônio ou e-mail repetido, equipamento indisponível, empréstimo já devolvido, equipamento com histórico, colaborador com atraso) | 409 | `{"mensagem": "..."}` |
| Dados inválidos no cadastro (campo vazio, e-mail com formato errado, tipo ausente) | 400 | `{"campo": "mensagem"}` |

## Regras de negócio

| Regra | O que diz | Onde mora |
|---|---|---|
| R1 | Não existem dois equipamentos com o mesmo patrimônio | `EquipamentoService` |
| R2 | Todo equipamento novo nasce disponível, mesmo que o JSON mande `"disponivel": false` | `EquipamentoService` |
| R3 | Não existem dois colaboradores com o mesmo e-mail | `ColaboradorService` |
| R4 | Só se empresta equipamento disponível | `Equipamento` |
| R5 | Ao emprestar, o equipamento fica indisponível, a data do empréstimo é hoje e a devolução prevista é daqui a 14 dias | `Emprestimo` + `Equipamento` + `EmprestimoService` |
| R6 | Um empréstimo já devolvido não pode ser devolvido de novo | `Emprestimo` |
| R7 | Ao devolver, a data de devolução é hoje e o equipamento volta a ficar disponível | `Emprestimo` + `Equipamento` + `EmprestimoService` |
| R8 | Um empréstimo está atrasado quando ainda não foi devolvido e a data prevista já passou. É calculado na hora, não é uma coluna | `Emprestimo` + filtro no `EmprestimoRepository` |
| R9 | Equipamento que já teve empréstimo não pode ser removido, porque o histórico precisa continuar existindo | `EquipamentoService` |
| R10 | Quem está com um empréstimo atrasado não pode pegar outro equipamento | `EmprestimoService` |

As regras que dependem só do próprio objeto ficam na entidade, o que impede um service de esquecê-las e permite testá-las sem banco. As que precisam consultar o banco ficam no service. Os métodos `emprestar` e `devolver` do `EmprestimoService` usam `@Transactional`: o empréstimo e o equipamento são salvos juntos, ou nada é salvo.

## Testes

Os testes estão em `src/test/java`:

- `EquipamentoTest`: emprestar um equipamento disponível, recusar o empréstimo de um indisponível (R4) e devolver.
- `EmprestimoTest`: prazo de 14 dias (R5), devolução (R7), devolução repetida (R6) e os casos de atraso (R8), incluindo o dia do prazo, em que o empréstimo ainda não está atrasado.
- `EquipamentoControllerTest` e `EmprestimoControllerTest`: testes de integração com MockMvc, que chamam as rotas como o Postman faria (cadastro 201, busca inexistente 404, empréstimo 201, devolução 200 e equipamento já emprestado 409). Rodam com um banco H2 em memória.
- `EmprestimoApplicationTests`: confere que a aplicação sobe.

Os testes das entidades recebem a data por parâmetro (`LocalDate`), em vez de usar a data de hoje dentro do método, para o teste escolher o dia que quiser.

Observação: o teste que sobe a aplicação usa o banco em arquivo, então é preciso parar a aplicação antes de rodar todos os testes.

## Integração contínua

O GitHub Actions (`.github/workflows/ci.yml`) roda `mvn -B test` a cada push e a cada Pull Request, e mostra na aba **Actions** se os testes passaram.

## Deploy

A API está publicada no Render, em https://emprestimo-6nu6.onrender.com. O Render pega o código do GitHub, monta a aplicação com o `Dockerfile` e republica a cada push na branch principal.

- A porta vem da variável `PORT` do Render (`server.port=${PORT:8080}`); no computador local, a aplicação usa a 8080.
- O console do H2 fica desligado na internet, pela variável `SPRING_H2_CONSOLE_ENABLED=false`.
- Verificação de saúde: `/actuator/health`.

## Bônus feitos

- **Nível 1:** Swagger (springdoc) e Actuator.
- **Nível 2:** validação (Bean Validation), tratamento de erros com 404, 409 e 400, relacionamento `@ManyToOne` no `Emprestimo` e testes de integração com MockMvc.
- **Nível 3:** GitHub Actions.

Spring Security com JWT não foi implementado e fica para uma próxima etapa.

## Decisões e limitações

- **Sem login:** a API não tem autenticação, então qualquer pessoa com o endereço consegue usar todas as rotas.
- **Banco de teste:** o H2 do Render perde os dados quando o serviço reinicia ou publica uma versão nova. Num sistema real, entraria um banco como o PostgreSQL.
- **Plano grátis do Render:** o serviço dorme depois de 15 minutos parado, e a primeira requisição depois disso leva cerca de um minuto.
- **Atualizar equipamento:** o `PUT` muda só o nome e o tipo. O patrimônio e a disponibilidade são protegidos, porque o `save` direto do que veio no JSON sobrescreveria os dois.
- **Validação:** o `@Valid` está nos cadastros (`POST`). Por isso o `PUT` de equipamento aceita um corpo sem patrimônio.
- **Prazo fixo:** os 14 dias de empréstimo são uma constante no `Emprestimo`, igual para todos os equipamentos.
- **Datas:** o sistema usa `LocalDate`, sem hora. O empréstimo vence no fim do dia do prazo, e só no dia seguinte passa a contar como atrasado.
- **Front-end:** a tela em `static` só consome a API: toda regra de negócio fica no back-end.
- **Exceções do Java:** os services e as entidades lançam exceções próprias (`NaoEncontradoException` e `ConflitoException`), e o `TratadorDeErros` define o status de cada uma.
