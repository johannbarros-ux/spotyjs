# Instruções do projeto SpotyJS

## Contexto

SpotyJS é um projeto acadêmico em Java para modelar um serviço de música usando orientação a objetos. O diagrama de classes enviado para a atividade é a referência principal para nomes, herança, associações, atributos e métodos.

O código-fonte fica em `src/` e usa o pacote padrão. Atualmente, algumas classes são apenas esqueletos; implemente o comportamento de forma incremental, sem inventar camadas ou dependências desnecessárias.

## Estrutura do domínio

- `Usuario` é a classe-base de usuários.
- `Cantor`, `Ouvinte` e `Administrador` estendem `Usuario`.
- `Musica` implementa `Reproduzivel`.
- `Playlist` representa uma lista de músicas e tem um proprietário.
- `Player` reproduz músicas e utiliza `FilaDeReproducao`.
- `Plano` representa as permissões do usuário, como download e pulo de músicas.
- `Main` é o ponto de entrada da aplicação.

Respeite as relações e multiplicidades apresentadas no diagrama. Não renomeie classes, atributos ou métodos existentes sem necessidade.

## Regras de implementação

- Use Java orientado a objetos, encapsulamento e tipos explícitos.
- Prefira atributos `private` e métodos públicos apenas quando fizerem parte da API da classe.
- Use `List` para coleções de músicas, curtidas e playlists quando o modelo exigir uma coleção.
- Valide entradas inválidas com exceções ou retornos coerentes; não esconda erros com `catch` genérico.
- Evite dependências externas: o projeto deve compilar com o JDK.
- Não adicione frameworks, banco de dados ou interface gráfica sem pedido explícito.
- Preserve o estilo simples e acadêmico do projeto.
- Faça alterações pequenas e relacionadas ao pedido; não refatore classes não envolvidas.

## Validação

Compile todos os arquivos com:

```bash
javac -d out src/*.java
```

Execute o programa com:

```bash
java -cp out Main
```

Ao alterar uma classe, compile o projeto inteiro, pois as classes possuem relações de herança e implementação.

## Como trabalhar neste projeto

Antes de editar:

1. Leia o arquivo diretamente envolvido e suas superclasses ou interfaces.
2. Compare a alteração com o diagrama de classes.
3. Faça a menor mudança completa.
4. Compile o projeto e explique brevemente o que mudou.

Se o pedido entrar em conflito com o diagrama, exigir uma decisão de design importante ou envolver uma mudança ampla, explique o conflito antes de escolher uma solução.
