# Hash Map Cheatsheet（雜湊表速查）

> **範圍** — 鍵→值的題型模式：查找、分組、索引表、前綴和表、重新映射。
> **另見** — *從本檔切出去的深入內容*：[hash_map_examples.md](./hash_map_examples.md) — 題解庫、單題深入（桶排序、rolling hash、拆半探測、最大頻率算術），以及有序 map（Java `TreeMap` / Python `SortedDict`）的參考。
> *鄰近的 cheatsheet*：[hashing.md](./hashing.md) — 雜湊本身怎麼運作，以及計數與 rolling hash 的慣用寫法；[set.md](./set.md) — 只管有沒有，不存值；[Collection.md](./Collection.md) — 一開始該選哪個容器。

## LeetCode 題目清單

- [Hash Table](https://leetcode.com/problem-list/hash-table/)

## 時間複雜度

| 資料結構 | 搜尋   | 插入   | 刪除   | 最小／最大  |
| -------------- | -------- | -------- | -------- | -------- |
| 雜湊表（平均） | O(1)     | O(1)     | O(1)     | O(n)     |

> 表中是平均情況。**最壞情況（所有鍵都碰撞）：O(n)。** 求最小／最大值必須全掃，因為雜湊不保證任何順序。

## 總覽
雜湊表（Hash Table／Dictionary）是最基本的資料結構之一，提供高效率的鍵值儲存與取用。

### 關鍵性質
- **複雜度**：見上面的[時間複雜度](#time-complexity)表；空間是 **O(n)**
- **底層實作**：陣列 + 鏈結串列／紅黑樹（Java HashMap）
- **雜湊碰撞**：用鏈接法（chaining）或開放定址法處理

### 什麼時候會發生雜湊碰撞
- **負載因子 > 0.75**：效能開始退化
- **雜湊函式不好**：很多鍵落到同一個 bucket
- **Java HashMap**：鏈結串列長度 > 8 時會轉成紅黑樹
- **解法**：鏈接法（每個 bucket 一條串列或一棵樹）或開放定址 —— 見 [hash_map_collision.md](https://github.com/yennanliu/CS_basics/blob/master/doc/hash_map_collision.md)

<p align="center"><img src="../pic/hash_op_101.png"></p>

- [NC - HashMap under the hood](https://www.linkedin.com/posts/neetcodeio_how-do-hashmaps-work-under-the-hood-activity-7298370869301526530-DsIi?utm_source=social_share_send&utm_medium=member_desktop_web&rcm=ACoAAA6fzw4BpOSBO1YeSrJwPZ-dNBhjC3jXTDE)

### 為什麼查找是 O(1)

- FAQ
    - 為什麼 hashmap 的搜尋時間複雜度 ~= O(1)？怎麼解釋？
        - 一句話：O(1) 是平均與最好情況。最壞情況可能是 O(N)（雜湊碰撞）
        - 雜湊函式很關鍵 -> 決定資料怎麼存、以及碰撞會不會發生
        - 操作
            - 插入
                - 拿到 key，用雜湊函式算出 hash 值
                - 依 hash 值在記憶體中找到對應 bucket
                - 把 key 和 value 存進該 bucket
            - 查詢
                - 依 key 算出 index
                - 依 index 找到 bucket 位置
                    - 注意！！！這裡用位元運算（`int pos = (n - 1) & hash`），所以這步是 O(1)（直接算出 bucket 位址，不必掃過所有元素）
                - 走訪該 key 底下的所有元素（只有一個的話就一次）
                - 回傳 value
        <p align="center"><img src="../pic/hash_map1.png"></p>
        <p align="center"><img src="../pic/hash_map2.jpeg"></p>
        - [ref 1](https://blog.csdn.net/junqing_wu/article/details/104606619)
        - [ref 2](https://blog.csdn.net/john1337/article/details/104727895)

### 什麼時候該用／不該用

- 該用的時候 
	- 需要 ~ O(1) 讀寫的場景
    - 用快取換效能（空間換時間）
    - `sum, pair, continuous` 這類題目
    - 想避開雙層迴圈（O(N^2)）

- 不該用的時候
	- 資料是時間序列 
	- 資料需要保持順序 
	- https://www.reddit.com/r/learnprogramming/comments/29t4s4/when_is_it_bad_to_use_a_hash_table/

### 基本操作

- `get`：從 dict 取值，鍵不存在時給預設值
```text
In [10]: d = {'a': 1, 'b': 2}
    ...: d['a']
Out[10]: 1

In [11]: d.get('a')
Out[11]: 1

In [12]: d.get('c', 0)
Out[12]: 0

In [13]: d.get('z')

In [14]:
```

- `setdefault()`
	- https://www.w3schools.com/python/ref_dictionary_setdefault.asp
```python
#-------------------------------------------------------------------------------
# setdefault : will creatte key if key NOT existed (with value as well if defined)
#-------------------------------------------------------------------------------

# syntax
d.setdefault(new_key)
d.setdefault(new_key, new_value)

# 662 Maximum Width of Binary Tree
car = {
  "brand": "Ford",
  "model": "Mustang",
  "year": 1964
}

# example 1) insert key "my_key", since my_key not existed, -> make it as new key and value as None (since not defined)
car.setdefault("my_key")
print (car)
# In [18]: car
# Out[18]: {'brand': 'Ford', 'model': 'Mustang', 'year': 1964, 'my_key': None}

# example 2) insert key "color", since my_key not existed, -> make it as new key and value as white
car.setdefault("color", "white")
print (car)
# Out[22]:
# {'brand': 'Ford',
#  'model': 'Mustang',
#  'year': 1964,
#  'my_key': None,
#  'color': 'white'}
```

- 對 ***hashmap (dict)*** 做排序
```python
# https://stackoverflow.com/questions/613183/how-do-i-sort-a-dictionary-by-value

x = {1: 2, 3: 4, 4: 3, 2: 1, 0: 0}
In [11]: x.items()
Out[11]: dict_items([(1, 2), (3, 4), (4, 3), (2, 1), (0, 0)])

#----------------------------------
# Sort hashMap by key/value !!!
#----------------------------------
x = {1: 2, 3: 4, 4: 3, 2: 1, 0: 0}
# note : have to use sorted(xxx, key=yyy), instead of xxx.sorted(....)
### NOTE this !!! : x.items()
sorted_x = sorted(x.items(), key=lambda kv: kv[1])
print (sorted_x)
# [(0, 0), (2, 1), (1, 2), (4, 3), (3, 4)]

x = {1: 2, 3: 4, 4: 3, 2: 1, 0: 0}
sorted_x = sorted(x.items(), key=lambda kv: kv[0])
print (sorted_x)
# [(0, 0), (1, 2), (2, 1), (3, 4), (4, 3)]

# 451  Sort Characters By Frequency
import collections
class Solution(object):
    def frequencySort(self, s):
        count = collections.Counter(s)
        count_dict = dict(count)
        """
        NOTE this !!!
            1. use sorted()
            2. count_dict.items()
        """
        count_tuple_sorted = sorted(count_dict.items(), key=lambda kv : -kv[1])
        res = ''
        for item in count_tuple_sorted:
            res += item[0] * item[1]
        return res
```

```text
# dict values -> array
In [6]:
   ...: mydict = {'a':['a1','a2','a3'], 'b':['b1','b2','b3']}
   ...:
   ...: res = [mydict[x] for x in mydict]
   ...:
   ...: print (res)
[['a1', 'a2', 'a3'], ['b1', 'b2', 'b3']]
```

### 參考資料

- [Java HashMap](https://bbs.huaweicloud.com/blogs/276884?utm_source=juejin&utm_medium=bbs-ex&utm_campaign=other&utm_content=content)
    - 底層：陣列 + 鏈結串列／紅黑樹
        - 鏈結串列長度 > 8 -> 把串列轉成紅黑樹
        - 鏈結串列長度 < 6 -> 把紅黑樹轉回串列
- N sum：
    - [n_sum.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/n_sum.md)
- LC 參考
    - [prefix_sum.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/prefix_sum.md)
- 其他 
	- https://blog.techbridge.cc/2017/01/21/simple-hash-table-intro/
	- https://www.freecodecamp.org/news/hash-tables/

## 模板與演算法

### 模板對照表

| # | 模板 | map 的形狀 | 怎麼認出來 | 代表 LC |
|---|----------|-----------|-----------------|------------|
| 1 | 頻率計數 | `{item: count}` | 「count」、「frequency」、「anagram」、「top-K」 | 242, 49, 347, 451 |
| 2 | 已見過的索引表 | `{value: index}` | 「找一組配對」、「湊出 target」、補數 | 1, 15, 532, 1010 |
| 3 | 用算出來的鍵分組 | `{canonical_key: [items]}` | 「group」、「同一條線」、「同一個特徵」 | 49, 149, 609, 987 |
| 4 | 前綴和 → 計數表 | `{prefixSum: count}` / `{prefixSum: firstIndex}` | 「子陣列和等於 K／可被 K 整除」 | 560, 974, 525, 325 |
| 5 | 滑動視窗 + 字元計數 | `{char: count in window}` | 「最長／最短的子字串，使得 ...」 | 3, 76, 424, 438, 567 |
| 6 | 排名表 | `{value: rank}` | 「依照 ... 給定的順序」 | 953, 791, 105 |
| 7 | 雙射（兩個 map） | `{x: y}` **和** `{y: x}` | 「一對一」、「isomorphic」、「符合這個 pattern」 | 205, 290 |
| 8 | 快取／記憶化 | `{state: result}` | 「O(1) 的 get 和 put」、「把遞迴記憶化」 | 146, 460, 139, 322 |
| 9 | 圖／樹的節點表 | `{node: neighbours}`、`{child: parent}` | 「clone」、「父指標」、「序列化子樹」 | 133, 138, 652, 1257 |
| 10 | 虛擬映射（重新映射） | `{badIndex: goodIndex}` | 「隨機挑，但要排除 ...」 | 710 |
| 11 | map + 另一個結構 | map + 堆積／堆疊／第二個 map | 「用堆積做 top-K」、「next greater」 | 347, 496, 739 |

> 那些其實是針對一兩題的深入討論 —— O(n) top-K 的桶排序、rolling hash、拆半探測的配對查找、最大頻率算術，以及有序 map（`TreeMap` / `SortedDict`）—— 放在 [hash_map_examples.md](./hash_map_examples.md#templates--algorithms)。

### 模板 1：頻率計數
```python
# Universal Counting Template
def counting_pattern(arr):
    count = {}  # or collections.defaultdict(int)
    result = []
    
    # Count frequency
    for item in arr:
        count[item] = count.get(item, 0) + 1
        # or count[item] += 1 with defaultdict
    
    # Process based on frequency
    for key, freq in count.items():
        if meets_condition(freq):
            result.append(key)
    
    return result

# Examples: LC 49, LC 242, LC 451, LC 347, LC 692
```

### 模板 2：已見過的索引表（Two Sum 的形狀）
```python
# Two Sum Pattern Template
def two_sum_pattern(nums, target):
    seen = {}  # {value: index}
    
    for i, num in enumerate(nums):
        complement = target - num
        
        if complement in seen:
            return [seen[complement], i]
        
        seen[num] = i
    
    return []

# Variations:
# - Multiple pairs: collect all instead of returning first
# - K-diff pairs: check for num+k and num-k
# - Examples: LC 1, LC 167, LC 15, LC 532, LC 1010
```

### 模板 3：用算出來的鍵分組

**模式**：拿元素的*正規形式*去雜湊，而不是元素本身 —— 所有共用同一個正規形式的東西就會落到同一個 bucket。最簡單的正規形式是排序後的字串（LC 49）；一般化的情況則是某種關係的正規化不變量（LC 149）。

```python
# LC 049 Group Anagrams
# IDEA : HASH TABLE
class Solution:
    def groupAnagrams(self, strs):
        res = {}
        for item in strs:
            k = ''.join(sorted(item))  # sort the string 
            if k not in res:  #  check if exists in res 
                res[k] = []
            res[k].append(item)  # if same, put all the same string into dict k 
        return [res[x] for x in res]  # output the result 
```

#### 一般化：鍵就是一個正規化的不變量 ⭐⭐⭐⭐

**模式**：map 的鍵不是原始的值 —— 它是值與值之間某種**關係的正規形式**。兩個元素會在 map 裡撞在一起，剛好就是它們共享你在意的那個性質。

**關鍵想法（幾何）**：「通過錨點 P 的同一條線」⇔「同一個方向向量 `(dx, dy)`」。原始的 `(dx, dy)` 不能當鍵（`(1,2)` 和 `(2,4)` 是同一條線），而 `dy/dx` 用浮點數會掉精度、遇到垂直線還會除以零。**正規化**：先除以 `gcd`，再強制統一符號。

```java
// java
// LC 149 - Max Points on a Line
// IDEA: anchor at each point, group the others by a gcd-normalized slope key
// time = O(n^2), space = O(n)
public int maxPoints(int[][] points) {
    int n = points.length;
    if (n <= 2) return n;
    int best = 1;
    for (int i = 0; i < n; i++) {
        Map<String, Integer> slopeCount = new HashMap<>();
        for (int j = i + 1; j < n; j++) {
            int dx = points[j][0] - points[i][0];
            int dy = points[j][1] - points[i][1];
            int g = gcd(Math.abs(dx), Math.abs(dy));
            if (g != 0) { dx /= g; dy /= g; }
            // canonical direction: force dx > 0, or dx == 0 && dy > 0
            if (dx < 0 || (dx == 0 && dy < 0)) { dx = -dx; dy = -dy; }
            String key = dx + "/" + dy;
            int cnt = slopeCount.merge(key, 1, Integer::sum);
            best = Math.max(best, cnt + 1);   // +1 for the anchor point itself
        }
    }
    return best;
}

private int gcd(int a, int b) { return b == 0 ? a : gcd(b, a % b); }
```

```python
# python
# LC 149 - Max Points on a Line
# IDEA: anchor at each point, group the others by a gcd-normalized slope key
# time = O(n^2), space = O(n)
from collections import defaultdict
from math import gcd

def maxPoints(points: list) -> int:
    n = len(points)
    if n <= 2:
        return n
    best = 1
    for i in range(n):
        slope_count = defaultdict(int)
        x1, y1 = points[i]
        for j in range(i + 1, n):
            dx, dy = points[j][0] - x1, points[j][1] - y1
            g = gcd(abs(dx), abs(dy))
            if g:
                dx, dy = dx // g, dy // g
            if dx < 0 or (dx == 0 and dy < 0):     # canonical sign
                dx, dy = -dx, -dy
            slope_count[(dx, dy)] += 1
            best = max(best, slope_count[(dx, dy)] + 1)   # +1 = anchor point
    return best
```

**三個陷阱**（都是面試常見的追問）：
1. **浮點斜率** `dy/dx` —— 精度會掉，垂直線還會 `ZeroDivisionError`。改用約分後的整數配對。
2. **忘了正規化符號** —— `(1,2)` 和 `(-1,-2)` 是同一條線，卻變成兩個不同的鍵。要強制成同一個方向。
3. **忘了 `+1`** —— map 數的是錨點的*夥伴*，錨點自己不在 map 裡。

**變形**（都是同一招「自己發明一個正規化的鍵」）：

| 題目 | LC# | 變化點 —— 這個鍵編碼了什麼 |
|---------|-----|----------------------------------|
| Minimum Area Rectangle | 939 | 鍵 = 放進 set 的點本身；走訪**對角線配對** `(x1,y1),(x2,y2)`（`x1!=x2 && y1!=y2`），再檢查另外兩個角在不在 |
| Most Stones Removed with Same Row or Column | 947 | 鍵 = `row` 與 `~col`（位元取反讓列與行落在互不重疊的 id 空間）→ 在 map 上跑併查集 |
| Vertical Order Traversal of a Binary Tree | 987 | 鍵 = **行偏移量** `col`（root = 0、左 = `col-1`、右 = `col+1`）；值 = 待排序的 `(row, val)` 清單 |
---

### 模板 4：前綴和 → 計數表 ⭐⭐⭐⭐⭐

**先看前綴和陣列** —— `nums[i..j] 的和 = preSum[j+1] - preSum[i]`：

```python
# (algorithm book (labu) p.350)
my_array = [1,2,3,4,5]
my_array_pre = [0] * (len(my_array)+1)
cur = 0
for i in range(len(my_array)):
    cur += my_array[i]
    my_array_pre[i+1] += cur

# In [17]: print ("my_array = " + str(my_array))
#     ...: print ("my_array_pre = " + str(my_array_pre))
# my_array = [1, 2, 3, 4, 5]
# my_array_pre = [0, 1, 3, 6, 10, 15]

#-----------------------------------------------
# Get sub array sum !!!!!!!
#    -> nums[i..j] sum = preSum[j+1] - preSum[i]
#-----------------------------------------------

# example 1 : sum of [1,2]
my_array_pre[1+1] - my_array_pre[0]  # 1's index is 0, and 2's index is 1. (my_array = [1, 2, 3, 4, 5])

# example 2 : sum of [2,3,4]
my_array_pre[3+1] - my_array_pre[1] # 2's index is 1, and 4's index is 3. (my_array = [1, 2, 3, 4, 5])
```

```python
# Prefix Sum Pattern Template
def prefix_sum_pattern(nums, target):
    prefix_sum = 0
    sum_count = {0: 1}  # {sum: count/index}
    result = 0

    for num in nums:
        prefix_sum += num

        # Check if (prefix_sum - target) exists
        if prefix_sum - target in sum_count:
            result += sum_count[prefix_sum - target]

        # Update current prefix sum count
        sum_count[prefix_sum] = sum_count.get(prefix_sum, 0) + 1

    return result

# For max length problems, store index instead of count:
# sum_index = {0: -1}, then calculate i - sum_index[prefix_sum - target]
# Examples: LC 560, LC 325, LC 525, LC 523
```

**依題型分的關鍵差異**：
- **計數類**（LC 560, 930, 974）：存 `{sum: count}`，先查再更新
  - **LC 974 變形**：改用餘數 `{remainder: count}`，**一定要處理負餘數！**
- **最大長度類**（LC 325, 525）：存 `{sum: first_index}`，只在鍵是新的時候才寫入
  - **LC 525 變形**：先轉換問題（0→-1、1→+1），用 `{0: -1}` 初始化，只記第一次出現的位置
- **存在性類**（LC 523）：存 `{sum: any_index}`，找到一個就好

#### 核心模式：數出所有和為 k 的子陣列 — LC 560

**核心概念**：用雜湊表在單層迴圈、O(N) 時間內，數出所有和等於 target 的子陣列組合。

**關鍵洞見**：
```text
If we want subarray[i,j] to sum to k:
  presum[j] - presum[i-1] = k
  → presum[i-1] = presum[j] - k

So at index j, check if (presum[j] - k) exists in map!
```

**實作上的關鍵細節**：

1. **存的是次數，不是索引**：
   ```java
   Map<Integer, Integer> map = new HashMap<>();  // {prefixSum: count}
   ```
   - 同一個前綴和可能出現**很多次**
   - 我們要數出**所有**合法子陣列，不是找到一個就好
   - 例：`[1, -1, 1, -1]` 在 k=0 時有多組解

2. **用 `map.put(0, 1)` 初始化**：
   ```java
   map.put(0, 1);  // Handle subarrays starting from index 0
   ```
   - 當 `presum[j] == k` 時，`presum[j] - k = 0`
   - 這是為了把從頭開始的那些子陣列也數進去

3. **先查再更新**（順序很關鍵）：
   ```java
   for (int num : nums) {
       presum += num;

       // 1. CHECK first: count how many previous prefix sums = (presum - k)
       if (map.containsKey(presum - k)) {
           count += map.get(presum - k);  // Add ALL occurrences
       }

       // 2. UPDATE after: add current prefix sum for future iterations
       map.put(presum, map.getOrDefault(presum, 0) + 1);
   }
   ```
   - **為什麼是這個順序？** 避免當前這段子陣列跟自己配對
   - 當前的前綴和只該被*之後*的迭代看到

**為什麼這個模式能抓到所有組合**：
- map 存了先前出現過的所有前綴和與各自的次數
- 查 `presum - k` 時，拿到的是它先前出現的**所有**次數
- 每一次先前的出現，都代表一個合法的起點
- `count += map.get(presum - k)` 一次把所有以當前索引結尾的合法子陣列加進來

**例子走查**（`nums = [1,1,1], k = 2`）：
```text
i=0: num=1, presum=1
  - Check: (1-2)=-1 not in map → count=0
  - Update: map={0:1, 1:1}

i=1: num=1, presum=2
  - Check: (2-2)=0 in map, count += map[0] = 1 → count=1
  - Update: map={0:1, 1:1, 2:1}

i=2: num=1, presum=3
  - Check: (3-2)=1 in map, count += map[1] = 1 → count=2
  - Update: map={0:1, 1:1, 2:1, 3:1}

Result: count=2 (subarrays [1,1] and [1,1])
```

**相關的 LC 題目（同一模式）**：
- LC 560: Subarray Sum Equals K（就是這個模式本身）
- LC 325: Maximum Size Subarray Sum Equals k（存索引而不是次數）
- LC 930: Binary Subarrays with Sum
- **LC 974: Subarray Sums Divisible by K**（改用餘數 `{remainder: count}`，**記得處理負數！**）

**什麼時候存次數、什麼時候存索引**：
| 題型 | map 的值 | 例子 | 特別注意 |
|-------------|-----------|---------|---------------|
| 數出所有子陣列 | `count` | LC 560, 930, 974 | 先查再更新 |
| 計數（帶取餘） | `count` | **LC 974** | **用餘數當鍵；處理負數！** |
| 找最長子陣列 | `index`（第一次出現） | LC 325, 525 | 只存第一次出現 |
| 找最長（需先轉換） | `index`（第一次出現） | **LC 525** | **把 0→-1、1→+1；初始化 {0:-1}** |
| 判斷是否存在 | `boolean/index` | LC 523 | 任一次出現都可以 |

**常見錯誤**：
1. ❌ 計數類的題目卻用了 `{prefixSum: index}`
2. ❌ 在查詢之前就更新 map（會把自己也數進去）
3. ❌ 忘了 `map.put(0, 1)` 這個初始化
4. ❌ 沒處理「前綴和本身就等於 k」的情況
5. ❌ **[LC 974] 忘了處理負餘數**（Java／Python 裡 `-7 % 5 = -2`，要加上 k 才會變成 3）

---

### 模板 5：滑動視窗搭配雜湊表
```python
# Sliding Window with HashMap Template
def sliding_window_hashmap(s, pattern):
    if len(pattern) > len(s):
        return []
    
    pattern_count = {}
    window_count = {}
    
    # Count pattern frequency
    for char in pattern:
        pattern_count[char] = pattern_count.get(char, 0) + 1
    
    left = 0
    result = []
    
    for right in range(len(s)):
        # Expand window
        char = s[right]
        window_count[char] = window_count.get(char, 0) + 1
        
        # Contract window if needed
        while window_size_condition_met():
            # Check if current window is valid
            if window_count == pattern_count:
                result.append(left)
            
            # Remove leftmost character
            left_char = s[left]
            window_count[left_char] -= 1
            if window_count[left_char] == 0:
                del window_count[left_char]
            left += 1
    
    return result

# Examples: LC 3, LC 76, LC 438, LC 567
```

**map 直接比對的捷徑**（LC 567 Permutation in String）：視窗是*固定大小*時不需要 matched 計數器 —— 直接比較兩個頻率表就好。

```java
// LC 567
// ...
     /** NOTE !!!
     *
     *  we use below trick to
     *
     *  -> 1) check if `new reached s2 val` is in s1 map
     *  -> 2) check if 2 map are equal
     *
     *  -> so we have more simple code, and clean logic
     */
    if (map2.equals(map1)) {
        return true;
    }
// ...
```

---

### 模板 6：排名表 —— 值對應到位置 ⭐⭐⭐⭐⭐

**模式**：當題目自己定義了一套**順序**（「這套外星字母」、「這個排列」），先一次算好 `value -> rank`，之後每次比較就從 O(m) 的掃描變成 O(1) 的整數比較。

**怎麼認出來**：出現「依照 ... 給定的順序」這種說法 —— 那就是排名表。

```java
// java
// LC 953 - Verifying an Alien Dictionary
// IDEA: char -> rank map turns an arbitrary alphabet into comparable ints
// time = O(total chars), space = O(1)  (26 keys)
public boolean isAlienSorted(String[] words, String order) {
    int[] rank = new int[26];                       // char -> position in `order`
    for (int i = 0; i < order.length(); i++) rank[order.charAt(i) - 'a'] = i;
    for (int i = 0; i + 1 < words.length; i++) {
        if (!inOrder(words[i], words[i + 1], rank)) return false;
    }
    return true;
}

private boolean inOrder(String a, String b, int[] rank) {
    int n = Math.min(a.length(), b.length());
    for (int i = 0; i < n; i++) {
        int ra = rank[a.charAt(i) - 'a'], rb = rank[b.charAt(i) - 'a'];
        if (ra != rb) return ra < rb;
    }
    return a.length() <= b.length();                // prefix must come first: "app" < "apple"
}

// LC 791 - Custom Sort String  (counting sort, no comparator needed)
// time = O(n + m), space = O(1)
public String customSortString(String order, String s) {
    int[] cnt = new int[26];
    for (char c : s.toCharArray()) cnt[c - 'a']++;
    StringBuilder sb = new StringBuilder();
    for (char c : order.toCharArray()) {            // ranked chars first, in rank order
        while (cnt[c - 'a'] > 0) { sb.append(c); cnt[c - 'a']--; }
    }
    for (char c = 'a'; c <= 'z'; c++) {             // unranked chars: any order
        while (cnt[c - 'a'] > 0) { sb.append(c); cnt[c - 'a']--; }
    }
    return sb.toString();
}
```

```python
# python
# LC 953 - Verifying an Alien Dictionary
# IDEA: map every word to a list of ranks, then plain list comparison does the lexicographic work
# time = O(total chars), space = O(total chars)
def isAlienSorted(words: list, order: str) -> bool:
    rank = {c: i for i, c in enumerate(order)}
    keys = [[rank[c] for c in w] for w in words]
    # python list compare == lexicographic compare, and [1,2] < [1,2,3] handles the prefix rule
    return all(keys[i] <= keys[i + 1] for i in range(len(keys) - 1))

# python
# LC 791 - Custom Sort String
# IDEA: rank map as a sort key; unranked chars get rank len(order) (stable sort keeps them last)
# time = O(n log n + m), space = O(n)
def customSortString(order: str, s: str) -> str:
    rank = {c: i for i, c in enumerate(order)}
    return "".join(sorted(s, key=lambda c: rank.get(c, len(order))))
```

**前綴規則是每個人都會踩的坑**：共同前綴比完之後，*比較短*的那個字必須排在前面。`["apple", "app"]` **不是**排序好的。

**變形 —— 用「值 → 索引」表切分陣列（LC 105 / LC 106）**：同一個 map，只是這裡的「排名」是*在中序序列中的位置*，把 O(n²) 的「掃中序找 root」變成 O(1)，整個建樹變成 O(n)。

```python
# python
# LC 105 - Construct Binary Tree from Preorder and Inorder Traversal
# IDEA: {value: index in inorder} → O(1) root split
# time = O(n), space = O(n)
def buildTree(preorder: list, inorder: list):
    idx = {v: i for i, v in enumerate(inorder)}   # values are unique (given)
    pre = [0]                                     # pointer into preorder

    def build(lo, hi):
        if lo > hi:
            return None
        node = TreeNode(preorder[pre[0]])
        pre[0] += 1
        mid = idx[node.val]                       # O(1) instead of inorder.index(...)
        node.left = build(lo, mid - 1)
        node.right = build(mid + 1, hi)
        return node

    return build(0, len(inorder) - 1)
```

---

### 模板 7：雙射（雙向映射）

**模式**：同時維護兩個 map（`x→y` 與 `y→x`），並**兩個方向都**檢查一致性。只要映射必須是一對一的就用得上（LC 205 Isomorphic Strings、LC 290 Word Pattern）。

**為什麼要兩個 map？** 一個 map 抓得到 `a→b` 的衝突；第二個才抓得到 `b→a` 的衝突（兩個不同的 `x` 對到同一個 `y`）。

```python
# LC 205 Isomorphic Strings
def isIsomorphic(s: str, t: str) -> bool:
    s2t, t2s = {}, {}
    for a, b in zip(s, t):
        if s2t.get(a, b) != b or t2s.get(b, a) != a:
            return False
        s2t[a] = b
        t2s[b] = a
    return True

# LC 290 Word Pattern
def wordPattern(pattern: str, s: str) -> bool:
    words = s.split()
    if len(pattern) != len(words):
        return False
    p2w, w2p = {}, {}
    for p, w in zip(pattern, words):
        if p2w.get(p, w) != w or w2p.get(w, p) != p:
            return False
        p2w[p] = w
        w2p[w] = p
    return True
```

**常見錯誤**：只用一個 map —— 兩個鍵對到同一個值時就會出錯（`"aa"` vs `"ab"`）。

---

### 模板 8：用雜湊表做快取／記憶化
```python
# Caching/Memoization Template
class CacheTemplate:
    def __init__(self, capacity):
        self.capacity = capacity
        self.cache = {}  # key -> value
        self.usage = {}  # key -> usage_info
    
    def get(self, key):
        if key in self.cache:
            self.update_usage(key)
            return self.cache[key]
        return -1
    
    def put(self, key, value):
        if len(self.cache) >= self.capacity:
            self.evict()
        
        self.cache[key] = value
        self.update_usage(key)
    
    def update_usage(self, key):
        # Update usage tracking
        pass
    
    def evict(self):
        # Remove least recently/frequently used
        pass

# Examples: LC 146 (LRU), LC 460 (LFU)
```

> 以子問題狀態當鍵、用 dict 做的 top-down DP（LC 139, 1048, 322）也是同一份模板 —— 完整寫在 [hash_map_examples.md → Hash Map + Memoization / DP](./hash_map_examples.md#hash-map--memoization--dp)。

---

### 模板 9：用雜湊表解圖論題
```python
# Graph with HashMap Template
def graph_hashmap_pattern(graph_input):
    # Build adjacency list/map
    graph = {}  # node -> [neighbors] or node -> {neighbor: weight}
    
    for edge in graph_input:
        node1, node2 = edge[0], edge[1]
        if node1 not in graph:
            graph[node1] = []
        if node2 not in graph:
            graph[node2] = []
        
        graph[node1].append(node2)
        graph[node2].append(node1)  # for undirected
    
    # Process using DFS/BFS with visited tracking
    visited = set()
    result = []
    
    def dfs(node):
        if node in visited:
            return
        
        visited.add(node)
        result.append(node)
        
        for neighbor in graph.get(node, []):
            dfs(neighbor)
    
    return result

# Examples: LC 133, LC 200, LC 694, LC 1257
```

---

### 模板 10：虛擬映射（重新映射）

#### 核心想法

當你要**從一個有洞的範圍裡隨機取樣**（有些值被列入黑名單），與其用拒絕取樣（那會白白浪費很多次 `random` 呼叫），不如把壞掉的位置**重新映射**到合法的替代值，讓每次挑選都是 O(1)。

**關鍵洞見**：如果 `[0, N)` 裡有 `M` 個黑名單數字，那就剛好有 `N - M` 個合法數字。所以永遠只在 `[0, N-M)` 裡抽索引 —— 把這個上界叫做 `bound`。落在這個範圍內的黑名單索引，就**改導向**到從尾端 `[bound, N)` 取出的合法索引。

#### 步驟

1. **算出 `bound = N - blacklist.length`** —— 這就是安全的隨機範圍。
2. **建 `blackSet`** 以便 O(1) 判斷是否在黑名單中。
3. **讓 `last` 指標從 `N-1` 往下走**，跳過黑名單值，收集可用的替代目標。
4. **對每個 `b < bound` 的黑名單值**，映射 `b → last`（下一個合法的尾端索引）。
5. **`pick()` 時**：抽 `idx = random.nextInt(bound)`；回傳 `mapping.getOrDefault(idx, idx)`。

#### 圖解

```text
n=10, blacklist=[2,3,5,8]   →   bound = 10 - 4 = 6

RANDOM RANGE  [0, bound)
|----|----|----|----|----|----|
  0    1    2    3    4    5
             X    X         X
             ↑bad inside range — must remap

TAIL RANGE  [bound, n)
|----|----|----|----|
  6    7    8    9
             X            ← also blacklisted, skip it

Remapping (last starts at 9, walks left skipping blacklisted):
  b=2  →  last=9 (valid)  → map 2→9,  last=8
  b=3  →  last=8 (blacklisted, skip) → last=7 (valid) → map 3→7, last=6
  b=5  →  last=6 (valid)  → map 5→6,  last=5

Final mapping: { 2→9, 3→7, 5→6 }

pick() result for each index in [0,5]:
  0 → 0   (not mapped, return directly)
  1 → 1
  2 → 9   (remapped)
  3 → 7   (remapped)
  4 → 4
  5 → 6   (remapped)

Valid numbers returned: {0,1,4,6,7,9} ✓ uniformly distributed
```

#### Java 模板

```java
// LC 710 - Random Pick with Blacklist
class Solution {
    private Map<Integer, Integer> mapping = new HashMap<>();
    private Random random = new Random();
    private int bound;

    public Solution(int n, int[] blacklist) {
        bound = n - blacklist.length;

        Set<Integer> blackSet = new HashSet<>();
        for (int b : blacklist) blackSet.add(b);

        int last = n - 1;
        for (int b : blacklist) {
            if (b < bound) {
                // Skip tail values that are also blacklisted
                while (blackSet.contains(last)) last--;
                mapping.put(b, last);
                last--;
            }
        }
    }

    public int pick() {
        int idx = random.nextInt(bound);
        return mapping.getOrDefault(idx, idx);  // remap if blacklisted, else return directly
    }
}
```

#### Python 模板

```python
import random

class Solution:
    def __init__(self, n: int, blacklist: list[int]):
        self.bound = n - len(blacklist)
        black_set = set(blacklist)
        self.mapping = {}

        last = n - 1
        for b in blacklist:
            if b < self.bound:
                while last in black_set:
                    last -= 1
                self.mapping[b] = last
                last -= 1

    def pick(self) -> int:
        idx = random.randrange(self.bound)
        return self.mapping.get(idx, idx)
```

#### 複雜度

| 操作 | 時間 | 空間 |
|-----------|------|-------|
| 建構子 | O(B)，B = 黑名單大小 | O(B) |
| `pick()` | O(1) | O(1) |

#### 為什麼這樣行得通

- `bound = N - B` 剛好是合法數字的個數，所以 `random.nextInt(bound)` 永遠落在合法的計數範圍內。
- 落在 `[0, bound)` 裡的黑名單索引就是少數的「壞位置」—— 剛好有 B 個需要重新映射。
- 尾端 `[bound, N)` 也剛好有 B 個位置，其中不在黑名單的那些就是替代品。雙指標的走法保證這是一對一的配對。
- `[0, bound)` 裡不在黑名單的索引，會原封不動地從 `getOrDefault` 掉出來 → 沒有額外成本。

#### 相似／相關的 LC 題目

| 題目 | LC# | 難度 | 關鍵想法 |
|---------|-----|------------|----------|
| Random Pick with Blacklist | 710 | Hard | 虛擬重新映射（就是這個模式） |
| Random Pick Index | 398 | Medium | 蓄水池抽樣 |
| Random Pick with Weight | 528 | Medium | 前綴和 + 二分搜尋 |
| Shuffle an Array | 384 | Medium | Fisher-Yates（原地交換映射） |

---

### 模板 11：把雜湊表跟其他結構組起來

#### 1. 多個雜湊表
```python
# Track multiple relationships simultaneously
def complex_problem(arr):
    index_map = {}      # value -> index
    freq_map = {}       # value -> frequency
    reverse_map = {}    # index -> value
    
    for i, val in enumerate(arr):
        index_map[val] = i
        freq_map[val] = freq_map.get(val, 0) + 1
        reverse_map[i] = val
```

#### 2. 雜湊表 + 其他資料結構
```python
# Hash Map + Priority Queue (Heap)
import heapq
from collections import defaultdict

def top_k_frequent(nums, k):
    count = defaultdict(int)
    for num in nums:
        count[num] += 1
    
    # Use heap with frequency
    heap = []
    for num, freq in count.items():
        heapq.heappush(heap, (-freq, num))  # Max heap using negative values
    
    result = []
    for _ in range(k):
        result.append(heapq.heappop(heap)[1])
    return result
```

> top-K 不用堆積的 O(n) 解法，見 [Bucket Sort via Hash Map](./hash_map_examples.md#bucket-sort-via-hash-map-top-k-frequency-on)；用單調堆疊算出 `next greater` 再存進 map，見 [Monotonic Stack + Hash Map](./hash_map_examples.md#monotonic-stack--hash-map)。

## 總結與速查

### 題目 → 模式決策表

| 怎麼認出來 | 模式 | 模板 | 時間／空間 | 題目 |
|-----------------|---------|----------|--------------|----------|
| 元素、字元或 pattern 的出現次數；「出現最多次」、「anagram」、重複值 | 計數／頻率表 | [T1](#template-1-frequency-counter) | O(n) / O(n) | 242, 49, 451, 347, 692, 387, 819, 811, 1207, 383, 299, 349, 350 |
| 湊出 target 的配對、三元組或補數；「two sum」、「k-diff」、「可被 60 整除」 | 已見過的索引表 | [T2](#template-2-seen-before-index-map-two-sum-shape) | O(n) / O(n) | 1, 15, 16, 18, 167, 532, 653, 1010, 1679, 1711, 2006 |
| 依某種*推導出來*的形式歸為一類；「group」、「同一條線」、「同一列或同一行」 | 用算出來的鍵分組 | [T3](#template-3-grouping-by-a-computed-key) | O(n·k) / O(n) | 49, 149, 609, 939, 947, 987 |
| 子陣列的某個性質：和等於 k、和可被 k 整除、0 和 1 一樣多、剛好 k 個奇數 | 前綴和 → 計數表 | [T4](#template-4-prefix-sum--count-map-) | O(n) / O(n) | 560, 325, 523, 525, 930, 974, 1248, 724 |
| 一個視窗依字元上的某個條件時而擴張、時而收縮 | 滑動視窗 + 字元計數 | [T5](#template-5-sliding-window-with-hash-map) | O(n) / O(k) | 3, 76, 424, 438, 567, 159, 340, 904, 1004, 1208, 1234 |
| 「依照 ... 給定的順序」、自訂字母表、某個排列、依索引切分 | 排名表 | [T6](#template-6-rank-map--value-to-position-) | O(n) / O(n) | 953, 791, 105, 106 |
| 一個**雙向**都必須是一對一的映射 | 雙射（兩個 map） | [T7](#template-7-bijection-two-way-mapping) | O(n) / O(n) | 205, 290 |
| 「設計一個 cache」、O(1) get + put、淘汰機制，或值得記憶化的遞迴 | 快取／記憶化 | [T8](#template-8-hash-map-for-caching--memoization) | 攤還 O(1) / O(n) | 146, 460, 705, 706, 380, 381, 432, 355, 981, 1244, 139, 322, 1048 |
| 節點之間的關係：clone、父指標、子樹特徵、把等式當成邊 | 圖／樹的節點表 | [T9](#template-9-graph-problems-with-hash-map) | O(n) / O(n) | 133, 138, 652, 721, 734, 399, 947, 1257 |
| 「均勻隨機挑一個，但絕不能挑到這些值」 | 虛擬映射（重新映射） | [T10](#template-10-virtual-map-remapping) | 每次挑 O(1) / O(B) | 710, 398, 528, 384 |
| 光有 map 不夠 —— 你還需要順序、堆積或堆疊 | map + 另一個結構 | [T11](#template-11-combining-hash-maps-with-other-structures) | 視情況 | 347, 496, 503, 739, 853, 729, 846, 352 |

> 上表每一列的最壞情況，在所有鍵都碰撞時都是**每次操作 O(n)**。逐題的表格（約 90 題，各自標難度與一句話洞見）在 [hash_map_examples.md → Problems by Pattern](./hash_map_examples.md#problems-by-pattern)。

### 關鍵洞見與辨識訊號

1. **空間換時間**：雜湊表用額外的 O(n) 空間換平均 O(1) 的查找
2. **前綴和的魔法**：`subarray[i,j] = prefixSum[j] - prefixSum[i-1]`
3. **視窗狀態**：用雜湊表有效率地維護視窗的性質
4. **補數思維**：不要窮舉所有配對，改成把元素存起來再查它的補數
5. **索引還是值**：想清楚 map 的值要存索引、存值，還是兩個都存
6. **頻率計數**：大部分字串／陣列題都能靠頻率分析解掉

**面試時要留意的訊號：**
- 「找重複／重複出現的子字串」→ rolling hash，或二分搜尋 + 雜湊
- 「把一組值一致地映射到另一組」→ 雙射（兩個 map）
- 「把快取最佳化」→ 用 OrderedDict／雙向鏈結串列做 LRU
- 追問「如果陣列非常大呢？」→ 省空間的雜湊（rolling hash、座標壓縮）

### 實作最佳實務

#### Python 最佳實務
```python
# 1. Use defaultdict for cleaner counting code
from collections import defaultdict
count = defaultdict(int)  # No need for get(key, 0)

# 2. Use Counter for frequency problems
from collections import Counter
freq = Counter(arr)  # Automatically counts frequencies

# 3. Handle edge cases with dict.get()
value = my_dict.get(key, default_value)

# 4. Clean up zero counts to save space
if count[key] == 0:
    del count[key]

# 5. Use enumerate when you need both index and value
for i, val in enumerate(arr):
    ...        # use both i and val
```

#### Java 最佳實務
```java
// 1. Use getOrDefault to avoid null checks
map.put(key, map.getOrDefault(key, 0) + 1);

// 2. Use containsKey for existence checks
if (map.containsKey(key)) { /* ... */ }

// 3. Initialize with appropriate capacity
Map<String, Integer> map = new HashMap<>(expectedSize);

// 4. Use putIfAbsent for first occurrence
map.putIfAbsent(key, index);  // Only puts if key doesn't exist
```

**效能**：

1. **選對雜湊函式**：Python 內建的 hash 通常已經夠好
2. **避免不必要的 rehash**：能預先給定大小就給
3. **記得清理**：頻率表裡計數歸零的項目要移除
4. **用合適的負載因子**：預設的 0.75 通常就是最佳解

### 該避開的常見錯誤

1. **以為不會碰撞**：記住最壞情況的時間複雜度是 O(n)，不是 O(1)

2. **索引越界**： 
   ```python
   # Wrong: Can cause index errors
   if target - nums[i] in seen:
       return [i, seen[target - nums[i]]]
   seen[nums[i]] = i
   
   # Right: Check existence first
   if target - nums[i] in seen:
       return [seen[target - nums[i]], i]
   seen[nums[i]] = i
   ```

3. **在走訪過程中修改 dict**：
   ```python
   # Wrong: Can cause runtime errors
   for key in my_dict:
       if condition:
           del my_dict[key]
   
   # Right: Collect keys first
   to_delete = [k for k, v in my_dict.items() if condition]
   for k in to_delete:
       del my_dict[k]
   ```

4. **忽略邊界情況**：
   - 空陣列
   - 只有一個元素的陣列
   - 所有元素都相同
   - target 根本湊不出來

5. **選錯資料結構**：
   - 只要判斷存不存在就用 `set()`
   - 需要鍵值映射才用 `dict()`
   - 純粹算次數就用 `Counter()`

### 面試準備檢查清單

- [ ] 熟練全部 6 份模板，以及各自的使用時機
- [ ] 每個分類練 3-5 題
- [ ] 搞懂每個模式的時間／空間複雜度
- [ ] 知道常見的邊界情況，以及怎麼處理
- [ ] 能把雜湊碰撞的解法講清楚
- [ ] Python dict 和 Java HashMap 的 API 都要用得順
- [ ] 知道什麼時候**不該**用雜湊表（已排序的資料、範圍查詢等等）
