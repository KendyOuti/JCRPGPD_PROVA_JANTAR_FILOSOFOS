import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String args[]) {
        
        final int NUM_FILOSOFOS = 5;
        final long TEMPO_LIMITE_SEGUNDOS = 120; 

        ExecutorService executor = Executors.newFixedThreadPool(NUM_FILOSOFOS);
        
        Garfo[] garfos = new Garfo[NUM_FILOSOFOS];
        List<Filosofo> filosofos = new ArrayList<>(); 
        
        // 1. Criar os Garfos
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            garfos[i] = new Garfo(i + 1);
        }
        
        // 2. Cria a instância do Monitor Mesa
        Mesa monitorMesa = new Mesa(NUM_FILOSOFOS, garfos);

        // 3. Criar os Filósofos e Atribuir o Monitor
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            // Passamos o ID (0 a 4) para o Monitor
            Filosofo f = new Filosofo("F" + String.valueOf(i + 1), i); 
            
            // Atribui o Monitor central
            f.setMesa(monitorMesa); 

            filosofos.add(f); 
        }

        // 4. Iniciar a execução
        for (Filosofo f : filosofos) {
            executor.submit(f); 
        }
        
        // 5. Encerrar o programa após o tempo limite
        executor.shutdown(); 
        
        try {
            System.out.println("\nTarefa 4 - Solução com Monitores e Garantia de Fairness");
            System.out.println("\n*** Programa esperando por no máximo " + TEMPO_LIMITE_SEGUNDOS + " segundos (2 minutos)... ***");

            if (!executor.awaitTermination(TEMPO_LIMITE_SEGUNDOS, TimeUnit.SECONDS)) {
                
                System.out.println("\n*** TEMPO LIMITE DE " + TEMPO_LIMITE_SEGUNDOS + " SEGUNDOS ATINGIDO. FORÇANDO PARADA DAS THREADS. ***");
                executor.shutdownNow(); 
                executor.awaitTermination(5, TimeUnit.SECONDS); 
            }
            
        } catch(InterruptedException e) {
            System.err.println("A thread principal foi interrompida.");
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\n*** Programa Encerrado. ***");
        
        // 6. Exibir Estatísticas
        exibirEstatisticas(filosofos);
    }
    
    private static void exibirEstatisticas(List<Filosofo> filosofos) {
        System.out.println("\n=============================================");
        System.out.println("     ESTATÍSTICAS DE REFEIÇÕES (2 MINUTOS)   ");
        System.out.println("=============================================");
        
        int totalRefeicoes = 0;

        System.out.printf("| %-10s | %-12s | %-19s |%n", "Filósofo", "Refeições", "Ordem/Controle");
        System.out.println("|------------|--------------|---------------------|");

        for (Filosofo f : filosofos) {
            int refeicoes = f.getRefeicoesComidas();
            totalRefeicoes += refeicoes;
            
            String nomeFilosofo = f.getNome();
            String ordem = "Monitor (wait/notify)"; 
            
            System.out.printf("| %-10s | %-12d | %-19s |%n", nomeFilosofo, refeicoes, ordem);
        }
        
        System.out.println("=============================================");
        System.out.printf(" TOTAL GERAL DE REFEIÇÕES: %d%n", totalRefeicoes);
        System.out.println("=============================================");
    }
}