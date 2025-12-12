# 📄 Tarefa1: Implementação Básica com Deadlock

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

A análise do Log 1 **comprova a ocorrência do deadlock**, pois o log para abruptamente no momento exato em que o ciclo de espera circular é formado:

  * **Aquisição Exaustiva (1/2):** O log registra que todos os 5 filósofos (`F1`, `F2`, `F3`, `F4`, `F5`) **conseguiram pegar seu Garfo Esquerdo**.
  * **Espera Circular:** O log então mostra cada filósofo **tentando e sendo bloqueado** ao tentar adquirir o Garfo Direito:
      * `|LOG| Filosofo F1 TENTA pegar Garfo Direito (ID: 2)`
      * `|LOG| Filosofo F2 TENTA pegar Garfo Direito (ID: 3)`
      * `|LOG| Filosofo F4 TENTA pegar Garfo Direito (ID: 5)`
      * `|LOG| Filosofo F5 TENTA pegar Garfo Direito (ID: 1)`
      * `|LOG| Filosofo F3 TENTA pegar Garfo Direito (ID: 4)`
  * **Congelamento:** Não há mais registros de `CONSEGUIU` ou `COMEÇA A COMER`. A thread principal é interrompida, confirmando que o sistema estava congelado no ciclo de espera circular.

### 2\. Evidência de Mitigação (Log 2)

O Log 2, por outro lado, demonstra a **quebra acidental** do ciclo de espera pelo escalonamento da JVM e pela aleatoriedade do tempo de execução:

  * **Progresso:** O log registra múltiplas ocorrências de filósofos completando o ciclo (`COMEÇA A COMER` seguido de `SOLTOU Garfo Direito` e `SOLTOU Garfo Esquerdo`).
  * **Interrupção pelo Timeout:** A execução produtiva (sem deadlock) durou até o limite máximo e foi interrompida pelo mecanismo de segurança: `*** TEMPO LIMITE DE 30 SEGUNDOS ATINGIDO. FORÇANDO PARADA DAS THREADS. ***`.

### Conclusão

Esta implementação é estruturalmente falha. **O código é vulnerável ao deadlock**, como demonstrado no Log 1. A atividade observada no Log 2 representa apenas uma execução onde o escalonador e os tempos aleatórios evitaram o impasse por sorte.
