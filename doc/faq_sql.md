# SQL FAQ


#### 1. Given 2 table a (3k rows), b (4k rows), what's the 
   result count of `select a.Color, b.Size from a cross join b` ?

- 3k * 4k = 12k  
- https://www.essentialsql.com/cross-join-introduction/
- http://www.sqlservertutorial.net/sql-server-basics/sql-server-cross-join/
- (cross join pic)

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/cross_join.png" width="500" height="300">

#### 2. Difference between `full outer Join` And `Union` in SQL ? 
- Full outer Join: Joins two table, and gives the 1) Matched record + 2) Unmatched Record from Right table + 3) Unmatched Record from left table
	- i.e. table A (a,b,c), table B (c,d).

- Full outer join : return all columns in table A, table B (no depulicate)
	- -> `select A*, B.* from A full outer join B on A.c = B.c`
```sql     
 		# output : 
			 a | b | c1 | c2 | d
			 a1  b1  c11  c21   d1
			 a2 b2  c12  c22   d2
			 a3  b3  c13  c23   d3
			 .......             
```
<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/full_outer_join.gif" width="500" height="300">

- Union : Consolidate the result of two queries and returns as single result set.
	- i.e. table A (a,b,c), table B (c,d).
	- -> `select A.a, A.b from A union select B.c, B.d from B `
```sql 
		# output :   
		  	  col1 | col2
		  	   a1     b1
		  	   a2     b2
		  	   c1     d2
		  	   c2     d2
		  	   ..........
```

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/union_sql.png" width="500" height="300">

- In short :   
	- Full outer join : Join all `columns` (No matter if table A, B has same  columns )
	- Union           : Join  all `rows`    (if table A, B has same columns)
	- Cross join      : ` Combinations` of all columns within table A and B 
- https://www.quora.com/What-is-the-difference-between-full-outer-join-and-union-in-SQL
- https://www.solutionfactory.in/posts/Difference-between-Join-And-Union-in-SQL 

#### 3. Find the orders in Quater ? (no hard-code)

```sql
-- postgre

SELECT extract(QUARTER
               FROM order_timestamp) AS QUARTER,
       count(*)
FROM orders 
GROUP BY 1;

```


```sql
-- mysql

SELECT QUARTER(order_timestamp) AS QUARTER,
       count(*)
FROM orders 
GROUP BY 1;

```

#### 4. Select value, and max value with conditions in one query?

```sql
-- postgre

SELECT commit_timestamp,
       max(commit_timestamp) max_commit_time,

  (SELECT max(commit_timestamp)
   FROM git_commit
   WHERE commit_timestamp >= '2019-01-01'
     AND commit_timestamp <= '2019-12-31' ) AS max_commit_time_in_timeslot
FROM git_commit
GROUP BY 1
LIMIT 10 ;


```

#### 5. Select data with value larger than average value?

```sql
-- postgre
-- V1

WITH user_commit_count AS
  (SELECT user_id,
          count(*) AS commit_count
   FROM git_commit
   GROUP BY 1),
     avg_commit AS
  (SELECT avg(commit_count)::int AS avg_commit_count
   FROM user_commit_count)
SELECT user_id,
       commit_count,
  (SELECT *
   FROM avg_commit)
FROM user_commit_count
WHERE commit_count >
    (SELECT avg_commit_count
     FROM avg_commit)
LIMIT 30;

```

### 4. select list of integer from 1 -> 100 (recursive CTE)?
```sql
-- postgre

WITH RECURSIVE my_num AS
  (SELECT 1 AS seqnum
   UNION ALL SELECT seqnum + 1
   FROM my_num
   WHERE seqnum < 100 )
SELECT seqnum
FROM my_num;

```