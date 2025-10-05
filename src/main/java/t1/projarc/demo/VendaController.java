package t1.projarc.demo;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    @GetMapping
    public List<Venda> listarVendas() {
        return Venda.getVendas();
    }
    
    @PostMapping
    public Venda realizarVenda(@RequestParam int clienteId, @RequestParam int roboId) {
        Cliente cliente = Cliente.buscarClientePorId(clienteId);
        Robo robo = Robo.buscarRoboPorId(roboId);
        
        if (cliente != null && robo != null && robo.getStatus() == StatusRobo.DISPONIVEL && 
        robo.getStatus() != StatusRobo.DESCARTADO) {
            return new Venda(robo, cliente);
        }
        return null;
    }
    
    @GetMapping("/buscar")
    public Venda buscarVenda(@RequestParam int id) {
        return Venda.buscarVendaPorId(id);
    }
}
