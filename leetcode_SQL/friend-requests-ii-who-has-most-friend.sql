-- 602. Friend Requests II: Who Has Most Friend? 

-- In social network like Facebook or Twitter, people send friend requests and accept others' requests as well.

-- Table request_accepted

-- +--------------+-------------+------------+
-- | requester_id | accepter_id | accept_date|
-- |--------------|-------------|------------|
-- | 1            | 2           | 2016_06-03 |
-- | 1            | 3           | 2016-06-08 |
-- | 2            | 3           | 2016-06-08 |
-- | 3            | 4           | 2016-06-09 |
-- +--------------+-------------+------------+
-- This table holds the data of friend acceptance, while requester_id and accepter_id both are the id of a person.

-- Write a query to find the the people who has most friends and the most friends number under the following rules:

-- It is guaranteed there is only 1 people having the most friends.
-- The friend request could only been accepted once, which mean there is no multiple records with the same requester_id and accepter_id value.
-- For the sample data above, the result is:

-- Result table:
-- +------+------+
-- | id   | num  |
-- |------|------|
-- | 3    | 3    |
-- +------+------+
-- The person with id '3' is a friend of people '1', '2' and '4', so he has 3 friends in total, which is the most number than any others.
-- Follow-up:
-- In the real world, multiple people could have the same most number of friends, can you find all these people in this case?


# V0

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