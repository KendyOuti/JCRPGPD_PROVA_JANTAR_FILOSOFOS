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

### Resultados da Execução

A seguir estão as estatísticas obtidas após uma execução do programa por 2 minutos (120.000 ms).

| Filósofo (ID) | Ordem de Aquisição | Refeições Comidas |
| :---: | :--- | :---: |
| **F1** | Esquerdo $\rightarrow$ Direito (Padrão) | 14 |
| **F2** | Esquerdo $\rightarrow$ Direito (Padrão) | 15 |
| **F3** | Esquerdo $\rightarrow$ Direito (Padrão) | 15 |
| **F4** | Esquerdo $\rightarrow$ Direito (Padrão) | 16 |
| **F5** | Esquerdo $\rightarrow$ Direito (Padrão) | 16 |
| **Total Geral de Refeições** | | **76** |

### Comparação de Desempenho (Tarefa 2 vs. Tarefa 3)

| Critério | Tarefa 2: Ordem Assimétrica | Tarefa 3: Semáforo ($N-1$) |
| :--- | :--- | :--- |
| **Produtividade Total (Refeições)** | **114** | **76** |
| **Melhor Desempenho Individual** | 25 (F3) | 16 (F4, F5) |
| **Variação % (Justiça/Inequidade)** | $16\%$ (Variação de $25 \rightarrow 21$) | **$12.5\%$** (Variação de $16 \rightarrow 14$) |

### Conclusão Comparativa

1.  **Produtividade (Throughput):** A solução de **Ordem Assimétrica (Tarefa 2)** foi superior em produtividade (114 vs. 76 refeições). Isso ocorre porque a Tarefa 3 impõe uma restrição ativa e rígida ($N-1$), limitando o número de *threads* que podem concorrer pelos garfos, o que reduz a taxa total de refeições.

2.  **Justiça (*Fairness*):** A solução de **Semáforo (Tarefa 3)** demonstrou ser mais justa. Com uma variação de apenas **$12.5\%$** entre o máximo e o mínimo, ela distribuiu o acesso ao recurso de forma mais equitativa. A restrição ativa do Semáforo força as *threads* a se revezarem na mesa, mitigando o risco de *starvation* em comparação com a competição livre da Tarefa 2 ($16\%$ de variação).

**Conclusão Final:** Ambas as soluções garantem a prevenção do *deadlock*. A escolha entre elas depende da prioridade: se a meta é maximizar o número total de operações (Produtividade), a **Ordem Assimétrica** é melhor; se a meta é garantir que todas as *threads* progridam de forma mais igualitária (Justiça), a abordagem com **Semáforos** é preferível.