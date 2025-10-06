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
class RoboControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Test
    void testListarRobos() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/acmebots/listarobos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].modelo").exists())
                .andExpect(jsonPath("$[0].valor").exists());
    }

    @Test
    void testValidarRoboMetrico_ComParametrosValidos() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(post("/acmebots/validarobometric")
                .param("autonomia", "5000")
                .param("carga", "50")
                .param("temperaturaMinima", "10")
                .param("temperaturaMaxima", "60"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testValidarRoboImperial_ComParametrosValidos() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(post("/acmebots/validaroboimperial")
                .param("autonomia", "3.1")
                .param("carga", "110")
                .param("temperaturaMinima", "50")
                .param("temperaturaMaxima", "140"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testCadastrarRoboMetrico_Sucesso() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(post("/acmebots/cadastro/cadrobometric")
                .param("modelo", "T-Test-" + System.currentTimeMillis())
                .param("valor", "18000")
                .param("autonomia", "9000")
                .param("carga", "130")
                .param("temperatura", "42")
                .param("dataCadastro", "2023-06-01"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testCadastrarRoboImperial_Sucesso() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(post("/acmebots/cadastro/cadroboimperial")
                .param("modelo", "TX-Imperial-" + System.currentTimeMillis())
                .param("valor", "35000")
                .param("autonomiaMilhas", "6.2")
                .param("cargaLibras", "176")
                .param("temperaturaFahrenheit", "107.6")
                .param("dataCadastro", "2023-07-01"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testAtualizarStatusRobo_Sucesso() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(put("/acmebots/cadastro/atualizarobo/1/estado/VENDIDO"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is("VENDIDO")));
    }

    @Test
    void testAtualizarStatusRobo_IDInexistente() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(put("/acmebots/cadastro/atualizarobo/99999/estado/VENDIDO"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void testValidarRoboMetrico_FiltroRestritivo() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(post("/acmebots/validarobometric")
                .param("autonomia", "50000")
                .param("carga", "1000")
                .param("temperaturaMinima", "10")
                .param("temperaturaMaxima", "15"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testEndpointsExistem() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/acmebots/listarobos"))
                .andExpect(status().isOk());
                
        mockMvc.perform(post("/acmebots/validarobometric")
                .param("autonomia", "1")
                .param("carga", "1")
                .param("temperaturaMinima", "1")
                .param("temperaturaMaxima", "100"))
                .andExpect(status().isOk());
    }
}