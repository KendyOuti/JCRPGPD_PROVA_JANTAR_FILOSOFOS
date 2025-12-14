## 📝 RELATORIO.md

# Relatório de Análise Comparativa do Problema do Jantar dos Filósofos

## 1. Introdução: Breve Descrição do Problema

O Problema do Jantar dos Filósofos é um clássico desafio da ciência da computação, proposto por Edsger Dijkstra em 1965, usado para ilustrar e testar problemas de concorrência em sistemas operacionais e programação paralela.

O cenário é simples, mas suas implicações são complexas:

* **O Cenário:** Cinco filósofos (`N=5`) sentam-se ao redor de uma mesa, alternando entre pensar e comer.
* **Os Recursos (Garfos):** Há cinco garfos dispostos na mesa, um entre cada par de filósofos.
* **A Regra Crítica:** Para que um filósofo consiga comer, ele deve adquirir **dois garfos**: o da sua esquerda e o da sua direita.

### O Desafio da Concorrência

O problema central reside na gestão de recursos compartilhados (os garfos) e na necessidade de sincronização entre as *threads* (os filósofos). A falha na sincronização pode levar a dois problemas críticos em sistemas concorrentes:

1.  **Deadlock (Impasse Mortal):** Ocorre se cada filósofo pegar seu garfo esquerdo (ou direito) e, em seguida, esperar indefinidamente pelo outro garfo que está ocupado pelo seu vizinho. O sistema inteiro para, pois ninguém consegue comer.
2.  **Starvation (Inanição):** Ocorre se um ou mais filósofos nunca conseguirem obter os dois garfos necessários, mesmo que o sistema continue a funcionar. Eles podem ser consistentemente preteridos por outros.

O objetivo das soluções implementadas (Tarefas 2, 3 e 4) é exatamente prevenir o *deadlock* e a *starvation*, garantindo que a utilização dos recursos seja eficiente e justa.

---

## 2. Metodologia: Descrição de como os testes foram realizados

A análise comparativa entre as três soluções propostas (Ordem Assimétrica, Semáforo Central N-1 e Monitor Central) foi realizada através de testes de concorrência controlados, com o objetivo de quantificar o desempenho, a justiça e a eficiência de cada abordagem na resolução do problema do Jantar dos Filósofos com $N=5$ filósofos.

### 2.1. Preparação e Instrumentação do Código

Para a realização dos testes, o código-fonte original de cada tarefa (Tarefas 2, 3 e 4) foi instrumentado para coletar métricas detalhadas.

* **Estrutura de Teste:** O código original foi copiado para uma nova estrutura de diretórios. A pasta `test` foi criada no mesmo nível da pasta `src`, contendo subpastas para cada solução: `tarefa2_teste`, `tarefa3_teste`, e `tarefa4_teste`.
* **Instrumentação dos Arquivos:**
    * **Tempo de Execução:** O `Main.java` de cada solução foi modificado para impor um limite de execução de **5 minutos (300 segundos)**, conforme requerido.
    * **Coleta de Espera:** A classe `Filosofo.java` em todas as versões foi modificada para registrar e acumular o tempo que a *thread* passa bloqueada na tentativa de adquirir os garfos (utilizando `System.currentTimeMillis()` antes e depois dos pontos de bloqueio: `synchronized`, `Semaphore.acquire()` ou `Monitor.pegarGarfos()`).
    * **Cálculo de Métricas:** O `Main.java` foi expandido para calcular as métricas finais de **Tempo Médio de Espera**, **Coeficiente de Variação** e **Taxa de Utilização Estimada** (Throughput) ao final da execução.
* **Documentação das Alterações:** Em cada pasta de teste (`tarefaX_teste`) foi incluído um arquivo `README.md` detalhando as diferenças exatas entre a versão original da solução e a versão instrumentada para a coleta de dados.

### 2.2. Execução dos Testes e Coleta de Dados

Cada solução foi executada individualmente em um ambiente consistente, seguindo os passos abaixo:

