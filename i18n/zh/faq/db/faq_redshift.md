<!-- d41e32b3496e -->
# AWS REDSHIFT FAQ

- 1) AWS Redshift 資料庫開發者指南

	- https://docs.aws.amazon.com/en_us/redshift/latest/dg/c-who-should-use-this-guide.html

- 2) Redshift 和 PostgreSQL 差在哪
	- Amazon Redshift 是專為線上分析處理 `(OLAP)` 與商業智慧（BI）應用設計的，這類應用要對大量資料下複雜查詢。因為要解的問題完全不同，Redshift 使用的特化儲存結構與查詢執行引擎和 PostgreSQL 的實作完全不一樣。舉例來說，線上交易處理（OLTP）應用通常是按列（row）存資料

	- ***`Amazon Redshift` 是按欄（column）存資料，並使用特化的資料壓縮編碼，把記憶體使用與磁碟 I/O 壓到最佳***。

	- ***`PostgreSQL` 有些適合小規模 OLTP 的功能，例如次要索引（secondary index）與高效率的單列資料操作，在 Redshift 裡被拿掉了，以換取效能***。

	- https://docs.aws.amazon.com/en_us/redshift/latest/dg/c_redshift-and-postgres-sql.html

- 3) 資料表設計指南
	- https://docs.aws.amazon.com/en_us/redshift/latest/dg/c_designing-tables-best-practices.html
	- https://github.com/awsdocs/amazon-redshift-developer-guide

- 4) 選擇 sort key
	- 建表時可以指定一個或多個欄位作為 sort key。Amazon Redshift 會依照 sort key 把資料以排序後的順序存在磁碟上。資料怎麼排序，對磁碟 I/O、欄式壓縮與查詢效能都有很大的影響。

	- 這個步驟裡，SSB 各表的 sort key 是依照以下最佳實務挑的：

		- 如果最常查的是近期資料，就把時間戳欄位放在 sort key 的第一個位置。

		- 如果經常對某個欄位做範圍過濾或等值過濾，就把那個欄位設為 sort key。

		- 如果經常 join 某張（維度）表，就把 join 用的欄位設為 sort key。

	- https://docs.aws.amazon.com/en_us/redshift/latest/dg/tutorial-tuning-tables-sort-keys.html

- 5) 選擇 distribution style
	- 載入資料時，Amazon Redshift 會依照該表的 distribution style，把列分配到各個 node slice 上。每個節點有幾個 slice 取決於叢集的節點規格。例如本教學用的 dc1.large 叢集有四個節點、每個節點兩個 slice，所以整個叢集共八個 slice。所有節點都會參與平行查詢，各自處理分散在 slice 上的資料。

	- 查詢執行時，查詢最佳化器會依需要把列重新分配到各個計算節點，以完成 join 與彙總。重分配可能是把特定的列送到某些節點去 join，也可能是把整張表廣播到所有節點。

	- 指定 distribution style 時，目標是：

		- 讓要 join 的表的列放在一起（collocate）

		- join 欄位的列落在同一批 slice 上時，查詢執行期間要搬動的資料就變少。

		- 讓資料平均分散在叢集的各個 slice 上。資料分佈平均，工作量才能平均分配給所有 slice。

	- Distribution style 有哪些
		- KEY distribution
			- 依某一欄的值來分配列。Leader node 會盡量把值相同的列放到同一個 node slice。如果兩張表都以 join key 分配，leader node 就會依 join 欄位的值把列放在一起，讓共同欄位中相同的值實際存在一起。

		- ALL distribution
			- 整張表的副本複製到每個節點。EVEN 或 KEY 只在每個節點上放表的一部分列，ALL 則保證這張表參與的每一個 join 都已經在本地有完整資料。

		- EVEN distribution
			- 不看任何欄位的值，以 round-robin 的方式把列平均散到各個 slice。當一張表不參與 join，或是 KEY 與 ALL 都沒有明顯較好時，EVEN 就很合適。EVEN 是預設的 distribution style。

	- https://docs.aws.amazon.com/en_us/redshift/latest/dg/tutorial-tuning-tables-distribution.html

- 6) 檢視壓縮編碼（compression encoding）
	- 壓縮是欄位層級的操作，讓資料存起來時佔的空間更小。壓縮省下儲存空間，也減少從儲存體讀出的資料量，因此降低磁碟 I/O、進而提升查詢效能。

	- 預設情況下，Amazon Redshift 以原始、未壓縮的格式存放資料。在 Redshift 資料庫裡建表時，可以為欄位定義壓縮型別（編碼）。詳情見官方文件的 Compression Encodings。

	- 你可以在建表時手動為欄位指定壓縮編碼，也可以用 COPY 指令讓它分析載入的資料、自動套用壓縮編碼。

	- 壓縮編碼的種類
		- Raw
		- Bytedict
		- LZO
		- Runlength
		- Text255
		- Text32K
	- 範例
		- https://docs.aws.amazon.com/en_us/redshift/latest/dg/Examples__compression_encodings_in_CREATE_TABLE_statements.html
