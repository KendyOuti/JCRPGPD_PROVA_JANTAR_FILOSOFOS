# ➡️ Tarefa 3: Solução com Semáforos

## 🍽️ Objetivo

Implementar uma solução para o Problema do Jantar dos Filósofos utilizando a estrutura de controle de concorrência **`Semaphore`** do Java. O objetivo é limitar o número de filósofos que podem tentar pegar os garfos simultaneamente, garantindo a prevenção de *deadlock*.

## 🚀 Como Compilar e Executar

1.  **Estrutura de Arquivos:** Certifique-se de que os arquivos `Garfo.java`, `Filosofo.java`, e `Main.java` (versão TAREFA 3) estão no mesmo diretório.

2.  **Compilação:**

    ```bash
    javac Garfo.java Filosofo.java Main.java
    ```

3.  **Execução:**

    ```bash
    java Main
    ```

    *O programa irá rodar por **2 minutos** (120 segundos), monitorando a atividade contínua de Pensar/Comer e exibindo as estatísticas de refeições ao final.*

## ⚙️ Como a Solução Funciona

Esta abordagem utiliza um semáforo central, agindo como um "porteiro" que controla a entrada de filósofos na área de refeição.

1.  **Semáforo Central (Permissões = 4):** Foi criado um `Semaphore` com 4 permissões (capacidade $N-1$, onde $N=5$ filósofos). Este semáforo é compartilhado por todas as *threads*.
2.  **Controle de Acesso:** Antes de qualquer filósofo tentar adquirir o primeiro garfo, ele deve invocar `limiteFilosofos.acquire()`.
3.  **Restrição:** Se 5 filósofos tentarem comer, apenas 4 conseguirão a permissão. O quinto filósofo ficará bloqueado no `acquire()`, esperando que um dos 4 ativos termine de comer e devolva a permissão.
4.  **Recursos:** Enquanto o Semáforo controla o **acesso à tentativa**, a exclusão mútua dos garfos individuais é mantida pelo `synchronized` (locks) nos objetos `Garfo`.
5.  **Saída:** Após terminar de comer e liberar os garfos (`synchronized` blocks), o filósofo invoca `limiteFilosofos.release()`, devolvendo uma permissão e permitindo que outro filósofo acesse a mesa.

## 🛡️ Por que ela Previne Deadlock

A solução com semáforos previne o *deadlock* garantindo que a **Condição de Retenção e Espera** não possa levar a uma **Espera Circular Completa**.

  * **A Chave $N-1$:** Para $N=5$ filósofos e 5 garfos, o *deadlock* ocorre quando todos os 5 adquirem 1 garfo e esperam pelo 2º.
  * **Quebrando o Ciclo:** Ao limitar o número máximo de filósofos ativos na mesa para **4** (Permissões = $N-1$), garantimos que, no pior cenário, apenas 4 garfos podem ser retidos. O 5º garfo permanecerá livre, ou pelo menos um dos 4 filósofos ativos conseguirá pegar seu segundo garfo.
  * **Progressão Garantida:** Como há sempre, no máximo, 4 filósofos sentados e 5 garfos disponíveis, **sempre haverá pelo menos um garfo livre**. Isso garante que a Espera Circular nunca se feche e que o sistema continue progredindo.

## ➕ Vantagens e Desvantagens dessa Abordagem

| Categoria | Vantagens (Por que usar Semáforo) | Desvantagens (Limitações) |
| :--- | :--- | :--- |
| **Garantia** | Prevenção de *deadlock* é **matematicamente garantida** pelo princípio $N-1$. | O Semáforo em si não garante justiça perfeita (ainda pode haver *starvation*). |
| **Design** | Separação de preocupações: Semáforo gerencia a **concorrência total**, e `synchronized` gerencia a **exclusão mútua** do recurso. | Requer um recurso central (o Semáforo), o que pode se tornar um gargalo em sistemas muito maiores. |
| **Manutenção** | O código do filósofo é simplificado, pois todos usam a mesma lógica de aquisição de garfos (Padrão $E \rightarrow D$). | Adiciona uma camada extra de sincronização (`acquire`/`release`) que pode levar a erros se o `release` for esquecido. |

