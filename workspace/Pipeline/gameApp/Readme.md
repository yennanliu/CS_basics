# GameApp (An online FPS game)


## Part 1) Data Model Design
```sql

# account 
CREATE TABLE account IF NOT EXISTS account (
    account_id serial PRIMARY KEY,
    user_id VARCHAR(30),
    balance numeric,
    points int
)

# user
CREATE TABLE IF NOT EXISTS user (
    user_id serial PRIMARY KEY,
    username VARCHAR(30) NOT NULL,
    email VARCHAR(30),
    created_on TIMESTAMP NOT NULL,
    last_login TIMESTAMP,
    validated BOOLEAN
)

# game
CREATE TABLE IF NOT EXISTS game (
    game_id serial PRIMARY KEY,
    game_user_id VARCHAR(30) NOT NULL,
    created_on TIMESTAMP NOT NULL,
    end_on TIMESTAMP,
    location VARCHAR(10),
    status VARCHAR(10),
)

# game_user
CREATE TABLE IF NOT EXISTS game_user (
    game_user_id serial PRIMARY KEY,
    game_id VARCHAR(30) NOT NULL,
    user_id VARCHAR(30) NOT NULL
)

# transaction
CREATE TABLE IF NOT EXISTS transaction (
    transaction_id serial PRIMARY KEY,
    user_id VARCHAR(30),
    created_on TIMESTAMP NOT NULL,
    type int,
    amount numeric,
    payment VARCHAR(10),
    in_game BOOLEAN
)

```

## Part 2) ETL

## Part 3) SQL

## Part 4) Prod Sense & DashBoard


## Ref
- https://jaxenter.com/tag/data-modelling
- https://vertabelo.com/blog/a-database-model-for-action-games/
- https://www.databasestar.com/sample-database-video-games/
- https://github.com/jgoodman/MySQL-RPG-Schema