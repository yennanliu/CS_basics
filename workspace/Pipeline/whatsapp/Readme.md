# WhatsApp

## Part 0) Biz Requirement
- Case 1:
	- Define product success metrics (whatsapp)
	- Case Study:
		- If MAU is down in someday, what may be the reasons ?
			- new app version
			- something in/outside app is wrong
			- check raw data to verify if MAU is readlly down
			- compare with DAU, if DAU is down as well -> user retention dow
			- check competitors
		- How to define new users, churned users, returned users ?
			- new users
				- new register user
				- new install user
			- churn users:
				- A churned user is a user who has stopped using an app. There are two kinds of actions a user takes related to churn: 
					- either lapsing in use (which means no more sessions being recorded)
					- uninstalling the app from the device itself. 
				But essentially, churn rate is the number of users that leave your app in a given period of time.
				- https://www.adjust.com/glossary/churn-definition/
			- returned users
			- how to define a table, for above calculation ? (pct is needed as well)
	- SQL:
		- how to get thses value ? `UID, first_active_date, last_active_date, previous_active_date`
		- how to update this table per daily data partition (distribution) ?
			- https://cloud.google.com/bigquery/docs/using-dml-with-partitioned-tables
			- https://blog.heroku.com/handling-very-large-tables-in-postgres-using-partitioning
			- https://www.enterprisedb.com/postgres-tutorials/how-use-table-partitioning-scale-postgresql
			- https://severalnines.com/database-blog/guide-partitioning-data-postgresql
			- https://docs.microsoft.com/en-us/archive/msdn-magazine/2014/october/sql-server-implement-large-fast-updatable-tables-for-responsive-real-time-reporting

	- Python (ETL)
		```
		join -> transform -> union -> lookup -> insert
		```
		```
		# ETL update steps (if we select FK as constraints, it protects the data Integrity, but lost parallelism)
		# https://qimia.io/en/blog/Data-Warehousing-Guide-The-ETL-Processing

		date dimension -> all other dimensions -> atomic fact tables -> aggregated facts
		```
		- implement above SQL questions via python
		- [An ETL Flow for Dimensional Data Warehouses](https://www.linkedin.com/pulse/narrow-road-star-schema-basic-etl-flow-dimensional-data-vince-donovan/)
		- [setting-up-etl-using-python-simplified](https://hevodata.com/learn/setting-up-etl-using-python-simplified/)
		- https://qimia.io/en/blog/Data-Warehousing-Guide-The-ETL-Processing
		- https://github.com/patelatharva/Data_Warehouse_with_Amazon_Redshift
		- https://docs.aws.amazon.com/redshift/latest/dg/t_updating-inserting-using-staging-tables-.html
		- optimization idea
			- 1) Change Target Table to Un-logged Mode
				- The UNLOGGED mode ensures PostgreSQL is not sending table write operations to the Write Ahead Log (WAL). This can make the load process significantly fast. However, since the operations are not logged, data cannot be recovered if there is a crash or unclean server shutdown during the load. PostgreSQL will automatically truncate any unlogged table once it restarts.
				- https://www.2ndquadrant.com/en/blog/7-best-practice-tips-for-postgresql-bulk-data-loading/
			- 2) Drop and Recreate Indexes
