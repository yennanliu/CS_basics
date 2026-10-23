# LeetCode Discuss 掃描：google（2026-10-23）

> 掃描範圍：LeetCode Discuss 上最近 100 篇符合 `google` 的貼文，其中 53 篇落在最近 30 天內
> （2026-09-24 – 2026-10-22）。約 10 篇寫出實際的 coding 題目，加上 1 篇彙整清單，全部完整讀過。
> 用 `/lc-discussion google --days 30` 產生；LC 狀態取自 `data/progress.txt` 的最新結果（`xref`），
> README 的 status 欄並列參考。`相近` 表示貼文沒寫題號，是依題意對應到的題目。
>
> 與上一份 [`lc_discussion_google_20261002.md`](./lc_discussion_google_20261002.md) 重疊的貼文
> （8543506、8546037）標為 `前次已收`，不重複算成新的出現次數。

## 值得讀的貼文

L3 相關的放前面。

| 日期 | 貼文 | 內容 |
|---|---|---|
| 10-10 | [L3 phone screen + Googlyness](https://leetcode.com/discuss/post/8565687/google-l3-phone-screen-googlyness-by-ano-9bci/) | 員工排班 `[name, start, end]`，把時間軸切成不重疊的區段，每段列出當時在班的所有人（sweep line，`相近` LC 1943）。作者沒先講思路就開始寫，有 bug，面試官給例子後修好 |
| 10-05 | [L3 phone screen，求評估](https://leetcode.com/discuss/post/8556519/l3-google-chances-by-anonymous_user-wpyw/) | 開場先問 2–3 題「寫 code 時怎麼用 AI、怎麼驗證」。題目：N 個點數正方形（含旋轉的），O(N²) 用對角線推另外兩點；follow-up 重複點、改數菱形。作者自評 Lean Hire 左右 |
| 09-27 | [Google India L3，已接 offer](https://leetcode.com/discuss/post/8543506/google-india-l3-interview-experience-off-1uw5/)（128▲，`前次已收`） | Phone screen：LC 3026 變形，回傳 index。Onsite：a/b 字串最多切 2 刀平分給兩人；刪掉一個元素後能否切成兩段和相等的子陣列 |
| 10-21 | [Google 線上 coding 測驗（OA）](https://leetcode.com/discuss/post/8581940/google-online-coding-challenge-for-sde-2-lwfa/) | 機器人 `R`/`L`/`S` 碰撞計數，對撞算 2、撞到靜止算 1。範例 `RLRSLL → 5` 與 LC 2211 完全相同（`相近`） |
| 10-20 | [L4 DSA round 1](https://leetcode.com/discuss/post/8580786/google-l4-dsa-round-1-by-anonymous_user-hr02/)（12▲） | 每個節點最多 3 個鄰居的無環圖，找一個當根後是二元樹的節點；follow-up：每層顏色交替、圖不連通、三色 RWB 循環。最後一題要 O(n)，作者沒在時間內推出（re-rooting，`無對應 LC`） |
| 10-09 | [L4，10/5 面試，被拒](https://leetcode.com/discuss/post/8564735/google-l4-interview-experience-date-octo-iq9r/) | 衛星圖 `X`/`.` 格子，給一個座標做 BFS（LC 200 / 695 `相近`）；follow-up 數被島嶼包住的湖（LC 1254 `相近`）。作者沒聽懂 follow-up，反省應該更早釐清題意 |
| 10-03 | [Google 2026 年 8 月 virtual](https://leetcode.com/discuss/post/8552887/google-august-2026-virtual-by-avi4each-nmck/)（9▲） | 老鼠從起點走到終點，路徑的安全度 = 經過格子到貓的最小 Manhattan 距離，求最大值；follow-up 多隻貓（LC 2812 `相近`） |
| 10-14 | [SDE 3 phone screen，USA](https://leetcode.com/discuss/post/8572982/google-sde-3-interview-phone-screen-expe-ov8g/)（11▲） | 傳輸線邊權在 0..1，求起點到終點效率最大的路徑（LC 1514 `相近`）。面試官追問為何不能 BFS；作者沒問 constraints、沒跑例子，被拒 |
| 10-22 | [Cloud AI engineer onsite](https://leetcode.com/discuss/post/8583957/recent-google-interview-experience-for-g-gkmp/) | 子陣列分數 = 頭 + 尾 − 中間元素和，求最大值。拆成 `(a[i] + P[i+1]) + (a[j] − P[j])` 後就是 LC 1014 的形狀（`相近`）。作者因 code 有 bug 未過 |
| 10-02 | [L4 India，被拒](https://leetcode.com/discuss/post/8551877/google-l4-software-engineer-india-aug-20-zgn1/)（29▲） | LC 1 Two Sum（named），之後 15 分鐘機率題：F 回傳最好的 4 則廣告、G 回傳一組中最差的，各只能呼叫一次，求選到最佳廣告的最高機率（`無對應 LC`）。結果 Lean No Hire |

只有過程、沒有題目的貼文（team matching、薪資、等結果、L5/L6 求評估）沒有列入。
[8542384](https://leetcode.com/discuss/post/8542384/google-l3-team-match-india-2yoe-by-anony-90y3/)
只提到 L3 onsite 有一輪考 segment tree，沒有題目細節。

## 重複出現的題型

- **格子上「離危險最遠」的路徑（LC 2812）。** 這次 [8552887](https://leetcode.com/discuss/post/8552887/google-august-2026-virtual-by-avi4each-nmck/)，
  加上前次的 [8522716](https://leetcode.com/discuss/post/8522716/google-l4-interview-experience-by-grewal-12ay/)，
  兩篇獨立回報都是老鼠與貓，follow-up 都會走到多隻貓 = multi-source BFS。
- **點集組成正方形，含旋轉的。** 這次 8556519，前次 8550611，兩篇不同的 L3 phone screen；follow-up 都是重複點與菱形。
  核心是「兩點當對角線，推出另外兩點，查 set」。
- **最佳路徑的變形：** 最大化乘積（1514，Dijkstra 改成 max-heap）和最大化最小值（2812）。
  面試官會追問「為什麼 BFS 不行」——要能說出邊權不同時 BFS 的層序不等於最佳順序。
- **prefix sum 拆式子。** Cloud AI 那題、3026、「刪一個元素後切成等和兩段」三題都是同一招：
  把 pair 的分數拆成只跟 `i` 有關 + 只跟 `j` 有關的兩項，掃一遍維護前面的最佳值或 hashmap。
- **島嶼與湖。** 從座標出發的 flood fill，follow-up 數被包住的湖；前次也有同類（1254 / 湖裡有陸地）。
- **非 coding 的新趨勢：** L3 phone screen 開場問 AI 使用習慣（8556519），另一篇
  [8567411](https://leetcode.com/discuss/post/8567411/google-swe-l3-ai-fluency-round-what-to-e-whzz/) 提到 team match 後有「AI Fluency」輪。

### 關於「2026 年確認出現的 8 題」（`前次已收`）

[8546037](https://leetcode.com/discuss/post/8546037/google-2026-interviews-8-leetcode-proble-6c8a/)
（200、416、174、410、128、346、315、2188）是從 Glassdoor、Blind 整理的題型清單，不是第一手心得，作者自己也這樣說。
只當題型參考，不算出現次數。

## 對照你的練習紀錄

狀態欄格式：`log 最新結果 / README status`。

### 從沒記錄過

| LC | 題目 | 出處 | 狀態 |
|---|---|---|---|
| 2211 | Count Collisions on a Road | OA 機器人碰撞（`相近`，範例相同） | never / 不在 README |
| 1014 | Best Sightseeing Pair | Cloud AI 子陣列頭尾減中間（`相近`） | never / AGAIN |
| 963 | Minimum Area Rectangle II | 含旋轉的正方形計數（`相近`，同一招對角線） | never / AGAIN |
| 2025 | Maximum Number of Ways to Partition an Array | 刪一個元素後切成等和兩段（`相近`） | never / 不在 README |
| 2188 | Minimum Time to Finish the Race | 彙整清單（非第一手） | never / 不在 README |

### 有紀錄但最新結果是 again 或沒有結果

| LC | 題目 | 出處 | 狀態 |
|---|---|---|---|
| 2812 | Find the Safest Path in a Grid | 老鼠與貓（`相近`，兩篇） | again（10-03）/ AGAIN |
| 3026 | Maximum Good Subarray Sum | L3 phone screen（named，`前次已收`） | again（10-04）/ AGAIN |
| 310 | Minimum Height Trees | L4 找合法的根（re-rooting 題型參考） | again / AGAIN |
| 1943 | Describe the Painting | L3 排班時間軸（`相近`） | none / AGAIN |
| 759 | Employee Free Time | L3 排班時間軸（同類） | none / AGAIN |
| 1254 | Number of Closed Islands | L4 湖（`相近`） | none / AGAIN |
| 1020 | Number of Enclaves | L4 湖（同類） | none / AGAIN |
| 695 | Max Area of Island | L4 從座標 flood fill（`相近`） | none / AGAIN |
| 2013 | Detect Squares | 正方形計數（軸對齊版本） | none / 不在 README |
| 128 | Longest Consecutive Sequence | 彙整清單 | again / AGAIN |
| 410 | Split Array Largest Sum | 彙整清單 | none / AGAIN |
| 174 | Dungeon Game | 彙整清單 | none / 不在 README |
| 315 | Count of Smaller Numbers After Self | 彙整清單 | none / 不在 README |
| 346 | Moving Average from Data Stream | 彙整清單 | none / OK |

### 已經 ok

| LC | 題目 | 出處 | 狀態 |
|---|---|---|---|
| 1514 | Path with Maximum Probability | SDE 3 phone screen（`相近`） | ok（07-13）/ AGAIN |
| 200 | Number of Islands | L4 flood fill、彙整清單 | ok（09-17）/ OK |
| 1 | Two Sum | L4 India（named） | ok（09-17）/ OK |
| 416 | Partition Equal Subset Sum | 彙整清單 | ok（09-16）/ AGAIN |

## 建議的下一輪練習

先做 **2812、963、2211、1014、3026**：

1. **2812**：兩篇獨立回報，上次 `again`。先寫 multi-source BFS 求每格到貓的距離，再 binary search 或 max-heap 找路徑。
2. **963**：正方形計數連續兩次出現在 L3 phone screen，從沒練過；做完再試「數旋轉正方形」與菱形 follow-up。
3. **2211**：10/21 的 OA 原題，從沒練過，stack 或「去掉兩端往外走的車」都可以。
4. **1014**：學會把 pair 分數拆成 `f(i) + g(j)`，就能解 Cloud AI 那題。
5. **3026**：上次 `again`，L3 phone screen 原題變形；練回傳 index 的版本。

時間夠再補 **1943**（L3 排班 sweep line）和 **1254**（湖）。做完用 `/lc-log` 記錄：

```text
2812(ok|again), 963(ok|again), 2211(ok|again), 1014(ok|again), 3026(ok|again)
```

重新產生這份對照：

```bash
python3 script/lc_discussion.py xref 2211 1014 963 2025 2188 2812 3026 310 1943 759 1254 1020 695 2013 128 410 174 315 346 1514 200 1 416
```
