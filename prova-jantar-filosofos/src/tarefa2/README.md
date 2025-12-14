# ➡️ Tarefa 2: Solução com Prevenção de Deadlock

## 🍽️ Objetivo

Modificar a implementação da Tarefa 1 para **prevenir o *deadlock***, utilizando a técnica de **ordem de aquisição de recursos assimétrica**. O objetivo é garantir que todos os filósofos consigam comer continuamente, eliminando a possibilidade de impasse.

## 🚀 Como Compilar e Executar

1.  **Compilação:** Certifique-se de que os arquivos `Garfo.java`, `Filosofo.java`, e `Main.java` (versão TAREFA 2) estão no mesmo diretório.

    ```bash
    javac Garfo.java Filosofo.java Main.java
    ```

2.  **Execução:**

    ```bash
    java Main
    ```

    *O programa irá rodar por **2 minutos** (120 segundos), gerando logs que demonstram a alternância contínua entre Pensar e Comer, seguida pela exibição das **estatísticas de refeições coletadas automaticamente**.*

## 🍽️ Como a Solução Previne Deadlock

Esta solução utiliza a técnica de **quebrar a condição de Espera Circular** (uma das quatro condições de Coffman) através da **aquisição assimétrica de recursos**.

  * **Filósofos F1, F2, F3, F5 (Ordem Normal):** Pegam o garfo **Esquerdo** e depois o **Direito**.
  * **Filósofo F4 (Ordem Invertida):** Este filósofo foi instruído a pegar o garfo **Direito** e depois o **Esquerdo**.

**Mecanismo de Prevenção:**
Ao impor uma ordem não uniforme (assimetria) para o Filósofo F4, a dependência cíclica $F1 \rightarrow F2 \rightarrow F3 \rightarrow F4 \rightarrow F5 \rightarrow F1$ é interrompida. O Filósofo 4, ao inverter a ordem, espera pelo recurso que o Filósofo 5 não está segurando (o Garfo Direito), quebrando a cadeia de dependência. Como a regra de aquisição não é mais idêntica para todos, **o ciclo de espera não pode se fechar**, eliminando o *deadlock*.

## ⏳ Possibilidade de Starvation (Inanição)

**Sim, a possibilidade de *starvation* (inanição) ainda existe.**

  * ***Starvation*** ocorre quando uma *thread* é constantemente preterida pelo agendador ou pela política de sincronização, impedindo-a de adquirir os recursos necessários para progredir.
  * **Por que existe:** A solução de inversão de ordem previne o *deadlock* total, mas **não garante a equidade** (*fairness*) no acesso aos recursos. O sistema ainda opera sob competição e aleatoriedade. É possível que os vizinhos de um filósofo sejam consistentemente mais rápidos ou privilegiados, resultando em um **acesso desigual** aos garfos.

## ⚖️ Comparação dos Resultados com a Tarefa 1

| Característica | Tarefa 1 (Deadlock) | Tarefa 2 (Prevenção) |
| :--- | :--- | :--- |
| **Deadlock** | Ocorre sob agendamento desfavorável (risco estrutural). | **Não ocorre** (risco estrutural eliminado pela assimetria). |
| **Atividade Log** | Atividade cessa, *threads* ficam presas em "tenta pegar Garfo DIREITO". | Atividade contínua por 2 minutos, alternando Comer/Pensar. |
| **Produtividade** | Baixa ou nula (em caso de impasse). | Alta e contínua (múltiplas refeições registradas). |
| **Prevenção** | Nenhuma. | Quebra de Espera Circular (Ordem Assimétrica). |
| **Starvation** | Alto risco. | Risco mitigado, mas **ainda presente** devido à competição não controlada. |

## 📊 Conclusão e Análise Estatística (Tarefa 2 - Ordem Assimétrica)

Os resultados estatísticos a seguir foram coletados e exibidos pelo código `Main.java` após uma execução de 2 minutos (120.000 ms). O registro completo destes dados está visível na imagem **`tarefa2_log.png`**.

### Resultados da Execução

| Filósofo (ID) | Ordem de Aquisição | Refeições Comidas |
| :---: | :--- | :---: |
| **F1** | Esquerdo $\rightarrow$ Direito | 24 |
| **F2** | Esquerdo $\rightarrow$ Direito | 24 |
| **F3** | Esquerdo $\rightarrow$ Direito | **25** |
| **F4** | **Direito $\rightarrow$ Esquerdo** (Invertida) | **20** |
| **F5** | Esquerdo $\rightarrow$ Direito | 22 |
| **Total Geral de Refeições** | | **115** |

### Análise Crítica dos Resultados

1.  **Prevenção de Deadlock Confirmada:** O registro de **115 refeições no total** e a manutenção da atividade contínua durante 2 minutos **confirmam o sucesso da solução**. O sistema progrediu ininterruptamente, provando que a quebra da condição de Espera Circular (através da ordem assimétrica de aquisição de garfos) eliminou o *deadlock*.
2.  **Mitigação, mas Presença de Inequidade (*Fairness*):** A distribuição das refeições é relativamente agrupada (entre 20 e 25), indicando que o *starvation* total foi evitado. No entanto, a diferença entre o máximo e o mínimo demonstra uma clara falta de justiça (*fairness*) na distribuição de recursos.
3.  **Filósofos com Maior Inequidade (Starvation Potencial):**
    * **F3 (25 refeições):** Apresentou o maior número de refeições.
    * **F4 (20 refeições):** Apresentou o menor número de refeições. F4, o filósofo cuja ordem de aquisição foi invertida para prevenir o *deadlock*, é o mais afetado pela assimetria imposta, comprovando que a estratégia de prevenção afeta diretamente a justiça.
4.  **Conclusão:** Esta disparidade (variação de 25% entre o máximo e o mínimo) demonstra que, embora a solução de inversão garanta a **produtividade** (previne *deadlock*), ela não garante a **justiça** (*fairness*) no uso dos recursos, mantendo o risco de *starvation* (inanição) para os filósofos na posição desfavorecida.



