public class Mesa {
    
    private final int numFilosofos;
    private final Garfo[] garfos;

    // Estado de cada filósofo: PENSANDO, COM FOME, COMENDO
    private enum Estado { PENSANDO, COM_FOME, COMENDO };
    private final Estado[] estado;

    public Mesa(int numFilosofos, Garfo[] garfos) {
        this.numFilosofos = numFilosofos;
        this.garfos = garfos;
        this.estado = new Estado[numFilosofos];
        
        for (int i = 0; i < numFilosofos; i++) {
            this.estado[i] = Estado.PENSANDO;
        }
        System.out.println("|LOG| Monitor Mesa inicializado.");
    }
    
    // Método sincronizado para pegar os garfos
    public synchronized void pegarGarfos(int idFilosofo) throws InterruptedException {
        
        System.out.println(
            "|LOG| F" + (idFilosofo + 1) + " TENTA pegar garfos.");
        
        // 1. Filósofo se declara com fome
        estado[idFilosofo] = Estado.COM_FOME;
        
        // 2. Tenta comer imediatamente
        testar(idFilosofo); 
        
        // 3. Se não pode comer, espera (usa while para evitar Notificação Perdida)
        while (estado[idFilosofo] != Estado.COMENDO) {
            System.out.println(
                "|LOG| F" + (idFilosofo + 1) + 
                " não pode comer. Aguardando...");
            // Libera o lock e espera ser acordado
            this.wait(); 
        }

        // Se saiu do wait(), o estado é COMENDO
        int idGarfoEsquerdo = garfos[idFilosofo].getId();
        int idGarfoDireito = garfos[ (idFilosofo + 1) % numFilosofos ].getId();
        System.out.println("|LOG| *** F" + (idFilosofo + 1) + " CONSEGUIU os garfos e VAI COMER. Garfos: " + idGarfoEsquerdo + " e " + idGarfoDireito);
    }
    
    // Método sincronizado para largar os garfos
    public synchronized void largarGarfos(int idFilosofo) {
        
        estado[idFilosofo] = Estado.PENSANDO;
        System.out.println(
            "|LOG| F" + (idFilosofo + 1) + " largou garfos e está PENSANDO.");

        // 1. Testa se os vizinhos podem comer agora e acorda-os (se necessário)
        testar((idFilosofo + numFilosofos - 1) % numFilosofos); // Esquerda
        testar((idFilosofo + 1) % numFilosofos); // Direita
        
        // 2. Acorda todos os filósofos que estão em wait() para que reavaliem a condição
        this.notifyAll(); 
    }

    /**
     * Testa se o filósofo 'i' pode comer e, se sim, muda o estado para COMENDO.
     */
    private void testar(int i) {
        int esq = (i + numFilosofos - 1) % numFilosofos;
        int dir = (i + 1) % numFilosofos;
        
        // Condição: FAMINTO E nenhum dos vizinhos está COMENDO
        if (estado[i] == Estado.COM_FOME && 
            estado[esq] != Estado.COMENDO && 
            estado[dir] != Estado.COMENDO) {
            
            estado[i] = Estado.COMENDO;
            System.out.println("|LOG| F" + (i + 1) + " PODE COMER. Estado atualizado.");
        }
    }
}