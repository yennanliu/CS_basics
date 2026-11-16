/*

1661. Average Time of Process per Machine
https://leetcode.com/problems/average-time-of-process-per-machine/
Easy

Table: Activity

+----------------+---------+
| Column Name    | Type    |
+----------------+---------+
| machine_id     | int     |
| process_id     | int     |
| activity_type  | enum    |
| timestamp      | float   |
+----------------+---------+
The table shows the user activities for a factory website.
(machine_id, process_id, activity_type) is the primary key (combination of columns with unique values) of this table.
machine_id is the ID of a machine.
process_id is the ID of a process running on the machine with ID machine_id.
activity_type is an ENUM (category) of type ('start', 'end').
timestamp is a float representing the current time in seconds.
'start' means the machine starts the process at the given timestamp and 'end' means the machine ends the process at the given timestamp.
The `start` timestamp will always be less than or equal to the `end` timestamp for every `(machine_id, process_id)` pair.
It is guaranteed that each (machine_id, process_id) pair has a 'start' and 'end' timestamp.

There is a factory website that has several machines, each running one or more processes. Write a solution to find the average time each machine takes to complete a process.

The time to complete a process is the 'end' timestamp minus the 'start' timestamp. The average time is calculated by the total time to complete every process on the machine divided by the number of processes that were run on that machine.

The resulting table should have the machine_id along with the average time as processing_time, which should be rounded to 3 decimal places.

Return the result table in any order.

The result format is in the following example.

Example 1:

Input:
Activity table:
+------------+------------+---------------+-----------+
| machine_id | process_id | activity_type | timestamp |
+------------+------------+---------------+-----------+
| 0          | 0          | start         | 0.712     |
| 0          | 0          | end           | 1.520     |
| 0          | 1          | start         | 3.140     |
| 0          | 1          | end           | 4.120     |
| 1          | 0          | start         | 0.550     |
| 1          | 0          | end           | 1.550     |
| 1          | 1          | start         | 0.430     |
| 1          | 1          | end           | 1.420     |
| 2          | 0          | start         | 4.100     |
| 2          | 0          | end           | 4.512     |
| 2          | 1          | start         | 2.500     |
| 2          | 1          | end           | 5.000     |
+------------+------------+---------------+-----------+
Output:
+------------+-----------------+
| machine_id | processing_time |
+------------+-----------------+
| 0          | 0.894           |
| 1          | 0.995           |
| 2          | 1.456           |
+------------+-----------------+
Explanation:
There are 3 machines running 2 processes each.
Machine 0's average time is ((1.520 - 0.712) + (4.120 - 3.140)) / 2 = 0.894
Machine 1's average time is ((1.550 - 0.550) + (1.420 - 0.430)) / 2 = 0.995
Machine 2's average time is ((4.512 - 4.100) + (5.000 - 2.500)) / 2 = 1.456

*/

# V0
# IDEA : SELF JOIN EACH 'start' ROW TO ITS 'end' ROW
#
#   One row per (machine, process) pair is wanted, holding end - start. Joining
#   Activity to itself on the same machine and process, with one side 'start'
#   and the other 'end', produces exactly that pair. AVG over the pairs is then
#   total time / number of processes on the machine.
#
# time = O(n), space = O(n)    -- n = rows in Activity; hash join on (machine_id, process_id) + hash group
SELECT
    s.machine_id,
    ROUND(AVG(e.timestamp - s.timestamp), 3) AS processing_time
FROM Activity AS s
JOIN Activity AS e
    ON s.machine_id = e.machine_id
   AND s.process_id = e.process_id
   AND s.activity_type = 'start'
   AND e.activity_type = 'end'
GROUP BY s.machine_id;

# V1
# IDEA : CONDITIONAL AGGREGATION, NO JOIN
#
#   Sum every 'end' timestamp and subtract every 'start' timestamp per machine:
#   that is the machine's total processing time. Each process contributes one
#   start and one end, so COUNT(DISTINCT process_id) is the number of processes.
#
# time = O(n), space = O(n)    -- one scan of Activity, hash group by machine (DISTINCT keeps a set per group)
SELECT
    machine_id,
    ROUND(
        SUM(CASE WHEN activity_type = 'end' THEN timestamp ELSE -timestamp END)
            / COUNT(DISTINCT process_id),
        3
    ) AS processing_time
FROM Activity
GROUP BY machine_id;
