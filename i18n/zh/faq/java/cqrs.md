<!-- 813d3c60b1de -->
# 命令查詢責任分離（CQRS）

> **範圍** — 把系統的寫入模型與讀取模型拆開：CQRS 帶來什麼（簡單、可擴展、速度）、代價是什麼（最終一致性、投影器、重複的程式碼），以及什麼時候值得這樣做。
> **另見**：[`../backend/api_design.md`](../backend/api_design.md) — 擺在它前面的 API 介面；[`../backend/be_programming_notes_pt2.md`](../backend/be_programming_notes_pt2.md) — outbox、saga 與幕等消費者，CQRS 的投影器都會用到。

| | Command（命令） | Query（查詢） |
|---|---------|-------|
| 改變狀態 | **會** | 不會 |
| 回傳資料 | 不回傳（最多一個 id 或 ack） | **回傳** |
| 模型 | 正規化、強一致、負責維護不變條件 | 反正規化、為讀取最佳化（materialized view） |
| 隨什麼擴展 | 寫入吞吐量 | 讀取吞吐量 — 通常是大得多的那個數字 |

**CQRS 不等於 event sourcing**；兩者常常一起用（事件當寫入日誌，投影當讀取模型），
但少了其中任何一個，另一個仍然成立。

- 命令與查詢責任分離 (command, query)
- 分開設計以下兩種操作：會改變系統狀態但不會回傳值的操作，稱之為Command，
  以及不會改變狀態但會回傳值的操作，稱為Query 

- Pros : 3S

- Simplicity：
	CQRS將系統分成Write Model與Read Model，這兩種模型的行為與責任大不相同。從單一責任原則（Single Responsibility Principle；SRP）的角度來看，CQRS進一步簡化不同模型之內的複雜度。
	寫入模型：
		Strong consistency
		normalized data model
		one-way dependency
	讀取模型：
		Eventually consistency
		de-normalized data model (materialized view)
		any-way dependency
- Scalability：
	很多系統的讀取頻率遠大於寫入，例如在電子商務系統中大部分的使用者都在瀏覽資料，少部分的操作才會改變系統狀態。在這種情況下，套用CQRS可以單獨針對讀取部分加以拓展，如圖3所示。
- Speed (Performance)：
	綜合上述兩個優點，CQRS可以提升系統反應速度與效能，因為開發人員可以分別針對寫入端與讀取端採取不同的優化策略。例如，寫入端採取Event Sourcing簡化與加速寫入操作，讀取端因為不會改變狀態，可以用各種快取工具加快讀取。圖4是ezKanban團隊成員杜奕萱在她的碩士論文《套用命令與查詢責任分離以簡化聚合依賴：以 ezKanban 為例》中針對套用CQRS之後ezKanban的GetBoardContent查詢所做的效能測試，可以發現套用CQRS之後有著非常巨大的讀取效能提升。

- CQRS 讓我們可以分別為寫入與讀取操作
	挑選最合適的領域模型；
	不必硬做出一個同時支援兩者的複雜模型

- 它讓我們可以各自挑選適合的 repository，
	分別對付讀取與寫入的難處，
	例如寫入要高吞吐量、讀取要低延遲

- 在分散式架構中，它天生就和事件驅動的
	程式模型互補：責任分離，模型也更單純


- Cons
	- implementation complexity
		- example :
		- 例如，如圖1所示，ezKanban在資料庫端也套用CQRS，因此讀取資料庫與寫入資料庫之間的狀態同步是最終一致性。為了維持最終一致性，就會有新的設計工作產生：同步的訊息就需要考慮順序（ordering）與at least once等議題，而負責產生讀取資料庫的Projector（投影器）設計則須考慮idempotent與replay events等問題。另外，如何撰寫Projector也是一個問題。

	- 只有複雜的領域模型才划得來這個模式多出來的複雜度；單純的領域模型不需要這一切也管得好
	
	- 一定程度的程式碼重複是免不了的，相對於它帶來的好處，這是可以接受的代價；不過還是要個案判斷
	
	- 分開的 repository 會帶來一致性問題，要讓寫入端與讀取端永遠完全同步很困難；多數時候只能接受最終一致性

<!-- 667fc60dddce -->
## 什麼時候該用

| 該用 CQRS 的時候 | 不該用的時候 |
|---------------------|-----------|
| 讀取遠多於寫入，而兩者需要不同的儲存方式或資料形狀 | 領域就只是單純的 CRUD — 多出第二個模型卻什麼也沒換到 |
| 寫入模型真的複雜（不變條件、聚合），而讀取只是扁平的投影 | 讀取端完全無法容忍看到舊資料 |
| 讀寫兩端需要各自擴展、各自部署、各自失敗 | 團隊沒有意願接受最終一致性以及它帶來的除錯成本 |

有一個幾乎不花成本的中間做法：資料庫只留一個，但把*程式碼*的路徑分開 —— 命令處理器
負責改變聚合，查詢處理器直接讀投影或 DTO。等到流量真的有需求，再把儲存拆開。

---

<!-- 45d81a63253b -->
## 參考資料
- [Teddy Chen — CQRS（繁體中文）](https://teddy-chen-tw.blogspot.com/2022/07/8cqrs.html)
- [Martin Fowler — CQRS](https://martinfowler.com/bliki/CQRS.html)
- [`../backend/api_design.md`](../backend/api_design.md) · [`../backend/be_programming_notes_pt2.md`](../backend/be_programming_notes_pt2.md)
	- https://www.baeldung.com/cqrs-event-sourcing-java
