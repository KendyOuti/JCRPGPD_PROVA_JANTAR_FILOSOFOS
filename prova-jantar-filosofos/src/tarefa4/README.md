# ➡️ Tarefa 4: Solução com Monitores e Garantia de Fairness

## 🍽️ Objetivo

Implementar uma solução utilizando a arquitetura de **Monitor** (`Mesa.java`) e o controle de sincronização básico do Java (`synchronized`, `wait()`, `notifyAll()`). O objetivo é garantir a **justiça (*fairness*)** na distribuição dos garfos, prevenindo de forma eficaz o *deadlock* e o *starvation*.

## 🚀 Como Compilar e Executar

1.  **Estrutura de Arquivos:** Certifique-se de que `Garfo.java`, `Filosofo.java`, `Mesa.java`, e `Main.java` (versão TAREFA 4) estão no mesmo diretório.

2.  **Compilação:**

    ```bash
    javac Garfo.java Filosofo.java Mesa.java Main.java
    ```

3.  **Execução:**

    ```bash
    java Main
    ```

    *O programa executa por **2 minutos** (120 segundos), utilizando a classe `Mesa` como Monitor central para controlar o acesso aos garfos. Ao final, exibe as estatísticas de refeições.*

## ⚙️ Como o Monitor Garante Fairness

O Monitor (`Mesa.java`) implementa uma lógica de **sinalização e reavaliação de estado** que atua como um mecanismo de fila lógica, garantindo que nenhum filósofo seja ignorado (prevenção de *starvation*).

1.  **Declaração de Fome:** Um filósofo que deseja comer muda seu estado para `COM_FOME` no Monitor.
2.  **Espera Condicional:** Se ele não puder comer imediatamente (devido a vizinhos comendo), ele entra em `wait()` dentro de um laço `while`. O `while` é fundamental, pois garante que a thread **só saia do estado de espera** se sua condição (`podeComer()`) for verdadeira.
3.  **Liberação Total (`notifyAll()`):** Quando um filósofo termina e libera os recursos (`soltarGarfos`), ele chama `notifyAll()`. Isso acorda **todas** as threads que estavam em `wait()`.
4.  **Seleção Justa:** Todas as threads acordadas reavaliam a condição `podeComer()`. Se a liberação do garfo satisfez a condição de um filósofo que estava esperando há mais tempo (`COM_FOME`), o escalonador garantirá que ele adquira o Lock e avance. Isso elimina a possibilidade de que um filósofo seja continuamente preterido em favor de seus vizinhos mais rápidos, garantindo a justiça.

## 🛡️ Como Deadlock e Starvation são Prevenidos

| Problema | Prevenção na Tarefa 4 (Monitores) |
| :--- | :--- |
| **Deadlock** | **Quebra de Retenção e Espera:** O Monitor garante que um filósofo **nunca** segura um garfo enquanto espera por outro. A aquisição dos dois garfos (mudança para `COMENDO`) é uma **operação atômica** dentro do Monitor. Se ele não puder pegar os dois, ele espera, sem reter nenhum recurso. |
| **Starvation** | **Garantia de Fairness:** O uso do estado `COM_FOME` e a liberação abrangente de todas as threads (`notifyAll()`) asseguram que o filósofo com a condição de comer satisfeita e que estava esperando por mais tempo será o próximo a ser acordado e prosseguir.  |

## ⚖️ Comparações de Desempenho com Soluções Anteriores (Tarefa 4)

A Tarefa 4 (Monitor) foi projetada especificamente para otimizar a **Justiça (*Fairness*)**, garantindo que a aquisição de garfos só ocorra sob condições seguras, ao custo teórico de uma leve perda de **Produtividade**. 

### Resultados da Execução da Tarefa 4 (Monitor Central)

O resultado final e as estatísticas de 2 minutos para o Monitor Central estão presentes no log no arquivo `tarefa4_log.png`.

| Filósofo (ID) | Refeições Comidas | Ordem/Controle |
| :---: | :---: | :---: |
| **F1** | 23 | Monitor (wait/notify) |
| **F2** | 24 | Monitor (wait/notify) |
| **F3** | 23 | Monitor (wait/notify) |
| **F4** | 24 | Monitor (wait/notify) |
| **F5** | **22** | Monitor (wait/notify) |
| **Total Geral de Refeições** | **116** | |

### Comparação de Desempenho e Justiça (T2, T3 e T4)

| Característica | Tarefa 2 (Ordem Assimétrica) | Tarefa 3 (Semáforo N-1) | **Tarefa 4 (Monitor/Fairness)** |
| :--- | :--- | :--- | :--- |
| **Produtividade Total (Ref.)** | 115 | 110 | **116** |
| **Máx. Refeições** | 25 | 23 | 24 |
| **Mín. Refeições** | 20 | 21 | **22** |
| **Variação % (Inequidade)** | 25% | 9.5% | **8.33%** |

O cálculo da Variação Percentual, utilizada como métrica de Inequidade/Justiça, é dado por:
$$\text{Variação Percentual} = \frac{(\text{Máx. Refeições} - \text{Mín. Refeições})}{\text{Máx. Refeições}} \times 100$$

### Análise Crítica dos Resultados (Tarefa 4)

1.  **Justiça Comprovada:** A variação percentual é de aproximadamente **8.33%**, a **menor de todas as soluções**. Este baixo valor **confirma o sucesso do Monitor na garantia de *fairness***, superando a Ordem Assimétrica (25%) e o Semáforo (9.5%) ao equilibrar a distribuição de recursos.
2.  **Produtividade (116 Refeições):** A Tarefa 4 atingiu a maior produtividade total, mostrando que, apesar da complexidade da lógica de controle, o *overhead* não comprometeu significativamente o *throughput*.
3.  **Garantia de Prevenção:** O Monitor é a solução conceitualmente mais robusta, garantindo tanto a prevenção de *deadlock* quanto a prevenção de *starvation*.

## 📈 Trade-offs entre as Diferentes Abordagens

| Abordagem | Vantagens | Desvantagens |
| :--- | :--- | :--- |
| **Tarefa 2 (Ordem)** | Mais alta produtividade total. | Risco de *Starvation* e maior variação na distribuição (25%). |
| **Tarefa 3 (Semáforo)** | Garantia matemática simples contra *deadlock* ($N-1$). Baixa latência (não medida aqui, mas observada na análise do **RELATORIO.md**). | Menor produtividade total. |
| **Tarefa 4 (Monitor)** | **Melhor Justiça (*Fairness*)** (variação de 8.33%). Prevenção garantida de *deadlock* e *starvation*. | Código mais complexo e mais suscetível a erros de sincronização (`wait`/`notifyAll`). |