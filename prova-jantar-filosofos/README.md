## 📄 README.md - Projeto Jantar dos Filósofos

# 🍽️ Jantar dos Filósofos: Avaliação de Programação Paralela

Este repositório contém a solução para a avaliação final da disciplina de Programação Paralela e Distribuída, focada na implementação e análise de diferentes abordagens para o clássico **Problema do Jantar dos Filósofos**.

O problema simula um cenário onde múltiplos processos (filósofos) precisam compartilhar recursos limitados (garfos), sendo um excelente estudo de caso para conceitos de concorrência, como **deadlock**, **starvation** e **exclusão mútua**.

## 📝 Instruções Gerais da Avaliação

| Item | Detalhes |
| :--- | :--- |
| **Data Limite de Entrega** | 14/12/2025 às 23h59 (Enviar o link do repositório) |
| **Recursos Permitidos** | ChatGPT, Internet, livros, materiais de estudo e documentação |
| **Recursos Vetados** | Cópia explícita de solução de colegas ou de outros repositórios |
| **Linguagem de Implementação** | Java (Requisito da Tarefa 1, implícito nas demais) |
| **Avaliação** | Corrigibilidade, qualidade do código, documentação, testes e compreensão dos conceitos |
| **Código e Documentação** | O repositório deve conter código-fonte completo, documentação das soluções, testes e este `README.md` com instruções |

## 📂 Estrutura do Repositório

O repositório está organizado para separar cada solução em sua própria pasta, incluindo as versões originais (`src`) e as versões instrumentadas para coleta de dados e testes (`test`).

```
prova-jantar-filosofos/
├── README.md              # Instruções gerais, estrutura e compilação (Este arquivo)
├── RELATORIO.md           # Relatório comparativo e análise final (Tarefa 5)
├── src/
│   ├── tarefa1/           # Implementação básica com deadlock (Tarefa 1)
│   ├── tarefa2/           # Solução com ordem de garfos (Tarefa 2)
│   ├── tarefa3/           # Solução com semáforos (Tarefa 3)
│   └── tarefa4/           # Solução com monitor (Mesa) (Tarefa 4)
└── test/ 
    ├── tarefa2_teste/      # Versão T2 instrumentada para 5 min + métricas
    ├── tarefa3_teste/      # Versão T3 instrumentada para 5 min + métricas
    └── tarefa4_teste/      # Versão T4 instrumentada para 5 min + métricas
```

## 🛠️ Instruções de Compilação e Execução

As instruções a seguir são genéricas e devem ser aplicadas a qualquer uma das pastas que contenham o programa principal (`Main.java`), seja em `src/` (execuções rápidas de demonstração) ou em `test/` (execuções longas de 5 minutos para coleta de métricas).

### 1\. Compilação das Classes

Para compilar os arquivos Java (`.java`) em uma determinada pasta, navegue até o diretório via terminal e utilize o comando `javac *.java`:

```bash
# Exemplo de navegação para a Tarefa 4 original:
cd src/tarefa4/ 

# Compila todos os arquivos .java no diretório atual:
javac *.java 
```

### 2\. Execução do Programa

Após a compilação bem-sucedida, execute o programa principal (`Main.class`) utilizando o comando `java Main`:

```bash
# Executa o programa principal na pasta atual:
java Main 
```

  * **Pastas `src/`:** A execução geralmente é rápida, destinada a demonstrar o comportamento (e o *deadlock* na Tarefa 1).
  * **Pastas `test/`:** A execução é controlada por **300 segundos (5 minutos)** para coletar dados de desempenho e justiça.

## 🧠 Análise e Documentação por Tarefa

Os arquivos de código-fonte e o **[RELATORIO.md](https://www.google.com/search?q=./RELATORIO.md)** contêm a documentação detalhada, conforme os requisitos de cada tarefa.

  * **Tarefa 1 (Deadlock):** A implementação básica demonstra o deadlock circular. Uma explicação detalhada sobre a **condição para deadlock** (e.g., espera circular, posse e espera, exclusão mútua e não preempção) está no código e/ou documentação.
  * **Tarefa 2 (Ordem Diferente):** Explica a prevenção do *deadlock* pela quebra da condição de espera circular e discute a possibilidade de **starvation**.
  * **Tarefa 3 (Semáforos):** Explica como o semáforo previne *deadlock* limitando o número de filósofos que podem tentar comer simultaneamente (o que impede a espera circular).
  * **Tarefa 4 (Monitor/Fairness):** Detalha como o monitor centraliza o controle e utiliza `wait()`/`notifyAll()` ou mecanismos de fila para garantir **fairness**, prevenindo tanto *deadlock* quanto *starvation*.
  * **Tarefa 5 (Relatório):** O arquivo **[RELATORIO.md](https://www.google.com/search?q=./RELATORIO.md)** apresenta todas as métricas de desempenho (refeições, tempo de espera, utilização, *fairness*) e uma análise crítica comparando as soluções em termos de performance, complexidade e uso de recursos.
