#### Stage 1: Build the application
# Currently has issue on maven wrapper so install the maven directly in the images :))
FROM maven:3.8-openjdk-17-slim as build

# Set the current working directory inside the image
WORKDIR /app

# Copy maven executable to the image
COPY mvnw .
COPY .mvn .mvn
# Copy the pom.xml file
COPY pom.xml .

# Build all the dependencies in preparation to go offline. 
# This is a Maven goal that resolves all project dependencies and plugins and 
# downloads them to the local Maven repository. It ensures that Maven has all the necessary dependencies available
# locally, so it doesn't need to fetch them from the internet during each build. This is particularly
# the pom.xml file has changed.
RUN mvn dependency:go-offline -B

# Copy the project source
COPY src src

# Package the application
RUN mvn package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

#### Stage 2: A minimal docker image with command to run the app 
FROM maven:3.8-openjdk-17-slim

ARG DEPENDENCY=/app/target/dependency

# Copy project dependencies from the build stage
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","com.khai.quizguru.QuizGuruApplication"]