1.  **Execução Controlada:** Cada `Main.java` foi executado, permitindo que o programa rodasse até o limite de **5 minutos**.
2.  **Métricas Coletadas:** Ao final da execução de cada teste, o programa imprimiu as seguintes métricas no console, que foram registradas para a análise comparativa:
    * **Nº Total de Refeições:** Soma de todas as refeições de todos os filósofos (mede o *Throughput* bruto).
    * **Tempo Médio de Espera:** Tempo médio em milissegundos que um filósofo esperou antes de uma tentativa de comer (mede a Performance/Latência).
    * **Coeficiente de Variação (CV):** Desvio padrão do número de refeições dividido pela média (mede a **Justiça** ou *Fairness*).
    * **Taxa de Utilização Estimada dos Garfos:** Proporção do tempo total em que os recursos estiveram sendo utilizados, em relação à capacidade máxima.

O registro dessas métricas em cada uma das três soluções forma a base de dados para a próxima seção de Resultados.

---

## 3. Resultados: Tabelas e Comparação de Métricas

### 3.2. Resultados Individuais das Execuções (5 Minutos)

Os testes foram executados por aproximadamente 300 segundos, e os dados brutos de cada solução foram coletados. As imagens dos logs de resultados (`tarefaX_teste_log.png`) estão localizadas em seus respectivos diretórios (`/test/tarefaX_teste`).

#### 3.2.1. Tarefa 2: Solução com Prevenção de Deadlock (T2 - Ordem Assimétrica)

Esta solução utiliza uma regra assimétrica de aquisição de garfos para prevenir *deadlock*.

| Filósofo | Refeições | Tentativas | Tempo Médio Esp. (ms) |
| :--- | :--- | :--- | :--- |
| F1 | 60 | 60 | 1056,70 |
| F2 | 59 | 59 | 1173,34 |
| F3 | 60 | 60 | 1236,73 |
| F4 | 54 | 54 | 1519,93 |
| F5 | 54 | 54 | 1571,52 |
| **Geral** | **287** | **287** | **1247,66** |

> **Métricas Chave:** Total de Refeições: **287** | Tempo Médio Espera: 1247,66 ms | CV: **4,88%** | Utilização: 38,27%

#### 3.2.2. Tarefa 3: Solução com Semáforos (T3 - $N-1$)

Esta solução utiliza um semáforo com $N-1$ permissões para limitar o número de filósofos que tentam pegar garfos simultaneamente.

| Filósofo | Refeições | Tentativas | Tempo Médio Esp. (ms) |
| :--- | :--- | :--- | :--- |
| F1 | 44 | 45 | 107,42 |
| F2 | 46 | 46 | 109,04 |
| F3 | 45 | 46 | 119,11 |
| F4 | 46 | 46 | 131,24 |
| F5 | 46 | 47 | 72,94 |
| **Geral** | **227** | **230** | **117,31** |

> **Métricas Chave:** Total de Refeições: 227 | Tempo Médio Espera: **117,31 ms** | CV: 1,76% | Utilização: 30,27%

#### 3.2.3. Tarefa 4: Solução com Monitores e Garantia de Fairness (T4 - Monitor Central)

Esta solução utiliza um Monitor para gerenciar o acesso aos garfos.

| Filósofo | Refeições | Tentativas | Tempo Médio Esp. (ms) |
| :--- | :--- | :--- | :--- |
| F1 | 57 | 58 | 1169,97 |
| F2 | 58 | 58 | 1416,40 |
| F3 | 59 | 59 | 1082,88 |
| F4 | 58 | 59 | 1227,93 |
| F5 | 58 | 58 | 1214,14 |
| **Geral** | **290** | **292** | **1221,50** |

> **Métricas Chave:** Total de Refeições: **290** | Tempo Médio Espera: 1221,50 ms | CV: **1,09%** | Utilização: **38,67%**

### 3.3. Tabela de Resultados Consolidados e Comparativos

| Métrica | Solução 1 (T2) | Solução 2 (T3) | Solução 3 (T4) |
| :--- | :--- | :--- | :--- |
| **Controle Utilizado** | Ordem Assimétrica | Semáforo N-1 | Monitor Central |
| **Total de Refeições (Vazão)** | 287 | 227 | **290** |
| **Tempo Médio Espera (Latência)** | 1247,66 ms | **117,31 ms** | 1221,50 ms |
| **Coef. de Variação (%)** | 4,88% | 1,76% | **1,09%** |
| **Taxa de Utilização (%)** | 38,27% | 30,27% | **38,67%** |

