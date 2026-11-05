# SpotyJS

Aplicativo de streaming musical desenvolvido em Java, com orientacao a objetos e interface de terminal.

## Requisitos
- JDK 17 ou superior
- Maven 3.8 ou superior
- Conexao com a internet na primeira compilacao (para baixar Gson, JLayer e JUnit)

## Como executar
Na pasta raiz do projeto:

```bash
mvn clean test
mvn exec:java
```

No Windows, os mesmos comandos funcionam no PowerShell ou terminal do VS Code, desde que Java e Maven estejam no PATH.

## Contas e perfis
- **Ouvinte:** consulta o catalogo, reproduz faixas, curte musicas, cria playlists e segue cantores.
- **Cantor:** cadastra faixas MP3 locais, cria playlists e consulta seus seguidores.
- **Administrador:** consulta usuarios, bloqueia/desbloqueia contas e remove musicas do catalogo.

A primeira execucao cria uma conta administrativa:
- E-mail: `admin@spotyjs.com`
- Senha inicial: `admin123`

Defina a variavel de ambiente `SPOTYJS_ADMIN_PASSWORD` antes da primeira execucao para escolher outra senha. A senha e armazenada com PBKDF2-HMAC-SHA256 e salt individual. Em um sistema real, a conta inicial e a senha devem ser configuradas fora do codigo.

## Catalogo e audio
O programa carrega automaticamente as tres faixas MP3 que ja estao em `src/musicas/`. O player usa JLayer e executa o audio em uma thread separada. O volume fica registrado como configuracao do player; o JLayer utilizado nesta versao nao oferece controle de volume.

Para cadastrar uma faixa, o cantor informa o caminho de um arquivo MP3 existente no computador. O arquivo nao e copiado para o repositorio.

## Persistencia
As contas, hashes de senha, curtidas, relacionamentos, playlists e catalogo sao salvos em `dados/spotyjs.json` ao sair dos menus e ao encerrar o programa. Esse arquivo e criado automaticamente e nao deve ser enviado ao GitHub.

## Organizacao e conceitos de POO
- `Usuario` e abstrata; `Ouvinte`, `Cantor` e `Administrador` herdam dela.
- `Player` implementa `Reproduzivel`; `Musica` representa os metadados e o caminho do arquivo.
- `Playlist` e `FilaDeReproducao` encapsulam colecoes e validam operacoes.
- `AppDataStore` concentra a persistencia JSON.

## Observacoes
Este e um projeto academico de demonstracao. O player implementa reproducao e pausa de MP3; o comportamento de pausa depende dos frames suportados pelo JLayer. O recurso de download premium e validado pelo modelo, mas ainda nao existe uma tela/fluxo de download no menu.