- Case 2:
	- Case Study:
		- Define product success metrics (consider a photo-upload app)
		- how to evaluate if product is successful ?
	- Python (ETL)
		- write a stream etl process the data for above metric calculation
		- optimization idea
			- read very large file
				- [ref1](https://stackoverflow.com/questions/6475328/how-can-i-read-large-text-files-in-python-line-by-line-without-loading-it-into/6475407)
				- [ref2](https://gist.github.com/iyvinjose/e6c1cb2821abd5f01fd1b9065cbc759d)
				- [Processing large files using python](https://www.blopig.com/blog/2016/08/processing-large-files-using-python/)
				- [Python Read Big File Example](https://www.code-learner.com/python-read-big-file-example/)
				- [file io sample code](https://github.com/yennanliu/utility_Python/tree/master/file_io)

- Case 3:
	- Prod sense
		- DAU, MAU ...
		- engagement
	- Data modeling
		- fact table
			- what's the grain of fact table ?
			- what's the fact we are looking at ?
			- what's the process measuring ?
			- better to select `atomic data` as the fact table data
				- [atomic-data-types](https://docs.oracle.com/en/database/other-databases/nosql-database/21.1/sqlreferencefornosql/atomic-data-types.html)
			- `AS LOW LEVEL ATOMIC DATA` as possible -> biz questions can be in various forms
		- dimension table
			- who, what, where, when, why, how
			- how do biz ppl describe the data resulting from the biz process measurement events ?


## Part 1) Case 1
- SQL
```sql
# Write SQL get new users, churned users, returned users 

# new users
SELECT DATE(created_on) AS created_date,
       COUNT(DISTINCT user_id) AS new_user
FROM USER
GROUP BY created_date
ORDER BY created_date

# churned, returned users
# http://blog.forcerank.it/sql-for-calculating-churn-retention-reengagement
WITH month_usage AS
  (SELECT user_id,
          datediff(MONTH, '1970-01-01', created_on) AS time_period, 
          min(created_on) AS first_day,
          max(created_on) AS last_day
   FROM activity_detail
   WHERE activity_type = 'login'
   GROUP BY user_id,
            time_period),
     lag_lead AS
  (SELECT user_id,
          time_period,
          first_day,
          last_day,
          lag(time_period, 1) OVER (PARTITION BY user_id
                                    ORDER BY user_id,
                                             time_period) AS lag_time_period,
          lead(time_period, 1) OVER (PARTITION BY user_id
                                    ORDER BY user_id,
                                             time_period) AS lead_time_period
          FROM month_usage),
     calculated AS
  (SELECT CASE
              WHEN lag_time_period IS NULL THEN 'NEW'
              WHEN lead_time_period - last_day = 1 THEN 'ACTIVE'
              WHEN time_period - lag_time_period > 2 THEN 'Return'
          END AS this_month_value,
          CASE
              WHEN lead_time_period - last_day > 1
                   OR lead_time_period - last_day IS NULL THEN 'CHURN'
          END AS next_month_churn)
  
SELECT * FROM calculated
```

## Part 2) Case 2

## Part 3) Case 3
- Data Modeling
	- Target : Tracking user metrics (DAU, MAU, user activity)
```
# why, who, when, where, how
   
                    user   platform
                      ↑     ↑
                      ↑     ↑
       time_dim ← ← fact_activity → → location
                      ↓     ↓
                      ↓     ↓
         activity_detail   promotion

```
- ddl
```sql

# fact_activity
CREATE TABLE IF NOT EXISTS fact_activity(
	activity_id VARCHAR(30),
	user_id VARCHAR(30),
	created_on TIMESTAMP,
	activity VARCHAR(10),
	platform_id VARCHAR(30),
	location_id VARCHAR(10),
	promotion_id VARCHAR(30),
	CONSTRAINT created_on FOREIGN KEY (created_on) REFERENCES time_dim (created_on)
)

# time_dim
CREATE TABLE IF NOT EXISTS time_dim(
	created_on TIMESTAMP,
	year Int,
	month Int,
	day Int,
	created_on_unix_time Int,
	CONSTRAINT created_on FOREIGN KEY (created_on) REFERENCES fact_activity (created_on)
)

# user
CREATE TABLE IF NOT EXISTS user(
	user_id VARCHAR(30),
	last_name VARCHAR(10),
	first_name VARCHAR(10),
	address VARCHAR(50),
	gender CHAR(1),
	register_on TIMESTAMP,
	created_on TIMESTAMP,
	last_login TIMESTAMP,
	validated BOOLEAN,
	CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES fact_activity (user_id)
)

# platform
CREATE TABLE IF NOT EXISTS platform(
	platform_id VARCHAR(30),
	platform_name VARCHAR(10),
	model VARCHAR(10),
	os VARCHAR(10),
	CONSTRAINT platform_id FOREIGN KEY platform_id REFERENCES fact_activity (platform_id)
)

# location
CREATE TABLE IF NOT EXISTS location(
	location_id VARCHAR(30),
	city VARCHAR(30),
	country VARCHAR(10),
	CONSTRAINT location_id FOREIGN KEY location_id REFERENCES fact_activity (location_id)
)

# promotion
CREATE TABLE IF NOT EXISTS promotion(
	promotion_id VARCHAR(30),
	promotion_name VARCHAR(30),
	start_time TIMESTAMP,
	end_time TIMESTAMP,
	CONSTRAINT promotion_id FOREIGN KEY promotion_id REFERENCES fact_activity (promotion_id)
)

# activity_detail
CREATE TABLE IF NOT EXISTS activity_detail(
	user_id VARCHAR(30),
	activity_id VARCHAR(30),
	activity_contents VARCHAR(30),
	activity_extra VARCHAR(50),
	activity_type VARCHAR(10),
	created_on TIMESTAMP,
	CONSTRAINT activity_id FOREIGN KEY activity_id REFERENCES fact_activity (activity_id)
)
```