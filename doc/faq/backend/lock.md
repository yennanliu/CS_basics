# LOCK

### 1) Mysql Lock

#### 全局鎖

 - Use cases : DB backup
 - All tables in DB become "read only" status

```sql
# sql

# SQL client 1
flush tables with read lock;

# SQL client 2 (will be locked)
INSERT INTO blog_db.authors(`id`,`email`,`name`,`url`, `create_time`, `update_time`)
    -> values
    -> (100,  "bill@google.com", "bill", "" ,  now(), now()),
    -> (101,  "jack@uber.com", "jack", "" , now(), now()),
    -> (102, "yen@google.com", "yen", "" , now(), now());

# unlock
unlock tables
```

- Ref
	- https://www.xiaolincoding.com/mysql/lock/mysql_lock.html#%E8%A1%A8%E7%BA%A7%E9%94%81
	- https://medium.com/@martin87713/mysql-lock-55ca187e4af2


#### 表級鎖

- Types
	- 表鎖
	- 元数据锁（MDL)
	- 意向锁
	- AUTO-INC 锁


- 表鎖
	- 會話退出後, 會釋放所有表鎖
	- 避免在InnoDB表使用表鎖, 顆粒範圍太大

```sql
# sql

# SQL client 1
# 表级别的共享锁，也就是读锁；
lock tables authors read;

# 表级别的独占锁，也就是写锁；
lock tables comment write;

# unlock all locked table in current session
unlock tables

# SQL client 2
```

- 元数据锁（MDL)
	- 當對DB操作時, DB會自動加上元數據鎖
		- CURD 操作: MDL讀鎖
		- 對表結構變更操作: MDL寫鎖
	- ㄧ般不需要顯式使用元數據鎖

- 意向鎖

	- 在使用 InnoDB引擎的表裡對某些記錄加上 "共享鎖定"之前，需要先在表格層級加上一個 意向共享鎖定"
	- 在使用 InnoDB引擎的表裡對某些紀錄加上“獨佔鎖”之前，需要先在表格級別加上一個 “意向獨佔鎖”
	- 意向共享鎖, 意向獨佔鎖 是表級鎖，不會和行級的共享鎖和獨佔鎖發生衝突
	- 而且意向鎖之間也不會發生衝突，只會和共享表鎖（lock tables ... read ）和獨佔表鎖（lock tables ... write）發生衝突
	- so, 意向鎖的目的是為了快速判斷表裡是否有記錄被加鎖。
	- 表鎖, 行鎖皆滿足以下:
		讀讀分享、讀寫互斥、寫寫互斥

- AUTO-INC
	- 表裡的主鍵通常都會設定成自增的，這是透過對主鍵欄位聲明 AUTO_INCREMENT 屬性來實現的。
	之後可以在插入資料時，可以不指定主鍵的值，資料庫會自動給主鍵賦值遞增的值，這主要是透過 AUTO-INC 鎖定實現的。
	- AUTO-INC鎖定是特殊的表鎖機制，鎖定不是再一個交易提交後才釋放，而是再執行完插入語句後就會立即釋放。在插入資料時，會加上一個表格層級的 AUTO-INC 鎖定，然後為被 AUTO_INCREMENT 修飾的欄位賦值遞增的值，等插入語句執行完成後，才會將 AUTO-INC 鎖定釋放掉。


#### 行級鎖
	- InnoDB 引擎是支援行級鎖定的，而 MyISAM 引擎並不支援行級鎖定

```sql
# sql

# SQL client 1
# 对读取的记录加共享锁
# select * from accounts where id = 5 lock in share mode;
select * from authors where id = 1 lock in share modes;


# 对读取的记录加独占锁
select * from authors for update;

# SQL client 2
```


### 2) Redis Lock

### 3) Zookeeper (ZK) Lock

### 4) Modern DB deal with concurrence:

- Types
 - 2PL (2 phase locking)
 - MVCC (Multiversion concurrency control) (多版本並行控制)

- Ref
 - https://www.fanyilun.me/2017/04/20/MySQL%E5%8A%A0%E9%94%81%E5%88%86%E6%9E%90/?source=post_page-----19833c18baab--------------------------------