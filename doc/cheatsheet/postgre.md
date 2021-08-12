## PostgreSQL Cheatsheet


### 1) array_to_string : array to str
```sql
select array_to_string(array[1,2,3], ',');
# 123
```

### 2) cast : type transform
```sql
select cast(id as varchar) from student;
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