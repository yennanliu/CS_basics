# Java Multi-thread FAQ

- https://javaguide.cn/java/concurrent/java-concurrent-questions-01.html
- https://javaguide.cn/java/concurrent/java-concurrent-questions-02.html
- https://javaguide.cn/java/concurrent/java-concurrent-questions-03.html

### Java multi thread ?

### Async VS Sync ?

- Async : return and NOT wait till receive response
- Sync : ONLY return and when receive response

同步：發出一個呼叫之後，在沒有得到結果之前， 該呼叫就不可以返回，一直等待。
非同步：呼叫在發出之後，不用等待返回結果，該呼叫直接傳回。