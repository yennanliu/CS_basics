# Real-time pipeline for auto QA test 

- Client side event : Click Timeline/transaction/category/ Change category / Back
- Design a system collect as many as real-time scenarios 
- Form of the returned scenarios
- Ways to guarantee the squential integrity of data 
- Describe the DB/data structure/end points ..

# pipeline infra 


# event data  

```

timestamp          uid         sid         event platform country 
20170101 07:00:00 u000000001  s000000001  click_timeline ios UK 
...


```

# Description 



# Test 
```flow 
st=>start: 开始 
e=>end: 登录 
io1=>inputoutput: 输入用户名密码 
sub1=>subroutine: 数据库查询子类 
cond=>condition: 是否有此用户 
cond2=>condition: 密码是否正确 
op=>operation: 读入用户信息

st->io1->sub1->cond 
cond(yes,right)->cond2 
cond(no)->io1(right) 
cond2(yes,right)->op->e 
cond2(no)->io1 
```