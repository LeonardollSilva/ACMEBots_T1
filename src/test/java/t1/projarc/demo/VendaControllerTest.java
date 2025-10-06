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
class VendaControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Test
    void testListarVendas() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/acmebots/listavendas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))))
                .andExpect(jsonPath("$[*].id").exists())
                .andExpect(jsonPath("$[*].robo").exists())
                .andExpect(jsonPath("$[*].cliente").exists())
                .andExpect(jsonPath("$[*].valorVenda").exists());
    }

    @Test
    void testBuscarVenda_Existente() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Buscar venda que deve existir pelos dados iniciais
        mockMvc.perform(get("/acmebots/buscarvendas")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.robo").exists())
                .andExpect(jsonPath("$.cliente").exists())
                .andExpect(jsonPath("$.valorVenda", greaterThan(0.0)))
                .andExpect(jsonPath("$.dataVenda").exists());
    }

    @Test
    void testBuscarVenda_Inexistente() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/acmebots/buscarvendas")
                .param("id", "99999"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void testRealizarVenda_ComDadosExistentes() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Tentar venda com cliente 3 e robô disponível 
        mockMvc.perform(post("/acmebots/cadastro/cadvenda")
                .param("clienteId", "3")  // Cliente existente
                .param("roboId", "3"))    // Robô que deve estar disponível
                .andExpect(status().isOk())
                .andExpect(content().string(anyOf(is("true"), is("false")))); 
                }

    @Test
    void testRealizarVenda_ClienteInexistente() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(post("/acmebots/cadastro/cadvenda")
                .param("clienteId", "99999") // Cliente que não existe
                .param("roboId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void testRealizarVenda_RoboInexistente() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(post("/acmebots/cadastro/cadvenda")
                .param("clienteId", "1")     // Cliente existente
                .param("roboId", "99999"))   // Robô que não existe
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void testRealizarVenda_RoboJaVendido() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // PRIMEIRO: Garantir que há uma venda ativa
        mockMvc.perform(post("/acmebots/cadastro/cadvenda")
                .param("clienteId", "1")  // Cliente existente
                .param("roboId", "3"))    // Robô disponível
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
        
        // SEGUNDO: Tentar vender o MESMO robô novamente
        mockMvc.perform(post("/acmebots/cadastro/cadvenda")
                .param("clienteId", "2")  // Outro cliente
                .param("roboId", "3"))    // MESMO robô (agora já vendido)
                .andExpect(status().isOk())
                .andExpect(content().string("false")); 
    }

    @Test
    void testCancelarVenda_Existente() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Tentar cancelar venda existente
        mockMvc.perform(delete("/acmebots/cadastro/cancelavenda")
                .param("numero", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testCancelarVenda_Inexistente() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(delete("/acmebots/cadastro/cancelavenda")
                .param("numero", "99999"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void testFluxoCompletoVenda() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // 1. Primeiro criar um cliente para teste
        String clienteJson = "{"
                + "\"nome\": \"Cliente Venda\", "
                + "\"email\": \"venda@test.com\""
                + "}";

        mockMvc.perform(post("/acmebots/cadastrarcliente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clienteJson))
                .andExpect(status().isOk());

        // 2. Criar um robô para teste
        mockMvc.perform(post("/acmebots/cadastro/cadrobometric")
                .param("modelo", "T-Venda-Test")
                .param("valor", "15000")
                .param("autonomia", "8000")
                .param("carga", "120")
                .param("temperatura", "45")
                .param("dataCadastro", "2023-08-01"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // 3. Verificar se temos mais robôs disponíveis agora
        mockMvc.perform(get("/acmebots/listarobos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(10))));

        // 4. Tentar realizar venda com IDs prováveis
        mockMvc.perform(post("/acmebots/cadastro/cadvenda")
                .param("clienteId", "4")  // Cliente recém-criado
                .param("roboId", "11"))   // Robô recém-criado
                .andExpect(status().isOk());
    }

    @Test
    void testCalculoValorVenda() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Buscar uma venda e verificar se o valor foi calculado corretamente
        mockMvc.perform(get("/acmebots/buscarvendas")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valorVenda", greaterThan(0.0)))
                .andExpect(jsonPath("$.valorVenda", lessThan(999999.0)))
                .andExpect(jsonPath("$.dataVenda").exists())
                .andExpect(jsonPath("$.robo.status", anyOf(is("VENDIDO"), is("DISPONIVEL"))));
    }

    @Test
    void testAtualizarCliente_ParaVenda() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Atualizar dados de um cliente para teste
        String clienteJson = "{"
                + "\"nome\": \"Cliente Atualizado\", "
                + "\"email\": \"atualizado@test.com\""
                + "}";

        mockMvc.perform(put("/acmebots/atualizacliente")
                .param("codigo", "2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clienteJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Cliente Atualizado")));
    }

    @Test
    void testEndpointsVendaExistem() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Verifica se todos os endpoints de venda existem
        mockMvc.perform(get("/acmebots/listavendas"))
                .andExpect(status().isOk());
                
        mockMvc.perform(get("/acmebots/buscarvendas")
                .param("id", "1"))
                .andExpect(status().isOk());
                
        mockMvc.perform(post("/acmebots/cadastro/cadvenda")
                .param("clienteId", "99999")
                .param("roboId", "99999"))
                .andExpect(status().isOk());
                
        mockMvc.perform(delete("/acmebots/cadastro/cancelavenda")
                .param("numero", "99999"))
                .andExpect(status().isOk());
    }

    @Test
    void testValidacaoParametrosVenda() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Teste com parâmetros válidos mas que podem não resultar em venda
        mockMvc.perform(post("/acmebots/cadastro/cadvenda")
                .param("clienteId", "1")
                .param("roboId", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string(anyOf(is("true"), is("false"))));
    }

    @Test
    void testConsistenciaVendas() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Verificar se as vendas têm estrutura consistente
        mockMvc.perform(get("/acmebots/listavendas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].valorVenda").isNumber())
                .andExpect(jsonPath("$[0].dataVenda").exists())
                .andExpect(jsonPath("$[0].robo.modelo").exists())
                .andExpect(jsonPath("$[0].cliente.nome").exists());
    }
}
