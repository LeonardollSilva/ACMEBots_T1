package t1.projarc.demo;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    
    @GetMapping
    public List<Cliente> listarClientes() {
        return Cliente.getClientes();
    }
    
    @PostMapping
    public Cliente criarCliente(@RequestBody Cliente cliente) {
        return new Cliente(cliente.getNome(), cliente.getEmail());
    }
    
    @GetMapping("/buscar")
    public Cliente buscarCliente(@RequestParam int id) {
        return Cliente.buscarClientePorId(id);
    }
    
    @PutMapping("/atualizar")
    public Cliente atualizarCliente(@RequestParam int id, @RequestBody Cliente cliente) {
        cliente.setId(id);
        Cliente.atualizarCliente(cliente);
        return cliente;
    }
    
    @DeleteMapping("/deletar")
    public void deletarCliente(@RequestParam int id) {
        Cliente.removerPorId(id);
        
    }
}
