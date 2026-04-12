.PHONY: help up down logs ps clean

help:
@echo make up - Start Postgres + Redis
@echo make down - Stop all containers
@echo make ps - Show container status
@echo make clean - Stop and delete volumes
@echo make db-shell - Open psql shell
@echo make redis-cli - Open Redis CLI

up:
docker compose up -d postgres redis

down:
docker compose down

logs:
docker compose logs -f

ps:
docker compose ps

clean:
docker compose down -v

db-shell:
docker compose exec postgres psql -U emergency_user -d emergency_db

redis-cli:
docker compose exec redis redis-cli
