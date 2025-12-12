import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String args[]) {
        
        final int NUM_FILOSOFOS = 5;
        // Definindo o limite de tempo em segundos
        final long TEMPO_LIMITE_SEGUNDOS = 30; 

        // ExecutorService para gerenciar o pool de threads e o encerramento com timeout
        ExecutorService executor = Executors.newFixedThreadPool(NUM_FILOSOFOS);
        
        List<Garfo> garfos = new ArrayList<>();
        List<Filosofo> filosofos = new ArrayList<>();

        // 1. Criar os Garfos
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            garfos.add(new Garfo(i + 1));
        }

        // 2. Criar os Filósofos e Atribuir os Garfos
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            
            Filosofo f = new Filosofo("F" + String.valueOf(i + 1));

            // Garfo Esquerdo é o Garfo[i]
            Garfo garfoEsquerdo = garfos.get(i);
            f.setGarfoEsquerdo(garfoEsquerdo);

            // Garfo Direito é o Garfo[(i + 1) % NUM_FILOSOFOS] (circular)
            Garfo garfoDireito = garfos.get((i + 1) % NUM_FILOSOFOS);
            f.setGarfoDireito(garfoDireito);

            filosofos.add(f);
        }
        
        // 3. Configurar a referência do próximo (para a lógica de notify())
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            Filosofo proximoFilosofo = filosofos.get((i + 1) % NUM_FILOSOFOS);
            filosofos.get(i).setProximoFilosofo(proximoFilosofo);
        }

        // 4. Iniciar a execução dos filósofos
        for (Filosofo f : filosofos) {
            executor.submit(f); 
        }
        
        // 5. Encerrar o programa após o tempo limite
        executor.shutdown(); // Inicia o desligamento, mas espera a conclusão das tarefas
        
        try {
            System.out.println("\n*** Programa esperando por no máximo " + TEMPO_LIMITE_SEGUNDOS + " segundos... ***");

            if (!executor.awaitTermination(TEMPO_LIMITE_SEGUNDOS, TimeUnit.SECONDS)) {
                
                System.out.println("\n*** TEMPO LIMITE DE " + TEMPO_LIMITE_SEGUNDOS + " SEGUNDOS ATINGIDO. FORÇANDO PARADA DAS THREADS. ***");
                executor.shutdownNow(); 
            }
            
        } catch(InterruptedException e) {
            System.err.println("A thread principal foi interrompida.");
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\n*** Programa Encerrado. ***");
    }
}