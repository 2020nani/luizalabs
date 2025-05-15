FROM azul/zulu-openjdk-alpine:21 AS builder

# Instala Maven e dependências necessárias
RUN apk add --no-cache maven

WORKDIR /app

# Copia os arquivos do projeto
COPY pom.xml .
COPY src ./src

# Executa o build do projeto
RUN mvn clean package -DskipTests

# Etapa final (imagem runtime, usando a mesma imagem base)
FROM azul/zulu-openjdk-alpine:21

WORKDIR /opt/app

# Copia o JAR gerado da etapa de build
COPY --from=builder /app/target/*.jar /opt/app/app.jar

# Entrypoint
ENTRYPOINT ["sh", "-c", "java -jar /opt/app/app.jar"]



