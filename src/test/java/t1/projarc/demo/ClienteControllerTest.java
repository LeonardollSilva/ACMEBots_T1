package t1.projarc.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
class ClienteControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Test
    void testListarClientes() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/acmebots/listaclientes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].nome").exists())
                .andExpect(jsonPath("$[0].email").exists())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    void testConsultarCliente_Existente() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/acmebots/consultacliente")
                .param("codigo", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome").exists())
                .andExpect(jsonPath("$.email").exists());
    }

    @Test
    void testConsultarCliente_Inexistente() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/acmebots/consultacliente")
                .param("codigo", "99999"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void testCadastrarCliente_Sucesso() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // NOME CURTO para não exceder 100 caracteres
        String clienteJson = "{"
                + "\"nome\": \"Kyle Reese\","  // Nome curto e válido
                + "\"email\": \"kyle.test@resistance.com\""
                + "}";

        mockMvc.perform(post("/acmebots/cadastrarcliente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clienteJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome", is("Kyle Reese")))
                .andExpect(jsonPath("$.email", is("kyle.test@resistance.com")))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testCadastrarCliente_NomeSimples() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        String clienteJson = "{"
                + "\"nome\": \"Ana Silva\","  // Nome válido
                + "\"email\": \"ana.silva@email.com\""
                + "}";

        mockMvc.perform(post("/acmebots/cadastrarcliente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clienteJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome", is("Ana Silva")))
                .andExpect(jsonPath("$.email", is("ana.silva@email.com")))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testCadastrarCliente_DadosVariados() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        String clienteJson = "{"
                + "\"nome\": \"Pedro Santos Costa\","  //  Nome válido
                + "\"email\": \"pedro.costa@empresa.com.br\""
                + "}";

        mockMvc.perform(post("/acmebots/cadastrarcliente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clienteJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome", is("Pedro Santos Costa")))
                .andExpect(jsonPath("$.email", is("pedro.costa@empresa.com.br")))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testAtualizarCliente() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        String clienteJson = "{"
                + "\"nome\": \"Nome Atualizado\","  // Nome curto
                + "\"email\": \"email.atualizado@test.com\""
                + "}";

        mockMvc.perform(put("/acmebots/atualizacliente")
                .param("codigo", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clienteJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Nome Atualizado")));
    }

    @Test
    void testDeletarCliente() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // NOME CURTO para não exceder validação
        String clienteJson = "{"
                + "\"nome\": \"Cliente Deletar\","  // Nome curto
                + "\"email\": \"deletar@test.com\""
                + "}";

        // Criar cliente
        mockMvc.perform(post("/acmebots/cadastrarcliente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clienteJson))
                .andExpect(status().isOk());

        // Deletar usando ID provável
        mockMvc.perform(delete("/acmebots/deletacliente")
                .param("id", "4"))
                .andExpect(status().isOk());
    }

    @Test
    void testEndpointsClienteExistem() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/acmebots/listaclientes"))
                .andExpect(status().isOk());
                
        mockMvc.perform(get("/acmebots/consultacliente")
                .param("codigo", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void testFluxoCompletoCliente() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // NOMES CURTOS para não exceder validação
        String clienteJson = "{"
                + "\"nome\": \"Cliente Fluxo\","  // 
                + "\"email\": \"fluxo@test.com\""
                + "}";

        mockMvc.perform(post("/acmebots/cadastrarcliente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clienteJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Cliente Fluxo")))
                .andExpect(jsonPath("$.email", is("fluxo@test.com")));
                
        mockMvc.perform(get("/acmebots/listaclientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(3))));
    }

    @Test
    void testCadastrarCliente_NomeInvalido() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        String clienteJson = "{"
                + "\"nome\": \"A\","  // Nome muito curto
                + "\"email\": \"email@test.com\""
                + "}";

        mockMvc.perform(post("/acmebots/cadastrarcliente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clienteJson))
                .andExpect(status().isInternalServerError()); 
    }

    @Test
    void testCadastrarCliente_EmailInvalido() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        String clienteJson = "{"
                + "\"nome\": \"Nome Valido\","
                + "\"email\": \"email-sem-arroba\""  // Email inválido
                + "}";

        mockMvc.perform(post("/acmebots/cadastrarcliente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clienteJson))
                .andExpect(status().isInternalServerError()); 
    }
}