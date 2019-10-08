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
