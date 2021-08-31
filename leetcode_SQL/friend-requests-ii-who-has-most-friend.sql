/*

In social network like Facebook or Twitter, people send friend requests and accept others' requests as well.

Table request_accepted holds the data of friend acceptance, while requester_id and accepter_id both are the id of a person.

| requester_id | accepter_id | accept_date|
|--------------|-------------|------------|
| 1            | 2           | 2016_06-03 |
| 1            | 3           | 2016-06-08 |
| 2            | 3           | 2016-06-08 |
| 3            | 4           | 2016-06-09 |
Write a query to find the the people who has most friends and the most friends number. For the sample data above, the result is:

| id | num |
|----|-----|
| 3  | 3   |
Note:

It is guaranteed there is only 1 people having the most friends.
The friend request could only been accepted once, which mean there is no multiple records with the same requester_id and accepter_id value. Explanation:
The person with id '3' is a friend of people '1', '2' and '4', so he has 3 friends in total, which is the most number than any others. Follow-up:
In the real world, multiple people could have the same most number of friends, can you find all these people in this case?

*/

# V0
# NOTE : The friend request could only been accepted once, which mean there is no multiple records with the same requester_id and accepter_id value.
WITH 
cte1 AS
  (SELECT ids,
          COUNT(*) AS cnt
   FROM
     (SELECT requester_id AS ids
      FROM request_accepted
      UNION ALL 
      SELECT accepter_id AS ids
      FROM request_accepted)
   GROUP BY ids)

SELECT ids AS id,
       cnt AS num
FROM cte1
ORDER BY num DESC
LIMIT 1

# V0'
# NOTE : The friend request could only been accepted once, which mean there is no multiple records with the same requester_id and accepter_id value.
select ids as id, cnt as num
from
(
select ids, count(*) as cnt
   from
   (
        select requester_id as ids from request_accepted
        union all
        select accepter_id from request_accepted
    ) as tbl1
   group by ids
   ) as tbl2
order by cnt desc
limit 1

# V1
# https://leetcode.com/articles/friend-requests-ii-who-has-most-friend/
# IDEA 
-- Approach: Union requester_id and accepter_id [Accepted]
-- Algorithm
-- Being friends is bidirectional, so if one person accepts a request from another person, both of them will have one more friend.
-- Thus, we can union column requester_id and accepter_id, and then count the number of the occurrence of each person.
-- select requester_id as ids from request_accepted
-- union all
-- select accepter_id from request_accepted;
-- Note: Here we should use union all instead of union because union all will keep all the records even the 'duplicated' one.
# `UNION ALL` will return ALL data in 1 column, not matter if duplicated
# https://www.fooish.com/sql/union.html
select ids as id, cnt as num
from
(
select ids, count(*) as cnt
   from
   (
        select requester_id as ids from request_accepted
        union all
        select accepter_id from request_accepted
    ) as tbl1
   group by ids
   ) as tbl2
order by cnt desc
limit 1

# V1'
# http://runxinzhi.com/lightwindy-p-9698975.html
select ids as id, cnt as num
from
(
select ids, count(*) as cnt
   from
   (
        select requester_id as ids from request_accepted
        union all
        select accepter_id from request_accepted
    ) as tbl1
   group by ids
   ) as tbl2
order by cnt desc
limit 1;

# V1''
# http://runxinzhi.com/lightwindy-p-9698975.html
select a.id, count(*) as num
from 
(select requester_id as id from request_accepted
union all 
select accepter_id as id from request_accepted) a
group by id
order by num desc
limit 1

# V1'''
# http://runxinzhi.com/lightwindy-p-9698975.html
select t2.Id as id, t2.num as num
from (
select t1.Id, sum(cnt) as num
from(
select accepter_id as Id, count(*) as cnt
from request_accepted
group by accepter_id
union all
select requester_id as Id, count(*) as cnt
from request_accepted 
group by requester_id) t1
group by t1.Id ) t2 
order by t2.num DESC
limit 1

# V2
# https://zhuanlan.zhihu.com/p/259261538
SELECT c.people as id, SUM(c.cnt) AS num
FROM (
SELECT requester_id AS people, COUNT(DISTINCT accepter_id) AS cnt
FROM request_accepted
GROUP BY requester_id
UNION ALL   
SELECT accepter_id AS people, COUNT(DISTINCT requester_id) AS cnt
FROM request_accepted
GROUP BY accepter_id 
) AS c
GROUP BY c.people
ORDER BY SUM(c.cnt) DESC
LIMIT 1;