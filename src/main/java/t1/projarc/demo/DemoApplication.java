package t1.projarc.demo;

import java.time.LocalDate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	@PostConstruct
	public void init() {
		System.out.println("Aplicación iniciada corretamente.");
		// CRIANDO 10 ROBOS
		new Robo("T-800", 15000.0, 8000.0, 120.0, 45.0, LocalDate.of(2020, 1, 15));
        new Robo("T-1000", 25000.0, 12000.0, 80.0, 50.0, LocalDate.of(2021, 3, 10));
        new Robo("T-850", 18000.0, 9000.0, 130.0, 42.0, LocalDate.of(2019, 8, 22));
        new Robo("T-X", 35000.0, 15000.0, 90.0, 55.0, LocalDate.of(2022, 6, 5));
        new Robo("T-600", 12000.0, 6000.0, 150.0, 40.0, LocalDate.of(2018, 11, 30)); // Antigo - será descartado
        new Robo("T-700", 14000.0, 7500.0, 140.0, 38.0, LocalDate.of(2021, 9, 12));
        new Robo("T-3000", 45000.0, 20000.0, 70.0, 60.0, LocalDate.of(2023, 2, 18));
        new Robo("T-5000", 55000.0, 25000.0, 60.0, 65.0, LocalDate.of(2023, 7, 25));
        new Robo("Rev-9", 40000.0, 18000.0, 85.0, 58.0, LocalDate.of(2022, 12, 8));
        new Robo("T-101", 16000.0, 8500.0, 125.0, 46.0, LocalDate.of(2017, 5, 14)); // Muito antigo - será descartado

        // CRIANDO 3 CLIENTES 
        new Cliente("Sarah Connor", "sarah.connor@resistance.com");
        new Cliente("John Connor", "john.connor@future.com");
        new Cliente("Kyle Reese", "kyle.reese@resistance.com");

		// CRIANDO 2 VENDAS INICIAIS
        Cliente cliente1 = Cliente.buscarClientePorId(1);
        Cliente cliente2 = Cliente.buscarClientePorId(2);
        Robo robo1 = Robo.buscarRoboPorId(1); 
		Robo robo2 = Robo.buscarRoboPorId(2); 
    
		if (cliente1 != null && robo1 != null && robo1.getStatus() == StatusRobo.DISPONIVEL) {
            new Venda(robo1, cliente1);
            System.out.println("✅ Venda 1: " + cliente1.getNome() + " comprou " + robo1.getModelo());
        }
        
        if (cliente2 != null && robo2 != null && robo2.getStatus() == StatusRobo.DISPONIVEL) {
            new Venda(robo2, cliente2);
            System.out.println("✅ Venda 2: " + cliente2.getNome() + " comprou " + robo2.getModelo());
        }

		System.out.println("\n SISTEMA ACMEBOTS - MODELOS TERMINATOR:");
        System.out.println(" Clientes: " + Cliente.getClientes().size());
        System.out.println(" Robôs: " + Robo.getRobos().size());
        System.out.println(" Vendas: " + Venda.getVendas().size());
        
        long disponiveis = Robo.getRobos().stream().filter(r -> r.getStatus() == StatusRobo.DISPONIVEL).count();
        long vendidos = Robo.getRobos().stream().filter(r -> r.getStatus() == StatusRobo.VENDIDO).count();
        long descartados = Robo.getRobos().stream().filter(r -> r.getStatus() == StatusRobo.DESCARTADO).count();
        
        System.out.println(" Disponíveis: " + disponiveis);
        System.out.println(" Vendidos: " + vendidos);
        System.out.println(" Descartados: " + descartados);
      
	}

}
