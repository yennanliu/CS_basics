# Database isolation levels

DB ACID : ACID(Atomicity, Consistency, Isolation, Durability)
Here we discuss level of Isolation.

There are 4 isolation levels:

Repeatable Read
Read Committed
Read Uncommitted
Serializable

p.s. MySQL 的 InnoDB 預設的 Isolation Level 是 Repeatable Read
p.s. PostgreSQL 預設的 Isolation Level 是 Read Committed

NOTE:
	- Serializable : highest isolation level
	- Read Uncommitted : lowest isolcation level

## Repeatable Read

Consistent reads within the same transaction read the snapshot established by the first read. This means that if you issue several plain (nonlocking) SELECT statements within the same transaction, these SELECT statements are consistent also with respect to each other

根據 MySQL 文件說明，如果在同一個 Transaction 
內的 nonblocking read 會是第 1 個 read 的 snapshot ，
所以相同的 read query 都會取得跟第 1 個 read 一樣的結果。


## Read Committed

了解 Repeatable Read 之後，就能很迅速理解 Read Committed 
-> 因為 Read Commited 即使在同一個 Transaction 內使用相同的 
select 仍會拿到最新的資料。


## Read Uncommitted

如其名， Read Uncommitted 可以讀取到其他 transactions 尚未 commit 時的資料，
這被稱為 `dirty read`，因為讀取尚未 commit 的資料會有些風險，這些資料在 commit 之前仍有可能還會變化，或者最後被 rollback ，
導致此時讀取到的資料與資料庫最新狀態不一致。
除了 dirty read 這問題之外，其他行為倒跟 Read Committed 一樣。


## Serializable

此 isolation level 在 autocommit 未啟用的情況下，
會將所有的 select 都視為 select ... for share 的形式處理。此時，所有的transactions 都能讀取資料，但是如果有其中 1 個 transaction 欲更新資料，
就必須等其他 transactions 都 commit 了才行。
另外，如果有其中 1 個 transaction 更新資料但是尚未 commit ，
其他的 transactions 如要讀取資料，就得等該 transaction commit 。


## Ref
- https://myapollo.com.tw/blog/database-transaction-isolation-levels/