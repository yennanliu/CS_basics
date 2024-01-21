# Database isolation levels

DB ACID : ACID(Atomicity, Consistency, Isolation, Durability)
Here we discuss the level of Isolation.

- 4 type of isolation levels:
 - Read Uncommitted
 - Read Committed
 - Repeatable Read
 - Serializable

p.s. MySQL 的 InnoDB 預設的 Isolation Level 是 Repeatable Read
p.s. PostgreSQL 預設的 Isolation Level 是 Read Committed

NOTE:
 - Serializable: `highest` isolation level
 - Read Uncommitted: `lowest` isolation level
 - (least strict) `Read Uncommitted -> Read Committed -> Repeatable Read -> Serializable` (most strict)


## Read Uncommitted

在transaction裡, 可以讀到已更新/插入, 但尚未commit的資料,會造成dirty read問題

如其名， Read Uncommitted 可以讀取到其他 transactions 尚未 commit 時的資料，
這被稱為 `dirty read`，因為讀取尚未 commit 的資料會有些風險，這些資料在 commit 之前仍有可能還會變化，或者最後被 rollback ，
導致此時讀取到的資料與資料庫最新狀態不一致。
除了 dirty read 這問題之外，其他行為倒跟 Read Committed 一樣。


## Read Committed

在transaction裡, 只能讀到已經commit的資料,沒有commit的資料則讀不到
解決了dirty read問題


## Repeatable Read:

代表每次 transaction 要讀取特定欄位的資料時，只要 query 條件相同，
讀取到的資料就會相同。在這個等級解決了 Non-repeatable reads 的問題。

更嚴格一點的限制：讀取中資料會被鎖定，確保同一筆交易中的讀取資料必須相同


Consistent reads within the same transaction read the snapshot established by the first read. This means that if you issue several plain (nonlocking) SELECT statements within the same transaction, these SELECT statements are consistent also with respect to each other

根據 MySQL 文件說明，如果在同一個 Transaction 內的 nonblocking read 會是第 1 個 read 的 snapshot ， 所以相同的 read query 都會取得跟第 1 個 read 一樣的結果。


## Serializable

此 isolation level 在 autocommit 未啟用的情況下，
會將所有的 select 都視為 select ... for share 的形式處理。此時，所有的transactions 都能讀取資料，但是如果有其中 1 個 transaction 欲更新資料，
就必須等其他 transactions 都 commit 了才行。
另外，如果有其中 1 個 transaction 更新資料但是尚未 commit ，
其他的 transactions 如要讀取資料，就得等該 transaction commit 。

## Ref

- https://blog.amis.com/database-transaction-isolation-a1e448a7736e
- https://www.mysql.tw/2023/05/mysql-lock.html
- https://fanyilun.me/2015/12/29/%E4%BA%8B%E5%8A%A1%E7%9A%84%E9%9A%94%E7%A6%BB%E7%BA%A7%E5%88%AB%E4%BB%A5%E5%8F%8AMysql%E4%BA%8B%E5%8A%A1%E7%9A%84%E4%BD%BF%E7%94%A8/
- https://blog.csdn.net/weixin_45670060/article/details/119977481
- https://ithelp.ithome.com.tw/articles/10194749
- https://myapollo.com.tw/blog/database-transaction-isolation-levels/

