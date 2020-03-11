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

### 6. select list of integer from 1 -> 100 (recursive CTE)?
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

#### 7. Given tables: movie, actor (multi to multi relations), please design a data model and query that can report number of actor with given movie-id/movie-name ?


### 8. Solve the "many-to-many" DB design problem?
- > 
https://dzone.com/articles/how-to-handle-a-many-to-many-relationship-in-datab


### 9. Which op is faster `union` or `union all`?
->
`union all` is faster, since it can avoid possible duplicates.

### 10. Example of use variable in SQL ?
->
```sql
# mysql
SELECT 
@x AS x, 
@y AS y, 
@x + 1 AS x_Plus_1, 
@x := @x + 1 AS updated_x
FROM
  (SELECT @x := 0, @y := 1) INIT;

```

### 11. Delete duplicate record from table ?
- https://github.com/yennanliu/CS_basics/blob/master/leetcode_SQL/delete-duplicate-emails.sql
->
```sql
DELETE a
FROM TABLE a,
           TABLE b
WHERE a.id = b.id
  AND a.timestamp > b.timestamp
```
- Follow up : if in the "whole column duplicate" case?
->
```sql

-- build the table 
CREATE TABLE IF NOT EXIST test( id int, age int);
TRUNCATE TABLE test; 
INSERT INTO test VALUES (1,1);
INSERT INTO test VALUES (2,2);
INSERT INTO test VALUES (3,3);
INSERT INTO test VALUES (3,3);
INSERT INTO test VALUES (3,3);
INSERT INTO test VALUES (3,3);
SELECT * FROM test;

-- delete duplicated
-- V1 
DELETE
FROM test
WHERE id IN
    (SELECT id
     FROM
       (SELECT 
        id,
        age,
        ROW_NUMBER() OVER (PARTITION BY id ORDER BY id) AS order_
        FROM test) sub
     WHERE order_ > 2 )

-- delete duplicated
-- V2
DELETE FROM test
WHERE id IN (
  SELECT calc_id FROM (
    SELECT MAX(id) AS calc_id
    FROM test
    GROUP BY id, age
    HAVING COUNT(id) > 1
  ) temp
); 

``` 

### 12. SQL deal with NULL value ?
->

- `IS NULL` VS ` = NULL`

```sql
-- select data where SALARY is not null
 SELECT  ID, NAME, AGE, ADDRESS, SALARY
   FROM CUSTOMERS
   WHERE SALARY IS NOT NULL; 

-- select data where SALARY is  null
 SELECT  ID, NAME, AGE, ADDRESS, SALARY
   FROM CUSTOMERS
   WHERE SALARY IS NULL; 

```

- COALESCE

```sql

-- COALESCE
SELECT StudentId, StudentName, Department, 
Semester_I, Semester_II, Semester_III,
COALESCE(Semester_I, Semester_II, Semester_III, 0) AS COALESCE_Result
FROM StudentDetails

-- above SQL is as same as below 
-- i.e. COALESCE(a, b, c, 0)
--  -> if a is not null then a 
--      -> if b is not null then b
--          -> if c is not null then c 
--                -> else 0 
SELECT StudentId, StudentName, Department, 
Semester_I, Semester_II, Semester_III,
COALESCE(Semester_I, Semester_II, Semester_III, 0) AS COALESCE_Result,
CASE
  WHEN Semester_I IS NOT NULL THEN Semester_I
  WHEN Semester_II IS NOT NULL THEN Semester_II
  WHEN Semester_III IS NOT NULL THEN Semester_III
  ELSE 0
END CASE_Result
FROM StudentDetails

```

- Addition value with Null

```sql

-- Method 1  : IFNULL
SELECT IFNULL(NULL, 0) + value; 

-- Method 2  : COALESCE
SELECT COALESCE(NULL, value); 

```

- Any Value + NULL = Any Value?

```sql
SELECT NULL + 100;
-- NULL 
```

- Output of NULL != NULL, NULL = NULL ?

```sql
SELECT NULL != NULL;
-- NULL 

SELECT NULL = NULL;
-- NULL 

```
- https://www.codeproject.com/Articles/1017058/Handle-NULL-in-SQL-Server
- https://www.tutorialspoint.com/sql/sql-null-values.htm

### 13. When/how sub query ?

- When
  - A subquery is used to return data that will be used in the main query as a condition to further restrict the data to be retrieved.
  - A subquery may occur in :
    - A SELECT clause
    - A FROM clause
    - A WHERE clause
  - The inner query executes first before its parent query so that the results of an inner query can be passed to the outer query.

- How 

```sql
-- sub-query pattern
SELECT column_name [, column_name ]
FROM   table1 [, table2 ]
WHERE  column_name OPERATOR
   (SELECT column_name [, column_name ]
   FROM table1 [, table2 ]
   [WHERE])

```

- https://www.tutorialspoint.com/sql/sql-sub-queries.htm
- https://www.w3resource.com/sql/subqueries/understanding-sql-subqueries.php
- https://www.sqlservertutorial.net/sql-server-basics/sql-server-subquery/

### 14. SQL join VS Sub-query ?

-> By cases. (performance VS readable)

- https://www.quora.com/Which-is-faster-joins-or-subqueries
- https://stackoverflow.com/questions/2577174/join-vs-sub-query
- https://stackoverflow.com/questions/3856164/sql-joins-vs-sql-subqueries-performance
