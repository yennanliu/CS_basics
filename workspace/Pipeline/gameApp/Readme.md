# GameApp (Online FPS game)


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

# activity
CREATE TABLE IF NOT EXISTS activity (
    activity_id serial PRIMARY KEY,
    user_id VARCHAR(30),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    activity_type VARCHAR(10)
)

```

## Part 2) ETL
- Batch
- Stream

## Part 3) SQL
```sql
# get DAU (day acitve user)
SELECT date(start_time) AS date,
       COUNT(DISTINCT user_id) AS dau
FROM activity
WHERE activity_type = 'login'
GROUP BY date
ORDER BY date

# get MAU (month acitve user)
SELECT month(start_time) AS MONTH,
       COUNT(DISTINCT user_id) AS mau
FROM activity
WHERE activity_type = 'login'
GROUP BY MONTH
ORDER BY MONTH

# get ARPU （Average Revenue Per User)
WITH dau AS
  (SELECT date(start_time) AS date,
          COUNT(DISTINCT user_id) AS dau
   FROM activity
   WHERE activity_type = 'login'
   GROUP BY date),
     day_pay AS
  (SELECT date(start_time) AS date,
          NULLIF(SUM(amount), 0) AS amount
   FROM TRANSACTION
   GROUP BY date)
SELECT d.date,
       ROUND(CASE
                 WHEN d.dau = 0 THEN 0
                 ELSE p.amount / d.dau
             END) AS arpu
FROM dau d
LEFT JOIN day_pay p ON d.date = p.date
ORDER BY d.date

# get ARPPU (Average Revenue Per Paid User)

# get cohort user

```

## Part 4) Prod Sense & DashBoard


## Ref
- https://jaxenter.com/tag/data-modelling
- https://vertabelo.com/blog/a-database-model-for-action-games/
- https://www.databasestar.com/sample-database-video-games/
- https://github.com/jgoodman/MySQL-RPG-Schema