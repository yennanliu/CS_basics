# Cache

### 1) Cache 三大現象 : 緩存穿透, 緩存擊穿, 緩存雪崩

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/cache_failure_type.webp" width="500" height="300">

1. 快取雪崩 (Cache Avalanche, 緩存雪崩)

- 在某個時刻所有的 cache同時發生過期或者redis 服務失效，導致大量的 request 直接打在資料庫上，當流量巨大時資料庫很可能會被打掛，此時DBA若重啟資料庫可能又會被新的一波流量再打掛，這樣的狀況就是快取雪崩

- Solution:
	- 因為是同個時間點，所有的cache key大規模失效，因此可以在設定 cache 時，給予每個cache key 隨機的過期時間，或者 不設定過期時間 。每個cache key 過期時間的設定，其核心理念在你想要更新資料的頻率。


2. 快取擊穿 (Hotspot Invalid, 緩存擊穿)

- 快取擊穿則是某個熱門的 cache key過期。所以，當高併發集中在此熱門的key又快取失效過期時，流量就會直接打在資料庫上，這樣子的狀況就叫快取擊穿

- Solution:
	- 其中一個方法是將熱點key設為不過期，另一個方法則是在application寫lock(互斥鎖)以確保共用資源在多執行緒下可以排隊拿取資源，不過此作法會造成系統吞吐量下降，並阻礙其他線程。

3. 快取穿透 (Cache Penetration, 緩存穿透)

- 快取穿透是指client request 的資料並不存在於 cache 中並且也不存在於資料庫中，因此每次的請求就會直接穿過cache並打在資料庫中。同樣，若這樣類行的請求量一多，也是會將資料庫打掛。舉個簡單的例子，若 client 請求 id=-1的資料，但是在我們的資料庫中是從id=1開始的流水號，那麼-1則永遠不會拿到資料，並且請求會直接打在資料庫上

- Solution:
	- 因為是查找不存在的資料，因此可以在application中過濾非法請求，也就是當client 請求id = -1時，直接將請求做例外處理，不要讓他打在資料庫上。另一種方式，則是將id=-1寫入cache中並回傳對應的處理，例如當id=-1則redis則回傳null。還有另一種方式則是使用 " 布隆過濾器 "(Bloom Filter)判斷請求的key是否存在於集合中，若存在則直接去redis拿取，若不在則直接回傳對應訊息


- Ref
	- https://totoroliu.medium.com/redis-%E5%BF%AB%E5%8F%96%E9%9B%AA%E5%B4%A9-%E6%93%8A%E7%A9%BF-%E7%A9%BF%E9%80%8F-8bc02f09fe8f
	- https://xiaolincoding.com/redis/cluster/cache_problem.html#%E7%BC%93%E5%AD%98%E9%9B%AA%E5%B4%A9