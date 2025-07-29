# PostgreSQL 15 Docker Setup Guide for Ubuntu

This guide will help you set up PostgreSQL 15 in Docker on Ubuntu with data persistence.

## Prerequisites

- Ubuntu 20.04 or later
- Docker installed and running
- Docker Compose
- Docker network "nginx-net" already created

## Run PostgreSQL 15 Container

Create a `docker-compose.yml` file:

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15
    container_name: postgres-trade
    restart: unless-stopped
    environment:
      POSTGRES_DB: trade
      POSTGRES_USER: trade_user
      POSTGRES_PASSWORD: your_secure_password
    ports:
      - "5432:5432"
    volumes:
      - /home/database/trade:/var/lib/postgresql/data
    networks:
      - nginx-net
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U trade_user -d trade"]
      interval: 30s
      timeout: 10s
      retries: 3

networks:
  nginx-net:
    external: true
```

Then run:
```bash
docker-compose up -d
```

### Rebuild and restart the container:
```bash
# Rebuild the container (useful after docker-compose.yml changes)
docker-compose down
docker-compose build --no-cache
docker-compose up -d

# Or in one command
docker-compose up -d --build --force-recreate
```

## Step 1: Verify Installation

```bash
# Check if container is running
docker ps

# Connect to PostgreSQL
docker exec -it postgres-trade psql -U trade_user -d trade

# Or connect from host (if you have psql client installed)
psql -h localhost -p 5432 -U trade_user -d trade
```

## Step 2: Basic PostgreSQL Commands

Once connected to PostgreSQL:

```sql
-- List all databases
\l

-- List all tables in current database
\dt

-- Show current user
SELECT current_user;

-- Show current database
SELECT current_database();

-- Exit PostgreSQL
\q
```

## Step 3: Connection Details

- **Host**: localhost
- **Port**: 5432
- **Database**: trade
- **Username**: trade_user
- **Password**: your_secure_password (replace with your actual password)

## Step 4: Useful Docker Commands

```bash
# Stop the container
docker stop postgres-trade

# Start the container
docker start postgres-trade

# Restart the container
docker restart postgres-trade

# View container logs
docker logs postgres-trade

# Remove container (WARNING: This will delete the container but keep the data)
docker rm postgres-trade

# Remove container and data volume (WARNING: This will delete everything)
docker rm -v postgres-trade
```

## Step 5: Backup and Restore

### Create a backup:
```bash
docker exec postgres-trade pg_dump -U trade_user trade > backup.sql
```

### Restore from backup:
```bash
docker exec -i postgres-trade psql -U trade_user trade < backup.sql
```

## Step 6: Log Management and Purging

### View PostgreSQL logs:
```bash
# View container logs
docker logs postgres-trade

# Follow logs in real-time
docker logs -f postgres-trade

# View last 100 lines
docker logs --tail 100 postgres-trade
```

### Purge PostgreSQL logs:
```bash
# Clear container logs
docker logs --since 0 postgres-trade > /dev/null
docker container logs --truncate 0 postgres-trade

# Or restart container to clear logs
docker restart postgres-trade
```

### Configure log rotation in docker-compose.yml:
```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15
    container_name: postgres-trade
    restart: unless-stopped
    environment:
      POSTGRES_DB: trade
      POSTGRES_USER: trade_user
      POSTGRES_PASSWORD: your_secure_password
    ports:
      - "5432:5432"
    volumes:
      - /home/database/trade:/var/lib/postgresql/data
    networks:
      - nginx-net
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U trade_user -d trade"]
      interval: 30s
      timeout: 10s
      retries: 3
    logging:
      driver: "json-file"
      options:
        max-size: "10m"
        max-file: "3"

networks:
  nginx-net:
    external: true
```

### Manual log cleanup script:
Create a script `cleanup-logs.sh`:
```bash
#!/bin/bash

# Stop the container
docker stop postgres-trade

# Clear logs
docker container logs --truncate 0 postgres-trade

# Start the container
docker start postgres-trade

echo "PostgreSQL logs have been purged"
```

Make it executable:
```bash
chmod +x cleanup-logs.sh
```

### Automated log rotation with cron:
Add to crontab for daily log cleanup:
```bash
# Edit crontab
crontab -e

# Add this line for daily cleanup at 2 AM
0 2 * * * docker container logs --truncate 0 postgres-trade
```

## Security Considerations

1. **Change the default password** - Always use a strong password
2. **Limit network access** - Only expose the port if needed externally
3. **Use environment variables** - Store sensitive data in environment files
4. **Regular backups** - Set up automated backup procedures
5. **Update regularly** - Keep PostgreSQL and Docker images updated

## Troubleshooting

### Container won't start:
```bash
# Check logs
docker logs postgres-trade

# Check if port is already in use
sudo netstat -tlnp | grep 5432
```

### Permission issues:
```bash
# Fix data directory permissions
sudo chown -R 999:999 /home/database/trade
```

### Connection refused:
- Ensure the container is running: `docker ps`
- Check if the port is exposed: `docker port postgres-trade`
- Verify firewall settings

## Next Steps

1. Configure your application to connect to the PostgreSQL instance
2. Set up database migrations
3. Configure connection pooling if needed
4. Set up monitoring and logging
5. Implement backup strategies

## Environment Variables Reference

| Variable | Description | Default |
|----------|-------------|---------|
| POSTGRES_DB | Database name | postgres |
| POSTGRES_USER | Username | postgres |
| POSTGRES_PASSWORD | Password | (required) |
| POSTGRES_INITDB_ARGS | Additional initdb arguments | - |
| PGDATA | Data directory | /var/lib/postgresql/data |

## Notes

- The data directory `/home/database/trade` will persist your data even if the container is removed
- PostgreSQL 15 is the latest stable version as of this guide
- Consider using Docker Compose for easier management of multiple services
- Always backup your data before making major changes
