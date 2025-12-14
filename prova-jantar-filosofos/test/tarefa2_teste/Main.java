import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String args[]) {
        
        final int NUM_FILOSOFOS = 5;
        // Tempo de execução alterado para 5 minutos (300 segundos)
        final long TEMPO_LIMITE_SEGUNDOS = 300; 

        ExecutorService executor = Executors.newFixedThreadPool(NUM_FILOSOFOS);
        
        Garfo[] garfos = new Garfo[NUM_FILOSOFOS];
        List<Filosofo> filosofos = new ArrayList<>(); 
        
        // 1. Criar os Garfos
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            garfos[i] = new Garfo(i + 1);
        }
        
        // 2. Criar os Filósofos e atribuir Garfos
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            Garfo garfoEsquerdo = garfos[i];
            Garfo garfoDireito = garfos[(i + 1) % NUM_FILOSOFOS];

            Filosofo f = new Filosofo("F" + String.valueOf(i + 1), i, garfoEsquerdo, garfoDireito);
            filosofos.add(f); 
        }

        // 3. Iniciar a execução e medir o tempo total
        long inicioExecucao = System.currentTimeMillis();
        for (Filosofo f : filosofos) {
            executor.submit(f); 
        }
        
        // 4. Encerrar o programa após o tempo limite
        executor.shutdown(); 
        try {
            if (!executor.awaitTermination(TEMPO_LIMITE_SEGUNDOS, TimeUnit.SECONDS)) {
                executor.shutdownNow(); 
                executor.awaitTermination(5, TimeUnit.SECONDS); 
            }
        } catch(InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        long duracaoExecucaoMs = System.currentTimeMillis() - inicioExecucao;
        
        // 5. Exibir Estatísticas - Passando NUM_FILOSOFOS
        exibirEstatisticas(filosofos, duracaoExecucaoMs, "Ordem Assimétrica (T2)", NUM_FILOSOFOS);
    }
    
    // Método que recebe NUM_FILOSOFOS como argumento
    private static void exibirEstatisticas(List<Filosofo> filosofos, long duracaoExecucaoMs, String controle, final int NUM_FILOSOFOS) {
        
        System.out.println("\n=======================================================");
        System.out.println("  ESTATÍSTICAS DE REFEIÇÕES (CONTROLE: " + controle + ")");
        System.out.println("TEMPO DE EXECUÇÃO: " + (duracaoExecucaoMs / 1000.0) + " segundos");
        System.out.println("=======================================================");
        
        int totalRefeicoes = 0;
        long totalTempoEspera = 0;
        int totalTentativas = 0;
        double somaQuadradosDiferencas = 0;
        
        System.out.printf("| %-10s | %-12s | %-12s | %-15s |%n", "Filósofo", "Refeições", "Tentativas", "Tempo Médio Esp. (ms)");
        System.out.println("|------------|--------------|--------------|-----------------------|");

        for (Filosofo f : filosofos) {
            int refeicoes = f.getRefeicoesComidas();
            long tempoTotalEspera = f.getTempoTotalEspera();
            int tentativas = f.getTentativasComer();

            double tempoMedioEspera = (tentativas > 0) ? (double) tempoTotalEspera / tentativas : 0;
            
            totalRefeicoes += refeicoes;
            totalTempoEspera += tempoTotalEspera;
            totalTentativas += tentativas;
            
            System.out.printf("| %-10s | %-12d | %-12d | %-22.2f |%n", f.getNome(), refeicoes, tentativas, tempoMedioEspera);
        }
        
        // --- Cálculo de Coeficiente de Variação (Justiça) ---
        double mediaRefeicoes = (double) totalRefeicoes / filosofos.size();
        for (Filosofo f : filosofos) {
            somaQuadradosDiferencas += Math.pow(f.getRefeicoesComidas() - mediaRefeicoes, 2);
        }
        double desvioPadrao = Math.sqrt(somaQuadradosDiferencas / filosofos.size());
        double coeficienteVariacao = (mediaRefeicoes > 0) ? (desvioPadrao / mediaRefeicoes) * 100 : 0; // em %

        // --- Cálculo da Utilização dos Garfos (Throughput) ---
        // Assumimos 2000ms como tempo médio para comer (média de 1000ms a 3000ms)
        final double TEMPO_MEDIO_COMENDO_MS = 2000.0; 
        double tempoTotalComendoMs = totalRefeicoes * TEMPO_MEDIO_COMENDO_MS;
        
        // Utilização = (Tempo total que os filósofos ficaram comendo / (Tempo total de execução * Número de filósofos))
        double utilizacaoGeral = (tempoTotalComendoMs / (duracaoExecucaoMs * NUM_FILOSOFOS)) * 100; // em %

        System.out.println("=======================================================");
        System.out.printf(" TOTAL GERAL DE REFEIÇÕES: %d%n", totalRefeicoes);
        System.out.printf(" COEFICIENTE DE VARIAÇÃO (Justiça): %.2f%%%n", coeficienteVariacao);
        System.out.printf(" TAXA DE UTILIZAÇÃO (Estimada): %.2f%%%n", utilizacaoGeral);
        System.out.println("=======================================================");
    }
}
