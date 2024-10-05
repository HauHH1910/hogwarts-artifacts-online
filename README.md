<h1>Hogwarts Artifacts Online</h1>
<p>Hogwarts and Artifact online is a backend service for learning Spring Boot with the approaching TDD method</p>
<hr>

# Tech Stack
    Spring Boot
    Spring Data JPA
    Spring Security
    Spring Test
    H2 Database
    Docker
    Maven
    Validation
    DevTools
    Lombok
    Oauth2 Server
<hr>

# How to build application
Step 1: Building with maven to executable jar (-DskipTests is for skipping the test)

    mvn clean install -DskipTests

Step 2: Building file jar with docker

    docker build -t hogwarts-artifacts-online:1.0 .

Step 3: Running application with docker

    docker -d -p 8080:8080 hogwarts-artifacts-online:1.0

Step 4: Run application with ID of the container 

    docker start {containerID}