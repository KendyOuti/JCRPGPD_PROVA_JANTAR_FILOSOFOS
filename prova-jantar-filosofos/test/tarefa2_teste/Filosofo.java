import java.util.Random;

public class Filosofo implements Runnable {

    private final Random random = new Random();
    private final String nome; 
    private final Garfo garfoEsquerdo;
    private final Garfo garfoDireito;
    private final int id; // Usado para a regra assimétrica (ID par/ímpar)
    
    private int refeicoesComidas = 0; 

    // VARIÁVEIS PARA AS MÉTRICAS DA TAREFA 5
    private long tempoTotalEspera = 0;
    private long inicioEspera; 
    private int tentativasComer = 0; 
    
    public Filosofo(String nome, int id, Garfo garfoEsquerdo, Garfo garfoDireito) {
        this.nome = nome;
        this.id = id;
        this.garfoEsquerdo = garfoEsquerdo;
        this.garfoDireito = garfoDireito;
    }

    public int getRefeicoesComidas() {
        return refeicoesComidas;
    }
    
    // MÉTODO getNome() CORRIGIDO
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
            // 1. MARCA O INÍCIO DA ESPERA (antes de tentar adquirir o 1º lock)
            inicioEspera = System.currentTimeMillis();
            tentativasComer++;

            // 2. LÓGICA ASSIMÉTRICA: Previne Deadlock
            if (id % 2 == 0) { // Filósofos pares pegam Esquerdo, depois Direito
                synchronized (garfoEsquerdo) {
                    synchronized (garfoDireito) {
                        // FIM DA ESPERA (adquiriu o 2º lock)
                        long fimEspera = System.currentTimeMillis();
                        tempoTotalEspera += (fimEspera - inicioEspera);
                        
                        executarFaseComer();
                    }
                }
            } else { // Filósofos ímpares pegam Direito, depois Esquerdo
                synchronized (garfoDireito) {
                    synchronized (garfoEsquerdo) {
                        // FIM DA ESPERA (adquiriu o 2º lock)
                        long fimEspera = System.currentTimeMillis();
                        tempoTotalEspera += (fimEspera - inicioEspera);
                        
                        executarFaseComer();
                    }
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void executarFaseComer() throws InterruptedException {
        this.refeicoesComidas++; 
        final long tempoComendo = this.random.nextLong(1000, 3000); 

        System.out.println(
            "|LOG| *** Filosofo " + this.nome + 
            " COMEÇA A COMER (Refeição #" + this.refeicoesComidas + " - T2)");

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