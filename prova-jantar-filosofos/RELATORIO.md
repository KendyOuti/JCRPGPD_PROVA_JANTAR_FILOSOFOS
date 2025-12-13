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

Para avaliar o desempenho e a justiça de cada solução implementada, foram coletadas as métricas detalhadas a seguir durante o período de 5 minutos de execução controlada.

### 3.1. Definição das Métricas

As métricas escolhidas refletem os aspectos cruciais de um sistema concorrente: **Throughput (Vazão)**, **Latência (Espera)** e **Fairness (Justiça)**.

1.  **Número Total de Vezes que Cada Filósofo Comeu (Refeições Totais):** Mede o *Throughput* global do sistema. Um valor mais alto indica que o mecanismo de controle é mais eficiente em liberar os recursos.
2.  **Tempo Médio de Espera entre Tentativas de Comer:** O tempo médio em milissegundos que um filósofo passa bloqueado no ponto de sincronização. Um valor mais baixo indica um sistema mais responsivo (menor Latência).
3.  **Taxa de Utilização dos Garfos (Estimada):** A porcentagem do tempo total em que os recursos foram *efetivamente* utilizados. Uma taxa mais alta indica melhor aproveitamento da capacidade máxima teórica.
4.  **Distribuição Justa de Oportunidades (Coeficiente de Variação - CV):** Mede a justiça (*Fairness*) na distribuição de oportunidades. **Quanto mais próximo de zero, mais justa é a distribuição**, e menor é o risco de *starvation*.

### 3.2. Resultados Individuais das Execuções (5 Minutos)

Abaixo estão os dados brutos coletados ao final da execução de cada uma das três soluções:

#### 3.2.1. Tarefa 2: Solução com Prevenção de Deadlock (T2)

Esta solução utiliza uma regra assimétrica de aquisição de garfos (pares pegam Esquerda-Direita; ímpares pegam Direita-Esquerda) para prevenir *deadlock*.

| Filósofo | Refeições | Tentativas | Tempo Médio Esp. (ms) |
| :--- | :--- | :--- | :--- |
| F1 | 62 | 62 | 961,32 |
| F2 | 59 | 59 | 1061,83 |
| F3 | 59 | 59 | 1228,12 |
| F4 | 55 | 55 | 1546,44 |
| F5 | 56 | 56 | 1488,70 |
| **Geral** | **291** | **331** | **1097,07** |

> **Métricas Chave:** Total de Refeições: 291 | Tempo Médio Espera: 1097,07 ms | CV: 4,26% | Utilização: 38,80%

#### 3.2.2. Tarefa 3: Solução com Semáforos (T3)

Esta solução utiliza um semáforo com $N-1$ permissões (4 permissões para 5 filósofos) para limitar o número de filósofos que tentam pegar garfos simultaneamente.

| Filósofo | Refeições | Tentativas | Tempo Médio Esp. (ms) |
| :--- | :--- | :--- | :--- |
| F1 | 55 | 56 | 29,38 |
| F2 | 58 | 58 | 23,00 |
| F3 | 56 | 57 | 31,05 |
| F4 | 57 | 58 | 70,41 |
| F5 | 56 | 56 | 92,68 |
| **Geral** | **282** | **285** | **49,20** |

> **Métricas Chave:** Total de Refeições: 282 | Tempo Médio Espera: 49,20 ms |CV: 1,81% | Utilização: 37,60%

#### 3.2.3. Tarefa 4: Solução com Monitores e Garantia de Fairness (T4)

Esta solução utiliza um Monitor (classe `Mesa` com `synchronized`, `wait`/`notifyAll`) para gerenciar o acesso aos garfos, permitindo que um filósofo coma apenas se seus vizinhos não estiverem comendo.

| Filósofo | Refeições | Tentativas | Tempo Médio Esp. (ms) |
| :--- | :--- | :--- | :--- |
| F1 | 57 | 57 | 1057,37 |
| F2 | 57 | 57 | 1218,00 |
| F3 | 56 | 56 | 1149,52 |
| F4 | 57 | 57 | 1246,28 |
| F5 | 58 | 58 | 1349,79 |
| **Geral** | **285** | **285** | **1204,89** |

> **Métricas Chave:** Total de Refeições: 285 | Tempo Médio Espera: 1204,89 ms | CV: 1,11% | Utilização: 38,00%

### 3.3. Tabela de Resultados Consolidados e Comparativos

Para facilitar a análise, os dados chave de cada teste são comparados na tabela abaixo:

| Métrica | Solução 1 (T2) | Solução 2 (T3) | Solução 3 (T4) |
| :--- | :--- | :--- | :--- |
| **Controle Utilizado** | Ordem Assimétrica | Semáforo N-1 | Monitor Central |
| **Tempo de Execução (s)** | 300,006 | 300,004 | 300,005 |
| **Total de Refeições** | **291** | 282 | 285 |
| **Tempo Médio Espera (ms)** | 1097,07 | **49,20** | 1204,89 |
| **Coef. de Variação (%)** | 4,26% | 1,81% | **1,11%** |
| **Taxa de Utilização (%)** | **38,80%** | 37,60% | 38,00% |

