<!-- 7dd919fd5809 -->
# 單調堆疊資料結構

> **範圍** — next greater／previous smaller／span／直方圖這類題目 — 堆疊本身保持有序，所以每個元素只被推入與彈出一次。
> **另見**：[stack.md](./stack.md) — 單純的 LIFO 題目；[monotonic_queue.md](./monotonic_queue.md) — 滑動視窗版的對應物；[heap.md](./heap.md) — 當你要的是全域極值而不是鄰近極值時。

<!-- 54006b7abcb6 -->
## LeetCode 題目清單

- [Monotonic Stack](https://leetcode.com/problem-list/monotonic-stack/)
- [Stack](https://leetcode.com/problem-list/stack/)

<!-- 525b2bf37b06 -->
## 總覽
**單調堆疊**是一種特化的堆疊，內部元素永遠維持單調（嚴格遞增或嚴格遞減）的順序。它能有效率地解決 next greater／smaller 元素、直方圖面積，以及序列最佳化這類問題。

<!-- 263bfa8298c5 -->
### 關鍵性質
- **時間複雜度**：大多數操作是 O(n)（每個元素只推入／彈出一次）
- **空間複雜度**：O(n)，用來存堆疊
- **核心想法**：依序處理元素的同時，維持堆疊的單調性
- **什麼時候用**：找 next/previous greater/smaller 元素、直方圖題、序列最佳化

<!-- f4be869618a6 -->
### 參考資料
- [LeetCode Monotonic Stack Pattern](https://leetcode.com/tag/monotonic-stack/)
- [Stack Data Structure Fundamentals](https://en.wikipedia.org/wiki/Stack_(abstract_data_type))

<!-- 5ab0cfa81ec9 -->
## 題型分類

<!-- 8be361d467ed -->
### **模式 1：Next/Previous Greater Element** — LC 739
- **描述**：找出比目前元素大的下一個或前一個元素
- **範例**：LC 496（Next Greater Element I）、LC 503（Next Greater Element II）、LC 739（Daily Temperatures）
- **模式**：用遞減單調堆疊，遇到更大的元素就彈出

<!-- 61739f0cd485 -->
### **模式 2：Next/Previous Smaller Element** — LC 84
- **描述**：找出比目前元素小的下一個或前一個元素
- **範例**：LC 84（Largest Rectangle）、LC 42（Trapping Rain Water）、LC 907（Sum of Subarray Minimums）
- **模式**：用遞增單調堆疊，遇到更小的元素就彈出

<!-- e6c573186815 -->
### **模式 3：直方圖與面積問題** — LC 84
- **描述**：用高度資訊算面積、矩形或體積
- **範例**：LC 84（Largest Rectangle in Histogram）、LC 42（Trapping Rain Water）、LC 85（Maximal Rectangle）
- **模式**：用單調堆疊找出邊界，再算邊界之間的面積

<!-- ca4c5ca6cb5f -->
### **模式 4：序列順序與驗證** — LC 456
- **描述**：驗證序列、找出特定樣式，或維持順序限制
- **範例**：LC 456（132 Pattern）、LC 901（Online Stock Span）、LC 1856（Maximum Subarray Min-Product）
- **模式**：用堆疊維持序列性質並驗證樣式

<!-- d491615a2120 -->
### **模式 5：最佳化與最大／最小** — LC 1793
- **描述**：在最大或最小限制下找最佳解
- **範例**：LC 1944（Number of Visible People）、LC 2104（Sum of Subarray Ranges）、LC 1793（Maximum Score）
- **模式**：用單調性質維持最佳候選

<!-- dd1b536e0105 -->
### **模式 6：環狀陣列** — LC 503
- **描述**：處理環狀或循環的陣列問題
- **範例**：LC 503（Next Greater Element II）、LC 853（Car Fleet II）
- **模式**：把陣列走兩遍，或用模運算搭配單調堆疊

<!-- ddc7f484e4af -->
## 模板與演算法

<!-- 15e4c715a845 -->
### 模板比較表
| 模板類型 | 使用情境 | 堆疊順序 | 什麼時候用 |
|---------------|----------|-------------|-------------|
| **遞減堆疊** | Next/Previous Greater | 遞減 | 找比目前元素大的元素 |
| **遞增堆疊** | Next/Previous Smaller | 遞增 | 找比目前元素小的元素 |
| **直方圖面積** | 矩形／面積問題 | 遞增 | 用高度算面積 |
| **環狀陣列** | 循環問題 | 視情況 | 處理環狀序列 |
| **樣式驗證** | 序列驗證 | 視情況 | 驗證特定樣式 |
| **最佳化堆疊** | 最大／最小問題 | 視情況 | 維持最佳候選 |

<!-- 2cc4178b0f27 -->
### 通用模板

<!--CODE-->

<!--CODE-->

<!-- 7b8ce189a59c -->
### 模板 1：Next Greater Element（遞減堆疊） — LC 496

<!--CODE-->

<!--CODE-->

<!-- 75f120a39672 -->
### 模板 2：Next Smaller Element（遞增堆疊） — LC 84

<!--CODE-->

<!-- 73442667d780 -->
### 模板 3：Largest Rectangle in Histogram — LC 84

<!--CODE-->

<!--CODE-->

<!-- a950019aaf62 -->
### 模板 4：環狀陣列處理 — LC 503

<!--CODE-->

<!-- 80d29d4f8e51 -->
### 模板 5：堆疊中帶額外資訊

<!--CODE-->

<!-- 8e57cba46d2f -->
### 模板 6：樣式驗證（132 Pattern） — LC 456

<!--CODE-->

<!-- c8b75ab397b3 -->
## 依模式分類的題目

<!-- cae2934e6af7 -->
### 以模式分類的題目清單

<!-- 91ec32e45234 -->
#### **模式 1：Next/Previous Greater Element 題目**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Next Greater Element I | 496 | 遞減堆疊 | Easy | 模板 1 |
| Next Greater Element II | 503 | 環狀陣列 | Medium | 模板 4 |
| Daily Temperatures | 739 | 計算距離 | Medium | 模板 1 |
| Remove K Digits | 402 | 貪婪 + 堆疊 | Medium | 模板 1 |
| Remove Duplicate Letters | 316 | 字典序 + 堆疊 | Medium | 模板 1 |
| Sliding Window Maximum | 239 | 單調雙端佇列 | Hard | 模板 1 |
| Shortest Unsorted Array | 581 | 兩趟堆疊 | Medium | 模板 1 |
| Sum of Subarray Ranges | 2104 | next greater + smaller | Medium | 模板 1+2 |

<!-- c58b3c0ea998 -->
#### **模式 2：Next/Previous Smaller Element 題目**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Largest Rectangle in Histogram | 84 | 計算面積 | Hard | 模板 3 |
| Maximal Rectangle | 85 | 二維直方圖 | Hard | 模板 3 |
| Sum of Subarray Minimums | 907 | 貢獻法 | Medium | 模板 2 |
| Number of Valid Subarrays | 1063 | 數較小元素 | Medium | 模板 2 |
| Minimum Cost Tree From Leaf Values | 1130 | 最佳合併 | Medium | 模板 2 |
| Find the Most Competitive Subsequence | 1673 | 選子序列 | Medium | 模板 2 |
| Maximum Subarray Min-Product | 1856 | 以最小值為樞紐 | Medium | 模板 2 |

<!-- 533d2a1d1c45 -->
#### **模式 3：直方圖與面積題目**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Trapping Rain Water | 42 | 計算水位 | Hard | 模板 2 |
| Container With Most Water | 11 | 也可用雙指標 | Medium | 模板 2 |
| Maximal Rectangle | 85 | 逐列直方圖 | Hard | 模板 3 |
| Maximum Rectangle | 221 | DP + 直方圖 | Medium | 模板 3 |
| Minimum Number of Taps | 1326 | 區間覆蓋 | Hard | 模板 2 |
| Constrained Subsequence Sum | 1425 | DP + 單調雙端佇列 | Hard | 模板 2 |

<!-- ac48766bb14d -->
#### **模式 4：序列順序與驗證題目**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| 132 Pattern | 456 | 樣式偵測 | Medium | 模板 6 |
| Online Stock Span | 901 | 單調堆疊 | Medium | 模板 1 |
| Score of Parentheses | 856 | 巢狀結構 | Medium | 模板 5 |
| Valid Parenthesis String | 678 | 平衡驗證 | Medium | 模板 5 |
| Minimum Add to Make Parentheses Valid | 921 | 平衡計數 | Medium | 模板 5 |
| Validate Stack Sequences | 946 | 模擬序列 | Medium | 模板 5 |
| Maximum Nesting Depth of Parentheses | 1614 | 追蹤深度 | Easy | 模板 5 |
| Minimum Remove to Make Valid Parentheses | 1249 | 平衡 + 移除 | Medium | 模板 5 |

<!-- ce8aff03aebe -->
#### **模式 5：最佳化與最大／最小題目**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Maximum Score of Good Subarray | 1793 | 雙指標 + 堆疊 | Hard | 模板 2 |
| Number of Visible People in Queue | 1944 | 視線問題 | Medium | 模板 1 |
| Car Fleet | 853 | 計算時間 | Medium | 模板 1 |
| Car Fleet II | 1776 | 碰撞時間 | Hard | 模板 1 |
| Buildings With Ocean View | 1762 | 由右往左掃 | Medium | 模板 1 |
| Find the Winner of Circular Game | 1823 | 約瑟夫問題 | Medium | 模板 4 |
| Maximum Width Ramp | 962 | 索引差 | Medium | 模板 1 |
| Pancake Sorting | 969 | 反轉操作 | Medium | 模板 1 |

<!-- 8623d6c14c1a -->
#### **模式 6：環狀陣列題目**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Next Greater Element II | 503 | 走兩遍陣列 | Medium | 模板 4 |
| Car Fleet II | 1776 | 環狀碰撞 | Hard | 模板 4 |
| Circular Array Loop | 457 | 環偵測 | Medium | 模板 4 |
| Design Circular Queue | 622 | 環狀緩衝區 | Medium | 模板 4 |
| Design Circular Deque | 641 | 雙端環狀 | Medium | 模板 4 |

<!-- 637355cce2fe -->
#### **進階／混合模式題目**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Sum of Total Strength of Wizards | 2281 | 多個堆疊 | Hard | 多個 |
| Number of Ways to Rearrange Sticks | 1866 | 組合數學 + 堆疊 | Hard | 模板 5 |
| Basic Calculator | 224 | 運算式求值 | Hard | 模板 5 |
| Basic Calculator II | 227 | 運算子優先序 | Medium | 模板 5 |
| Basic Calculator III | 772 | 完整運算式解析 | Hard | 模板 5 |
| Evaluate Reverse Polish Notation | 150 | 後序求值 | Medium | 模板 5 |
| Decode String | 394 | 巢狀解碼 | Medium | 模板 5 |
| Find Duplicate Subtrees | 652 | 樹的序列化 | Medium | 模板 5 |
| Exclusive Time of Functions | 636 | 模擬呼叫堆疊 | Medium | 模板 5 |
| Minimum Window Subsequence | 727 | 雙指標 + 堆疊 | Hard | 模板 5 |

<!-- fd42e616b25c -->
### 題目難度分布
- **Easy（8 題）**：基本的 next greater/smaller、簡單驗證
- **Medium（28 題）**：最常見的難度，涵蓋各種模式
- **Hard（16 題）**：複雜的面積計算、進階最佳化

<!-- 8881145ad28f -->
### 模板使用頻率
- **模板 1（Next Greater）**：15 題
- **模板 2（Next Smaller）**：12 題  
- **模板 3（直方圖）**：8 題
- **模板 4（環狀）**：6 題
- **模板 5（驗證／複雜）**：11 題
- **多個模板混用**：8 題

<!-- 25bf1aebdd68 -->
## 模式選擇策略

<!-- b924d018f09b -->
### 決策流程圖

<!--CODE-->

<!-- 37688ee07c72 -->
### 逐步分析題目

1. **先看清核心需求**
   - 查詢 next/previous 元素 → 模板 1、2、4
   - 計算面積／矩形 → 模板 3
   - 樣式驗證 → 模板 5、6
   - 最佳化問題 → 模板 1、2、5

2. **決定堆疊順序**
   - 要找較大的元素 → 遞減堆疊（彈出較小的）
   - 要找較小的元素 → 遞增堆疊（彈出較大的）
   - 面積計算 → 通常是遞增堆疊
   - 樣式偵測 → 視樣式而定

3. **決定處理方向**
   - 由左往右：最常見，也最自然
   - 由右往左：找「next」元素時有時比較好寫
   - 環狀：把陣列處理多遍

4. **決定堆疊裡放什麼**
   - 索引：需要位置資訊時
   - 值：只需要比大小時
   - Tuple：需要額外資訊時

<!-- 7d5d392b53d9 -->
### 模板選擇速查

| 題型 | 模板 | 堆疊內容 | 處理順序 |
|--------------|----------|---------------|------------------|
| **Next Greater** | 模板 1 | 索引 | 由左往右 |
| **Next Smaller** | 模板 2 | 索引 | 由左往右 |
| **Previous Greater** | 模板 1 | 索引 | 由左往右 |
| **Previous Smaller** | 模板 2 | 索引 | 由左往右 |
| **直方圖面積** | 模板 3 | 索引 | 由左往右 |
| **環狀陣列** | 模板 4 | 索引 | 走兩遍 |
| **樣式偵測** | 模板 6 | 值 | 由右往左 |
| **複雜驗證** | 模板 5 | Tuple | 視情況 |

<!-- ea4fa3974f42 -->
## 總結與速查

<!-- 4ac1c03c0ebb -->
### 複雜度速查
| 操作 | 時間 | 空間 | 備註 |
|-----------|------|-------|-------|
| **推入堆疊** | O(1) | - | 每個元素只推入一次 |
| **彈出堆疊** | O(1) | - | 每個元素只彈出一次 |
| **整體演算法** | O(n) | O(n) | 攤還線性時間 |
| **Next Greater/Smaller** | O(n) | O(n) | 單趟掃過陣列 |
| **直方圖面積** | O(n) | O(n) | 帶堆疊的線性掃描 |
| **環狀陣列** | O(n) | O(n) | 走兩趟，複雜度不變 |

<!-- ac9608f5d17e -->
### 模板速查
| 模板 | 模式 | 關鍵程式碼 |
|----------|---------|------------------|
| **模板 1** | Next Greater | `while stack and nums[stack[-1]] < nums[i]` |
| **模板 2** | Next Smaller | `while stack and nums[stack[-1]] > nums[i]` |
| **模板 3** | 直方圖 | `while stack and heights[stack[-1]] > h` |
| **模板 4** | 環狀 | `for i in range(2 * n)` |
| **模板 5** | 驗證 | 在堆疊中存額外資訊 |
| **模板 6** | 樣式偵測 | 由右往左，同時追蹤條件 |

<!-- c3d0316b51ed -->
### 常見模式與技巧

<!-- e3817760927f -->
#### **Next Greater Element 模式**
<!--CODE-->

<!-- 6d9613eb0878 -->
#### **子陣列的貢獻法**
<!--CODE-->

<!-- c95196d81d5f -->
#### **直方圖面積計算**
<!--CODE-->

<!-- c10c78f63d3d -->
### 解題步驟

1. **步驟 1：判斷模式類型**
   - 找關鍵字：next/previous、greater/smaller、面積、矩形
   - 確認有沒有環狀／循環的需求
   - 判斷是否需要驗證或樣式偵測

2. **步驟 2：挑對模板**
   - 用決策流程圖
   - 想清楚堆疊順序（遞增還是遞減）
   - 決定堆疊裡要存什麼資訊

3. **步驟 3：實作核心邏輯**
   - 準備好堆疊與結果容器
   - 寫出 while 迴圈，彈出條件要對
   - 妥善處理彈出的元素
   - 處理最後留在堆疊裡的元素

4. **步驟 4：處理邊界情況**
   - 空陣列
   - 只有一個元素
   - 所有元素都相同
   - 嚴格遞增／遞減的序列

5. **步驟 5：最佳化與驗證**
   - 確認時間複雜度是 O(n)
   - 檢查空間複雜度
   - 用範例輸入驗證
   - 必要時處理整數溢位

<!-- 16ba5a0b35bf -->
### 常見錯誤與提醒

<!-- 8be916f099a9 -->
#### 常見錯誤
- **堆疊順序搞反**：next greater 題卻用遞增堆疊（應該用遞減）
- **索引與值混淆**：需要算距離時卻只存了值
- **處理不完整**：忘了處理最後留在堆疊裡的元素
- **邊界問題**：沒有妥善處理堆疊為空的情況
- **環狀邏輯**：環狀陣列處理不正確（漏了第二趟）
- **條件寫錯**：比較運算子用錯（< vs <=、> vs >=）

<!-- 9c68fc7d1a8f -->
#### 最佳實務
- 需要位置資訊時，**一律存索引**
- 用**哨兵值**（例如 0）簡化邊界處理
- 除非真的需要由右往左，否則**由左往右處理**
- **變數名要清楚**：用 `stack`、`result`、`current_idx`，不要用 `s`、`res`、`i`
- **在 while 條件加註解**，說明維持的單調性質
- **先處理邊界情況**，再寫主要演算法

<!-- 4cb0f4188d5e -->
### 面試技巧

1. **辨識模式**
   - 聽到「next greater/smaller」這類關鍵字就要有反應
   - 面積／矩形題常常用得上單調堆疊
   - 序列驗證題可能需要以堆疊為基礎的做法

2. **解題流程**
   - 先從暴力解開始，把題目搞懂
   - 判斷單調性質能不能把解法優化
   - 畫例子，把堆疊的行為視覺化

3. **面試時的溝通**
   - 解釋為什麼這題適合用單調堆疊
   - 用例子把堆疊的狀態走一遍給面試官看
   - 討論時間／空間複雜度的取捨

4. **實作提醒**
   - 從模板骨架開始寫
   - 把心力放在 while 條件寫對
   - 用簡單的例子測試（像 [2,1,2,4,3,1]）

5. **預期會有的追問**
   - 有重複值怎麼辦？
   - 如果要的是 previous 而不是 next 呢？
   - 空間複雜度還能再優化嗎？
   - 怎麼延伸到二維問題？

<!-- 4258aaf6e39b -->
### 相關主題

- **堆疊**：單調堆疊是堆疊這個資料結構的特化應用
- **雙端佇列**：滑動視窗最大值用的是單調雙端佇列
- **雙指標**：某些面積計算題的替代解法
- **動態規劃**：有些最佳化問題會把 DP 和單調堆疊搭在一起
- **二分搜尋**：在有序結構中找邊界
- **線段樹**：區間最大／最小值的進階查詢

<!-- 598a9bfd2a3c -->
## LC 範例

<!-- 31306025281d -->
### 2-1) Daily Temperatures (LC 739) — 單調遞減堆疊
> 堆疊存索引；遇到更暖的一天就彈出。

<!--CODE-->

<!-- e1949cf091ab -->
### 2-2) Largest Rectangle in Histogram (LC 84) — 單調遞增堆疊
> 來了比較矮的柱子就彈出；用堆疊算出寬度後求面積。

<!--CODE-->

<!-- dfcdfb12cb0c -->
### 2-3) Next Greater Element I (LC 496) — 單調堆疊 + HashMap
> 先把 nums2 的 next greater 全部算好，再回答 nums1 的查詢。

<!--CODE-->

<!-- 8fc2287cd16b -->
### 2-4) Trapping Rain Water (LC 42) — 單調堆疊
> 來了比較高的柱子就彈出；接到的水 =（較小高度的差）* 寬度。

<!--CODE-->

<!-- fbcf2b452d79 -->
### 2-5) Next Greater Element II (LC 503) — 環狀單調堆疊
> 把陣列走兩遍（或用模運算）來處理環狀的 next greater 查詢。

<!--CODE-->

<!-- 930d692dd877 -->
### 2-6) Online Stock Span (LC 901) — 單調遞減堆疊
> 把先前 <= 目前價格的都彈掉；span = 距離上一個更高價過了幾天。

<!--CODE-->

<!-- 7d08827d3d07 -->
### 2-7) Sum of Subarray Minimums (LC 907) — 單調堆疊
> 對每個元素，找出它是最小值的左右邊界；用單調堆疊做。

<!--CODE-->

<!-- 474f95c71ae0 -->
#### **貢獻法 — 把 `left[i]` / `right[i]` 視覺化（Python）** ⭐⭐⭐⭐⭐

> `leetcode_python/Math/sum-of-subarray-minimums.py`

**核心想法：** 每個子陣列都恰好有一個最小值，所以不要去列舉子陣列，而是反過來問*「有多少個子陣列的最小值是 `arr[i]`？」* — 然後把 `arr[i] * count` 全部加起來。

對每個索引 `i`，這個數量會拆成兩個互相獨立的選擇：

<!--CODE-->

- 一個子陣列要讓 `arr[i]` 當最小值，就必須**起點**落在 `(PSE, i]`、**終點**落在 `[i, NSE)`。
- 兩個範圍互相獨立 → 相乘。

**處理重複值（避免重複計算）：** 左邊那趟用 **`>=`**、右邊那趟用 **`>`**（刻意不對稱）。這樣相等的值只會被算在其中一側。

<!--CODE-->

**堆疊為空時為什麼 `left[i] = i + 1`：** 堆疊為空代表左邊沒有任何元素比 `arr[i]` 小 — `arr[i]` 主宰了整個前綴。想像中的左邊界落在索引 `-1`，所以左邊的選擇涵蓋索引 `0..i`，也就是 `i - (-1) = i + 1`。

**在 `arr = [3, 1, 2, 4]` 上的圖解追蹤：**

<!--CODE-->

<!-- c61086ab6fcd -->
### 2-8) Remove K Digits (LC 402) — 單調遞增堆疊
> 維持遞增堆疊；來了比較小的數字就把前面的移掉。

<!--CODE-->

<!-- 1bc2d9f8b988 -->
### 2-9) Maximal Rectangle (LC 85) — 直方圖 + 單調堆疊
> 逐列算出直方圖高度；每一列套用 LC 84 的最大矩形邏輯。

<!--CODE-->

<!-- 69c97626aefb -->
### 2-10) Car Fleet (LC 853) — 對速度做單調堆疊
> 按位置排序；堆疊記錄車隊 — 併入前車隊的車就被移掉。

<!--CODE-->

<!-- dc3b63e72478 -->
### 2-11) Asteroid Collision (LC 735) — 堆疊模擬
> 向右飛的留在堆疊上；向左飛的一直和頂端相撞，直到穩定。

<!--CODE-->

<!-- 88d2b0451a37 -->
### 2-12) Sum of Subarray Ranges (LC 2104) — 雙單調堆疊（貢獻法）

> `sum(ranges) = sum(subarray maxs) − sum(subarray mins)`。最大值與最小值各跑一趟單調堆疊；每彈出一個元素，就算出它以最大／最小值的身分主宰了多少個子陣列。

<!-- c94b241d2d85 -->
#### 核心想法

<!--CODE-->

對每個元素 `nums[mid]`，找出它的**左**、**右**主宰邊界：
- **左邊界** `L` — 前一個會讓 `nums[mid]` 失去最大／最小身分的元素索引（沒有就是 `-1`）
- **右邊界** `R` — 下一個會取代它的元素索引（沒有就是 `n`）

`nums[mid]` 擔任最大／最小值的子陣列數量：
<!--CODE-->

**哨兵迴圈**讓 `i` 從 `0` 跑到 `n`（含）。當 `i == n` 時，用 `n` 當右邊界把堆疊裡剩下的索引全部清掉。

**對重複值安全的邊界規則**（避免相等元素被重複計算）：
- **最大值**那趟：`nums[mid] < nums[i]` 時彈出（嚴格）；左邊界是上一個*大於或等於*的元素。
- **最小值**那趟：`nums[mid] > nums[i]` 時彈出（嚴格）；左邊界是上一個*小於或等於*的元素。

---

<!-- 9a67ca029914 -->
#### 圖解追蹤 — `[1, 3, 2]` 的最大值那趟

<!--CODE-->

---

<!-- 30f2dc5ab2fe -->
#### 模式（Python）

<!--CODE-->

<!-- 16053810b080 -->
#### 模式（Java）

<!--CODE-->

<!-- da53403dc968 -->
#### 雙堆疊邏輯總結

| 趟次 | 堆疊類型 | 彈出條件 | 算出什麼 |
|------|-----------|---------------|----------|
| 最大值那趟 | 單調**遞減** | `nums[mid] < nums[i]` | 所有子陣列最大值的總和 |
| 最小值那趟 | 單調**遞增** | `nums[mid] > nums[i]` | 所有子陣列最小值的總和 |
| 兩趟都有 | `i = n` 的哨兵 | 一律清空 | 處理靠右邊界的元素 |

<!-- 5c1ae72d15d1 -->
#### 相似題目

| 題目 | LC# | 關鍵差異 |
|---------|-----|----------------|
| Sum of Subarray Ranges | 2104 | `max_sum − min_sum`；兩趟單調堆疊 |
| Sum of Subarray Minimums | 907 | 只算最小值的貢獻；單趟遞增堆疊 |
| Maximum Subarray Min-Product | 1856 | 最小值貢獻 × 子陣列和；前綴和 + 堆疊 |
| Sum of Total Strength of Wizards | 2281 | 最小值 × 和的和；前綴和的前綴和 + 堆疊 |
| Largest Rectangle in Histogram | 84 | 面積 = 高 × 寬；遇到較矮的柱子彈出 |
| Number of Visible People in Queue | 1944 | 每個元素彈出的次數就是答案 |

<!-- ca6b89cc9b36 -->
### 2-13) Longest Absolute File Path (LC 388) — 以巢狀深度為索引的堆疊 ⭐⭐⭐⭐⭐

> **模板 7：深度堆疊。** 這個堆疊不是按*值*單調，而是按**深度**單調：`stack[d]` 永遠存著深度 `d` 的累積路徑長度。處理深度為 `d` 的那一行之前，先彈到 `stack.size() == d`，就把剛結束的兄弟分支全部丟掉了。

**核心想法**
<!--CODE-->
- `depth` = 開頭 `\t` 的數量；剩下的部分就是名稱。
- **目錄**會推入 `parentLen + name.length() + 1`（`+1` 是 `/` 分隔符）。
- **檔案**（名稱含 `.`）永遠不推入 — 只用 `parentLen + name.length()` 更新答案。

<!--CODE-->

<!--CODE-->

**陷阱**
- 答案是最長的**到檔案的路徑**，所以碰到目錄時絕對不要更新最大值。
- 不要切掉字串之後才用 `line.count('\t')` 算深度 — 深度只能來自*開頭*的 tab。
- 空輸入／完全沒有檔案 → 回傳 `0`。

<!-- d04846452069 -->
### 2-14) Longest Valid Parentheses (LC 32) — 索引堆疊搭配基準哨兵 ⭐⭐⭐⭐⭐

> **模板 8：索引堆疊 + 哨兵基準。** 不要存字元，改存**索引**，並且先塞一個 `-1` 當作「目前這段合法區塊前一格的索引」。遇到 `)` 彈出之後，新的堆疊頂端就是最後一個沒配對到的索引，所以 `i - stack.peek()` 就是以 `i` 結尾的合法長度 — 完全不用另外記長度。

**遇到 `)` 的兩種情況**
<!--CODE-->

**在 `s = ")()())"` 上的圖解追蹤**
<!--CODE-->

<!--CODE-->

<!--CODE-->

**陷阱**
- 忘了先塞 `-1`，所有從索引 `0` 開始的區段都會算錯。
- 堆疊裡存的是**索引**，絕不是字元 — 整個技巧就靠索引運算。
- O(1) 空間的替代解：走兩趟（左→右，再右→左），用 `open`／`close` 計數，當 `close > open`（反向時是 `open > close`）就歸零。

<!-- 8929466af548 -->
### 2-15) Maximum Binary Tree (LC 654) — 用單調遞減堆疊建出笛卡兒樹 ⭐⭐⭐⭐

> **模板 9：用單調堆疊建樹。** 直覺的「找最大值，再左右遞迴」是 O(n²)。用**遞減**堆疊可以一趟建出同一棵樹：被 `num` 彈掉的元素都比 `num` 小、而且都在它左邊 → 它們變成 `num` 的**左**子樹；活下來的堆疊頂端比 `num` 大 → `num` 變成它的**右**子節點。根就是堆疊最底部那個。

<!--CODE-->

<!--CODE-->

<!--CODE-->

**為什麼 `cur.left` 會被一直覆寫：** 每彈出一次就重新指定一次 `cur.left`，而被彈出的節點彼此早就串好了（先被彈出的是前一個節點的右子節點），所以迴圈結束後 `cur.left` 正好指向整個被彈出區塊的根。

**相關：** LC 1008（Construct BST from Preorder Traversal）用的是鏡像的想法 — 遞增堆疊，較大的值成為最後一個被彈出節點的右子節點。

<!-- bf6afc600b9f -->
### 2-16) Min Stack (LC 155) — 輔助的非遞增堆疊 ⭐⭐⭐⭐

> **模板 10：平行的「最小值堆疊」。** 另外維護一個值**非遞增**的堆疊；它的頂端永遠是目前存活元素中的最小值。這就是單調堆疊在設計題裡的樣子。

<!--CODE-->

<!--CODE-->

**經典 bug：** 只在 `val < mins.peek()`（嚴格）時才推入新的最小值。碰到 `push(0); push(0); pop();`，那唯一存下的 `0` 會被移掉，`getMin()` 就回傳錯的值。要用 `<=`。
**省空間的變形：** 在單一堆疊裡存 `(val, minSoFar)` 這種 pair — 操作一樣是 O(1)，而且面試壓力下比較好講清楚。

<!-- a6e487ff662c -->
### 2-17) 既有模板的各種變形

| LC # | 題目 | 基礎模板 | 變化點 |
|------|---------|---------------|-----------|
| 1475 | Final Prices With a Special Discount in a Shop | 模板 2（next smaller） | 是 next smaller **或相等** — 條件改成 `prices[stack[-1]] >= prices[i]` 才彈出，而且折扣是 `price - prices[i]`，不是索引距離 |
| 1019 | Next Greater Node In Linked List | 模板 1（next greater） | 一樣的遞減堆疊，但輸入是鏈結串列 — 先走一趟轉成陣列（或邊走邊推入 `(index, val)`），因為答案陣列需要隨機存取 |
| 768 | Max Chunks To Make Sorted II | 模板 1（遞減彈出） | 堆疊裡放的是**各區塊的最大值**，不是原始元素；答案 = 最後的堆疊大小 |
| 769 | Max Chunks To Make Sorted | 模板 1（退化版） | 值是 `0..n-1` 的排列，所以用一個 running max 就能取代堆疊：只要 `runningMax == i` 就切一塊 |
| 1047 / 1209 | Remove All Adjacent Duplicates In String (I / II) | 模板 5（堆疊帶資訊） | 堆疊存 `(char, count)`；`count` 到 `k` 就彈出 — LC 1047 就是 `k = 2` 的特例 |

**Max Chunks To Make Sorted II (LC 768) — 區塊最大值堆疊**

<!--CODE-->

<!--CODE-->

<!-- 6a475faad7f1 -->
### 2-18) 值得知道的經典堆疊題（非單調）

> 這些用的是普通堆疊（沒有單調不變式），但常常和上面那些模式一起出現。

| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Simplify Path | 71 | 用 `/` 切開；元件推入，`..` 彈出，`.`／空字串跳過 | Medium |
| Backspace String Compare | 844 | 每個字串一個堆疊，或從後往前用雙指標做到 O(1) 空間 | Easy |
| Remove All Adjacent Duplicates In String | 1047 | 推入字元，和頂端相同就彈出 | Easy |
| Remove All Adjacent Duplicates in String II | 1209 | 堆疊存 `(char, count)`，count 到 `k` 就彈出 | Medium |
| Flatten Nested List Iterator | 341 | 堆疊裡放 iterator／list；在 `hasNext()` 中惰性攤平 | Medium |
| Binary Search Tree Iterator | 173 | 受控的迭代中序 — 堆疊存左脊 | Medium |
| Maximum Frequency Stack | 895 | `freq` map + 從頻率對應到值堆疊的 map | Hard |
| Baseball Game | 682 | 直接用堆疊模擬 `+`、`D`、`C` | Easy |
