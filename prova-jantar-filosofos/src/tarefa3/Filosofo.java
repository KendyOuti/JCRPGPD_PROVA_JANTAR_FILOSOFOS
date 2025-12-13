import java.util.Random;
import java.util.concurrent.Semaphore; // Importado o Semaphore

public class Filosofo implements Runnable {

    private final Random random = new Random();
    private final String nome; 

    private Filosofo proximo;
    private Garfo garfoEsquerdo; 
    private Garfo garfoDireito; 
    
    private int refeicoesComidas = 0; 
    
    // Semáforo central para controle de acesso à mesa
    private Semaphore limiteFilosofos;
    
    // O método comer será simplificado, usando apenas a ordem padrão.
    public Filosofo(String nome) {
        this.nome = nome;
    }

    public int getRefeicoesComidas() {
        return refeicoesComidas;
    }
    
    public String getNome() {
        return nome;
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
    
    // Setter para o Semáforo central
    public void setLimiteFilosofos(Semaphore limite) {
        this.limiteFilosofos = limite;
    }

    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {
            
            this.comer(0); 

            synchronized(this.proximo) {
                this.proximo.notify();
            }

            this.pensar(); 
        }
        
        System.out.println("|LOG| Filosofo " + this.nome + " encerrado devido à interrupção.");
    }


    public void comer(int turn) {
        
        try {
            // REQUISITO 2: Tenta adquirir uma permissão do semáforo (limite = 4)
            System.out.println("|LOG| Filosofo " + this.nome + " TENTA adquirir permissão da mesa (Semáforo)");
            limiteFilosofos.acquire();
            System.out.println("|LOG| Filosofo " + this.nome + " CONSEGUIU permissão da mesa. Permissões restantes: " + limiteFilosofos.availablePermits());

            // A ordem de aquisição agora é a PADRÃO (Esquerdo -> Direito) para todos
            // MANTENDO A SINCRONIZAÇÃO DOS GARFOS COM synchronized (REQUISITO 3)
            
            // 1. Tenta pegar Garfo Esquerdo
            System.out.println(
                "|LOG| Filosofo " + this.nome + 
                " TENTA pegar Garfo Esquerdo (ID: " + this.garfoEsquerdo.getId() + ")");
                
            synchronized (this.garfoEsquerdo) {
                
                System.out.println(
                    "|LOG| Filosofo " + this.nome + 
                    " CONSEGUIU pegar Garfo Esquerdo (ID: " + this.garfoEsquerdo.getId() + ")");

                // 2. Tenta pegar Garfo Direito
                System.out.println(
                    "|LOG| Filosofo " + this.nome + 
                    " TENTA pegar Garfo Direito (ID: " + this.garfoDireito.getId() + ")");

                synchronized (this.garfoDireito) {
                    executarFaseComer();
                } 
                // Garfo Direito liberado aqui

                System.out.println(
                    "|LOG| Filosofo " + this.nome + 
                    " SOLTOU Garfo Esquerdo (ID: " + this.garfoEsquerdo.getId() + ")");

            } // Garfo Esquerdo liberado aqui

        } catch (InterruptedException e) {
             Thread.currentThread().interrupt();
        } finally {
            // LIBERA a permissão do semáforo após tentar comer, mesmo se interrompido
            if (limiteFilosofos != null) {
                 System.out.println("|LOG| Filosofo " + this.nome + " LIBEROU permissão da mesa.");
                 limiteFilosofos.release();
            }
        }
    }
    
    private void executarFaseComer() {
        this.refeicoesComidas++; 
        
        final long tempoComendo = this.random.nextLong(1000, 3000); 

        System.out.println(
            "|LOG| *** Filosofo " + this.nome + 
            " COMEÇA A COMER (Refeição #" + this.refeicoesComidas + 
            ", Tempo: " + String.valueOf(tempoComendo / 1000.) + "s)");

        try {
            Thread.sleep(tempoComendo);
        } catch (InterruptedException e) {
            System.out.println("|LOG| Filosofo " + this.nome + " interrompido enquanto comia.");
            Thread.currentThread().interrupt();
        }

        System.out.println(
            "|LOG| Filosofo " + this.nome + 
            " TERMINOU de comer e SOLTOU Garfo Direito (ID: " + this.garfoDireito.getId() + ")");
    }

    public void pensar() {
        final long tempoPensamento = this.random.nextLong(1000, 3000); 

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