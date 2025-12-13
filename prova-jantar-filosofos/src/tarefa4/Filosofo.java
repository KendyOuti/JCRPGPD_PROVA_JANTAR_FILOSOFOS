import java.util.Random;

public class Filosofo implements Runnable {

    private final Random random = new Random();
    private final String nome; 
    private final int id; // ID do filósofo (0 a N-1)
    
    private Mesa monitorMesa; // Referência ao Monitor central
    
    private int refeicoesComidas = 0; 
    
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

    // Setter para o Monitor Mesa
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
            // 1. Tenta pegar os garfos chamando o Monitor
            monitorMesa.pegarGarfos(this.id);
            
            // 2. Se saiu de pegarGarfos(), significa que CONSEGUIU comer
            executarFaseComer();
            
        } catch (InterruptedException e) {
             Thread.currentThread().interrupt();
        } finally {
            // 3. Devolve os garfos chamando o Monitor
            // A chamada deve estar dentro do finally para garantir a liberação do recurso.
            if (!Thread.currentThread().isInterrupted()) {
                 monitorMesa.largarGarfos(this.id);
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
        
        System.out.println("|LOG| Filosofo " + this.nome + " TERMINOU de comer.");
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