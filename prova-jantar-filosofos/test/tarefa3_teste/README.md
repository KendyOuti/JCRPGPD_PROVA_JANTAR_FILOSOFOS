# Tarefa 3: Solução com Semáforo Central N-1 (Versão de Testes para o Relatório Final)

Esta versão da Tarefa 3 foi modificada para coletar métricas de desempenho e justiça exigidas pela **Tarefa 5 (Análise Comparativa)**, mantendo a solução original de prevenção de *deadlock* através do Semáforo Central com $N-1$ permissões.

## 🎯 Objetivo da Alteração

O objetivo principal desta versão é **instrumentar o código** para medir o desempenho estatístico do controle por Semáforo Central.

  * **Tempo de Execução:** Alterado de 2 minutos para **5 minutos (300 segundos)**.
  * **Coleta de Métricas:** Implementação de contadores para medir o tempo de espera no Semáforo e o *throughput* (vazão) do sistema.

## 🛠️ Alterações em Relação à Tarefa 3 Original

As modificações foram pontuais, garantindo a coleta de dados e a correção de erros de compilação.

### 1\. `Filosofo.java`

O `Filosofo.java` foi instrumentado para medir o tempo de bloqueio no Semáforo e corrigido para comunicação com o `Main`.

| Alteração | Detalhe | Propósito |
| :--- | :--- | :--- |
| **Novos Atributos** | `tempoTotalEspera`, `inicioEspera`, `tentativasComer` | Rastrear o tempo que o filósofo aguarda (bloqueado) na chamada `limiteFilosofos.acquire()`. |
| **Método `comer()`** | **Marcação de Tempo:** Adição de `System.currentTimeMillis()` **antes** do `acquire()` e **depois** do retorno do `acquire()`. | Medir o tempo que a *thread* ficou bloqueada aguardando a permissão do Semáforo Central. |
| **Getters** | `getTempoTotalEspera()`, `getTentativasComer()` | Expor as novas métricas para coleta. |
| **Correção de Erro** | **Adição do método `public String getNome()`** | Corrigir o erro de compilação `cannot find symbol method getNome()` no `Main.java`. |

### 2\. `Main.java`

O `Main.java` foi modificado para configurar o ambiente de teste de 5 minutos e processar os dados.

| Alteração | Detalhe | Propósito |
| :--- | :--- | :--- |
| **Tempo Limite** | `final long TEMPO_LIMITE_SEGUNDOS = 300;` | Configurar a execução para **5 minutos** (requisito da Tarefa 5). |
| **Cálculo de Métricas** | O método `exibirEstatisticas` foi expandido para calcular: 1. **Tempo Médio de Espera** (por tentativa). 2. **Coeficiente de Variação (CV)** (métrica de Justiça). 3. **Taxa de Utilização Estimada** (métrica de *Throughput*). | Fornecer todos os dados estatísticos necessários para o relatório final. |
| **Correção de Escopo** | O método `exibirEstatisticas` agora recebe `NUM_FILOSOFOS` como argumento. | Corrigir erro de compilação e garantir que o cálculo da Taxa de Utilização esteja correto. |
