# Songify

## Part 0) Biz Requirement

## Part 1) Case 1
- Batch ETL

## Part 2) Case 2
- Stream ETL

## Part 3) Case 3
- Data model
```
# data model design
# why, who, when, where, how

                    user   
                      ↑     
                      ↑     
       song ← ← song_play → → time_table
                      ↓     
                      ↓    
                   artist   
```
- [ddl](https://github.com/yennanliu/CS_basics/blob/master/workspace/Pipeline/songify/etl/sql_queries.py)

- SQL
```sql
# get the top artist per day ? (listened most)

# get the top user per day ? (listen most)

# get the accumulated user, artist ?

# get the avg listen songs count per day ?

# get the avg listen songs count per user ?

# get the users havne't listened any song ?
```
- prod Q
	- Biz model :
		- Ad
		- registration fee
		- 
	- Breakdown
		- https://new.qq.com/omn/20210110/20210110A05KY300.html
		- 
		- Step 1)
			- Active Users
				- DAU, WAU, MAU
				- compare DAU VS WAU, MAU -> check if any abnormal/inconsistent trend
				- if MAU > DAU : users Engagement is bad. DAU are usually from "different" group per day (DAU/MAU is low)
				- if MAU ~= DAU : users Engagement is good (DAU come from same group in a stable state) (DAU/MAU ~=1)
				- https://www.zhihu.com/question/24007425
				- DAT/MAU.. is a high level metric, we NEED to dig deep to understand what factors make this status, and the mechanisms
			- Engagement
				- avg use time
				- avg spend amount
				- activity
				- use coupon, promotion, extra features...

		- Step 2)
			- Users
				- check retention, cohort, churn rate
				- to understand current "prod health". If retention is low -> check the trend -> come up some hypothesis
				- compare retention from time to time as well as cohort -> to know if something happened before/now
				- also we can get sense from our cohort -> do we ever do well since now ? who're our good cohort ?
				- check churn rate, to see if it grows recently, past, and why ?