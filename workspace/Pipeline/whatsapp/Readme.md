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
	activity_id VARCHAR(30),
	activity_contents VARCHAR(30),
	activity_extra VARCHAR(50),
	created_on TIMESTAMP,
	CONSTRAINT activity_id FOREIGN KEY activity_id REFERENCES fact_activity (activity_id)
)
```