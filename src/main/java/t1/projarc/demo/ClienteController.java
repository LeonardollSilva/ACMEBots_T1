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
@RequestMapping("/acmebots")
public class ClienteController {
    
    @GetMapping("/listaclientes")
    public List<Cliente> listarClientes() {
        return Cliente.getClientes();
    }

    @PostMapping("/cadastrarcliente")
    public Cliente criarCliente(@RequestBody Cliente cliente) {
        return new Cliente(cliente.getNome(), cliente.getEmail());
    }

    @GetMapping("/consultacliente")
    public Cliente buscarCliente(@RequestParam int codigo) {
        return Cliente.buscarClientePorId(codigo);
    }

    @PutMapping("/atualizacliente")
    public Cliente atualizarCliente(@RequestParam int codigo, @RequestBody Cliente cliente) {
        cliente.setId(codigo);
        Cliente.atualizarCliente(cliente);
        return cliente;
    }

    @DeleteMapping("/deletacliente")
    public void deletarCliente(@RequestParam int id) {
        Cliente.removerPorId(id);
        
    }
}
