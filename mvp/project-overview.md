# Cryptocurrency Trading System Project Overview

## 1. Project Overview
- **Objective**: Build a modular AI+TA cryptocurrency trading system supporting data acquisition, signal generation, trade execution, risk management, backtesting, and monitoring. Initial MVP validates core functionality; later phases expand to multi-market support and SaaS commercialization.
- **Developer Background**: 5 years of Java Spring Boot experience, proficient in large-scale financial systems (low-latency, high-concurrency), skilled in Oracle SQL and PL/SQL, independent developer.
- **Development Timeline**: 12 months, 30 hours/month (1 hour/day).
- **Project Structure**:
  - **MVP 1-7**: Single Spring Boot project with package-based modularity.
  - **MVP 8-12**: Refactor into Gradle multi-module structure, with inter-module communication via RabbitMQ or REST APIs.
- **Deployment**: Local Docker (initial MVP), AWS EC2 free tier (MVP 12).

## 1.1. Current Project Status (MVP 1 Complete)
- **✅ COMPLETED**: MVP 1 - Project Foundation (95% complete)
  - Spring Boot 3.4.8 with Gradle 8.14.3
  - MyBatis Plus 3.0.4 ORM configured
  - PostgreSQL 15 database setup
  - JSON logging with Logback and Lombok SLF4J
  - Health check endpoints (`/api/health/app`)
  - Unit and integration tests with JUnit 5 and Testcontainers
  - GitHub repository with proper version control
  - Docker containerization ready
- **🔄 NEXT**: MVP 2 - Data Acquisition Module
  - CCXT Java integration for Binance Testnet
  - K-line data fetching and storage
  - Data cleaning and validation
- **📊 PROGRESS**: 8.3% complete (1/12 MVPs finished)

## 2. Technology Stack
| **Category** | **Technology** | **Status** | **Description** |
|--------------|----------------|------------|-----------------|
| **Framework** | Spring Boot 3.4.8 | ✅ **COMPLETED** | Core backend framework, supports REST APIs and async processing (@Async). |
| **ORM** | MyBatis Plus 3.0.4 | ✅ **COMPLETED** | Dynamic SQL, aligns with Oracle SQL experience, supports batch operations. |
| **Build Tool** | Gradle 8.14.3 | ✅ **COMPLETED** | Replaces Maven, concise configuration, supports multi-module refactoring. |
| **Database** | PostgreSQL 15 | ✅ **COMPLETED** | Structured data (trade records, logs), similar to Oracle transaction management. |
| | InfluxDB 2.7 | 🔄 **PLANNED** | Time-series data (K-lines), introduced in MVP 10. |
| **API Integration** | CCXT Java | 🔄 **PLANNED** | Exchange APIs (e.g., Binance Testnet). |
| | OkHttp 4.12.0 | 🔄 **PLANNED** | Efficient HTTP requests. |
| **AI/TA** | DeepLearning4J 1.0.0 | 🔄 **PLANNED** | LSTM model for price prediction, introduced in MVP 9. |
| | TA-Lib Java | 🔄 **PLANNED** | Technical indicators (MA, RSI), implemented in MVP 4. |
| **Monitoring** | Logback 1.4.11 + LogstashEncoder 8.0 | ✅ **COMPLETED** | JSON-structured logging with Lombok SLF4J, implemented in MVP 1. |
| | Micrometer + Prometheus + Grafana | 🔄 **PLANNED** | Real-time monitoring, introduced in MVP 11. |
| **Message Queue** | RabbitMQ 3.13 | 🔄 **PLANNED** | Inter-module communication, implemented in MVP 12. |
| **Deployment** | Docker 27.0 | ✅ **COMPLETED** | Containerized application, deployed in MVP 12. |
| | AWS EC2 | 🔄 **PLANNED** | Free tier deployment, MVP 12. |
| **Testing** | JUnit 5.10.0 | ✅ **COMPLETED** | Unit testing, >80% coverage. |
| | Testcontainers 1.19.0 | ✅ **COMPLETED** | Simulate PostgreSQL and InfluxDB. |

