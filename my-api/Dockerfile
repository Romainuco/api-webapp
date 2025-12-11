# --------------------------------------------------------
# Étape 1 : Build
# --------------------------------------------------------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# On copie TOUT d'un coup (pom.xml et dossier src)
COPY pom.xml .
COPY src ./src

# On lance directement la compilation.
# Maven téléchargera les dépendances à la volée ici.
RUN mvn clean package -DskipTests

# --------------------------------------------------------
# Étape 2 : Run
# --------------------------------------------------------
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]