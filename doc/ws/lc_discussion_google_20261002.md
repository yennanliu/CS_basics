# LeetCode Discuss 掃描：google（2026-10-02）

> 掃描範圍：LeetCode Discuss 上最近 200 篇提到 Google 的貼文（2026-07-29 – 2026-10-02），
> 約 125 篇與 Google 面試有關，其中約 30 篇寫出實際的 coding 題目，全部完整讀過。
> 用 `/lc-discussion google` 產生；LC 狀態取自 `data/progress.txt` 的最新結果（`xref`），
> README 的 status 欄並列參考。`相近` 表示貼文沒寫題號，是依題意對應到的題目。

## 值得讀的貼文

L3 相關的放前面。

| 日期 | 貼文 | 內容 |
|---|---|---|
| 09-27 | [Google India L3，已接 offer](https://leetcode.com/discuss/post/8543506/google-india-l3-interview-experience-off-1uw5/)（60▲） | Phone screen：LC 3026 的變形，改成回傳 index。Onsite：兩題「包在故事裡」的題目（a/b 字串最多切 2 刀平分給兩人；刪掉一個元素後，剩下的能否切成兩段和相等的子陣列） |
| 09-18 | [Google SWE II (L3)，India](https://leetcode.com/discuss/post/8527382/google-swe-ii-l3-interview-experience-in-hkeb/) | 相鄰差恰好為 1 的最長遞增子序列，follow-up 是差在 1..d 之間。從 u 到 v 所需的最小 security level。帶權二元樹上，切掉總權重最小的一組邊，讓每個葉節點都和根斷開，follow-up 是 N 元樹和負權重 |
| 09-16 | [Google L3 SWE，3 輪技術面](https://leetcode.com/discuss/post/8524882/google-l3-swe-interviews-content-safety-pmcnq/) | 用 line sweep 合併排班表。用 max-heap 實作 AdService，同一則廣告不能連續回傳兩次。計算「magical substring pairs」（作者自評這輪是 No Hire） |
| 10-01 | [L3 phone screen](https://leetcode.com/discuss/post/8550611/google-l3-interview-phone-screen-chances-8xik/) | 在 N 個點中數正方形（含旋轉的），O(N²)。Follow-up：重複的點，然後改成數菱形 |
| 09-13 | [L3 coding 輪](https://leetcode.com/discuss/post/8519361/l3-google-interview-experience-by-anonym-y2b5/) | 括號加上數字驅動的刪除規則，用 memoized backtracking 解 |
| 09-17 | [過去 6 個月 L3/L4 題目彙整](https://leetcode.com/discuss/post/8525983/questions-asked-by-google-in-past-6-mont-mma6/)（44▲） | 從其他貼文整理出約 75 題。有重複，但是目前最完整的一份清單 |
| 09-15 | [L4，附完整程式碼](https://leetcode.com/discuss/post/8522716/google-l4-interview-experience-by-grewal-12ay/) | 老鼠找路並盡量遠離貓（BFS + binary search）。LC 963。LC 1235。「區間內的值是否都不重複？」每次查詢 O(1) |
| 09-16 | [L4，2026 年 8 月](https://leetcode.com/discuss/post/8525084/google-l4-interview-experience-aug-2026-xbov1/) | LC 1101（`相近`），follow-up 加入 unfriend 事件（DSU with rollback）。Alice 走最短路徑並避開 Bob |

## 重複出現的題型

- **LC 1235 Job Scheduling。** 兩篇不同的 onsite 心得都考到，面試官期待 O(n log n)（取/不取的 DP 加 binary search）。
- **樹 DP：切斷帶權的邊，讓每個葉節點都和根斷開。** 兩篇各自獨立回報，一位是實習生，一位是 L3。
- **格子路徑計數，每一步只能往右、右上或右下**，follow-up 是「必須經過 (a,b)」或必經檢查點。出現兩次。
- **資料流版本的 Logger Rate Limiter（LC 359）。** 出現三種變形，其中一種要求重複的兩筆*都*隱藏，所以必須先把訊息 buffer 起來。
- **由點集組成矩形或正方形**（LC 939/963 和含旋轉的正方形計數）。5 次以上。
- **島嶼裡的湖或封閉島嶼**，還有「湖裡又有陸地」的變形。
- **2026 年的新東西：AI 相關問題。** 兩篇心得的面試一開頭就問「你怎麼使用 AI 產生的程式碼、怎麼驗證它」。最好先準備好答案。
- 綜觀這些貼文，決定一輪結果的通常不是主題本身，而是 follow-up 鏈：先解完一題，面試官加一個變化，再逼你從 DP 優化到最佳複雜度。

## 對照你的練習紀錄

### 從沒記錄過，但至少有一篇心得考到

| LC | 題目 | 出處 |
|---|---|---|
| 1235 | Maximum Profit in Job Scheduling | 2 次 onsite（README 的 status 欄也沒有標記） |
| 3026 | Maximum Good Subarray Sum | L3 phone screen |
| 1101 | The Earliest Moment When Everyone Become Friends | L4 onsite（`相近`） |
| 2812 / 1102 | Find the Safest Path in a Grid / Path With Maximum Minimum Value | 老鼠與貓那題（`相近`） |
| 1218 | Longest Arithmetic Subsequence of Given Difference | L3 phone screen（「差恰好為 1」那題，`相近`） |
| 1970 | Last Day Where You Can Still Cross | 彙整清單，加上蓋塔那題的變形 |
| 1293 | Shortest Path in a Grid with Obstacles Elimination | 訊號格子 BFS 那題的 follow-up（`相近`） |
| 381 | Insert Delete GetRandom O(1) - Duplicates allowed | 「probabilistic map」那題（`相近`） |
| 68 / 60 / 351 | Text Justification / Permutation Sequence / Android Unlock Patterns | 彙整清單 |
| 3453 | Separate Squares I | 用一條水平線把所有正方形面積切成兩半那題（`相近`，README 裡沒有） |

### 有紀錄，但最新結果是 `again` 或沒有結果

963、939（矩形）、1254（湖）、2402（Meeting Rooms III）、1631 和 778（minimax path 這一類，對應 security level 那題）、
621 和 358（AdService 的 cooldown follow-up）、329、410、128、91、743。

### 已經 `ok`

200、207、994、127、767、694、278、224、3。

### 關於「2026 年確認出現的 8 題」

[那篇](https://leetcode.com/discuss/post/8546037/google-2026-interviews-8-leetcode-proble-6c8a/)
（200、416、174、410、128、346、315、2188）是從 Glassdoor 和 Blind 整理題型，不是第一手心得，作者自己也有提醒這點。
上面那些第一手貼文比較可信。

## 建議的下一輪練習

先做 **1235、3026、1218、2812、1101**。每一題都直接來自 L3 或 L4 的面試，而且都還不在練習紀錄裡。
做完用 `/lc-log` 記錄：

```text
1235(ok|again), 3026(ok|again), 1218(ok|again), 2812(ok|again), 1101(ok|again)
```

重新產生這份對照：

```bash
python3 script/lc_discussion.py xref 1235 3026 1101 2812 1102 1218 1970 1293 381 68 60 351 3453
```
