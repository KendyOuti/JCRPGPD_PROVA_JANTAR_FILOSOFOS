import java.util.Random;

public class Filosofo implements Runnable {

    private final Random random = new Random();
    private final String nome; 

    private Filosofo proximo;
    private Garfo garfoEsquerdo; 
    private Garfo garfoDireito; 
    
    // Contador de refeições
    private int refeicoesComidas = 0; 
    
    private static final String FILOSOFO_ASSIMETRICO = "F4"; 

    public Filosofo(String nome) {
        this.nome = nome;
    }

    public int getRefeicoesComidas() {
        return refeicoesComidas;
    }
    
    // MÉTODO CORRIGIDO: Getter para o nome
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

    @Override
    public void run() {

        // Loop infinito, interrompível pela thread principal
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
        
        if (this.nome.equals(FILOSOFO_ASSIMETRICO)) {
            // REGRA ASSIMÉTRICA (F4): PEGAR DIREITO, DEPOIS ESQUERDO
            
            System.out.println(
                "|LOG| Filosofo " + this.nome + 
                " TENTA pegar Garfo DIREITO (ID: " + this.garfoDireito.getId() + ")");
                
            synchronized (this.garfoDireito) {
                
                System.out.println(
                    "|LOG| Filosofo " + this.nome + 
                    " CONSEGUIU pegar Garfo DIREITO (ID: " + this.garfoDireito.getId() + ")");

                System.out.println(
                    "|LOG| Filosofo " + this.nome + 
                    " TENTA pegar Garfo ESQUERDO (ID: " + this.garfoEsquerdo.getId() + ")");

                synchronized (this.garfoEsquerdo) {
                    executarFaseComer(); 
                } 

                System.out.println(
                    "|LOG| Filosofo " + this.nome + 
                    " SOLTOU Garfo DIREITO (ID: " + this.garfoDireito.getId() + ")");

            } 

        } else {
            // REGRA PADRÃO (F1, F2, F3, F5): PEGAR ESQUERDO, DEPOIS DIREITO
            
            System.out.println(
                "|LOG| Filosofo " + this.nome + 
                " TENTA pegar Garfo Esquerdo (ID: " + this.garfoEsquerdo.getId() + ")");
                
            synchronized (this.garfoEsquerdo) {
                
                System.out.println(
                    "|LOG| Filosofo " + this.nome + 
                    " CONSEGUIU pegar Garfo Esquerdo (ID: " + this.garfoEsquerdo.getId() + ")");

                System.out.println(
                    "|LOG| Filosofo " + this.nome + 
                    " TENTA pegar Garfo Direito (ID: " + this.garfoDireito.getId() + ")");

                synchronized (this.garfoDireito) {
                    executarFaseComer();
                } 

                System.out.println(
                    "|LOG| Filosofo " + this.nome + 
                    " SOLTOU Garfo Esquerdo (ID: " + this.garfoEsquerdo.getId() + ")");

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