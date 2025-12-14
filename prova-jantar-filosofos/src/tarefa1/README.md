# 📄 Tarefa 1: Implementação Básica com Deadlock

## 🍽️ Objetivo

Implementar o problema clássico do Jantar dos Filósofos em Java, com o objetivo de **demonstrar e registrar a condição de deadlock** sob alta concorrência.

## 🚀 Como Compilar e Executar

1.  **Compilação:** Certifique-se de que os arquivos `Garfo.java`, `Filosofo.java`, e `Main.java` estão no mesmo diretório.

    ```bash
    javac Garfo.java Filosofo.java Main.java
    ```

2.  **Execução:**

    ```bash
    java Main
    ```

    *O programa tem um **tempo limite de 30 segundos** definido, o que é crucial para interromper o processo caso o deadlock ocorra.*

## 💀 Documentação explicando o Deadlock

O **deadlock** é uma situação em que dois ou mais *threads* ficam permanentemente bloqueadas, esperando umas pelas outras para liberar um recurso. Esta implementação possui a estrutura lógica completa que leva ao *deadlock*.

### Causa Lógica do Deadlock (Espera Circular)

O *deadlock* é causado pela **ordem simétrica e uniforme de aquisição de recursos** (`synchronized`), que estabelece uma dependência circular entre os filósofos.

1.  **Posse e Espera:** Cada filósofo usa o objeto Garfo como um *lock* e tenta pegar o Garfo Esquerdo e, em seguida, o Garfo Direito.
2.  **Ordem Uniforme:** Todos os filósofos seguem a mesma regra: `pegar_esquerda` antes de `pegar_direita`.

Se, em um momento crucial, **todos os cinco filósofos** conseguirem pegar simultaneamente seus garfos esquerdos, eles entrarão em um **ciclo de espera circular (deadlock)**. Nesse estado, nenhum filósofo conseguirá obter o segundo garfo, e o sistema ficará completamente paralisado.

## ⚠️ Análise da Execução (Evidência de Deadlock)

Os resultados da execução demonstraram que a condição de *deadlock* **não é apenas teórica, mas se manifestou** em pelo menos uma das execuções observadas (Log 1), enquanto em outras a execução continuou (Log 2) até o *timeout*.

### 1\. Evidência do Log de Deadlock (Log 1)

A análise do Log 1 **comprova a ocorrência do deadlock**, pois o log para abruptamente no momento exato em que o ciclo de espera circular é formado.

A imagem **`tarefa1_log1.png`** registra este momento.

```log
*** Programa esperando por no máximo 30 segundos... ***
|LOG| Filosofo F3 TENTA pegar Garfo Esquerdo (ID: 3)
|LOG| Filosofo F5 TENTA pegar Garfo Esquerdo (ID: 5)
|LOG| Filosofo F4 TENTA pegar Garfo Esquerdo (ID: 4)
|LOG| Filosofo F4 CONSEGUIU pegar Garfo Esquerdo (ID: 4)
|LOG| Filosofo F2 TENTA pegar Garfo Esquerdo (ID: 2)
|LOG| Filosofo F1 TENTA pegar Garfo Esquerdo (ID: 1)
|LOG| Filosofo F1 CONSEGUIU pegar Garfo Esquerdo (ID: 1)
|LOG| Filosofo F1 TENTA pegar Garfo Direito (ID: 2)
|LOG| Filosofo F2 CONSEGUIU pegar Garfo Esquerdo (ID: 2)
|LOG| Filosofo F2 TENTA pegar Garfo Direito (ID: 3)
|LOG| Filosofo F4 TENTA pegar Garfo Direito (ID: 5)
|LOG| Filosofo F3 CONSEGUIU pegar Garfo Esquerdo (ID: 3)
|LOG| Filosofo F5 CONSEGUIU pegar Garfo Esquerdo (ID: 5)
|LOG| Filosofo F5 TENTA pegar Garfo Direito (ID: 1)
|LOG| Filosofo F3 TENTA pegar Garfo Direito (ID: 4)
```