O Coeficiente de Variação (CV) é a métrica de **Justiça (*Fairness*)**. Quanto menor o valor, mais justa é a distribuição de oportunidades entre os filósofos.

---

## 4. Análise Crítica: Comparação e Avaliação dos Resultados

A avaliação das três soluções revela *trade-offs* distintos entre o desempenho puro (*Throughput*) e a previsibilidade/justiça (Latência e *Fairness*).

### 4.1. Prevenção de Deadlock

Todas as três soluções foram bem-sucedidas em prevenir o *deadlock*, conforme demonstrado pela execução contínua de 5 minutos e pela realização de refeições por todos os filósofos.

| Solução | Mecanismo de Prevenção | Eficácia |
| :--- | :--- | :--- |
| **T2 (Ordem Assimétrica)** | Quebra da condição de **Espera Circular** ao impor uma ordem de aquisição de recursos (Garfo Esquerdo $\rightarrow$ Direito para pares; Direito $\rightarrow$ Esquerdo para ímpares). | Total (Funcional) |
| **T3 (Semáforo N-1)** | Quebra da condição de **Espera e Posse** ao limitar o número de threads na região crítica a $N-1$ (4 de 5), garantindo que sempre haja pelo menos um filósofo capaz de pegar os dois garfos. | Total (Funcional) |
| **T4 (Monitor Central)** | Quebra da condição de **Espera Circular** ao exigir uma condição lógica: só permite pegar os garfos se **ambos** os vizinhos não estiverem comendo. | Total (Funcional) |

### 4.2. Prevenção de Starvation (Justiça - *Fairness*)

A justiça é medida pelo **Coeficiente de Variação (CV)** do número de refeições.

| Solução | Coeficiente de Variação (CV) | Avaliação |
| :--- | :--- | :--- |
| **T2 (Ordem Assimétrica)** | **4,88%** | **Pior**. A ordem fixa cria a maior assimetria e desigualdade, resultando no maior risco de *starvation*. |
| **T3 (Semáforo N-1)** | 1,76% | **Bom**. O controle central ajuda a uniformizar o acesso, sendo muito mais justo que a T2. |
| **T4 (Monitor Central)** | **1,09%** | **Melhor**. O mecanismo baseado em estado (`wait/notifyAll`) e verificação de condição garante a distribuição mais equitativa de oportunidades, confirmando ser a solução mais justa. |

### 4.3. Performance/Throughput (*Latência vs. Vazão*)

O desempenho revela os *trade-offs* mais significativos.

| Métrica | T2 (Ordem Assimétrica) | T3 (Semáforo N-1) | T4 (Monitor Central) |
| :--- | :--- | :--- | :--- |
| **Total de Refeições (Vazão)** | 287 | 227 | **290** |
| **Taxa de Utilização (%)** | 38,27% | 30,27% | **38,67%** |
| **Tempo Médio Espera (ms)** | 1247,66 | **117,31** | 1221,50 |

#### Análise:
* **Vazão (Throughput):** O **Monitor Central (T4)** atingiu o maior *throughput* (290 refeições). Isso indica que, embora seja mais complexo, a inteligência da T4 em coordenar o uso dos garfos supera a simplicidade da T2, gerando maior produtividade no final.
* **Latência (Tempo de Espera):** A **T3 (Semáforo N-1)** é a vencedora absoluta em baixa latência (117,31 ms). A restrição $N-1$ força a espera em uma fila rápida de controle, o que resulta em um tempo de resposta muito mais rápido do que o bloqueio direto nos garfos (T2 e T4, com mais de 1.200 ms).
* **T2 (Ordem Assimétrica):** Apresentou a pior performance geral. Apesar de ser simples, sua baixa justiça e alta latência não resultaram na vazão máxima esperada, sendo superada pela T4.

### 4.4. Complexidade de Implementação

| Solução | Complexidade | Observação |
| :--- | :--- | :--- |
| **T2 (Ordem Assimétrica)** | Baixa | É a mais simples. Requer apenas a imposição de uma regra condicional simples (ID par/ímpar) ao usar locks nativos (`synchronized`). |
| **T3 (Semáforo N-1)** | Média | Requer a introdução de um novo objeto (`Semaphore`) e a coordenação de sua liberação em um bloco `finally`. Conceitualmente, a lógica N-1 é direta. |
| **T4 (Monitor Central)** | Alta | Envolve a criação de uma classe Monitor separada, gerenciamento do estado dos vizinhos (`PENSANDO`, `COM_FOME`, `COMENDO`), e uso correto de `wait()`, `notifyAll()`, e `while` loops para evitar notificação perdida. |

