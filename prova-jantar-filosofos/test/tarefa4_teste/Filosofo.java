import java.util.Random;

public class Filosofo implements Runnable {

    private final Random random = new Random();
    private final String nome; 
    private final int id; // ID do filósofo (0 a N-1)
    
    private Mesa monitorMesa; // Referência ao Monitor central
    
    private int refeicoesComidas = 0; 
    
    // VARIÁVEIS PARA AS MÉTRICAS DA TAREFA 5
    private long tempoTotalEspera = 0;
    private long inicioEspera; 
    private int tentativasComer = 0; 

    public Filosofo(String nome, int id) {
        this.nome = nome;
        this.id = id;
    }

    public int getRefeicoesComidas() {
        return refeicoesComidas;
    }
    
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
    
    public void setMesa(Mesa mesa) {
        this.monitorMesa = mesa;
    }

    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {
            this.comer(); 
            this.pensar(); 
        }
        
        System.out.println("|LOG| Filosofo " + this.nome + " encerrado devido à interrupção.");
    }


    public void comer() {
        try {
            // 1. MARCA O INÍCIO DA ESPERA
            inicioEspera = System.currentTimeMillis();
            tentativasComer++;

            // 2. Tenta pegar os garfos chamando o Monitor (PONTO DE BLOQUEIO/WAIT)
            monitorMesa.pegarGarfos(this.id);
            
            // 3. FIM DA ESPERA (o Monitor liberou a thread)
            long fimEspera = System.currentTimeMillis();
            tempoTotalEspera += (fimEspera - inicioEspera);
            
            // 4. Executa a fase de comer
            executarFaseComer();
            
        } catch (InterruptedException e) {
             Thread.currentThread().interrupt();
        } finally {
            // 5. Devolve os garfos chamando o Monitor
            if (monitorMesa != null && !Thread.currentThread().isInterrupted()) {
                monitorMesa.largarGarfos(this.id);
            }
        }
    }
    
    private void executarFaseComer() throws InterruptedException {
        this.refeicoesComidas++; 
        
        final long tempoComendo = this.random.nextLong(1000, 3000); 

        System.out.println(
            "|LOG| *** Filosofo " + this.nome + 
            " COMEÇA A COMER (Refeição #" + this.refeicoesComidas + " - T4)");

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