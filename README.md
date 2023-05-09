# Batalha naval

O jogo de batalha naval é um jogo bastante popular mundialmente. Esse projeto implementa o batalha naval com dois jogadores jogando um contra o outro. O projeto utiliza o prtocolo UDP para a comunicação e funciona com um servidor e dois clientes.

## Installation

- Para instalar, certifique-se de ter o [JDK](https://www.oracle.com/br/java/technologies/downloads/) instalado;
- Faça o clone desse repositório na branch master

## Usage

- Após ter feito o clone, vá até o local de instalação dentro da pasta 'src':
```bash
caminho-da-instalação/batalha-naval/src
```
- Rode o comando abaixo para compilar todos os arquivos java:
```bash
javac *.java
```
- No mesmo terminal que foi rodado o comando anterior, inicialize o servidor passando o ip da máquina e porta da sua escolha:
```bash
java Servidor 0.0.0.0 5000
```
- Em outro terminal, agora para o jogador, inicialize o cliente número 1 passando o ip do servidor do comnando passado:
```bash
java Cliente 0.0.0.0
```
- Repita o processo para o jogador 2:
```bash
java Cliente 0.0.0.0
```
