package t1.projarc.demo;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private static int proximoId = 1;
    
    private String nome;
    private String email;
    private int id;
    private static final int TAMANHO_MAXIMO = 100;
    private static Cliente[] clientes = new Cliente[TAMANHO_MAXIMO];
    private static int contador = 0;

    public Cliente() {
    }
    
    public Cliente(String nome, String email) {
        if(nome == null || nome.isEmpty() || nome.trim().length() < 2 || nome.trim().length() > 100 || !nome.matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
            throw new IllegalArgumentException("Nome não pode ser nulo, vazio ou ter menos de 2 ou mais de 100 caracteres.");
        }
        if(email == null || email.isEmpty() || !email.contains("@") || email.startsWith("@") || email.endsWith("@")) {
            throw new IllegalArgumentException("Email inválido.");
        }
        this.nome = nome;
        this.email = email;
        this.id = proximoId++;
         if (contador < TAMANHO_MAXIMO) {
            clientes[contador] = this;
            contador++;
        }
    }
    
    // Getters
    public String getNome() {
        return nome;
    }
    
    public String getEmail() {
        return email;
    }
    
    public int getId() {
        return id;
    }
    
    // Setters
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setId(int id) {
        this.id = id;
    }
     public static List<Cliente> getClientes() {
        List<Cliente> lista = new ArrayList<>();
        for (int i = 0; i < contador; i++) {
            lista.add(clientes[i]);
        }
        return lista;
    }
    
    public static Cliente buscarClientePorId(int id) {
        for (int i = 0; i < contador; i++) {
            if (clientes[i].getId() == id) {
                return clientes[i];
            }
        }
        return null;
    }
    
    public static boolean removerPorId(int id) {
        for (int i = 0; i < contador; i++) {
            if (clientes[i].getId() == id) {
                for (int j = i; j < contador - 1; j++) {
                    clientes[j] = clientes[j + 1];
                }
                clientes[contador - 1] = null;
                contador--;
                return true;
            }
        }
        return false;
    }
    
    public static void atualizarCliente(Cliente clienteAtualizado) {
        for (int i = 0; i < contador; i++) {
            if (clientes[i].getId() == clienteAtualizado.getId()) {
                clientes[i] = clienteAtualizado;
                break;
            }
        }
    }
}
