<!-- 90b6849d0b31 -->
# Python 並發 FAQ

> **範圍** — Python 實際上怎麼跑並發：GIL、執行緒與行程與 `asyncio` 的取捨、同步原語，以及該怎麼選。
> **另見**：[`faq_python.md`](./faq_python.md) — 語言本身；
> [`cs_basic.md`](./cs_basic.md) — 作業系統層面的行程、執行緒與排程。

---

<!-- 5cc9d90fecac -->
## 1) GIL ⭐⭐⭐⭐⭐

**全域直譯器鎖（Global Interpreter Lock）**是 CPython 裡的一把互斥鎖，它讓
**同一時間只有一條執行緒能執行 Python bytecode**，就算你有 16 核也一樣。

**它為什麼存在**：CPython 的記憶體管理是參考計數，而計數的更新不是原子的。一把粗粒度
的鎖讓直譯器（以及所有依它撰寫的 C 擴充）既正確、單執行緒又快，代價是沒辦法平行執行
bytecode。

**它擋住什麼、不擋什麼：**

| 工作 | 開多執行緒有用嗎？ | 為什麼 |
|------|---------------------|-----|
| CPU 密集的純 Python（迴圈、解析、數學） | ❌ 沒用 | 同時只有一條執行緒握著 GIL |
| 阻塞 I/O（網路、磁碟、`subprocess`） | ✅ 有用 | 系統呼叫期間 GIL 會**被釋放** |
| `time.sleep` | ✅ 有用 | 會釋放 GIL |
| NumPy / pandas / `hashlib` / 壓縮之類的重運算 | ✅ 通常有用 | C 擴充在長時間運算期間會釋放 GIL |

所以真正該記住的規則是：**等待用執行緒，運算用行程。**

<!--CODE-->

直譯器大約每 5 ms（`sys.setswitchinterval`）或在執行緒阻塞時切換一次。Python 3.13 出了
一個**實驗性的 free-threaded 版本**，沒有 GIL；在它成為預設之前，就照上面那張表理解。

---

<!-- 650c7280ec70 -->
## 2) 三種模型 ⭐⭐⭐⭐⭐

| | `threading` | `multiprocessing` | `asyncio` |
|---|-------------|-------------------|-----------|
| 單位 | OS 執行緒 | OS 行程 | 協程（同一條執行緒） |
| CPU 平行 | ❌（GIL） | ✅ 真正的平行 | ❌ |
| 並發 I/O | ✅ | ✅ | ✅✅（幾千個 socket） |
| 記憶體 | 共享 | 各自獨立 —— 資料要**pickle** 過去 | 共享 |
| 每單位成本 | 約 8 MB stack，啟動微秒級 | 數 MB，啟動毫秒級 | 約幾 KB，切換奈秒級 |
| 切換方式 | 搶佔式（任何 bytecode 邊界） | OS 排程器 | **協作式** —— 只在 `await` 處 |
| 失敗模式 | 資料競態 | 序列化成本、沒有共享狀態 | 一個阻塞呼叫凍住全部 |
| 適合 | 阻塞 I/O、舊的函式庫 | CPU 密集工作 | 大量並發網路呼叫 |

**一句話選擇：**

- **大量網路／磁碟等待，而函式庫是阻塞式的** → 執行緒（或執行緒池）。
- **大量網路等待，而函式庫是非同步的** → `asyncio`。
- **純 Python 的大量計算** → 行程（或用 NumPy，或把迴圈搬進 C）。
- **兩者都有** → I/O 用 `asyncio`，CPU 的部分交給 `ProcessPoolExecutor`。

---

<!-- c2efd736073a -->
## 3) 執行緒 ⭐⭐⭐⭐

<!-- 3ee499c1c3bb -->
### 該用的 API 是 `concurrent.futures`

<!--CODE-->

- `pool.map(fn, items)` 會保持輸入順序，想要全部結果時很適合。
- `as_completed` 誰先完成就先給誰 —— 顯示進度或提早結束時比較好。
- **future 的例外只有在你呼叫 `.result()` 時才會被丟出來。**忘了呼叫就會把失敗
  靜靜吞掉；這是用 executor 最常見的 bug。