---

## 4. Análise Crítica: Comparação e Avaliação dos Resultados

A avaliação das três soluções revela *trade-offs* distintos entre o desempenho puro (Throughput) e a previsibilidade/justiça (Latência e Fairness).

### 4.1. Prevenção de Deadlock

Todas as três soluções foram bem-sucedidas em prevenir o *deadlock*, conforme demonstrado pela execução contínua de 5 minutos e pela realização de refeições por todos os filósofos.

| Solução | Mecanismo de Prevenção | Eficácia |
| :--- | :--- | :--- |
| **T2 (Ordem Assimétrica)** | Quebra da condição de **Espera Circular** ao impor uma ordem de aquisição de recursos (Garfo Esquerdo $\rightarrow$ Direito para pares; Direito $\rightarrow$ Esquerdo para ímpares). | Total (Funcional) |
| **T3 (Semáforo N-1)** | Quebra da condição de **Espera e Posse** ao limitar o número de threads na região crítica a $N-1$ (4 de 5), garantindo que sempre haja pelo menos um filósofo capaz de pegar os dois garfos. | Total (Funcional) |
| **T4 (Monitor Central)** | Quebra da condição de **Espera Circular** ao exigir uma condição lógica: só permite pegar os garfos se **ambos** os vizinhos não estiverem comendo. | Total (Funcional) |

### 4.2. Prevenção de Starvation (Justiça - *Fairness*)

A justiça é medida pelo **Coeficiente de Variação (CV)** do número de refeições. Quanto menor o CV, mais justa e uniforme é a distribuição de oportunidades.

| Solução | Coeficiente de Variação (CV) | Avaliação |
| :--- | :--- | :--- |
| **T2 (Ordem Assimétrica)** | 4,26% | **Pior**. Embora funcional, a ordem fixa introduz uma assimetria no tempo de espera, sendo menos justo. |
| **T3 (Semáforo N-1)** | 1,81% | **Bom**. O controle centralizado ajuda a regular as entradas, distribuindo as oportunidades de forma mais uniforme. |
| **T4 (Monitor Central)** | **1,11%** | **Melhor**. O monitor é o mais justo porque a condição (`wait()`) é reavaliada por todos que estão esperando (`notifyAll()`), permitindo que a thread que satisfaz a condição primeiro (e que pode estar esperando há mais tempo) prossiga. |

### 4.3. Performance/Throughput (*Latência vs. Vazão*)

O desempenho é a área onde se observam os *trade-offs* mais significativos entre Latência (Tempo de Espera) e Vazão (Total de Refeições).

| Métrica | T2 (Ordem Assimétrica) | T3 (Semáforo N-1) | T4 (Monitor Central) |
| :--- | :--- | :--- | :--- |
| **Total de Refeições (Vazão)** | **291** | 282 | 285 |
| **Taxa de Utilização (%)** | **38,80%** | 37,60% | 38,00% |
| **Tempo Médio Espera (ms)** | 1097,07 | **49,20** | 1204,89 |

#### Análise:
* **Vazão (Throughput):** A **T2 (Ordem Assimétrica)** atingiu o maior *throughput* (291 refeições). Isso ocorre porque, apesar da alta latência individual, a T2 é a solução mais **otimista**, baseada em tentativa e erro. As *threads* gastam tempo esperando bloqueadas nos garfos, mas o mecanismo permite o maior grau de concorrência simultânea.
* **Latência (Tempo de Espera):** A **T3 (Semáforo N-1)** é a vencedora absoluta em baixa latência (49,20 ms). O Semáforo força a espera em uma fila rápida de controle central, garantindo que quando a thread for liberada, ela quase certamente comerá imediatamente.
* **T4 (Monitor Central):** Apresentou a pior latência (1204,89 ms). Assim como na T2, o tempo é gasto no bloqueio (`wait()`), mas a T4 oferece a melhor justiça (CV 1,11%).

### 4.4. Complexidade de Implementação

| Solução | Complexidade | Observação |
| :--- | :--- | :--- |
| **T2 (Ordem Assimétrica)** | Baixa | É a mais simples. Requer apenas a imposição de uma regra condicional simples (ID par/ímpar) ao usar locks nativos (`synchronized`). |
| **T3 (Semáforo N-1)** | Média | Requer a introdução de um novo objeto (`Semaphore`) e a coordenação de sua liberação em um bloco `finally`. Conceitualmente, a lógica N-1 é direta. |
| **T4 (Monitor Central)** | Alta | Envolve a criação de uma classe Monitor separada, gerenciamento do estado dos vizinhos (`PENSANDO`, `COM_FOME`, `COMENDO`), e uso correto de `wait()`, `notifyAll()`, e `while` loops para evitar notificação perdida. |

### 4.5. Uso de Recursos

