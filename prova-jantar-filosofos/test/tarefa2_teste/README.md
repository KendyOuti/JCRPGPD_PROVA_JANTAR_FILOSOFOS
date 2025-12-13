# Tarefa 2: Solução com Prevenção de Deadlock (Versão de Testes para o Relatório Final)

Esta versão da Tarefa 2 foi modificada para coletar métricas de desempenho e justiça exigidas pela **Tarefa 5 (Análise Comparativa)**, mantendo a solução original de prevenção de *deadlock* através da ordem de aquisição dos garfos.

## 🎯 Objetivo da Alteração

O objetivo principal desta versão é **instrumentar o código** para medir o desempenho estatístico do controle por Ordem Assimétrica.

  * **Tempo de Execução:** Alterado de 2 minutos para **5 minutos (300 segundos)**.
  * **Coleta de Métricas:** Implementação de contadores para medir o tempo de espera, o *throughput* (vazão) e a *fairness* (justiça) da distribuição de refeições.

## 🛠️ Alterações em Relação à Tarefa 2 Original

As modificações são divididas por arquivo:

### 1\. `Filosofo.java`

O `Filosofo.java` foi instrumentado para medir o tempo que a thread passa bloqueada (`synchronized`).

| Alteração | Detalhe | Propósito |
| :--- | :--- | :--- |
| **Novos Atributos** | `tempoTotalEspera`, `inicioEspera`, `tentativasComer` | Rastrear o tempo que o filósofo aguarda (bloqueado) para adquirir os dois locks. |
| **Método `comer()`** | **Marcação de Tempo:** Adição de `System.currentTimeMillis()` **antes** de tentar pegar o primeiro garfo e **depois** de pegar o segundo garfo. | Medir o tempo real de bloqueio dos locks (`synchronized`) a cada tentativa de comer. |
| **Getters** | `getTempoTotalEspera()`, `getTentativasComer()`, `getNome()` | Expor as novas métricas e corrigir erro de compilação do `getNome()`. |

### 2\. `Main.java`

O `Main.java` foi modificado para controlar a duração da execução e consolidar os dados estatísticos.

| Alteração | Detalhe | Propósito |
| :--- | :--- | :--- |
| **Tempo Limite** | `final long TEMPO_LIMITE_SEGUNDOS = 300;` | Configurar a execução para **5 minutos** (requisito da Tarefa 5). |
| **Cálculo de Métricas** | O método `exibirEstatisticas` foi expandido para calcular: 1. **Tempo Médio de Espera** (por tentativa). 2. **Coeficiente de Variação (CV)** sobre o número de refeições (métrica de Justiça). 3. **Taxa de Utilização Estimada** (métrica de *Throughput*). | Fornecer todos os dados estatísticos necessários para o relatório final. |
| **Correção de Escopo** | O método `exibirEstatisticas` agora recebe `NUM_FILOSOFOS` como argumento. | Corrigir erro de compilação e garantir que o cálculo da Taxa de Utilização esteja correto. |
