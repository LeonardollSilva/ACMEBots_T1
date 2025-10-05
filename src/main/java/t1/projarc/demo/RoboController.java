package t1.projarc.demo;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/robos")
public class RoboController {
    
    @GetMapping
    public List<Robo> listarRobos() {
        return Robo.getRobos();
    }
    
    @PostMapping
    public Robo criarRobo(@RequestBody Robo robo) {
        return new Robo(
            robo.getModelo(),
            robo.getValor(),
            robo.getAutonomia(),
            robo.getCarga(),
            robo.getTemperatura(),
            robo.getDataCadastro()
        );
    }

    @GetMapping("/buscar")
    public Robo buscarRoboPorId(@RequestParam int id) {
        return Robo.buscarRoboPorId(id);
    }

    @PutMapping("/atualizar")
    public Robo atualizarRobo(@RequestParam int id, @RequestBody Robo robo) {
        robo.setId(id);
        Robo.atualizarRobo(robo);
        return robo;
    }
    
    @DeleteMapping("/deletar")
    public void deletarRobo(@RequestParam int id) {
        Robo.removerRoboPorId(id);
    }

    // ADICIONAR ESTES NOVOS ENDPOINTS:

    @PutMapping("/devolver")
    public String devolverRobo(@RequestParam int id, @RequestParam boolean temDefeito) {
        boolean sucesso = Robo.devolverRobo(id, temDefeito);
        
        if (sucesso) {
            if (temDefeito) {
                return "Robô devolvido e descartado por defeito";
            } else {
                return "Robô devolvido com sucesso - aguardando inspeção";
            }
        }
        return "Erro: Robô não encontrado ou não está vendido";
    }

    @PutMapping("/restaurar")
    public String restaurarRobo(@RequestParam int id) {
        boolean sucesso = Robo.restaurarRobo(id);
        
        if (sucesso) {
            return "Robô restaurado e disponível para venda novamente";
        }
        return "Erro: Robô não encontrado ou não está devolvido";
    }

    @GetMapping("/disponiveis")
    public List<Robo> listarRobosDisponiveis() {
        return Robo.getRobos().stream()
                .filter(robo -> robo.getStatus() == StatusRobo.DISPONIVEL)
                .toList();
    }

    @GetMapping("/devolvidos")
    public List<Robo> listarRobosDevolvidos() {
        return Robo.getRobos().stream()
                .filter(robo -> robo.getStatus() == StatusRobo.DEVOLVIDO)
                .toList();
    }
}
