## PostgreSQL Cheatsheet

- Ref
	- https://www.163.com/dy/article/G0F041FP0538QE50.html
	- https://blog.csdn.net/LTAO427/article/details/108596780


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