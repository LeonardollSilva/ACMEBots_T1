package t1.projarc.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private RoboController roboController;
    
    @Autowired
    private ClienteController clienteController;
    
    @Autowired
    private VendaController vendaController;

    @Test
    void contextLoads() {
        // Teste básico - verifica se contexto Spring carrega
    }
    
    @Test
    void controllersAreLoaded() {
        // Verifica se todos os controladores foram injetados corretamente
        assert roboController != null;
        assert clienteController != null; 
        assert vendaController != null;
    }
    
    @Test
    void dataInitializationWorks() {
        // Verifica se os dados foram inicializados
        assert Cliente.getClientes().size() >= 3; // Pelo menos 3 clientes
        assert Robo.getRobos().size() >= 10;      // Pelo menos 10 robôs
        assert Venda.getVendas().size() >= 2;     // Pelo menos 2 vendas
    }
    
    @Test
    void systemIntegrationCheck() {
        // Teste de integração básico
        // Verifica se os dados estão conectados corretamente
        
        // Verificar se há clientes
        boolean hasClientes = Cliente.getClientes().size() > 0;
        assert hasClientes : "Sistema deve ter clientes inicializados";
        
        // Verificar se há robôs
        boolean hasRobos = Robo.getRobos().size() > 0;
        assert hasRobos : "Sistema deve ter robôs inicializados";
        
        // Verificar se há vendas
        boolean hasVendas = Venda.getVendas().size() > 0;
        assert hasVendas : "Sistema deve ter vendas inicializadas";
    }
}
