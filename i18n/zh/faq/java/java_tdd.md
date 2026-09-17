<!-- 24a9dff233e7 -->
# Java TDD FAQ

> **範圍** — Java 的測試驅動開發：red-green-refactor 循環、JUnit 5、AAA 結構、mock，以及什麼才值得測。
> **另見**：[`java_spring.md`](./java_spring.md) — Spring 的測試切片；[`java_exception.md`](./java_exception.md) — 對失敗做斷言。

<!-- 7b06ce1b028a -->
## 總覽

**測試驅動開發（TDD）**是先寫一個會失敗的測試，*再*寫讓它通過的程式碼。測試是拿來
驅動設計的，不只是驗證設計。

<!-- 7dabf376ed41 -->
## 1) Red-Green-Refactor 循環

<!--CODE-->

- **Red（紅燈）**：測試失敗（證明它真的在測東西）。
- **Green（綠燈）**：用最簡單的方式讓它通過 —— 就算先寫死也行，之後再一般化。
- **Refactor（重構）**：在通過的測試這張安全網底下改善結構。
- 每個循環盡量短（幾分鐘）。綠燈時就 commit。

**好處**：覆蓋率是寫出來就有的、回饋更快、設計更好（可測試就是低耦合）、測試本身
是活的文件、重構有信心。

---

<!-- 204e1f705e24 -->
## 2) JUnit 5 基礎

<!--CODE-->

常見的註解與斷言：

| 註解 | 意義 |
|------------|---------|
| `@Test` | 標記一個測試方法 |
| `@BeforeEach` / `@AfterEach` | 每個測試前／後各跑一次 |
| `@BeforeAll` / `@AfterAll` | 只跑一次（static） |
| `@DisplayName` | 給測試一個人看得懂的名字 |
| `@ParameterizedTest` | 用多組輸入跑同一個測試 |
| `@Disabled` | 跳過某個測試 |

主要斷言：`assertEquals`、`assertTrue`/`assertFalse`、`assertNull`、
`assertThrows`、`assertAll`（成組）、`assertTimeout`。

---

<!-- 744e57b2fb70 -->
## 3) 測試結構：AAA（Arrange-Act-Assert）

<!--CODE-->

一個測試只斷言一件邏輯上的行為。測試名稱要說行為
（`throwsWhenCartEmpty`），不要說實作。

---

<!-- 950cd5c65ab7 -->
## 4) Mock

用 test double 把受測單元和慢的、外部的協作者（資料庫、網路、時間）隔開。
以 Mockito 為例：

<!--CODE-->

名詞：
- **Stub**：回傳事先安排好的答案。
- **Mock**：stub 再加上記錄與驗證互動。
- **Fake**：輕量但真的能跑的實作（例如記憶體版的 repository）。

該 mock 的是**你自己掌握邊界的協作者**；不要去 mock 值物件，也不要 mock 受測類別本身。

---

<!-- b78978ee7e4a -->
## 5) 什麼該測

- **該測**：商業邏輯、邊界案例、界線值（0、1、最大值、空、null）、
  錯誤路徑，以及每一個分支與判斷。
- **不必糾結**：無腦的 getter/setter、框架自己的程式碼，或是 mock 到最後
  測試只是把實作再抄一遍。
- **FIRST** 原則：Fast（快）、Independent（獨立）、Repeatable（可重複）、
  Self-validating（自我驗證）、Timely（及時）。
- 目標是：一個測試只會因為一個理由失敗，而且讀起來就像行為的規格書。
