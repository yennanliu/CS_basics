# GameApp
- Online FPS game
- Raw event (json) from stream source
- Raw event sample as below:
```json
{"timetamp":1629857927,"user_id":"u0001","event_type":"login","platform":"mobile","os":"ios","version":"ios-11"}
{"timetamp":1629857927,"user_id":"u0001","transaction_id":"t001","event_type":"payment","platform":"mobile","os":"ios","version":"ios-11"}
...

```

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
WITH day_pay_dau AS
  (SELECT date(a.start_time) AS date,
          COUNT(DISTINCT a.user_id) AS dau
   FROM activity a
   INNER JOIN transactions t
   ON a.user_id = t.user_id
   AND date(a.start_time) = date(t.created_on)
   WHERE a.activity_type = 'login'
   GROUP BY a.date),
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
FROM day_pay_dau d
LEFT JOIN day_pay p ON d.date = p.date
ORDER BY d.date

# get retention rate
# https://chartio.com/learn/saas-metrics/how-to-perform-a-cohort-analysis-to-track-customer-retention-rate/
WITH "USER_FIRST_EVENT" as (
SELECT "Activity"."user_id" AS "User_Id",
DATE_TRUNC('day', MIN("Activity"."created_date"))::DATE AS "First_Event_Date"
FROM "public"."activity" AS "Activity"
GROUP BY "Activity"."user_id"),

"EVENT" as (SELECT "Activity"."user_id" AS "User_Id",
DATE_TRUNC('day', "Activity"."created_date")::DATE AS "User_Event_Date"
FROM "public"."activity" AS "Activity")

select "USER_FIRST_EVENT"."User_Id",
"USER_FIRST_EVENT"."First_Event_Date",
"EVENT"."User_Event_Date",
"EVENT"."User_Event_Date"- "USER_FIRST_EVENT"."First_Event_Date" as "Days_Since_Signup",
("EVENT"."User_Event_Date"- "USER_FIRST_EVENT"."First_Event_Date")/7 as "Weeks_Since_Signup"

FROM "USER_FIRST_EVENT"
JOIN "EVENT" on "USER_FIRST_EVENT"."User_Id" = "EVENT"."User_Id"
```

## Part 4) Prod Sense & DashBoard


## Ref
- https://jaxenter.com/tag/data-modelling
- https://vertabelo.com/blog/a-database-model-for-action-games/
- https://www.databasestar.com/sample-database-video-games/
- https://github.com/jgoodman/MySQL-RPG-Schema