### 4.5. Uso de Recursos

| Solução | Uso de Recursos | Observação |
| :--- | :--- | :--- |
| **T2 (Ordem Assimétrica)** | Locks de CPU/Memória | Utiliza locks de monitor nativos do Java (`synchronized`). A alta espera (1247,66 ms) é gasta como **tempo de bloqueio no recurso**. |
| **T3 (Semáforo N-1)** | Objeto de Sincronização Externo | Introduz um Semáforo como recurso de controle central. O tempo de espera (117,31 ms) é gasto esperando a **permissão** do semáforo, e não o garfo diretamente. |
| **T4 (Monitor Central)** | Estado e Controle Interno | Introduz complexidade e consumo de memória ao manter o **estado (`Estado[] estado`) de todos os filósofos** dentro do monitor, além de locks nativos e chamadas de `wait`/`notifyAll`. |

---

## 5. Conclusão: Qual solução é mais adequada para diferentes cenários e por quê?

A análise comparativa entre as três soluções demonstrou que a Solução 3 (Monitor Central) é a mais robusta, enquanto a Solução 2 (Semáforo N-1) é a mais reativa.

### Sumário dos Aprendizados Chave

| Métrica | T2: Ordem Assimétrica | T3: Semáforo N-1 | T4: Monitor Central |
| :--- | :--- | :--- | :--- |
| **Justiça (CV)** | Pior (4,88%) | Boa (1,76%) | **Melhor (1,09%)** |
| **Latência (Espera)** | Alta (1247,66 ms) | **Baixa (117,31 ms)** | Altíssima (1221,50 ms) |
| **Vazão (Refeições)** | Média (287) | Mínima (227) | **Máxima (290)** |
| **Complexidade** | Baixa | Média | Alta |

### Escolha da Solução Ideal por Cenário

Com base nos resultados e na análise de complexidade, as soluções se encaixam em diferentes cenários operacionais:

#### 🥇 1. Solução Ideal para Máxima Vazão e Justiça: Monitor Central (T4)

* **Vantagem:** A T4 forneceu a **melhor combinação**, liderando tanto em **Vazão (290 refeições)** quanto em **Justiça (CV 1,09%)**. É a solução mais robusta.
* **Trade-off:** Esta robustez tem o custo da **pior latência** (1221,50 ms).

#### 🥈 2. Solução Ideal para Baixa Latência e Controle de Fluxo: Semáforo N-1 (T3)

* **Vantagem:** A T3 entregou a **menor latência (117,31 ms)**.
* **Porquê:** O controle de permissão centralizado reduz drasticamente o tempo que as *threads* passam em estado bloqueado.
* **Trade-off:** A restrição $N-1$ causou a vazão mais baixa de todos os testes (227 refeições). É a solução ideal quando a **previsibilidade e o tempo de resposta rápido** (baixa latência) são mais críticos.

#### 🥉 3. Solução Ideal para Baixa Complexidade: Ordem Assimétrica (T2)

* **Vantagem:** É a mais simples de implementar (Baixa Complexidade).
* **Porquê:** Requer apenas uma regra local em torno de locks nativos.
* **Trade-off:** Essa simplicidade veio ao custo da **pior justiça (CV 4,88%)** e da **alta latência**, tornando-a a opção menos eficiente para cenários de alta concorrência.

### Conclusão Final

| Cenário | Solução Recomendada | Motivo Principal |
| :--- | :--- | :--- |
| **Alta Justiça e Vazão** | Monitor Central (T4) | O mecanismo de avaliação de estado e `notifyAll()` garante a melhor vazão e a distribuição de oportunidades mais equitativa. |
| **Baixa Latência** | Semáforo N-1 (T3) | O controle de permissão centralizado reduz drasticamente o tempo que as *threads* passam em estado bloqueado. |
| **Baixa Complexidade** | Ordem Assimétrica (T2) | Solução mais simples de codificar, mas com performance instável e injusta. |




