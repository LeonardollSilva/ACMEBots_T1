package t1.projarc.demo;

import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/acmebots")
public class RoboController {
    
    @GetMapping("/listarobos")
    public List<Robo> listarRobos() {
        return Robo.getRobos();
    }
    @PostMapping("/cadastrarobos")
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

    @GetMapping("/buscarobos")
    public Robo buscarRoboPorId(@RequestParam int id) {
        return Robo.buscarRoboPorId(id);
    }

    @PutMapping("/atualizarobos")
    public Robo atualizarRobo(@RequestParam int id, @RequestBody Robo robo) {
        robo.setId(id);
        Robo.atualizarRobo(robo);
        return robo;
    }
    
    @DeleteMapping("/deletarobos")
    public void deletarRobo(@RequestParam int id) {
        Robo.removerRoboPorId(id);
    }

    @PutMapping("/devolverobos")
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

    @PutMapping("/restaurarobos")
    public String restaurarRobo(@RequestParam int id) {
        boolean sucesso = Robo.restaurarRobo(id);
        
        if (sucesso) {
            return "Robô restaurado e disponível para venda novamente";
        }
        return "Erro: Robô não encontrado ou não está devolvido";
    }

    @GetMapping("/robosdisponiveis")
    public List<Robo> listarRobosDisponiveis() {
        return Robo.getRobos().stream()
                .filter(robo -> robo.getStatus() == StatusRobo.DISPONIVEL)
                .toList();
    }

    @GetMapping("/robosdevolvidos")
    public List<Robo> listarRobosDevolvidos() {
        return Robo.getRobos().stream()
                .filter(robo -> robo.getStatus() == StatusRobo.DEVOLVIDO)
                .toList();
    }

    @PostMapping("/validarobometric")
    public List<Robo> validaRoboMetric
    (@RequestParam double autonomia,
     @RequestParam double carga,
     @RequestParam double temperaturaMinima,
     @RequestParam double temperaturaMaxima
    ) {
        return Robo.getRobosDisponiveisMetric(autonomia, carga, temperaturaMinima, temperaturaMaxima);
    }

    @PostMapping("/validaroboimperial")
    public List<Robo> validaRoboImperial
    (@RequestParam double autonomia,
     @RequestParam double carga,
     @RequestParam double temperaturaMinima,
     @RequestParam double temperaturaMaxima
    ) {
        return Robo.getRobosDisponiveisImperial(autonomia, carga, temperaturaMinima, temperaturaMaxima);
    }

    @PostMapping("/cadastro/cadrobometric")
    public boolean cadastrarRoboMetrico(
        @RequestParam String modelo,
        @RequestParam double valor,
        @RequestParam double autonomia,
        @RequestParam double carga,
        @RequestParam double temperatura,
        @RequestParam String dataCadastro
    ) {
         try {
            LocalDate data = LocalDate.parse(dataCadastro);
            new Robo(modelo, valor, autonomia, carga, temperatura, data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @PostMapping("/cadastro/cadroboimperial")
    public boolean cadastrarRoboImperial(
            @RequestParam String modelo,
            @RequestParam double valor,
            @RequestParam double autonomiaMilhas,
            @RequestParam double cargaLibras,
            @RequestParam double temperaturaFahrenheit,
            @RequestParam String dataCadastro) {
        
        try {
            LocalDate data = LocalDate.parse(dataCadastro);
            
            // Converter imperial para métrico
            double autonomiaMetros = autonomiaMilhas / 0.000621371;
            double cargaKg = cargaLibras / 2.20462;
            double tempCelsius = (temperaturaFahrenheit - 32) * 5.0/9.0;
            
            new Robo(modelo, valor, autonomiaMetros, cargaKg, tempCelsius, data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @PutMapping("/cadastro/atualizarobo/{id}/estado/{status}")
    public Robo atualizarStatusRobo(@PathVariable int id, @PathVariable String status) {
        Robo robo = Robo.buscarRoboPorId(id);
        if (robo != null) {
            try {
                robo.setStatus(StatusRobo.valueOf(status.toUpperCase()));
                return robo;
            } catch (IllegalArgumentException e) {
                return null; // Status inválido
            }
        }
        return null;
    }
}

