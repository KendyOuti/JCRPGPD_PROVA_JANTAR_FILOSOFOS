import java.util.Random;
import java.util.concurrent.Semaphore;

public class Filosofo implements Runnable {

    private final Random random = new Random();
    private final String nome; 
    private final Garfo garfoEsquerdo;
    private final Garfo garfoDireito;
    private final Semaphore limiteFilosofos; // Semáforo central N-1
    
    private int refeicoesComidas = 0; 
    
    // VARIÁVEIS PARA AS MÉTRICAS DA TAREFA 5
    private long tempoTotalEspera = 0;
    private long inicioEspera; 
    private int tentativasComer = 0; 

    public Filosofo(String nome, Garfo garfoEsquerdo, Garfo garfoDireito, Semaphore limiteFilosofos) {
        this.nome = nome;
        this.garfoEsquerdo = garfoEsquerdo;
        this.garfoDireito = garfoDireito;
        this.limiteFilosofos = limiteFilosofos;
    }

    public int getRefeicoesComidas() {
        return refeicoesComidas;
    }
    
    // CORREÇÃO: ADIÇÃO DO MÉTODO getNome()
    public String getNome() {
        return nome;
    }

    // GETTERS PARA AS MÉTRICAS DA TAREFA 5
    public long getTempoTotalEspera() {
        return tempoTotalEspera;
    }

    public int getTentativasComer() {
        return tentativasComer;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            this.pensar();
            this.comer();
        }
    }

    public void comer() {
        try {
            // 1. MARCA O INÍCIO DA ESPERA (antes de adquirir a permissão do Semáforo)
            inicioEspera = System.currentTimeMillis();
            tentativasComer++;
            
            // 2. LÓGICA SEMÁFORO N-1: Bloqueio para prevenir Deadlock
            limiteFilosofos.acquire();

            // 3. FIM DA ESPERA NO SEMÁFORO (adquiriu a permissão)
            long fimEspera = System.currentTimeMillis();
            tempoTotalEspera += (fimEspera - inicioEspera);

            // 4. Aquisição dos Locks (Ordem Padrão E -> D)
            synchronized (garfoEsquerdo) {
                synchronized (garfoDireito) {
                    executarFaseComer();
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // 5. Liberação da Permissão
            if (!Thread.currentThread().isInterrupted()) {
                 limiteFilosofos.release();
            }
        }
    }
    
    private void executarFaseComer() throws InterruptedException {
        this.refeicoesComidas++; 
        final long tempoComendo = this.random.nextLong(1000, 3000); 

        System.out.println(
            "|LOG| *** Filosofo " + this.nome + 
            " COMEÇA A COMER (Refeição #" + this.refeicoesComidas + " - T3)");

        Thread.sleep(tempoComendo);
        
        System.out.println("|LOG| Filosofo " + this.nome + " TERMINOU de comer.");
    }

    public void pensar() {
        final long tempoPensamento = this.random.nextLong(1000, 3000); 
        System.out.println("|LOG| *** Filosofo " + this.nome + " COMEÇA A PENSAR.");
        try {
            Thread.sleep(tempoPensamento);
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}