## 3. System Architecture
- **Modules**:
  1. **Data Acquisition & Cleaning**: Fetch K-line data from Binance Testnet, clean and store in PostgreSQL (MVP 1-3), later InfluxDB (MVP 10).
  2. **Signal System**: TA strategies (MA crossover, MVP 4), AI strategies (LSTM, MVP 9).
  3. **Trading System**: Execute limit orders, log trades (MVP 5).
  4. **Risk Management**: 5% stop-loss, position limits (MVP 6).
  5. **Backtesting**: Simulate trades, calculate returns and drawdown (MVP 7).
  6. **Monitoring & Logging**: JSON logs (MVP 1), Telegram alerts (MVP 11).
  7. **Security**: API key encryption, IP restrictions (MVP 11).
- **Interaction**:
  - **MVP 1-7**: Single project, modules interact via Spring Services, organized in packages (e.g., `com.example.datafetch`).
  - **MVP 8-12**: Gradle multi-module, inter-module communication via RabbitMQ or REST APIs.
- **Database**:
  - PostgreSQL: Structured data (K-lines, trades, logs) using MyBatis Plus.
  - InfluxDB: Time-series K-lines (MVP 10) for efficient queries.
- **Reactive (Optional)**: Introduce Java Reactor in MVP 10-12 for high-concurrency scenarios (e.g., high-frequency K-line streams) using Spring WebFlux or R2DBC.

## 4. Module Functions and Implementation
| **Module** | **MVP** | **Status** | **Function** | **Implementation** |
|------------|---------|------------|--------------|--------------------|
| **Project Foundation** | MVP 1 | ✅ **COMPLETED** | Spring Boot setup, health checks, JSON logging. | Spring Boot 3.4.8, MyBatis Plus, Logback JSON, JUnit tests. |
| **Data Acquisition** | MVP 2-3 | 🔄 **PLANNED** | Fetch Binance Testnet K-lines, clean missing (forward fill) and outlier (Z-score) data. | CCXT Java, MyBatis Plus batch insert, Spring Batch for cleaning. |
| **Signal System** | MVP 4, 9 | 🔄 **PLANNED** | MA crossover strategy (MVP 4), LSTM price prediction (MVP 9). | TA-Lib Java, DeepLearning4J, MyBatis Plus for signal storage. |
| **Trading System** | MVP 5 | 🔄 **PLANNED** | Execute limit orders, log trades. | CCXT Java, Spring Retry, MyBatis Plus. |
| **Risk Management** | MVP 6 | 🔄 **PLANNED** | 5% stop-loss, 5% position limit. | Spring Service, MyBatis Plus for rule storage. |
| **Backtesting** | MVP 7 | 🔄 **PLANNED** | Simulate MA strategy, calculate returns and drawdown. | Spring Service, MyBatis Plus for results. |
| **Monitoring & Logging** | MVP 1, 11 | ✅ **PARTIAL** | JSON logs (MVP 1 ✅), Telegram alerts (MVP 11 🔄). | Logback with Lombok SLF4J ✅, OkHttp, Micrometer+Prometheus 🔄. |
| **Security** | MVP 11 | 🔄 **PLANNED** | API key encryption, IP restrictions. | Spring Security, MyBatis Plus for anomaly logging. |
| **Deployment** | MVP 12 | 🔄 **PLANNED** | Containerized deployment, module communication. | Docker ✅, RabbitMQ, AWS EC2 🔄. |

