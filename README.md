# Projeto Farmácia — Sistema Web em Java (NetBeans)

Este é um sistema web desenvolvido em **Java (Jakarta EE)** com **Servlets, JSP e JDBC**, voltado para o gerenciamento de uma farmácia.  
O projeto aplica boas práticas de engenharia de software e diversos **Design Patterns**, dentro de uma arquitetura **MVC (Model–View–Controller)**.

---

## Funcionalidades

- Cadastro e listagem de produtos  
- Gerenciamento de clientes  
- Criação e controle de pedidos  
- Geração de notas fiscais  
- Relatórios de pedidos (Command Pattern)  
- Construção de objetos complexos (Builder Pattern)

---

## Padrões de Projeto Utilizados

| Padrão | Aplicação no Projeto |
|--------|----------------------|
| **MVC** | Estrutura base: Controller (Servlets), Model (Entidades) e View (JSPs) |
| **DAO** | Isolamento do acesso ao banco de dados via classes DAO |
| **Builder** | Criação de objetos `Pedido` com flexibilidade e clareza |
| **Command** | Execução de relatórios com comandos reutilizáveis |
| **Service Layer** | Centralização das regras de negócio e validações |

---

## Tecnologias Utilizadas

- Java 17+  
- Jakarta Servlet API / JSP  
- MySQL (via JDBC)  
- NetBeans IDE  
- Apache Tomcat  
- Ant (build padrão do NetBeans)

---

## Estrutura de Pastas (resumo)

