
## Pré-requisitos

Antes de começar, certifique-se de ter instalado:

* **Java 17 (ou superior)**: [Download](https://www.oracle.com/java/technologies/javase-downloads.html)
* **Maven 3.8+**: [Download](https://maven.apache.org/download.cgi)
* **MySQL 8.0+**: [Download](https://dev.mysql.com/downloads/)
* **Git**: [Download](https://git-scm.com/downloads)
* Um editor de código (ex.: IntelliJ IDEA, VS Code)

## Passos para Configuração e Execução

### 1. Clonar o Repositório

Clone o projeto do GitHub para sua máquina:

```bash
git clone https://github.com/MarceloHabreu/reservas_lab.git
cd reservas_lab
```

### 2. Configurar o Arquivo `.env`

O projeto usa um arquivo `.env` para configurações do banco de dados. Siga estas etapas:

Copie o arquivo de exemplo:

```bash
cp .env.example .env
```

Abra o arquivo `.env` e edite com seus dados locais:

```env
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/reservas_lab?createDatabaseIfNotExist=true
SPRING_DATASOURCE_USERNAME=seu_usuario_mysql
SPRING_DATASOURCE_PASSWORD=sua_senha_mysql
```

Exemplo:

```env
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/reservas_lab?createDatabaseIfNotExist=true
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=minha_senha
```

### 3. Criar o Banco de Dados

Acesse o MySQL:

```bash
mysql -u seu_usuario_mysql -p
```

Crie o banco de dados:

```sql
CREATE DATABASE reservas_lab;
```

Saia do MySQL:

```sql
EXIT;
```

### 4. Instalar Dependências

Compile o projeto e baixe as dependências:

```bash
mvn clean install
```

### 5. Rodar o Projeto

Execute a aplicação:

```bash
mvn spring-boot:run
```

O que acontece:

* O Spring Boot iniciará na porta padrão (8080).
* O Flyway executará as migrations em `src/main/resources/db/migration`.
* Serão criadas tabelas e inseridos dados iniciais (ex.: papéis PROFESSOR, TECNICO\_LABORATORIO).

### 6. Verificar a Execução

* Acesse [http://localhost:8080](http://localhost:8080)
* Verifique os logs no terminal:

  > `Flyway: Successfully applied 1 migration to schema 'reservas_lab'`

No MySQL:

```sql
USE reservas_lab;
SHOW TABLES;
```

Você verá tabelas como: `usuario`, `escola`, `professor`, `tecnico_laboratorio`, `laboratorio`, `reserva`, `roles`, `usuarios_roles`, `flyway_schema_history`.

### 7. (Opcional) Testar a Aplicação

* Testar via endpoints REST (ex.: `POST /api/reservas`)
* Executar testes:

```bash
mvn test
```

## Solução de Problemas

### Erro de conexão com o MySQL:

* Verifique se o MySQL está rodando:

  ```bash
  mysqladmin -u root -p status
  ```
* Confirme usuário e senha do `.env`
* Certifique-se que a porta 3306 está liberada


## Estrutura do Projeto

```
reservas-lab/
├── src/main/java/com/fsu/reservas_lab      # Código-fonte
├── src/main/resources/db/migration          # Scripts Flyway
├── src/main/resources/application.yml       # Configurações Spring Boot
├── .env.example                             # Modelo para arquivo .env
```

## Contribuição

1. Faça um fork do repositório
2. Crie uma branch:

   ```bash
   git checkout -b minha-feature
   ```
3. Commit suas mudanças:

   ```bash
   git commit -m "Adiciona minha feature"
   ```
4. Push para a branch:

   ```bash
   git push origin minha-feature
   ```
5. Abra um Pull Request

