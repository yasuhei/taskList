### Primeiro commit

Neste primeiro commit, criei a estrutura base do projeto, separando as responsabilidades em `application`, `domain` e `infrastructure`.

No `domain`, criei a primeira entidade, `Task`, definindo seus atributos e tratando os campos que podem ser nulos de forma adequada.

Em seguida, criei a classe `TaskId` utilizando um `record`, garantindo que o identificador seja imutável.

Por fim, criei a interface `TaskRepository`, que será utilizada nos próximos commits para definir as operações de persistência da entidade `Task`.