#### Análise do Congelamento:

  * **Aquisição Exaustiva (1/2):** O log registra que todos os 5 filósofos (`F1`, `F2`, `F3`, `F4`, `F5`) **conseguiram pegar seu Garfo Esquerdo**.
  * **Espera Circular:** O log então mostra cada filósofo **tentando e sendo bloqueado** ao tentar adquirir o Garfo Direito:
      * `|LOG| Filosofo F1 TENTA pegar Garfo Direito (ID: 2)` (Garfo 2 está com F2)
      * `|LOG| Filosofo F2 TENTA pegar Garfo Direito (ID: 3)` (Garfo 3 está com F3)
      * `|LOG| Filosofo F3 TENTA pegar Garfo Direito (ID: 4)` (Garfo 4 está com F4)
      * `|LOG| Filosofo F4 TENTA pegar Garfo Direito (ID: 5)` (Garfo 5 está com F5)
      * `|LOG| Filosofo F5 TENTA pegar Garfo Direito (ID: 1)` (Garfo 1 está com F1)
  * **Congelamento:** Não há mais registros de `CONSEGUIU` ou `COMEÇA A COMER`. O ciclo de dependência mútua está completo, e o sistema permanece congelado até que a *thread* principal seja interrompida, confirmando o *deadlock*.

Com certeza! Vou adaptar o tópico **"2. Evidência de Mitigação (Log 2)"** com base na sua análise e no log fornecido, e adicionarei a explicação sobre os logs pós-30 segundos.

### 2. Evidência de Mitigação (Log 2)

O Log 2, armazenado no arquivo **`tarefa1_log2.txt`**, demonstra que, apesar da vulnerabilidade estrutural do código ao *deadlock*, a execução, neste caso específico, **não entrou em impasse** e o sistema continuou progredindo ativamente.

* **Progresso Contínuo:** O log registra múltiplas ocorrências de filósofos completando o ciclo completo de aquisição, consumo e liberação de recursos (`COMEÇA A COMER` seguido de `SOLTOU Garfo Direito` e `SOLTOU Garfo Esquerdo`). Isso confirma que o ciclo de espera circular foi quebrado repetidamente antes de se estabelecer.
* **Interrupção pelo Timeout:** A execução produtiva (sem *deadlock*) durou até o limite máximo e foi interrompida pelo mecanismo de segurança: `*** TEMPO LIMITE DE 30 SEGUNDOS ATINGIDO. FORÇANDO PARADA DAS THREADS. ***`.

### ⚠️ Explicação dos Logs Após o Timeout (30 Segundos)

É crucial notar que o log continua a registrar eventos **após** a linha `*** TEMPO LIMITE DE 30 SEGUNDOS ATINGIDO... ***`. Isso acontece devido ao processo assíncrono de desligamento de *threads* em Java:

1.  **Sinal de Interrupção:** Após 30 segundos, a *thread* principal envia um sinal de interrupção (`Thread.interrupt()`) a todas as *threads* dos filósofos.
2.  **Processamento da Interrupção:** As *threads* não param instantaneamente. Elas precisam processar essa interrupção, o que inclui:
    * **Finalizar Operações Críticas:** Se uma *thread* estava comendo, ela pode terminar a seção crítica (`SOLTOU Garfo Direito`, `SOLTOU Garfo Esquerdo`) antes de encerrar.
    * **Capturar a Interrupção:** O código finaliza com mensagens como `Filosofo F4 interrompido enquanto comia` ou `Filosofo F2 encerrado devido à interrupção`, que são logs gerados enquanto a *thread* está no processo de desligamento (bloco `finally`).

Portanto, os logs após o timeout não representam trabalho produtivo normal, mas sim a **sequência de eventos de *shutdown* limpo (ou forçado)** do sistema.

### Conclusão

Esta implementação é estruturalmente falha e **vulnerável ao *deadlock***, como demonstrado no Log 1. A atividade observada no Log 2, com o sistema funcionando ativamente por 30 segundos, representa apenas uma execução onde o escalonador da JVM e os tempos aleatórios de pensamento/comida evitaram o impasse por sorte.











