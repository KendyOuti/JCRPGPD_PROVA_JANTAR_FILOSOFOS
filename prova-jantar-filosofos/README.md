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

O repositório está organizado para separar cada solução em sua própria pasta, conforme a estrutura:

```
prova-jantar-filosofos/
├── README.md              # Instruções gerais, estrutura e compilação (Este arquivo)
├── RELATORIO.md           # Relatório comparativo e análise final (Tarefa 5)
└── src/
    ├── tarefa1/           # Implementação básica com deadlock (Tarefa 1)
    ├── tarefa2/           # Solução com prevenção de deadlock por ordem de garfos (Tarefa 2)
    ├── tarefa3/           # Solução com semáforos e limitação de filósofos (Tarefa 3)
    └── tarefa4/           # Solução com monitor (Mesa) e garantia de fairness (Tarefa 4)
```

## 🧠 Análise e Documentação por Tarefa

Os arquivos de código-fonte e o `RELATORIO.md` contêm a documentação detalhada, conforme os requisitos de cada tarefa.

  * **Tarefa 1 (Deadlock):** A implementação básica demonstra o deadlock circular. Uma explicação detalhada sobre a **condição para deadlock** (e.g., espera circular, posse e espera, exclusão mútua e não preempção) está no código e/ou documentação.
  * **Tarefa 2 (Ordem Diferente):** Explica a prevenção do deadlock pela quebra da condição de espera circular e discute a possibilidade de **starvation**.
  * **Tarefa 3 (Semáforos):** Explica como o semáforo previne deadlock limitando o número de filósofos que podem tentar comer simultaneamente (o que impede a espera circular).
  * **Tarefa 4 (Monitor/Fairness):** Detalha como o monitor centraliza o controle e utiliza `wait()`/`notifyAll()` ou mecanismos de fila para garantir **fairness**, prevenindo tanto deadlock quanto starvation.
  * **Tarefa 5 (Relatório):** O arquivo **`RELATORIO.md`** apresenta todas as métricas de desempenho (refeições, tempo de espera, utilização, fairness) e uma análise crítica comparando as soluções em termos de performance, complexidade e uso de recursos.
