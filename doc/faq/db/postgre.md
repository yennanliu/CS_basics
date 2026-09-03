# PostgreSQL Cheatsheet

- Ref
	- https://www.163.com/dy/article/G0F041FP0538QE50.html
	- https://www.itread01.com/content/1546958947.html
	- https://www.runoob.com/postgresql/postgresql-syntax.html


### 1) array_to_string : array to str
```sql
select array_to_string(array[1,2,3], ',');
# 123
```

### 2) cast : type transform
```sql
select cast(id as varchar) from student;
```

### 2') to_char : type transform
```sql
select to_char(write_date, 'yyyy-MM-dd hh24:MI:ss') from student
```

### 3) Concat : merge str
```sql
select
concat('student:id=', cast(s.id as varchar), 'name:',s.name, 'class:',ci.name)
from student as s
```

### 4) substring : get sub str
```sql
select substring('abcd',1,2); 
# ab
```

### 5) row_number() : get row num (user defined)
```sql
select
ROW_NUMBER() OVER (ORDER BY id desc) AS sequence_number,
id,name
from
student
```

### 6) array_agg : make elements to array
```sql
select array_agg(name order by name asc) from student
```

### 7) array : transform result to array
```sql
SELECT array(SELECT "name" FROM student);
```

### 8) ceil(num) : get int, (bigger one)
```sql
SELECT ceil(35.7)
#36
```

### 9) floor(num) : get int, (smaller one)
```sql
SELECT floor(35.7)
#35
```

### 10) round(numeric,int) : round to N decimal places
```sql
SELECT round(35.7856, 2);
-- 35.79

-- NOTE: the 2-argument form takes `numeric`, NOT `double precision`
SELECT round(35.7856::numeric, 2);   -- cast when the column is float8
```

### 11) age(timestamp1,timestamp2) : the interval between two timestamps
```sql
SELECT age(timestamp '2026-09-03', timestamp '1990-01-15');
-- 36 years 7 mons 19 days          <- calendar-aware (years/months), not just seconds

SELECT age(timestamp '1990-01-15');
-- same, measured against current_date (one-argument form)

-- For a plain elapsed duration, subtract instead:
SELECT '2026-09-03'::timestamp - '2026-08-30'::timestamp;   -- 4 days
```

### 12) date_trunc(text,timestamp)
```sql
select date_trunc('day',current_timestamp),date_trunc('hour',current_timestamp);
--       date_trunc      |      date_trunc      
-- ------------------------+------------------------
-- 2018-09-16 00:00:00+08 | 2018-09-16 11:00:00+08
```

### 13) date_part(text,timestamp)
```sql
select date_part('hour',now()),date_part('minute',now()),date_part('month',now());
date_part | date_part | date_part 
-- -----------+-----------+-----------
--         11 |        10 |        9
```