- 池的大小要照**等待量**抓，不是照核心數：做 I/O 時 `max_workers` 幾十個很正常。

<!-- 3e3bf81f1dfe -->
### 競態還是會發生

GIL **不會**讓你的程式碼變成執行緒安全 —— 它只是把 bytecode 的執行序列化，而
`counter += 1` 是一個讀-改-寫，會被編成好幾個 bytecode（幾個要看版本），
所以切換可能正好落在中間。

<!--CODE-->

| 原語 | 用途 |
|-----------|-----|
| `Lock` | 互斥 |
| `RLock` | 可重入 —— 同一條執行緒可以再取得一次 |
| `Semaphore(n)` | 限制並發數（例如同時 5 個 API 呼叫） |
| `Event` | 一次性的廣播旗標（`set()` / `wait()`） |
| `Condition` | 等某個條件成立，然後被通知 |
| `Barrier(n)` | 讓 n 條執行緒一起放行 |
| `queue.Queue` | **執行緒安全的交棒** —— 優先用它，不要共享 list 再加鎖 |
| `threading.local()` | 每條執行緒各自的狀態（request id、DB 連線） |

用 `queue.Queue` 寫的生產者／消費者完全不需要顯式加鎖 —— 對於大多數「執行緒之間怎麼
共享資料」的問題，它就是那個標準答案。

<!-- ad3721bbd4ce -->
### Daemon 執行緒與關閉

daemon 執行緒不會讓行程活著，而且會在結束時被粗暴地砍掉 —— 千萬不要把你在意的
`finally` 放在裡面。要乾淨地關掉 worker，請用佇列上的哨兵值或一個 `Event`。

---

<!-- 2b956baa8dc6 -->
## 4) 行程 ⭐⭐⭐⭐

<!--CODE-->

會咬人的地方：

- **任何跨越邊界的東西都會被 pickle。**lambda、閉包、開著的 socket 與資料庫
  handle 都過不去。送純資料，不要送包著資源的物件。
- **啟動方式**：`fork`（Linux 在 3.14 之前的預設 —— 快，但和執行緒併用不安全）、
  `spawn`（macOS/Windows 預設 —— 全新的直譯器，所以模組層級的程式碼會重跑一次）、
  `forkserver`。多數「在我的 Linux 上就好好的」都出在這裡的行為差異。
- **把工作分塊。**每個項目的 IPC 開銷可能遠大於運算本身；`chunksize` 是常見解法。
- 共享狀態：訊息用 `multiprocessing.Queue`/`Pipe`，大筆資料用 `Value`/`Array` 或
  `shared_memory`，`Manager` 方便但慢（它會透過一個伺服器行程做代理）。

---

<!-- 5f47b5be779d -->
## 5) asyncio ⭐⭐⭐⭐⭐

一條執行緒、一個事件迴圈、很多協程。協程會一直跑到遇上 `await`，然後把控制權交回
迴圈，讓迴圈去跑其他準備好的工作。

<!--CODE-->

<!-- 26b908754f5f -->
### 規則

- **`await` 是任務唯一能被暫停的地方。**協作式排程的意思是：一個慢的同步呼叫會擋住
  *其他所有*任務。
- **絕對不要在協程裡呼叫阻塞的程式碼** —— `time.sleep`、`requests.get`、吃重的 CPU
  迴圈。改用非同步版本（`asyncio.sleep`、`httpx`/`aiohttp`），或把它推出迴圈：
  `await asyncio.to_thread(blocking_fn, arg)`。
- 呼叫一個協程函式在被 await 或被排程之前什麼都不會發生 —— 少寫 `await` 的
  `fetch(url)` 就是那個經典的「怎麼什麼事都沒發生？」。
- 並發來自**任務**，不是來自 `await` 本身：在迴圈裡一個一個 await 是循序的；
  `TaskGroup` / `asyncio.gather` 才是並發。

