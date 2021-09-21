-- In social network like Facebook or Twitter, people send friend requests and accept others’ requests as well. Now given two tables as below:

-- Table: friend_request

-- | sender_id | send_to_id |request_date|
-- |-----------|------------|------------|
-- | 1         | 2          | 2016_06-01 |
-- | 1         | 3          | 2016_06-01 |
-- | 1         | 4          | 2016_06-01 |
-- | 2         | 3          | 2016_06-02 |
-- | 3         | 4          | 2016-06-09 |
 

-- Table: request_accepted

-- | requester_id | accepter_id |accept_date |
-- |--------------|-------------|------------|
-- | 1            | 2           | 2016_06-03 |
-- | 1            | 3           | 2016-06-08 |
-- | 2            | 3           | 2016-06-08 |
-- | 3            | 4           | 2016-06-09 |
-- | 3            | 4           | 2016-06-10 |
-- Write a query to find the overall acceptance rate of requests rounded to 2 decimals, which is the number of acceptance divide the number of requests.

-- For the sample data above, your query should return the following result.

-- |accept_rate|
-- |-----------|
-- |       0.80|
-- Note:

-- The accepted requests are not necessarily from the table friend_request. In this case, you just need to simply count the total accepted requests (no matter whether they are in the original requests), and divide it by the number of requests to get the acceptance rate.
-- It is possible that a sender sends multiple requests to the same receiver, and a request could be accepted more than once. In this case, the ‘duplicated’ requests or acceptances are only counted once.
-- If there is no requests at all, you should return 0.00 as the accept_rate. 
-- Explanation: There are 4 unique accepted requests, and there are 5 requests in total. So the rate is 0.80.

-- Follow-up:

-- Can you write a query to return the accept rate but for every month?
-- How about the cumulative accept rate for every day?
 

-- Intuition

-- Count the accepted requests and then divides it by the number of all requests.

-- Algorithm

-- To get the distinct number of accepted requests, we can query from the request_accepted table.

-- select count(*) from (select distinct requester_id, accepter_id from request_accepted;
-- With the same technique, we can have the total number of requests from the friend_request table:

-- select count(*) from (select distinct sender_id, send_to_id from friend_request;
-- At last, divide these two numbers and round it to a scale of 2 decimal places to get the required acceptance rate.

-- Wait! The divisor (total number of requests) could be '0' if the table friend_request is empty. So, we have to utilize ifnull to deal with this special case.

# V0
select
round(
    ifnull(
    (select count(*) from (select distinct requester_id, accepter_id from request_accepted) as A)
    /
    (select count(*) from (select distinct sender_id, send_to_id from friend_request) as B),
    0)
, 2) as accept_rate;　　

# V0'
# IDEA : CTE
WITH request_cnt AS
  (SELECT COUNT(1) AS cnt
   FROM
     (SELECT sender_id,
             send_to_id,
             COUNT(1)
      FROM friend_request
      GROUP BY sender_id,
               send_to_id)),
     accept_cnt AS
  (SELECT COUNT(1) AS cnt
   FROM
     (SELECT requester_id,
             accepter_id,
             COUNT(1)
      FROM request_accepted
      GROUP BY requester_id,
               accepter_id))
SELECT CASE(WHEN request_cnt.cnt = 0 THEN 0 ELSE ROUND(accept_cnt.cnt/request_cnt.cnt, 2) END) AS accept_rate

### Follow up ans ref
# https://zhuanlan.zhihu.com/p/258790804

# V1
# https://www.cnblogs.com/lightwindy/p/9698958.html
select
round(
    ifnull(
    (select count(*) from (select distinct requester_id, accepter_id from request_accepted) as A)
    /
    (select count(*) from (select distinct sender_id, send_to_id from friend_request) as B),
    0)
, 2) as accept_rate;　　

# V1'
# https://www.cnblogs.com/lightwindy/p/9698958.html
select coalesce(round
(count(distinct requester_id, accepter_id)
/
count(distinct sender_id, send_to_id),2),
0)
as accept_rate
from friend_request, request_accepted

# V2