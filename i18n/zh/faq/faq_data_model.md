<!-- caca7a62b960 -->
# 資料建模 FAQ

<!-- f821ccd2e600 -->
### 1) 資料建模有哪些設計 schema？請舉例說明
- 兩種常見設計
    - 星型 schema（Star Schema）
        - 最單純的一種：中間放一張事實表（fact table），周圍是它參照的多張維度表（dimension table）。所有維度表都直接接到事實表上；各維度表的主鍵在事實表裡當外鍵。
        - 星型 schema 相當簡單、有彈性，而且是`反正規化`的形式。

    - 雪花 schema（Snowflake Schema）
        - 雪花 schema 的`正規化`程度更高。事實表和星型 schema 一樣，但維度表被正規化了。因為維度表分成好幾層，看起來像雪花，因此得名。

<!-- 33c803940c67 -->
### 2) 星型和雪花哪個比較好？
- 看專案與情境
- 星型 schema
    - `反正規化` -> 資料有冗餘 -> 維護稍微麻煩
    - 查詢比較簡單（join 少）、跑得比較快（相對於雪花）
    - 目的：偏向指標分析。例如：「付給某位訂戶的理賠金額是多少？」
    - 比較吃記憶體
- 雪花 schema
    - `正規化` -> 沒有冗餘資料 -> 好維護
    - 查詢稍微複雜（要 join）、跑得比較慢（相對於星型）
    - 偏向維度分析。例如：「目前有效的某個方案綁了多少訂戶？」
    - 比較省記憶體

<!-- 197727e6ec5e -->
### 3) 什麼是事實表、維度表？
- 事實（Fact）
    - 事實代表量化的資料。
    - 例如應付淨額就是一個事實。事實表裡放的是數值資料，以及來自相關維度表的外鍵
    - 一般來說，事實表是正規化的形式
- 維度（Dimension）
    - 維度代表質性的資料。例如方案、產品、類別都是維度。
    - 維度表裡放的是描述性或文字性的屬性
    - 一般來說，維度表是反正規化的形式

<!-- dca7c3519898 -->
### 4) 你遇過哪些不同類型的維度？請逐一詳細說明並舉例
- 5 種維度
    - 一致維度（Conformed dimensions）
    - 雜項維度（Junk dimensions）
    - 角色扮演維度（Role-playing dimensions）
    - `緩慢變化維度（Slowly Changing Dimension, SCD）`
    - 退化維度（Degenerated dimensions）

<!-- f83696c1a1ba -->
### 5) 說明`緩慢變化維度（Slowly Changing Dimension, SCD）`
- SCD：Slowly Changing Dimensions，緩慢變化維度
- 類型
    - Type 0：
        - 什麼都不做（不管時間怎麼變，它就是不變）
    - Type 1：
        - 新紀錄直接覆蓋舊紀錄，舊紀錄不留痕跡。
        - 也就是屬性的舊值被新值取代的那種維度。Type-1 維度不保留歷史
    - Type 2：
        - 在顧客維度表裡新增一筆紀錄，因此這位顧客實際上被當成兩個人。
        - 這種維度會保留無限的歷史
    - Type 3：
        - 直接修改原本那筆紀錄來反映變化。
        - 這種維度只保留有限的歷史，並用一個額外欄位來存舊值。
    - Type 4
        - 這種維度把歷史資料保存在另一張表，主維度表只放目前的資料
- 參考資料
    - https://www.1keydata.com/datawarehousing/slowly-changing-dimensions.html
    - https://www.gushiciku.cn/pl/ggNK/zh-tw
    - https://help.aliyun.com/document_detail/295435.html#:~:text=SCD%E7%AE%80%E4%BB%8B,%E5%85%B3%E9%94%AEETL%E4%BB%BB%E5%8A%A1%E4%B9%8B%E4%B8%80%E3%80%82
    - https://www.cnblogs.com/biwork/p/3363749.html
    - https://www.codenong.com/cs107062349/

<!-- 8d07e45332be -->
### 6) 談談你對無事實事實表（factless fact）的理解？我們為什麼要用它？
- 想法
    - 無事實事實表是一張不含任何度量值的事實表，裡面只有維度鍵。
    - 業務上偶爾會出現非得用無事實事實表不可的情況。
    - 例子：假設你在維護一套員工出勤紀錄系統，就可以用一張帶三個鍵的無事實事實表。
    - 你會發現下面這張表沒有任何度量值。但如果要回答下面這個問題，用這一張無事實事實表就能輕鬆做到，不必拆成兩張事實表：
        - 「某個部門在某一天有多少員工出勤？」
<!--CODE-->

