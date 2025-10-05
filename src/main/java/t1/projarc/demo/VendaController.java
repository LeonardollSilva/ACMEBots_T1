package t1.projarc.demo;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/acmebots")
public class VendaController {

    @GetMapping("/listavendas")
    public List<Venda> listarVendas() {
        return Venda.getVendas();
    }
    
    @PostMapping("/cadastro/cadvenda")  
    public boolean realizarVenda(@RequestParam int clienteId, @RequestParam int roboId) {
        Cliente cliente = Cliente.buscarClientePorId(clienteId);
        Robo robo = Robo.buscarRoboPorId(roboId);
        
        if (cliente != null && robo != null && robo.getStatus() == StatusRobo.DISPONIVEL) {
            new Venda(robo, cliente);
            return true;
        }
        return false;
    }
    
    @GetMapping("/buscarvendas")
    public Venda buscarVenda(@RequestParam int id) {
        return Venda.buscarVendaPorId(id);
    }
}
