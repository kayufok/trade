# MVP 1 Detailed Execution Plan: Project Initialization and Environment Setup

## 1. Overview
- **Objective**: Set up a Spring Boot project with Gradle, configure MyBatis Plus for PostgreSQL connectivity, implement JSON logging with Logback and Lombok's SLF4J, and establish GitHub version control, delivering a runnable application.
- **Developer Background**: 5 years of Java Spring Boot experience, proficient in large-scale financial systems (low-latency, high-concurrency), skilled in Oracle SQL/PLSQL, independent developer.
- **Development Timeline**: 30 hours (1 hour/day, 1 month).
- **Technology Stack**:
  - Framework: Spring Boot 3.4.8
  - Build Tool: Gradle 8.10
  - ORM: MyBatis Plus 3.5.3
  - Database: PostgreSQL 15
  - Logging: Logback 1.4.11 with Lombok SLF4J
  - Testing: JUnit 5.10.0, Testcontainers 1.19.0
  - Java: 17 (LTS, stable for Spring Boot 3.4.8)
- **Deliverables**: Runnable Spring Boot application connected to PostgreSQL, outputting JSON logs, committed to GitHub.

## 2. Task Breakdown and Time Allocation
Total 30 hours, divided into the following tasks with detailed steps and estimated times.

### Task 1: Initialize Spring Boot Project and Gradle Configuration (10 hours)
- **Objective**: Create a Spring Boot project with Gradle, configure dependencies, and ensure it runs.
- **Steps**:
  1. Use Spring Initializr (https://start.spring.io) to create a project:
     - Select Gradle (Kotlin DSL), Spring Boot 3.4.8, Java 17.
     - Add dependencies: `spring-boot-starter-web`, `mybatis-plus-boot-starter`, `postgresql`, `logback-classic`, `logstash-logback-encoder`, `lombok`, `junit-jupiter`, `testcontainers-postgresql`.
  2. Configure `build.gradle.kts` with dependencies and Java 17.
  3. Set up `application.yml` for basic properties (port, context path).
  4. Implement a health check endpoint (`/health`) with `@Slf4j` for logging.
  5. Run the project to verify startup.
- **Time Allocation**:
  - 4 hours: Learn Gradle basics (https://docs.gradle.org/current/userguide/userguide.html).
  - 3 hours: Create project, configure `build.gradle.kts`.
  - 2 hours: Set up `application.yml`, implement health check.
  - 1 hour: Run and verify project.
- **Code Example**:
  ```kotlin
  // build.gradle.kts
  plugins {
      id("org.springframework.boot") version "3.4.8"
      id("java")
  }

  java {
      sourceCompatibility = JavaVersion.VERSION_17
      targetCompatibility = JavaVersion.VERSION_17
  }

  repositories {
      mavenCentral()
  }

  dependencies {
      implementation("org.springframework.boot:spring-boot-starter-web")
      implementation("com.baomidou:mybatis-plus-boot-starter:3.5.3")
      implementation("org.postgresql:postgresql:42.7.3")
      implementation("ch.qos.logback:logback-classic:1.4.11")
      implementation("net.logstash.logback:logstash-logback-encoder:8.0")
      implementation("org.projectlombok:lombok:1.18.34")
      annotationProcessor("org.projectlombok:lombok:1.18.34")
      testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
      testImplementation("org.testcontainers:postgresql:1.19.0")
  }

  tasks.test {
      useJUnitPlatform()
  }
  ```
  ```yaml
  # application.yml
  server:
    port: 8080
  spring:
    application:
      name: crypto-trading
    datasource:
      url: jdbc:postgresql://localhost:5432/trading
      username: postgres
      password: password
  mybatis-plus:
    mapper-locations: classpath*:mapper/*.xml
  logging:
    pattern:
      console: "%d{yyyy-MM-dd HH:mm:ss} %-5level %logger{36} - %msg%n"
  ```
  ```java
  // com.example.controller.HealthController
  import lombok.extern.slf4j.Slf4j;
  import org.springframework.web.bind.annotation.GetMapping;
  import org.springframework.web.bind.annotation.RestController;

  @Slf4j
  @RestController
  public class HealthController {
      @GetMapping("/health")
      public String healthCheck() {
          log.info("Health check executed, status: OK");
          return "OK";
      }
  }
  ```

### Task 2: Configure PostgreSQL and MyBatis Plus (8 hours)
- **Objective**: Install PostgreSQL, configure MyBatis Plus, and establish database connectivity.
- **Steps**:
  1. Install PostgreSQL 15 locally or via Docker.
  2. Create `trading` database, set up user and permissions.
  3. Configure MyBatis Plus, create a test Mapper for connectivity.
  4. Create a test table (`health_check`) and implement insert operation.
  5. Use MyBatis Plus `BaseMapper` for basic CRUD.
- **Time Allocation**:
  - 2 hours: Install PostgreSQL, create database.
  - 3 hours: Learn MyBatis Plus basics (https://mybatis.plus).
  - 2 hours: Configure MyBatis Plus, implement Mapper.
  - 1 hour: Test database connectivity and insert.
- **Code Example**:
  ```sql
  -- PostgreSQL initialization
  CREATE DATABASE trading;
  CREATE USER postgres WITH PASSWORD 'password';
  GRANT ALL PRIVILEGES ON DATABASE trading TO postgres;
  ```
  ```java
  // com.example.model.HealthCheck
  import lombok.Getter;
  import lombok.Setter;
  import java.time.LocalDateTime;

  @Getter
  @Setter
  public class HealthCheck {
      private Long id;
      private String status;
      private LocalDateTime timestamp;
  }
  ```
  ```java
  // com.example.mapper.HealthCheckMapper
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import org.apache.ibatis.annotations.Mapper;

  @Mapper
  public interface HealthCheckMapper extends BaseMapper<HealthCheck> {}
  ```
  ```xml
  <!-- resources/mapper/HealthCheckMapper.xml -->
  <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  <mapper namespace="com.example.mapper.HealthCheckMapper">
      <insert id="insertBatch" parameterType="java.util.List">
          INSERT INTO health_check (status, timestamp)
          VALUES
          <foreach collection="list" item="item" separator=",">
              (#{item.status}, #{item.timestamp})
          </foreach>
      </insert>
  </mapper>
  ```
  ```sql
  -- PostgreSQL table
  CREATE TABLE health_check (
      id SERIAL PRIMARY KEY,
      status VARCHAR(50),
      timestamp TIMESTAMP
  );
  ```
  ```java
  // com.example.service.HealthCheckService
  import lombok.extern.slf4j.Slf4j;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.stereotype.Service;
  import java.time.LocalDateTime;

  @Slf4j
  @Service
  public class HealthCheckService {
      @Autowired
      private HealthCheckMapper healthCheckMapper;

      public void saveHealthCheck(String status) {
          HealthCheck check = new HealthCheck();
          check.setStatus(status);
          check.setTimestamp(LocalDateTime.now());
          healthCheckMapper.insert(check);
          log.info("Saved health check with status: {}", status);
      }
  }
  ```

### Task 3: Configure Logback with Lombok SLF4J for JSON Logging (6 hours)
- **Objective**: Configure Logback to output JSON-structured logs using Lombok's `@Slf4j`.
- **Steps**:
  1. Add `logstash-logback-encoder` dependency for JSON logging.
  2. Create `logback-spring.xml` for JSON output to console.
  3. Use `@Slf4j` in `HealthController` and `HealthCheckService` for logging.
  4. Test JSON log output.
- **Time Allocation**:
  - 2 hours: Learn Logback and `LogstashEncoder` (https://github.com/logstash/logstash-logback-encoder).
  - 2 hours: Configure `logback-spring.xml`.
  - 1 hour: Implement `@Slf4j` logging.
  - 1 hour: Test JSON log output.
- **Code Example**:
  ```xml
  <!-- resources/logback-spring.xml -->
  <configuration>
      <appender name="CONSOLE_JSON" class="ch.qos.logback.core.ConsoleAppender">
          <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>
      </appender>
      <root level="INFO">
          <appender-ref ref="CONSOLE_JSON"/>
      </root>
  </configuration>
  ```

### Task 4: Set Up GitHub Version Control (6 hours)
- **Objective**: Initialize a GitHub repository and commit MVP 1 code.
- **Steps**:
  1. Create a GitHub repository (`crypto-trading`).
  2. Configure `.gitignore` to exclude Gradle build files and sensitive configs.
  3. Commit initial code (`build.gradle.kts`, `application.yml`, health check, etc.).
  4. Write a README documenting project goals and MVP 1 deliverables.
- **Time Allocation**:
  - 2 hours: Create repository, configure `.gitignore`.
  - 2 hours: Commit code.
  - 2 hours: Write README.
- **Code Example**:
  ```gitignore
  # .gitignore
  /build/
  *.log
  application.yml
  ```
  ```markdown
  # README.md
  # Crypto Trading System
  A modular AI+TA trading system for cryptocurrencies.

  ## MVP 1: Project Initialization
  - Set up Spring Boot 3.4.8 with Gradle (Java 17).
  - Configured MyBatis Plus and PostgreSQL.
  - Implemented JSON logging with Logback and Lombok SLF4J.
  - Health check endpoint: `/health`.
  ```

## 3. Testing Strategy
- **Unit Tests** (JUnit):
  - Test `/health` endpoint returns "OK" and logs JSON.
  - Test `HealthCheckService` insert logic.
- **Integration Tests** (Testcontainers):
  - Simulate PostgreSQL to verify database connectivity and insert.
- **Code Example**:
  ```java
  // com.example.controller.HealthControllerTest
  import org.junit.jupiter.api.Test;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.boot.test.context.SpringBootTest;
  import org.springframework.boot.test.web.client.TestRestTemplate;
  import static org.junit.jupiter.api.Assertions.assertEquals;

  @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
  class HealthControllerTest {
      @Autowired
      private TestRestTemplate restTemplate;

      @Test
      void testHealthCheck() {
          var response = restTemplate.getForEntity("/health", String.class);
          assertEquals("OK", response.getBody());
      }
  }
  ```
  ```java
  // com.example.service.HealthCheckServiceTest
  import org.junit.jupiter.api.Test;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.boot.test.context.SpringBootTest;
  import org.testcontainers.containers.PostgreSQLContainer;
  import org.testcontainers.junit.jupiter.Container;
  import org.testcontainers.junit.jupiter.Testcontainers;
  import static org.junit.jupiter.api.Assertions.assertNotNull;

  @SpringBootTest
  @Testcontainers
  class HealthCheckServiceTest {
      @Container
      private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
              .withDatabaseName("trading")
              .withUsername("postgres")
              .withPassword("password");

      @DynamicPropertySource
      static void configureProperties(DynamicPropertyRegistry registry) {
          registry.add("spring.datasource.url", postgres::getJdbcUrl);
          registry.add("spring.datasource.username", postgres::getUsername);
          registry.add("spring.datasource.password", postgres::getPassword);
      }

      @Autowired
      private HealthCheckMapper healthCheckMapper;

      @Test
      void testSaveHealthCheck() {
          HealthCheck check = new HealthCheck();
          check.setStatus("TEST");
          check.setTimestamp(LocalDateTime.now());
          healthCheckMapper.insert(check);
          assertNotNull(check.getId());
      }
  }
  ```

## 4. Performance and Stability Goals
- **Health Check Latency**: <100ms (REST API).
- **Database Connection**: Connect to PostgreSQL, single insert <50ms.
- **Log Output**: JSON logs correctly output to console with timestamp and message.

## 5. Risks and Mitigation
- **Risks**:
  - Gradle dependency conflicts.
  - MyBatis Plus configuration issues.
  - Lombok annotation processing issues in IntelliJ IDEA.
  - PostgreSQL local installation failures.
- **Mitigation**:
  - Allocate 2 hours for Gradle debugging (`gradle dependencies`).
  - Refer to MyBatis Plus quick start (https://mybatis.plus), use `BaseMapper`.
  - Install IntelliJ IDEA Lombok plugin, enable annotation processing.
  - Use Docker for PostgreSQL if local setup fails (`docker run -p 5432:5432 postgres:15`).

## 6. Next Steps
- **Complete MVP 1**: Ensure application runs, health endpoint works, database connects, logs output correctly.
- **Prepare for MVP 2**: Study CCXT Java documentation for K-line data acquisition.
- **Progress Tracking**: Commit code to GitHub by month-end, update README, log issues.