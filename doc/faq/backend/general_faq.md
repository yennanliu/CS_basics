# General FAQ

1. 反向代理?
	- https://www.explainthis.io/zh-hant/swe/why-nginx
	- https://hk.indeed.com/%E8%81%B7%E6%B6%AF%E8%B2%BC%E5%A3%AB/%E9%9D%A2%E8%A9%A6/%E5%BE%8C%E7%AB%AF%E5%B7%A5%E7%A8%8B%E5%B8%AB%E9%9D%A2%E8%A9%A6%E5%95%8F%E9%A1%8C

2. JWT ?

JWT 全名為 JSON Web Token，是一種基於 JSON 的開放標準（RFC 7519），JWT 會透過 HMAC、RSA、ECDS 等演算法進行加密，而 JWT 是以 Header、Payload、Signature 以 Base64 做編碼，並且以 . 來做分開（例如： xxxxxx.yyyyyy.zzzzz ）

JWT 的缺點:
	- Cross-site攻擊
	- Local storage不安全
	- 無法單獨被銷毀

使用時機:
	- Token 生命期較短時：讓擁有此 Token 的用戶能夠在時間以內完成某些操作（e.g. 登入、下載檔案等）
	- Token 僅單次使用：任何 Token 只用於一次後就會被拋棄，不存在於任何持久化的狀態。

3. TCP VS UDP

- TCP
	- TCP 全名 Transmission Control Protocol 是一種面向連接的協定，它在傳送資料前會建立一個可靠的連接，並且在傳送過程中提供錯誤檢查和重新傳送功能，確保傳送的資料不會丟失。

	- Process
		- Client 向 Server 主動傳送一個要求連線封包
		- Server 接收並確認這個封包後，也會回傳一個相對應的封包給 Client 確認，並等待。
		- Client 收到 Server的封包後，就確認了第一步驟發送的封包有被正確接收，如果 Client 也同意與 -Server 建立連線，就會再回傳一個確認封包告知Server。
		- Server 接收到也確認過後，就完成了三次交握，並建立連線。
	- 特性
		- 建立完連線後，後續進行傳輸封包都會加上序號
		- 而這個序號可以讓 TCP 擁有可靠的傳輸性，能夠確保：
			- 完整性：接收端能夠確認封包是否傳送完畢，判斷是否有缺漏。
			- 重傳處理：當發現有缺少封包或者逾時，則會在一定的時間內重新傳送。
			- 順序性：封包的序號可以確保接收方在收到封包時，重建順序。
		為了能夠提升整體傳輸效率，TCP並非一個封包一個封包傳送，而是利用滑動窗口的方式來傳輸，每一次傳輸一個窗口的量，藉此來提升傳輸效率。

- UDP
	- UDP 全名為 User Datagram Protocol 是一種面向非連接的協定，它沒有保證傳送資料的可靠性，因此傳送速度更快，通信引擎也更簡單。由下圖所示，傳輸方（Server）不需確認接收方（Client）是否有收到封包，封包也不像 TCP 管理得如此嚴密，也因為少了一些確認機制，不僅表頭資料會比較少，傳輸效率也比較好，適合應用的場景為：串流服務。

	- TCP 和 UDP 的主要差別在於傳送資料的可靠性：TCP 提供可靠的傳送，而 UDP 不保證傳送的可靠性。因此，如果對資料的完整性有更高的要求，則選用 TCP；如果通信需要更快的速度，則選用 UDP。

4. ORM

- https://www.explainthis.io/zh-hant/swe/orm-intro

5. MVC, MVVM

- https://www.explainthis.io/zh-hant/swe/orm-intro


6. HTTP, TCP, SOCKET 關係?

- TCP/IP 代表傳輸控制協定/網路協定，指的是一系列協定族。
- HTTP 本身就是一個協議，是從 Web 伺服器傳輸超文本到本機瀏覽器的傳送協議。
- Socket 是 TCP/IP 網路的 API，其實就是門面模式，它把複雜的 TCP/IP 協定族隱藏在 Socket 介面後面。 對使用者來說，一組簡單的介面就是全部，讓 Socket 去組織數據，以符合指定的協定。

綜上所述：

- 需要 IP 協定來連接網路。
- TCP 是一種允許我們安全地傳輸資料的機制，使用 TCP 協定來傳輸資料的 HTTP 是 Web 伺服器和用戶端使用的特殊協定。
- HTTP 是基於 TCP 協議，所以可以使用 Socket 去建立一個 TCP 連線。

6. HTTP 長連接, 短連接

HTTP 協定的長連接和短連接，實質上是 TCP 協定的長連接和短連接。

短連接
	- 在 HTTP/1.0 中預設使用短鏈接，也就是說，瀏覽器和伺服器每進行一次 HTTP 操作，就建立一次連接，但任務結束就中斷連接。 如果用戶端存取的某個 HTML 或其他類型的 Web 資源，如 JavaScript 檔案、圖像檔案、CSS 檔案等。 當瀏覽器每遇到這樣一個 Web 資源，就會建立一個 HTTP 會話。

長連接
	- 從 HTTP/1.1 起，預設使用長連接，以保持連接特性。 在使用長連線的情況下，當一個網頁開啟完成後，用戶端和伺服器之間用於傳輸 HTTP 資料的 TCP 連線不會關閉。 如果客戶端再次造訪這個伺服器上的網頁，會繼續使用這一條已經建立的連線。 Keep-Alive 不會永久保持連接，它有一個保持時間，可以在不同的伺服器軟體（如 Apache）中設定這個時間。

### Ref
- https://github.com/yongxinz/backend-interview