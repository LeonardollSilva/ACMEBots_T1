package t1.projarc.demo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Venda {
    private Robo robo;
    private Cliente cliente;
    private LocalDate dataVenda;
    private double valorVenda;
    private int id;
    private static int proximoId = 1;
    private static int contador = 0;
    private static final int TAMANHO_MAXIMO = 100;
    private static Venda[] vendas = new Venda[TAMANHO_MAXIMO];
    

    public Venda() {
    }
    public Venda(Robo robo, Cliente cliente) {
        this.robo = robo;
        this.cliente = cliente;
        this.dataVenda = LocalDate.now();
        this.valorVenda = robo.getValor() + (robo.getAutonomia() * 1) + (robo.getCarga() * 1) + (robo.getTemperatura() * 0.5);
        if(valorVenda > 1000){
            this.valorVenda = valorVenda * 0.95;
        }
        
        this.id = proximoId++;
        robo.setStatus(StatusRobo.VENDIDO);
        robo.setDataVenda(this.dataVenda);
        if (contador < TAMANHO_MAXIMO) {
            vendas[contador] = this;
            contador++;
        }
    }

    // Getters
    public Robo getRobo() {
        return robo;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public LocalDate getDataVenda() {
        return dataVenda;
    }
    public double getValorVenda() {
        return valorVenda;
    }
    public int getId() {
        return id;
    }
    //Setters
    public void setRobo(Robo robo) {
        this.robo = robo;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }
    public void setValorVenda(double valorVenda) {
        this.valorVenda = valorVenda;
    }
    public void setId(int id) {
        this.id = id;
    }
    public static List<Venda> getVendas() {
        List<Venda> lista = new ArrayList<>();
        for (int i = 0; i < contador; i++) {
            lista.add(vendas[i]);
        }
        return lista;
    }
    
    public static Venda buscarVendaPorId(int id) {
        for (int i = 0; i < contador; i++) {
            if (vendas[i].getId() == id) {
                return vendas[i];
            }
        }
        return null;
    }
    public static boolean removerVendaPorId(int id) {
    for (int i = 0; i < contador; i++) {
        if (vendas[i].getId() == id) {
            for (int j = i; j < contador - 1; j++) {
                vendas[j] = vendas[j + 1];
            }
            vendas[contador - 1] = null;
            contador--;
            return true;
        }
    }
    return false;
}
}
