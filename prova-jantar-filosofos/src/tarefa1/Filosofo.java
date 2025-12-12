import java.util.Random;

public class Filosofo implements Runnable {

    private final Random random = new Random();
    private final String nome;

    private Filosofo proximo;
    private Garfo garfoEsquerdo; 
    private Garfo garfoDireito; 

    public Filosofo(String nome) {
        this.nome = nome;
    }

    public void setGarfoEsquerdo(Garfo garfo) {
        this.garfoEsquerdo = garfo;
    }

    public void setGarfoDireito(Garfo garfo) {
        this.garfoDireito = garfo;
    }

    public void setProximoFilosofo(Filosofo next) {
        this.proximo = next;
    }

    @Override
    public void run() {

        // O loop verifica se a thread foi interrompida para permitir o encerramento.
        while (!Thread.currentThread().isInterrupted()) {
            
            // O 'turn' é fixo em 0, pois o ciclo é contínuo.
            this.comer(0); 

            synchronized(this.proximo) {
                this.proximo.notify();
            }

            this.pensar(); 
        }
        
        System.out.println("|LOG| Filosofo " + this.nome + " encerrado devido à interrupção.");
    }


    public void comer(int turn) {
        
        // Log: Quando um filósofo tenta pegar o garfo esquerdo
        System.out.println(
            "|LOG| Filosofo " + this.nome + 
            " TENTA pegar Garfo Esquerdo (ID: " + this.garfoEsquerdo.getId() + ")");
            
        // Garante exclusão mútua na aquisição do Garfo Esquerdo
        synchronized (this.garfoEsquerdo) {
            
            System.out.println(
                "|LOG| Filosofo " + this.nome + 
                " CONSEGUIU pegar Garfo Esquerdo (ID: " + this.garfoEsquerdo.getId() + ")");

            // Log: Quando um filósofo tenta pegar o garfo direito
            System.out.println(
                "|LOG| Filosofo " + this.nome + 
                " TENTA pegar Garfo Direito (ID: " + this.garfoDireito.getId() + ")");

            // Garante exclusão mútua na aquisição do Garfo Direito
            synchronized (this.garfoDireito) {
                
                // Log: Quando um filósofo consegue pegar ambos os garfos e começa a comer
                final long tempoComendo = this.random.nextLong(1000, 3000); 

                System.out.println(
                    "|LOG| *** Filosofo " + this.nome + 
                    " COMEÇA A COMER (Turno: " + String.valueOf(turn) + 
                    ", Tempo: " + String.valueOf(tempoComendo / 1000.) + "s)");

                try {
                    Thread.sleep(tempoComendo);
                } catch (InterruptedException e) {
                    System.out.println("|LOG| Filosofo " + this.nome + " interrompido enquanto comia.");
                    Thread.currentThread().interrupt();
                }

                // Log: Quando um filósofo termina de comer e solta o garfo direito
                System.out.println(
                    "|LOG| Filosofo " + this.nome + 
                    " TERMINOU de comer e SOLTOU Garfo Direito (ID: " + this.garfoDireito.getId() + ")");
                
            } 

            // Log: Quando um filósofo solta o garfo esquerdo
            System.out.println(
                "|LOG| Filosofo " + this.nome + 
                " SOLTOU Garfo Esquerdo (ID: " + this.garfoEsquerdo.getId() + ")");

        } 
    }

    public void pensar() {
        final long tempoPensamento = this.random.nextLong(1000, 3000); 

        // Log: Quando um filósofo começa a pensar
        System.out.println(
                "|LOG| *** Filosofo " + this.nome + 
                " COMEÇA A PENSAR (Tempo: " + String.valueOf(tempoPensamento / 1000.) + "s)");

        try {
            Thread.sleep(tempoPensamento);
        } catch(InterruptedException e) {
            System.out.println("|LOG| Filosofo " + this.nome + " interrompido enquanto pensava.");
            Thread.currentThread().interrupt();
        }
        
        System.out.println("|LOG| Filosofo " + this.nome + " TERMINOU de pensar.");
    }
}