| 工具 | 做什麼 |
|------|------|
| `asyncio.run(main())` | 在整個程式的生命週期裡擁有那個事件迴圈 |
| `TaskGroup`（3.11+） | 結構化並發：等全部完成，出錯時取消兄弟任務，丟出 `ExceptionGroup` |
| `gather(*aws)` | 比較舊的等價物；`return_exceptions=True` 可以收集錯誤而不是立刻失敗 |
| `wait_for(aw, timeout)` / `asyncio.timeout()` | 期限 —— 網路呼叫永遠要加一個 |
| `Semaphore` | 限制在途請求數，免得把對方打爆 |
| `Queue` | 非同步的生產者／消費者 |
| `to_thread` / `loop.run_in_executor` | 阻塞或 CPU 工作的逃生門 |

`async for` / `async with` 是非同步的迭代器與情境管理器協定
（`__aiter__`/`__anext__`、`__aenter__`/`__aexit__`）。

<!-- cdbb7589a442 -->
### 取消

取消一個任務，會在它下一個 `await` 處**在它內部**丟出 `CancelledError`。
讓它往外傳 —— 接住它然後「清理一下繼續跑」會讓關閉流程壞掉。清理放在 `finally`，
而 `asyncio.shield` 只留給那種非完成不可的少數操作。

---

<!-- a183650d6636 -->
## 6) 實例比較

同一件工作 —— 100 次 HTTP 呼叫，再對每個回應算 hash —— 三種寫法：

<!--CODE-->

面試官想聽到的重點：**說清楚你在等什麼。**等待用執行緒或 `asyncio` 疊起來；
運算用行程平行化。

---

<!-- fe5cd851628e -->
## 7) 面試常見問答

**Q：為什麼多執行緒沒有讓我 CPU 密集的 Python 變快？**
GIL 把 bytecode 的執行序列化了。改用 `multiprocessing`、用會釋放 GIL 的 C 擴充
函式庫（NumPy），或換一個 runtime。

**Q：既然有 GIL，為什麼還需要鎖？**
因為 GIL 會在 bytecode 之間被釋放。`x += 1`、先檢查再動作，以及任何多步驟的
不變條件，都可能被交錯。

**Q：一萬個並發連線，該用 `asyncio` 還是執行緒？**
`asyncio`。一萬條 OS 執行緒代表好幾 GB 的 stack 與沉重的 context switch；
一萬個協程每個只有幾 KB，切換就跟函式返回差不多。

**Q：什麼是競態條件？在 Python 裡怎麼找出來？**
兩條執行緒在共享的可變狀態上交錯。製造競爭來重現它（開更多執行緒、把
`sys.setswitchinterval` 調小），然後靠縮小共享狀態來修 —— 訊息傳遞優於鎖。

**Q：什麼是死鎖？怎麼避免？**
兩條執行緒各自握著對方要的鎖。用**全域一致的順序**取鎖、用
`acquire(timeout=…)`、把臨界區縮短。四個 Coffman 條件見
[`cs_basic.md`](./cs_basic.md)。

**Q：怎麼給工作設時限？**
非同步程式用 `asyncio.timeout()` / `wait_for`；用 executor 時用
`future.result(timeout=…)`。注意 Python 裡沒辦法強制砍掉一條執行緒 —— 設計上要用
協作式的停止旗標，或改用可以 terminate 的行程。

**Q：`threading.local()` 是做什麼的？**
存放不能共享、每條執行緒各自的狀態：寫 log 用的 request id、資料庫 session。
`asyncio` 的對應物是 `contextvars.ContextVar`，而且它還能跨 `await` 存活。

---

<!-- d53e2f2ac0ca -->
## 8) 重點檢查表

<!--CODE-->

---

<!-- 80e4e7fdf764 -->
## 參考資料

- [`asyncio` — Python 官方文件](https://docs.python.org/3/library/asyncio.html)
- [`concurrent.futures` — Python 官方文件](https://docs.python.org/3/library/concurrent.futures.html)
- [PEP 703 — 讓 GIL 變成可選](https://peps.python.org/pep-0703/)
- [`faq_python.md`](./faq_python.md) — 語言本身
