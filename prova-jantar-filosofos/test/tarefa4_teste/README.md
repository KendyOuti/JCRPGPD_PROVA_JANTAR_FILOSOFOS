# 📚 Tarefa 4: Solução com Monitores e Garantia de Fairness (Versão de Testes para o Relatório Final)

Esta versão da Tarefa 4 foi adaptada para coletar as métricas de desempenho e justiça exigidas pela **Tarefa 5 (Análise Comparativa)**, utilizando a solução de Monitor Central que previne *deadlock* e *starvation* através da verificação de estado (`COM_FOME`, `COMENDO`) e uso do `wait`/`notifyAll`.

## 🎯 Objetivo da Alteração

O objetivo principal desta versão é **instrumentar o código** para medir o desempenho estatístico da Solução de Monitor.

  * **Tempo de Execução:** Alterado de 2 minutos para **5 minutos (300 segundos)**.
  * **Coleta de Métricas:** Implementação de contadores para medir o tempo de espera dentro do Monitor, o *throughput* (vazão) e a *fairness* (justiça) do sistema.

## 🛠️ Alterações em Relação à Tarefa 4 Original

As modificações foram concentradas em `Filosofo.java` e `Main.java` para a coleta de dados.

### 1\. `Filosofo.java`

O `Filosofo.java` foi instrumentado para medir o tempo que a *thread* passa em estado de bloqueio (`wait()`) dentro do Monitor.

| Alteração | Detalhe | Propósito |
| :--- | :--- | :--- |
| **Novos Atributos** | `tempoTotalEspera`, `inicioEspera`, `tentativasComer` | Rastrear e acumular o tempo que o filósofo aguarda (bloqueado) dentro do Monitor na chamada `pegarGarfos()`. |
| **Método `comer()`** | **Marcação de Tempo:** Adição de `System.currentTimeMillis()` **antes** de chamar `monitorMesa.pegarGarfos()` e **depois** do retorno da chamada. | Medir o tempo exato em que a *thread* ficou esperando a condição para comer ser satisfeita (tempo de espera). |
| **Getters** | `getTempoTotalEspera()`, `getTentativasComer()`, `getNome()` | Expor as novas métricas para que o `Main.java` possa coletá-las. |

### 2\. `Main.java`

O `Main.java` foi modificado para configurar o ambiente de teste de 5 minutos e processar os dados finais.

| Alteração | Detalhe | Propósito |
| :--- | :--- | :--- |
| **Tempo Limite** | `final long TEMPO_LIMITE_SEGUNDOS = 300;` | Configurar a execução para **5 minutos** (requisito da Tarefa 5). |
| **Cálculo de Métricas** | O método `exibirEstatisticas` foi expandido para calcular: 1. **Tempo Médio de Espera** (por tentativa). 2. **Coeficiente de Variação (CV)** sobre o número de refeições (métrica de Justiça). 3. **Taxa de Utilização Estimada** (métrica de *Throughput*). | Fornecer todos os dados estatísticos necessários para o relatório final. |
| **Correção de Escopo** | O método `exibirEstatisticas` agora recebe `NUM_FILOSOFOS` como argumento. | Corrigir erro de compilação e garantir que o cálculo da Taxa de Utilização esteja correto. |

### 3\. `Mesa.java` e `Garfo.java`

  * **Não sofreram alterações**, mantendo a lógica central da solução Monitor e a definição do recurso.