<!-- d32369ad54cc -->
### 7) OLTP 與 OLAP 有什麼差別？
- OLTP
    - `線上交易處理系統（Online Transaction Processing System）`
    - OLTP 維護業務的交易資料，通常高度`正規化`。
    - 用的是正規化（接近 3NF）的模型，不是維度模型 —— 星型與雪花是資料倉儲的形狀
- OLAP
    - `線上分析處理系統（Online Analytical Processing System）`
    - OLAP 用於`分析與報表`，屬於`反正規化`的形式。
    - `星型`或`雪花` schema —— 想少 join 就用星型，某個維度大到正規化划得來時就用雪花（見上面第 1、2 題）
- 補充
    - MPP（大規模平行處理）
        - MPP 資料庫是一種針對平行處理最佳化的資料庫，讓許多運算單元能同時執行大量操作。
- 參考資料
    - https://searchdatamanagement.techtarget.com/definition/MPP-database-massively-parallel-processing-database#:~:text=An%20MPP%20database%20is%20a,different%20parts%20of%20the%20program
    - https://medium.com/@jockeyng/hadoop%E8%88%87mpp%E6%98%AF%E4%BB%80%E9%BA%BC%E9%97%9C%E4%BF%82-%E6%9C%89%E4%BB%80%E9%BA%BC%E5%8D%80%E5%88%A5%E5%92%8C%E8%81%AF%E7%B9%AB-afb4397e12a1

<!-- ea6539ff2e17 -->
### 8) 什麼是`代理鍵（Surrogate key）`？它和主鍵差在哪？
- `代理鍵`是`一個唯一識別碼，或由系統產生、可以當主鍵用的序號`。它可以是`一個欄位，也可以是多個欄位的組合`。和主鍵不同的是，它`不是`從`既有的`應用資料欄位裡挑出來的。

<!-- 4e52c80d6608 -->
### 9) 是不是所有資料庫都應該做到 3NF？
- 不是硬性規定。但要注意這個取捨的方向：`好維護與低冗餘正是正規化換來的`。只有在某個特定的讀取或分析工作負載真的需要把 join 拿掉時才反正規化，並且接受那些被複製出來的副本從此每次寫入都得跟著同步。

<!-- b140639f754b -->
### 10) 說明 1NF、2NF、3NF、4NF、5NF
- 1NF：
    - 每個屬性的定義域只含原子值，而且每個屬性的值只取該定義域中的單一個值。
- 2NF：
    - 表中沒有任何非主屬性，函數相依於任一候選鍵的真子集。
- 3NF：
    - 每個非主屬性對表中每一個候選鍵都是非遞移相依的。對描述主鍵沒有貢獻的屬性會被移出該表。換句話說，不允許遞移相依。
- 4NF：
    - 表中每一個非平凡的多值相依，都是對某個超鍵的相依。
- 5NF：
    - 表中每一個非平凡的 join 相依，都由該表的超鍵所蘊含。

<!-- 7aafddb67d29 -->
### 11) 列舉幾個資料建模時常犯的錯誤
- 把資料模型做得太龐大：
    - 大模型比較容易有設計缺陷。盡量把資料模型控制在 200 張表以內。
- 目的不清：
    - 如果你不知道這套業務方案要解決什麼，就很可能做出錯的資料模型。先把業務目的搞清楚，才做得出對的模型。
- 亂用代理鍵：
    - `不要沒事就用代理鍵。只有在自然鍵無法勝任主鍵時才用代理鍵`。

<!-- 07a53e7f8a42 -->
### 12) 如果某個欄位加了唯一性約束，那插入兩個 null 會不會報錯？
- 看引擎，所以要先問是哪一個。依 SQL 標準，`一個 null 值不等於另一個 null 值`，所以 `UNIQUE` 欄位可以接受很多個 null —— PostgreSQL、MySQL、Oracle 與 SQLite 都是這樣。`SQL Server 是例外`：它的唯一欄位只允許一個 null（在那邊若要標準行為，就用帶 `WHERE col IS NOT NULL` 的 filtered index）。

<!-- d0b4cffc2008 -->
### 13) 什麼是基數（cardinality）？
- 從數學角度想，它是一個集合裡的元素個數。從資料庫的角度想，基數講的是關聯中的數量關係：`一對一、一對多，或多對多`。

<!-- 2391b1abf09c -->
### 參考資料：
- 資料庫建模綜合
    - https://www.softwaretestinghelp.com/data-modeling-interview-questions-answers/
    - https://resources.biginterview.com/industry-specific/sql-interview-questions/#1-What-is-cardinality
- 資料庫的鍵
    - https://begriffs.com/posts/2018-01-01-sql-keys-in-depth.html
- 資料建模工具
    - https://www.softwaretestinghelp.com/data-modeling-tools/
