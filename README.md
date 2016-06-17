# Projeto de Sistemas Distribuídos 2015-2016 #

Grupo de SD 13 - Campus Alameda

João Coelho 77983 jskoelhoo@gmail.com 

Micael Batista 78941 micael.pina@gmail.com

Telma Correia 78572


Repositório:
[tecnico-distsys/A_13-project](https://github.com/tecnico-distsys/A_13-project/)

-------------------------------------------------------------------------------

## Instruções de instalação 

Devido ás dependências que os serviços requerem, existe uma ordem necessária de instalação.
A ordem pode ser:

  ws-handlers -> ca-ws -> ca-ws-cli -> broker-ws-cli -> transporter-ws-cli -> transporter-ws -> broker-ws -> broker-ws (backup) \[-> broker-ws-cli\] (para testar execução)

### Ambiente

[0] Iniciar sistema operativo

Linux

[1] Iniciar servidores de apoio

JUDDI: Instalar conforme especificado na [página da cadeira](http://disciplinas.tecnico.ulisboa.pt/leic-sod/2015-2016/labs/05-ws1/index.html)


[2] Criar pasta temporária

```
mkdir A13
```


[3] Obter código fonte do projeto (versão entregue)

```
git clone git@github.com:tecnico-distsys/A_13-project.git 
```


[4] Instalar módulos de bibliotecas auxiliares


Correr conforme especificado na [página da cadeira](http://disciplinas.tecnico.ulisboa.pt/leic-sod/2015-2016/labs/05-ws1/index.html)

-------------------------------------------------------------------------------
### Handlers

```
cd ws-handlers
mvn clean install
```


-------------------------------------------------------------------------------

### Serviço CA

[1] Construir e executar **servidor**

```
cd ca-ws
mvn clean install
mvn exec:java 
```

[2] Construir **cliente** e executar testes

```
cd ca-ws-cli
mvn clean generate-sources
mvn install
```

-------------------------------------------------------------------------------

### Serviço TRANSPORTER

[1] Construir e executar **servidor**

```
cd transporter-ws
mvn clean install
mvn exec:java 
mvn -Dws.i= [2-9] exec:java (dependendo do id do transporter a criar)
```

Para correr os testes: 
```
cd transporter-ws
mvn test 
```

[2] Construir **cliente** e executar testes

```
cd transporter-ws-cli
mvn clean install 
```

Para correr os testes (assumindo que o transporter 1 & 2 estão a correr): 
```
cd transporter-ws-cli
mvn test verify
```


-------------------------------------------------------------------------------

### Serviço BROKER

[1] Construir e executar **servidor**

```
cd broker-ws
mvn clean install
mvn exec:java
```
Para correr os testes: 
```
cd broker-ws
mvn test 
```

[2] Construir e executar **servidor** de backup

```
cd broker-ws
mvn clean install
mvn exec:java -Db=true -Dp=91
```

[3] Construir **cliente** e executar testes

```
cd broker-ws-cli
mvn clean install
(no final da instalação para testar funcionalidade) mvn exec:java
```

Para correr os testes (assumindo que o transporter 1 & 2 estão a correr): 
```
cd broker-ws-cli
mvn test verify
```

-------------------------------------------------------------------------------



**FIM**
