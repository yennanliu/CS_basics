# Command Query Responsibility Segregation (CQRS)

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

- CQRS provides us a convenient way to select separate domain models 
	appropriate for write and read operations; 
	we don’t have to create a complex domain model supporting both

- It helps us to select repositories that are individually suited 
	for handling the complexities of the read and write operations, 
	like high throughput for writing and low latency for reading

- It naturally complements event-based programming models in 
	a distributed architecture by providing a separation of 
	concerns as well as simpler domain models


- Cons
	- implementation complexity
		- example :
		- 例如，如圖1所示，ezKanban在資料庫端也套用CQRS，因此讀取資料庫與寫入資料庫之間的狀態同步是最終一致性。為了維持最終一致性，就會有新的設計工作產生：同步的訊息就需要考慮順序（ordering）與at least once等議題，而負責產生讀取資料庫的Projector（投影器）設計則須考慮idempotent與replay events等問題。另外，如何撰寫Projector也是一個問題。

	- Only a complex domain model can benefit from the added complexity of this pattern; a simple domain model can be managed without all this
	
	- Naturally leads to code duplication to some extent, which is an acceptable evil compared to the gain it leads us to; however, individual judgment is advised
	
	- Separate repositories lead to problems of consistency, and it’s difficult to keep the write and read repositories in perfect sync always; we often have to settle for eventual consistency

## Ref
- https://teddy-chen-tw.blogspot.com/2022/07/8cqrs.html

- example code
	- https://www.baeldung.com/cqrs-event-sourcing-java