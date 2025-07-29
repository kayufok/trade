# Trade - Cryptocurrency Trading System

Auto trading application built with Spring Boot, MyBatis Plus, and PostgreSQL.

## Features

### MVP 1 - Project Foundation ✅
- Spring Boot 3.4.8 with Gradle 8.14.3
- MyBatis Plus 3.0.4 ORM
- PostgreSQL 15 database
- JSON logging with Logback and Lombok SLF4J
- Health check endpoints (`/api/health/app`)
- Unit and integration tests with JUnit 5 and Testcontainers
- Docker containerization ready

### MVP 2 - Data Acquisition Module ✅
- Binance Testnet API integration using OkHttp
- K-line data fetching and validation
- PostgreSQL storage with MyBatis Plus
- REST API endpoints for data operations

## API Endpoints

### Health Check
- `GET /api/health/app` - Application health check

### K-line Data
- `GET /api/kline/fetch/{symbol}` - Fetch K-lines from Binance Testnet
- `POST /api/kline/fetch-and-store/{symbol}` - Fetch and store K-lines
- `GET /api/kline/stored/{symbol}` - Retrieve stored K-lines

## Configuration

### Setup Instructions

1. **Copy the example configuration:**
   ```bash
   cp src/main/resources/application-example.yml src/main/resources/application.yml
   ```

2. **Update the configuration with your credentials:**
   - Replace `your_username` and `your_password` with your PostgreSQL credentials
   - Replace `your_binance_testnet_api_key` and `your_binance_testnet_secret` with your Binance Testnet API keys

3. **Get Binance Testnet API Keys:**
   - Visit: https://testnet.binance.vision/
   - Create an account and generate API keys
   - Use the testnet URL: `https://testnet.binance.vision`

### Database Setup

1. **Create the database:**
   ```sql
   CREATE DATABASE trade;
   ```

2. **Run the DDL script:**
   ```bash
   psql -d trade -f ddl/create_tables.sql
   ```

## Database Schema

The application uses PostgreSQL with the following main tables:
- `kline` - K-line (candlestick) data
- `trade` - Executed trades
- `signal` - Trading signals
- `health_check` - System health records

See `ddl/create_tables.sql` for complete schema.

## Running the Application

```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun

# Run tests
./gradlew test
```

## Development

This project follows a modular MVP approach:
- **MVP 1**: Project foundation and health checks ✅
- **MVP 2**: Data acquisition and storage ✅
- **MVP 3**: Data cleaning and validation (planned)
- **MVP 4**: Technical analysis signals (planned)
- **MVP 5**: Trading execution (planned)