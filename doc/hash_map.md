# Hash Map ref

## Hash Map Defintion 
- dev 

## Hash Map Property 
- dev 

## Hash Map Collision probability
- extension of "birthday question"

### Birthday question
- https://zh.wikipedia.org/wiki/%E7%94%9F%E6%97%A5%E5%95%8F%E9%A1%8C
```
# Birthday question
There are people keep walking into a room, at least how many people 
make the probability of ANY 2 PEOPLE HAS SAME BIRTHERDAY > 50 % ?

-> select 1 birthday
-> probability 2nd person's birthday different from 1st : 364/365
-> probability 3rd person's birthday different from 1st, 2nd : 363/365
.
.
-> probability krd person's birthday different from 1st, 2nd ...: (365-(k-1))/365

So, the probability of K people in the room BUT ALL WITH UNIQUE  birthday
-> (365/365)*(364/365)*....(365-(k-1))/365
-> above term is equal as (365)!/((365^n)*(365-n)!)

```
```
# Birthday question extension 
# given n people, every person select a value randomly from N differnt values (N is any larger than 0 interger). 
# p(n) means any 2 persons select THE SAME value, what's p(n)

-> p(n) ~= 1 - 1*(exp(n^2/2*N))^(-1) # which is as below
```
<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/collision_probability.png">

### Hash Collision Probabilities
- https://preshing.com/20110504/hash-collision-probabilities/
- https://cs.stackexchange.com/questions/86065/probability-of-hash-collision-in-the-case-of-two-parallel-hashes
- https://stackoverflow.com/questions/43715934/probability-of-collisions-in-hash-table


## TODO
- write it as a blog post
- https://yennj12.js.org/yennj12_blog_V2/

## Ref
- https://rust-algo.club/collections/hash_map/
