package t1.projarc.demo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Robo {
    private String modelo;
    private double valor;
    private double autonomia;
    private double carga;
    private double temperatura;
    private int id;
    private LocalDate dataCadastro;
    private static int proximoId = 1;
    private static final int TAMANHO_MAXIMO = 100;
    private static Robo[] robos = new Robo[TAMANHO_MAXIMO];
    private static int contador = 0;
    private StatusRobo status;
    private LocalDate dataVenda;
    private LocalDate hoje = LocalDate.now();
    private LocalDate limiteDeSeteAnosAtras = hoje.minusYears(7);

    public Robo() {
    }
    
    public Robo(String modelo, double valor, double autonomia, double carga, double temperatura, LocalDate dataCadastro) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero.");
        }
        if (autonomia <= 0) {
            throw new IllegalArgumentException("Autonomia deve ser maior que zero.");
        }
        if (dataCadastro == null) {
            throw new IllegalArgumentException("Data de cadastro não pode ser nula.");
        }
        if (dataCadastro.isAfter(hoje)) {
            throw new IllegalArgumentException("Data de cadastro não pode ser uma data futura em relação ao sistema.");
        }
        this.modelo = modelo;
        this.valor = valor;
        this.autonomia = autonomia;
        this.carga = carga;
        this.temperatura = temperatura;
        this.dataCadastro = dataCadastro;
        this.id = proximoId++;
        
        if(dataCadastro.isBefore(limiteDeSeteAnosAtras)){
            this.status = StatusRobo.DESCARTADO;
        } else {
            this.status = StatusRobo.DISPONIVEL;
        }
        this.dataVenda = null;
        if (contador < TAMANHO_MAXIMO) {
            robos[contador] = this;
            contador++;
        }
    }
    
    // Getters
    public String getModelo() {
        return modelo;
    }

    public double getValor() {
        return valor;
    }

    public double getAutonomia() {
        return autonomia;
    }

    public double getCarga() {
        return carga;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public int getId() {
        return id;
    }
    public LocalDate getDataCadastro() {
        return dataCadastro;
    }
    public StatusRobo getStatus() {
        return status;
    }
    public LocalDate getDataVenda() {
        return dataVenda;
    }
    //Setters
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    public void setValor(double valor) {
        this.valor = valor;
    }
    public void setAutonomia(double autonomia) {
        this.autonomia = autonomia;
    }
    public void setCarga(double carga) {
        this.carga = carga;
    }
    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
    public void setStatus(StatusRobo status) {
        this.status = status;
    }
    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

    public static void verificarRobosVendidosParaDescarte() {
        LocalDate hoje = LocalDate.now();
        LocalDate limiteVenda = hoje.minusYears(5);
        
        for (int i = 0; i < contador; i++) {
            Robo robo = robos[i];
            
            if (robo.getStatus() == StatusRobo.VENDIDO && 
                robo.getDataVenda() != null && 
                robo.getDataVenda().isBefore(limiteVenda)) {
                robo.setStatus(StatusRobo.DESCARTADO);
            }
        }
    }

    public static List<Robo> getRobos() {
        verificarRobosVendidosParaDescarte();
        
        List<Robo> lista = new ArrayList<>();
        for (int i = 0; i < contador; i++) {
            lista.add(robos[i]);
        }
        return lista;
    }

    public static Robo buscarRoboPorId(int id) {
        verificarRobosVendidosParaDescarte();
        
        for (int i = 0; i < contador; i++) {
            if (robos[i].getId() == id) {
                return robos[i];
            }
        }
        return null;
    }
    public static boolean removerRoboPorId(int id) {
        for (int i = 0; i < contador; i++) {
            if (robos[i].getId() == id) {
                for (int j = i; j < contador - 1; j++) {
                    robos[j] = robos[j + 1];
                }
                robos[contador - 1] = null;
                contador--;
                return true;
            }
        }
        return false;
    }
    public static void atualizarRobo(Robo roboAtualizado) {
        for (int i = 0; i < contador; i++) {
            if (robos[i].getId() == roboAtualizado.getId()) {
                robos[i] = roboAtualizado;
                break;
            }
        }
    }

    public static boolean devolverRobo(int id, boolean temDefeito) {
        Robo robo = buscarRoboPorId(id);
        
        if (robo != null && robo.getStatus() == StatusRobo.VENDIDO) {
            if (temDefeito) {
                robo.setStatus(StatusRobo.DESCARTADO);
            } else {
                robo.setStatus(StatusRobo.DEVOLVIDO);
            }
            robo.setDataVenda(null); 
            return true;
        }
        return false; 
    }

    public static boolean restaurarRobo(int id) {
        Robo robo = buscarRoboPorId(id);
        
        if (robo != null && robo.getStatus() == StatusRobo.DEVOLVIDO) {
            robo.setStatus(StatusRobo.DISPONIVEL);
            return true;
        }
        return false;
    }
    private static double metrosParaMilhas(double metros) {
        return metros * 0.000621371;
    }
    private static double quilosParaLibras(double quilos) {
        return quilos * 2.20462;
    }
    private static double celsiusParaFahrenheit(double celsius) {
        return (celsius * 9.0/5.0) + 32;
    }
    private static double fahrenheitParaCelsius(double fahrenheit) {
        return (fahrenheit - 32) * 5.0/9.0;
    }
    private static double librasParaQuilos(double libras) {
        return libras / 2.20462;
    }
    private static double milhasParaMetros(double milhas) {
        return milhas / 0.000621371;
    }

    public static List<Robo> getRobosDisponiveisMetric(double autonomiaMinimaMetros, double cargaMinimaQuilos, double temperaturaMinimaCelsius, double temperaturaMaximaCelsius) {
        verificarRobosVendidosParaDescarte();
        
        List<Robo> listaDeRobosDisponiveis = new ArrayList<>();
        
        for (int i = 0; i < contador; i++) {
            Robo robo = robos[i];
            if (robo.getStatus() == StatusRobo.DISPONIVEL &&
                robo.getAutonomia() >= autonomiaMinimaMetros &&
                robo.getCarga() >= cargaMinimaQuilos &&
                robo.getTemperatura() >= temperaturaMinimaCelsius &&
                robo.getTemperatura() <= temperaturaMaximaCelsius) {
                listaDeRobosDisponiveis.add(robo);
            }
        }
        return listaDeRobosDisponiveis;
    }
    public static List<Robo> getRobosDisponiveisImperial(double autonomiaMinimaMilhas, double cargaMaximaLibras, double temperaturaMinimaFahrenheit, double temperaturaMaximaFahrenheit) {
        verificarRobosVendidosParaDescarte();
        
        List<Robo> listaDeRobosDisponiveis = new ArrayList<>();
        
        for (int i = 0; i < contador; i++) {
            Robo robo = robos[i];
            if (robo.getStatus() == StatusRobo.DISPONIVEL &&
                metrosParaMilhas(robo.getAutonomia()) >= autonomiaMinimaMilhas &&
                quilosParaLibras(robo.getCarga()) >= cargaMaximaLibras &&
                celsiusParaFahrenheit(robo.getTemperatura()) >= temperaturaMinimaFahrenheit &&
                celsiusParaFahrenheit(robo.getTemperatura()) <= temperaturaMaximaFahrenheit) {
                listaDeRobosDisponiveis.add(robo);
            }
        }
        return listaDeRobosDisponiveis;
    }
}
