# Use the official OpenJDK 17 Alpine image as the base image
FROM eclipse-temurin:17

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from your local machine into the container
COPY ./target/demo-0.0.1-SNAPSHOT.jar app.jar

# Command to run the JAR file
CMD ["java", "-jar", "app.jar"]
