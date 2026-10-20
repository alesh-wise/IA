# Trabalhos de IA

Trabalhos da unidade curricular de Inteligência Artificial, em Java.

# Lab 1 — Ball Sort Puzzle com procura A*

Resolução do puzzle **Ball Sort** com o algoritmo de procura **A\***.

## O puzzle

Há vários tubos com bolas de cores diferentes. Em cada jogada move-se a bola do
topo de um tubo para outro tubo, desde que o destino:

- não esteja cheio; e
- esteja vazio **ou** tenha no topo uma bola da mesma cor.

O objetivo é deixar cada tubo **vazio** ou **cheio com uma só cor**.
Cada jogada custa 1, por isso o programa procura o **menor número de jogadas**.

## Formato de entrada e saída

A primeira linha tem a **altura** dos tubos e o **número de tubos**. Seguem-se as
linhas do puzzle, de cima para baixo: cada coluna é um tubo, cada letra uma cor
e `E` uma posição vazia.

```
3 3
R B E
B B E
R R E
```

O programa escreve o número mínimo de jogadas:

```
5
```

## Como executar

```bash
javac -d out src/*.java
java -cp out Main < input.txt
```

Ou abrir o projeto no IntelliJ IDEA e correr a classe `Main`.
Os testes JUnit estão em `test/PuzzleUnitTests.java`.

## Estrutura

| Ficheiro | Descrição |
|---|---|
| `src/Ilayout.java` | Interface genérica de um estado: sucessores, teste de objetivo, custo e heurística. |
| `src/BallSortLayout.java` | Representação do puzzle (cada tubo é uma `Stack`), geração de jogadas e heurística. |
| `src/Asearch.java` | Procura A\* (ordena por `f = g + h`). |
| `src/BestFirst.java` | Procura de custo uniforme (ordena só por `g`), para comparação. |
| `src/Main.java` | Lê o puzzle do `stdin` e imprime o custo da solução. |

## Algoritmo

O `Asearch` mantém uma fila de prioridade de estados por expandir, ordenada por
`f = g + h`, e um mapa `melhorCusto` com o menor `g` conhecido para cada
configuração.

- Um filho só entra na fila se melhorar o custo conhecido para essa configuração.
- Como a `PriorityQueue` não permite baixar a prioridade de um elemento, a mesma
  configuração pode ficar na fila várias vezes. As cópias com custo pior são
  descartadas quando saem da fila.
- O teste de objetivo é feito ao **retirar** o estado da fila, o que garante a
  solução ótima.

## Heurística

```
h = nº total de bolas − Σ (por cor) maior "base" dessa cor
```

A **base** de um tubo é a sequência de bolas da mesma cor a contar do fundo.
Uma bola só pode ficar parada até ao fim se fizer parte da base do tubo onde a
sua cor vai acabar, e cada cor acaba num só tubo. Todas as outras bolas têm de
se mover pelo menos uma vez, e cada jogada move uma bola.

- **Admissível:** nunca sobrestima o número de jogadas, por isso o A\* encontra
  a solução ótima.
- **Consistente:** uma jogada altera `h` no máximo em 1, que é o custo da jogada.