| Solução | Uso de Recursos | Observação |
| :--- | :--- | :--- |
| **T2 (Ordem Assimétrica)** | Locks de CPU/Memória | Utiliza locks de monitor nativos do Java (`synchronized`). A alta espera (1097 ms) é gasta como **tempo de bloqueio no recurso**. |
| **T3 (Semáforo N-1)** | Objeto de Sincronização Externo | Introduz um Semáforo como recurso de controle central. O tempo de espera (49 ms) é gasto esperando a **permissão** do semáforo, e não o garfo diretamente. |
| **T4 (Monitor Central)** | Estado e Controle Interno | Introduz complexidade e consumo de memória ao manter o **estado (`Estado[] estado`) de todos os filósofos** dentro do monitor, além de locks nativos e chamadas de `wait`/`notifyAll`. |

---

## 5. Conclusão: Qual solução é mais adequada para diferentes cenários e por quê?

A análise comparativa entre as três soluções demonstrou que não existe uma solução universalmente "melhor". A escolha ideal depende da prioridade do sistema: **Vazão (Throughput)**, **Latência (Tempo de Espera)**, ou **Justiça (*Fairness*)**.

### Sumário dos Aprendizados Chave

| Métrica | T2: Ordem Assimétrica | T3: Semáforo N-1 | T4: Monitor Central |
| :--- | :--- | :--- | :--- |
| **Justiça (CV)** | Pior (4,26%) | Boa (1,81%) | **Melhor (1,11%)** |
| **Latência (Espera)** | Alta (1097,07 ms) | **Baixa (49,20 ms)** | Altíssima (1204,89 ms) |
| **Vazão (Refeições)** | **Máxima (291)** | Média (282) | Média (285) |
| **Complexidade** | Baixa | Média | Alta |

### Escolha da Solução Ideal por Cenário

Com base nos resultados e na análise de complexidade, as soluções se encaixam em diferentes cenários operacionais:

#### 🥇 1. Solução Ideal para Justiça (Fairness) e Prevenção de Starvation: Monitor Central (T4)

* **Vantagem:** A T4 forneceu o **melhor Coeficiente de Variação (1,11%)**, garantindo que todos os filósofos tivessem o número mais próximo de oportunidades de comer, minimizando o risco de *starvation*.
* **Porquê:** A lógica do Monitor, que avalia o estado dos vizinhos antes de permitir o acesso e usa o `notifyAll()` para acordar todas as *threads* famintas para reavaliação, é o mecanismo mais justo, pois garante que as condições sejam satisfeitas de forma explícita e uniforme.
* **Trade-off:** Esta justiça tem o custo da **pior latência** (1204,89 ms) devido à sobrecarga da coordenação central e do tempo gasto pelas *threads* em `wait()`.

#### 🥈 2. Solução Ideal para Baixa Latência e Controle de Fluxo: Semáforo N-1 (T3)

* **Vantagem:** A T3 entregou a **menor latência (49,20 ms)**.
* **Porquê:** Ao restringir o número de concorrentes para $N-1$, o Semáforo atua como um portão de entrada rápido. As *threads* gastam pouco tempo esperando pela permissão e muito mais tempo efetivamente trabalhando, tornando o sistema extremamente responsivo.
* **Trade-off:** A restrição centralizada causou uma vazão ligeiramente menor (282 refeições) em comparação com a T2, pois limita o potencial máximo de concorrência. É a solução ideal quando a **previsibilidade e o tempo de resposta rápido** (baixa latência) são mais críticos que o *throughput* bruto.

#### 🥉 3. Solução Ideal para Máxima Vazão (Throughput): Ordem Assimétrica (T2)

* **Vantagem:** A T2 atingiu a **maior vazão (291 refeições)** e a maior taxa de utilização dos garfos (38,80%).
* **Porquê:** A T2 é a solução mais "livre" e **otimista**. Ela não impõe uma restrição central de permissões nem um controle de estado complexo; apenas uma regra local para quebrar o *deadlock*. Isso permite que o sistema maximize o número de acessos simultâneos.
* **Trade-off:** Essa otimização para *throughput* veio ao custo da **pior justiça (CV 4,26%)** e da **alta latência** (1097,07 ms), pois os filósofos gastam muito tempo bloqueados esperando pelos recursos.

### Conclusão Final

| Cenário | Solução Recomendada | Motivo Principal |
| :--- | :--- | :--- |
| **Alta Justiça (Fairness)** | Monitor Central (T4) | O mecanismo de avaliação de estado e `notifyAll()` garante a distribuição de oportunidades mais equitativa (CV 1,11%). |
| **Baixa Latência** | Semáforo N-1 (T3) | O controle de permissão centralizado reduz drasticamente o tempo que as *threads* passam em estado bloqueado (49,20 ms). |
| **Máxima Vazão (Throughput)** | Ordem Assimétrica (T2) | Permite o maior grau de concorrência, resultando no maior número total de refeições (291), apesar da latência alta e desigual. |

A solução de **Monitor Central (T4)** é a mais elegante e robusta conceitualmente para o Problema do Jantar dos Filósofos, pois trata o acesso ao recurso de forma atômica e justa. Contudo, em termos de performance de execução (latência), a solução do **Semáforo N-1 (T3)** demonstrou ser a mais eficiente.

