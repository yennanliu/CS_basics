# Note for Database

## 1) DB type

- RDBMS (SQL) (Relational Database Management System)
	- MySQL 
		- ACID Compliance : Some versions are compliant
		- SQL Compliance : Some versions are compliant
		- Widely chosen for web based projects that need a database 
		  simply for straightforward data transactions. Though, for MySQL to underperform when strained by a heavy loads or when 
		  attempting to complete complex queries.
		- MySQL performs well in OLAP/OLTP systems when read speeds are required.
		- MySQL + InnoDB provides very good read/write speeds for OLTP 
		  scenarios. Overall, MySQL performs well with high concurrency scenarios.
		- MySQL is reliable and works well with Business Intelligence 
		  applications, as business intelligence applications are typically 
		  read-heavy.
		- MySQL has JSON data type support but no other NoSQL feature. It does 
		  not support indexing for JSON
		- Supports temporary tables but does not support materialized views.

	- PostgreSQL
		- ACID Compliance : Complete ACID Compliance
		- SQL Compliance  : Almost fully compliant
		- Widely used in large systems where read and write speeds are crucial 
		  and data needs to validated. In addition, it supports a variety of performance optimizations that are available only in commercial solutions such as Geospatial data support, concurrency without read locks, and so on (e.g. Oracle, SQL Server).
		- PostgreSQL performance is utilized best in systems requiring execution 
		  of complex queries.
		- PostgreSQL performs well in OLTP/OLAP systems when read/write speeds 
		  are required and extensive data analysis is needed.
		- PostgreSQL also works well with Business Intelligence applications but 
		  is better suited for Data Warehousing and data analysis applications 
		  that require fast read/write speeds.
		- PostgreSQL supports JSON and other NoSQL features like native XML 
		  support and key-value pairs with HSTORE. It also supports indexing 
		  JSON data for faster access.
		- Supports materialized views and temporary tables.

	- MSSQL/DB2/Oracle/SQLITE...
- No SQL
	- MongoDB
	- Redis
- Others

## 2) DB properties 

- RDBMS 
	- ACID : atomicity, consistency, isolation, and durability
		- Atomicity	
			- Guarantee that either all of the transaction succeeds or none of 
			  it does. You don’t get part of it succeeding and part of it not. If one part of the transaction fails, the whole transaction fails. With atomicity, it’s either “all or nothing”.

		- Consistency
			- This ensures that you guarantee that all data will be consistent. 
			  All data will be valid according to all defined rules, including any constraints, cascades, and triggers that have been applied on the database.
		
		- Isolation
			- Guarantees that all transactions will occur in isolation. No 
			  transaction will be affected by any other transaction. So a 
			  transaction cannot read data from any other transaction that has not yet completed.

		- Durability
			- Once transaction is committed, it will remain in the system – even 
			  if there’s a system crash immediately following the transaction. Any changes from the transaction must be stored permanently. If the system tells the user that the transaction has succeeded, the 
			  transaction must have, in fact, succeeded.

## 3) DB design 

- Process 


- Concept 

- STAR SCHEMA 
<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/star_schema.png" width="500" height="300">

- With star shape, `FACT table` as the star center, while others are `dimension table` which give describe the attribution of FACT table.  
- `Dimension tables` are independent with each other 

- SNOWFLAKE SCHEMA
<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/snowflake_schema.jpg" width="500" height="300">

- Is an extension of STAR SHEMA actually
- `FACT table` at center, `dimension table` at the rest, the difference is that : `dimension table` is extenable. i.e. 
can track multiples `dimension table` together 
- Pro : Can split the data count at each dimension table -> fast operation like `join`
- Con : Have to maintain extra tables 


- CONSTELLATION SCHEMA OR GALAXY SCHEMA

- Example 

## 4) DB tuning 

## 5) DB managment 

## 6) Case study 

## 7) Ref 
- https://deliveroo.engineering/2017/11/23/engineering-interviews.html
- https://www.2ndquadrant.com/en/postgresql/postgresql-vs-mysql/
- https://blog.xuite.net/jack101257/twblog/138494904-%E4%BC%81%E6%A5%AD%E8%B3%87%E6%96%99%E5%80%89%E5%84%B2DWH%E7%B0%A1%E4%BB%8B
- http://relyky.blogspot.com/2011/04/data-warehousedimensional-data-model.html


