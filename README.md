# 📦 LuizaLabs Desafio

Desafio técnico proposto pela **LuizaLabs**.  

---

## Arquitetura
A Arquitetura Limpa (Clean Architecture) foi escolhida para a API REST porque garante baixo acoplamento, alta testabilidade e fácil manutenção, separando a aplicação em três camadas bem definidas:

**Application** → Gerencia a entrada e saída de dados, incluindo controladores e casos de uso.

**Infrastructure** → Lida com configuração e adaptadores, abstraindo bancos de dados e APIs externas.

**Core** (Domínio) → Define as regras de negócio e entidades, mantendo independência da infraestrutura.

Essa arquitetura foi escolhida porque favorece modularidade, escalabilidade e organização, permitindo que mudanças na infraestrutura (como trocar o banco de dados ou adicionar um novo serviço) não impactem as regras de negócio. Além disso, facilita a escrita de testes unitários, pois o domínio não depende de frameworks ou tecnologias externas. A estrutura segue os princípios SOLID e DDD, tornando a API mais sustentável e evolutiva
---

## 🛠️ Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.4.5**
- **MongoDB**
- **Docker / Docker Compose**

---

## 🗂️ Estrutura de Pacotes

```text
src/main/java/com/luizalabs_desafio/
├── application
│   ├── advicer           # Tratamento global de exceções
│   ├── controller        # Controllers REST
│   ├── mapper            # DTOs de entrada e saída
├── core
│   ├── customer          # Entidades de domínio e regras de negócio relacionada ao dominio
│   ├── exception         # Exceções customizadas
    ├── utils             # Funções genericas e customizadas
├── infrastructure
│   ├── adapter           # Tem a função de fazer a ponte entre a camada de domínio (Core) e tecnologias externas
│   ├── repository     # Interfaces e implementações que lidam com bancos de dados ou armazenamento externo.
│   ├── swagger     # Documentação open api.
```
# Detalhes do Projeto
Este projeto é uma **API RESTful** desenvolvida com **Java e Spring Boot**, focada no gerenciamento de pedidos de clientes. Utiliza **MongoDB** como base de dados NoSQL e aplica uma arquitetura em camadas com boas práticas de desenvolvimento.

A escolha do MongoDB foi feita considerando que o desafio não exigia relacionamentos rígidos, tornando o modelo de documentos ideal. 

MongoDB também oferece alta performance comparado a SQL em cenários de inserção e recuperação intensiva. 

O uso de replicaset preserva a usabilidade de transações ACID, garantindo a atomicidade das operações, mantendo a confiabilidade do sistema.

(TDD) Testes unitarios cobrindo funcionalidades da api e teste integração com banco de dados em memoria h2 para validar o repository

# Otimizações de Performance

A aplicação foi configurada com foco em performance e estabilidade:

HikariCP: gerenciamento de conexões com pool otimizado (até 20 conexões, timeout ajustado).

Compressão de Respostas: ativa para payloads acima de 1KB, reduzindo tráfego.

Cache de Recursos Estáticos: habilitado por até 1 hora.

Limites de Upload: arquivos limitados a 2MB para segurança e controle de carga.

Logs e Monitoramento: actuator exposto com detalhes de saúde e logs de queries SQL.

G1GC ativado: coleta de lixo com pausas curtas e heap dump em caso de falha.

Essas configurações garantem melhor uso de recursos, menor latência e maior observabilidade.

# Como Executar o Projeto

Pré-requisitos:
- Java 21
- Maven
- Docker 
- Docker Compose

# Passos

- Clonar o repositório

  `git clone https://github.com/2020nani/luizalabs.git`

- Entrar na pasta raiz do repositorio
  
  `cd luizalabs`

- Subir o MongoDB/Build Projeto com Docker
  
  `docker-compose up -d`

   Api esta sendo acessada via localhost (http://localhost:8080)

   A collection com os endpoints utilizados esta dentro da raiz do projeto
   
   Para testar a api pode se utilizar a collection do postman ou utilizar a documentação da api (Swagger) acessando a url

  http://localhost:8080/luizalabs/v1/swagger-ui/index.html