## 5. Database Design
- **PostgreSQL** (MVP 1-9):
  ```sql
  CREATE TABLE kline (
      id SERIAL PRIMARY KEY,
      symbol VARCHAR(20),
      timestamp BIGINT,
      open DOUBLE PRECISION,
      high DOUBLE PRECISION,
      low DOUBLE PRECISION,
      close DOUBLE PRECISION,
      volume DOUBLE PRECISION,
      INDEX idx_timestamp (timestamp)
  );
  CREATE TABLE trade (
      id SERIAL PRIMARY KEY,
      symbol VARCHAR(20),
      timestamp BIGINT,
      price DOUBLE PRECISION,
      quantity DOUBLE PRECISION,
      type VARCHAR(10),
      INDEX idx_timestamp (timestamp)
  );
  CREATE TABLE signal (
      id SERIAL PRIMARY KEY,
      symbol VARCHAR(20),
      timestamp BIGINT,
      type VARCHAR(10), -- BUY/SELL
      INDEX idx_timestamp (timestamp)
  );
  ```
  - **Operations**: MyBatis Plus `BaseMapper` for CRUD, batch inserts via `insertBatch`.
- **InfluxDB** (MVP 10):
  - **Structure**: Measurement `kline`, tags (`symbol`), fields (`open`, `high`, `low`, `close`, `volume`).
  - **Operations**: Use `influxdb-java` client, optionally `influxdb-client-reactive` (Java Reactor) in MVP 12.

## 6. Performance and Latency Goals
- **K-line Query**: <50ms (PostgreSQL/InfluxDB).
- **Trade Execution**: <100ms (Binance Testnet).
- **Backtesting Speed**: 1M trades <10 minutes.
- **Concurrency**: MVP 1-7 single-machine, MVP 8-12 supports 1000 K-lines/second (with Reactor if needed).

## 7. Development and Testing Strategy
- **Development**:
  - **MVP 1**: ✅ **COMPLETED** - Single project, package-based structure using MyBatis Plus + JDBC.
  - **MVP 2-7**: 🔄 **PLANNED** - Continue single project structure.
  - **MVP 8**: 🔄 **PLANNED** - Refactor into Gradle multi-module, inter-module via Spring Services.
  - **MVP 10-12**: 🔄 **PLANNED** - Introduce Java Reactor (Spring WebFlux, R2DBC, or `influxdb-client-reactive`) for high concurrency if needed.
- **Testing**:
  - ✅ **COMPLETED**: JUnit unit tests with >80% coverage.
  - ✅ **COMPLETED**: Testcontainers for PostgreSQL simulation.
  - 🔄 **PLANNED**: Binance Testnet validation for trade execution.
- **Tools**:
  - ✅ **COMPLETED**: IntelliJ IDEA with Gradle and MyBatis Plus support.
  - ✅ **COMPLETED**: GitHub version control with proper .gitignore.
  - ✅ **COMPLETED**: Docker Desktop for local containerized testing.

## 8. Risks and Mitigation
- **Risks**: 
  - ✅ **RESOLVED**: MyBatis Plus learning curve (successfully implemented in MVP 1).
  - ✅ **RESOLVED**: Gradle configuration issues (successfully configured in MVP 1).
  - 🔄 **PLANNED**: Potential Reactor adoption delays (deferred until MVP 10).
- **Mitigation**:
  - ✅ **COMPLETED**: Allocated time in MVP 1 for Gradle and MyBatis Plus learning.
  - ✅ **COMPLETED**: Using stable versions (Spring Boot 3.4.8, MyBatis Plus 3.0.4).
  - 🔄 **PLANNED**: Defer Reactor until MVP 10, only for high-concurrency scenarios (multi-market).

## 9. Next Steps
- **MVP 1**: ✅ **COMPLETED** - Spring Boot project initialized, MyBatis Plus and PostgreSQL configured, JSON logging implemented, health checks working, tests passing.
- **MVP 2**: 🔄 **NEXT** - Implement data acquisition module with CCXT Java for Binance Testnet integration.
- **Learning**: ✅ **COMPLETED** - MyBatis Plus and Gradle successfully learned and implemented.
- **Progress Tracking**: ✅ **COMPLETED** - GitHub repository established with proper version control.