## 📊 Análise de Desempenho e Justiça (*Fairness*)

Esta seção consolida a análise da Tarefa 2 (Ordem Assimétrica) e da Tarefa 3 (Semáforo N-1) após execuções de 2 minutos, focando nos *trade-offs* entre Produtividade (*Throughput*) e Justiça (*Fairness*).

### Resultados da Execução da Tarefa 3 (Semáforo N-1)

A solução com Semáforo N-1 (onde apenas 4 dos 5 filósofos podem tentar pegar garfos simultaneamente) demonstra uma distribuição muito mais uniforme. O resultado final do log está no arquivo `tarefa3_log.png`. 

| Filósofo (ID) | Refeições Comidas | Ordem de Garfos |
| :---: | :---: | :--- |
| **F1** | 21 | Esquerdo $\rightarrow$ Direito (Padrão) |
| **F2** | 22 | Esquerdo $\rightarrow$ Direito (Padrão) |
| **F3** | 22 | Esquerdo $\rightarrow$ Direito (Padrão) |
| **F4** | **23** | Esquerdo $\rightarrow$ Direito (Padrão) |
| **F5** | 22 | Esquerdo $\rightarrow$ Direito (Padrão) |
| **Total Geral de Refeições** | **110** | |

### Comparação de Desempenho (Tarefa 2 vs. Tarefa 3)

| Critério | Tarefa 2: Ordem Assimétrica | Tarefa 3: Semáforo ($N-1$) |
| :--- | :--- | :--- |
| **Produtividade Total (Refeições)** | **115** | 110 |
| **Melhor Desempenho Individual** | 25 (F3) | 23 (F4) |
| **Pior Desempenho Individual** | 20 (F4) | 21 (F1) |
| **Variação Absoluta (Máx - Mín)** | 5 refeições (25 $\rightarrow$ 20) | **2 refeições** (23 $\rightarrow$ 21) |
| **Variação % (Justiça/Inequidade)** | $25\%$ (Inequidade Alta) | **$9.5\%$** (Melhor Justiça) |

*Nota: A Variação % é calculada como (Máx - Mín) / Mín.*

### Conclusão Comparativa

1.  **Produtividade (Throughput):** A solução de **Ordem Assimétrica (Tarefa 2)** foi superior em produtividade (115 vs. 110 refeições). A Tarefa 2, sendo mais otimista, permite que mais *threads* entrem na região crítica, embora isso leve a um maior tempo de espera bloqueado (maior latência) e a uma competição mais acirrada. A restrição rígida $N-1$ da Tarefa 3 limita o número total de *threads* ativas, reduzindo a vazão marginalmente.

2.  **Justiça (*Fairness*):** A solução de **Semáforo (Tarefa 3)** demonstrou ser significativamente mais justa. Com uma variação de apenas **$9.5\%$** entre o máximo e o mínimo, ela distribuiu o acesso ao recurso de forma mais equitativa. O Semáforo força as *threads* a se revezarem na mesa de maneira mais organizada, mitigando o risco de *starvation* inerente à competição livre da Tarefa 2 (25% de variação).

**Conclusão Final:** Ambas as soluções garantem a prevenção do *deadlock*. A escolha entre elas depende da prioridade do sistema:
* Se a meta é **maximizar o número total de operações** (Produtividade), a **Ordem Assimétrica (T2)** é marginalmente melhor.
* Se a meta é **garantir que todas as *threads* progridam de forma igualitária** e previsível (Justiça e mitigar *Starvation*), a abordagem com **Semáforos (T3)** é claramente superior, sendo a solução recomendada para sistemas que exigem alta *fairness*.


