-- Table: Project

-- +-------------+---------+
-- | Column Name | Type    |
-- +-------------+---------+
-- | project_id  | int     |
-- | employee_id | int     |
-- +-------------+---------+
-- (project_id, employee_id) is the primary key of this table.
-- employee_id is a foreign key to Employee table.
-- Table: Employee

-- +------------------+---------+
-- | Column Name      | Type    |
-- +------------------+---------+
-- | employee_id      | int     |
-- | name             | varchar |
-- | experience_years | int     |
-- +------------------+---------+
-- employee_id is the primary key of this table.
-- Write an SQL query that reports the most experienced employees in each project. In case of a tie, report all employees with the maximum number of experience years.

-- The query result format is in the following example:

-- Project table:
-- +-------------+-------------+
-- | project_id  | employee_id |
-- +-------------+-------------+
-- | 1           | 1           |
-- | 1           | 2           |
-- | 1           | 3           |
-- | 2           | 1           |
-- | 2           | 4           |
-- +-------------+-------------+

-- Employee table:
-- +-------------+--------+------------------+
-- | employee_id | name   | experience_years |
-- +-------------+--------+------------------+
-- | 1           | Khaled | 3                |
-- | 2           | Ali    | 2                |
-- | 3           | John   | 3                |
-- | 4           | Doe    | 2                |
-- +-------------+--------+------------------+

-- Result table:
-- +-------------+---------------+
-- | project_id  | employee_id   |
-- +-------------+---------------+
-- | 1           | 1             |
-- | 1           | 3             |
-- | 2           | 1             |
-- +-------------+---------------+
-- Both employees with id 1 and 3 have the most experience among the employees of the first project. For the second project, the employee with id 1 has the most experience.

# V0 
select project_id, Project.employee_id
from Project inner join Employee
on Project.employee_id = Employee.employee_id
where (project_id, experience_years) in 
    (select p.project_id as project_id, max(e.xperience_years) as experience_years
    from Project p inner join Employee e 
    on p.employee_id = e.employee_id
    group by p.project_id)

# V1 
# https://code.dennyzhang.com/project-employees-iii
select project_id, Project.employee_id
from Project inner join Employee
on Project.employee_id = Employee.employee_id
where (project_id, experience_years) in 
    (select project_id, max(experience_years) as years
    from Project inner join Employee
    on Project.employee_id = Employee.employee_id
    group by project_id)

# V2 
# Time:  O((m + n)^2)
# Space: O(m + n)
SELECT project_id, 
       p1.employee_id 
FROM   project AS p1 
       INNER JOIN employee AS e1 
               ON p1.employee_id = e1.employee_id 
WHERE  ( project_id, experience_years ) IN (SELECT project_id, 
                                                   Max(experience_years) 
                                            FROM   project AS p2 
                                                   INNER JOIN employee AS e2 
                                                           ON p2.employee_id = 
                                                              e2.employee_id 
                                            GROUP  BY project_id 
                                            ORDER  BY NULL) 