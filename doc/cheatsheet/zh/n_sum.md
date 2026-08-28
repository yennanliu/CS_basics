# N Sum

> **範圍** — 專門講 k-sum 這個題型家族：從 2Sum 到 kSum、先排序再雙向收斂的遞迴寫法，以及重複值的處理。
> **另見**：[2_pointers.md](./2_pointers.md) — 通用的收斂模板；[hash_map.md](./hash_map.md) — 未排序輸入的 O(n) 2Sum；[add_x_sum.md](./add_x_sum.md) — 完全不同的另一種「加總」題（數字逐位相加）。

## LeetCode 題目清單

- [Two Pointers](https://leetcode.com/problem-list/two-pointers/)
- [Hash Table](https://leetcode.com/problem-list/hash-table/)
- [Sorting](https://leetcode.com/problem-list/sorting/)

## 0) 概念  

### 0-1) 題型分類

- 題型
    - 2 Sum
        - 2 Sum
        - 2 Sum II：陣列已排序
        - 2 Sum III - 資料結構設計
        - 2 Sum IV - 輸入是一棵 BST
        - Two Sum Less Than K
    - 3 Sum
    - 4 Sum
    - N Sum

- 演算法
    - 排序
    - 雙指標
        - 排序 + 雙指標
    - 雜湊表操作
    - 陣列操作

- 資料結構
    - dict
    - set
    - 陣列
    - Treenode

## 1) 通用形式

```c++
// c++
//---------------------------------
// 2 SUM general form (target = 0)
//---------------------------------
// (algorithm book (labu) p.329)
vector<vector<int>> twoSumTarget(vector<int> & nums, int target){
    // need to sort nums first !
    sort(nums.begin(), nums.end());

    // init 2 pointers
    int lo = 0, hi = nums.size() - 1;

    vector<vector<int>> res;
    
    while (lo < hi){
        int sum = nums[lo] + nums[hi];
        int left = nums[lo], right = nums[hi];
        if (sum < target){
            while (lo < hi && nums[lo] == left) lo++;
        }else if (sum > target){
            while (lo < hi && nums[hi] == right) hi--;
        }else{
            res.push_back({left, right});
            /** AVOID ADDING duplicated combinations */
            while (lo < hi && nums[lo] == left) lo++;
            while (lo < hi && nums[hi] == right) hi--;
        }
    }
    return res;
}
```

```c++
// c++
//---------------------------------
// 3 SUM general form (target = N)
//---------------------------------
// (algorithm book (labu) p.331)
vector<vector<int>> twoSumTarget(vector<int> & nums, int start, int target){ // difference

    /** CHANGE left idx starts from "start", else are the same */
    // init 2 pointers
    int lo = start, hi = nums.size() - 1;

    vector<vector<int>> res;
    
    while (lo < hi){
        int sum = nums[lo] + nums[hi];
        int left = nums[lo], right = nums[hi];
        if (sum < target){
            while (lo < hi && nums[lo] == left) lo++;
        }else if (sum > target){
            while (lo < hi && nums[hi] == right) hi--;
        }else{
            res.push_back({left, right});
            /** AVOID ADDING duplicated combinations */
            while (lo < hi && nums[lo] == left) lo++;
            while (lo < hi && nums[hi] == right) hi--;
        }
    }
    return res;
}


/** get all triplet combinations with sum = target */
vector<vector<int>> threeSumTarget(vector<int> & nums, int target){
    // need to sort nums first !
    sort(nums.begin(), nums.end());

    // init
    int n = nums.size();
    vector<vector<int>> res;

    // looping for the 1st element in threeSum
    for (int i = 0; i < n; i++){
        // get twoSum from target - nums[i]
        vector<vector<int>>
        tuples = twoSumTarget(nums, i+1, target - nums[i]);

        // if exists two sum fit requirement, add nums[i] is what we need
        for (vector<int> & tuple: tuples){
            tuple.push_back(nums[i]);
            res.push_back(tuple);
        }

        /** neglect duplicated 1st element case, since duplocated combinations is NOT allowed */
        while (i < n - 1 && nums[i] == nums[i+1]) i++;
    }
    return res;
}
```

```c++
// c++
//---------------------------------
// K SUM general form (target = N)
//---------------------------------
// (algorithm book (labu) p.334)

vector<vector<int>> nSumTarget(vector<int> & nums, int n, int start, int target){

    int sz = nums.size();
    vector<vector<int>> res;

    // NEED to be AT LEAST 2 sum, and array size >= n
    if (n < 2 || sz < n) return res;

    // 2 sum is base case
    if (n == 2){
        // 2 pointers op
        int lo = start, hi = sz - 1;
        while (lo < hi){
            int sum = nums[lo] + nums[hi];
            int left = nums[lo], right = nums[hi];
            if (sum < target){
                while (lo < hi && nums[lo] == left) lo ++;
            }else if (sum > target){
                while (lo < hi && nums[hi] == right) hi --;
            }else{
                res.push_back({left, right});
                /** AVOID ADDING duplicated combinations */
                while (lo < hi && nums[lo] == left) lo++;
                while (lo < hi && nums[hi] == right) hi--;
            }
        }
    }else{
        /** n > 2, recursive get (n-1) Sum result */
        for (int i = start; i < sz; i++){
            vector<vector<int>>
            sub = nSumTarget(nums, n-1, i+1, target - nums[i]);
            for (vector<int> & arr : sub){
                // (n-1) Sum plus nums[i] is nSum
                arr.push_back(nums[i]);
                res.push_back(arr);
            }
            while (i < sz-1 && nums[i] == nums[i+1]) i++;
        }
    }
    return  res;
}
```

### 1-1) 快速決策表 ⭐⭐⭐⭐

| 目標 | 模板 | 時間 | LC |
|------|----------|------|-----|
| 找**一組**加起來等於 target 的數對，陣列未排序，要回傳索引 | 雜湊表補數（查 `target - x`） | O(n) | LC 1 |
| 找出**所有不重複**、加總等於 target 的組合 | 排序 + 固定 (k-2) 個索引 + 雙指標，跳過重複值 | O(n^(k-1)) | LC 15, LC 18 |
| 推廣到任意 `k` | 遞迴把 kSum 降階 → base case 是排序後的 2 Sum | O(n^(k-1)) | LC 18 |
| 求**最接近**的和，而非剛好命中 | 一樣的雙指標掃描，過程中追蹤最小的 abs(sum - target) | O(n^2) | LC 16 |
| 和要**小於**某個上界（不是剛好相等） | 雙指標，成功時移動 `l`（並記錄最大值） | O(n log n) | LC 1099 |
| 計算和等於 k 的**子陣列**（連續）數量 | 前綴和 + 雜湊表補數 | O(n) | LC 560 |

**核心想法**：所有 N Sum 最後都會化簡成 **2 Sum**。而 2 Sum 只有兩種引擎：
(a) **雜湊表補數** — 輸入未排序、需要索引，O(n)，但要去除重複組合並不容易；
(b) **排序後雙指標** — 要付 O(n log n) 的排序成本，但去重很自然，也支援「最接近／小於」這類變形。

### 1-2) 排序 + 雙指標（跳過重複值） ⭐⭐⭐⭐⭐

**模式**：先排序 → 固定外層的元素 → `lo/hi` 往中間收。去重要做在**兩個地方**：
外層迴圈（`nums[i] == nums[i-1] → continue`），以及命中之後（把指標推過相同的值）。

```java
// java
// LC 15 - 3Sum
// IDEA: sort, fix nums[i], then 2 pointers on the remaining sorted suffix
public List<List<Integer>> threeSum(int[] nums) {
    // time = O(n^2), space = O(log n) (sorting) excluding output
    Arrays.sort(nums);
    List<List<Integer>> res = new ArrayList<>();
    int n = nums.length;
    for (int i = 0; i < n - 2; i++) {
        if (nums[i] > 0) break;                        // sorted -> rest can't sum to 0
        if (i > 0 && nums[i] == nums[i - 1]) continue; // NOTE: skip duplicated 1st element
        int lo = i + 1, hi = n - 1;
        while (lo < hi) {
            int sum = nums[i] + nums[lo] + nums[hi];
            if (sum < 0) {
                lo++;
            } else if (sum > 0) {
                hi--;
            } else {
                res.add(Arrays.asList(nums[i], nums[lo], nums[hi]));
                // NOTE: skip duplicated 2nd / 3rd element
                while (lo < hi && nums[lo] == nums[lo + 1]) lo++;
                while (lo < hi && nums[hi] == nums[hi - 1]) hi--;
                lo++;
                hi--;
            }
        }
    }
    return res;
}
```

```python
# python
# LC 15 - 3Sum
# IDEA: sort, fix nums[i], then 2 pointers on the remaining sorted suffix
class Solution(object):
    def threeSum(self, nums):
        # time = O(n^2), space = O(n) (python sort = Timsort, O(n) aux) excluding output
        nums.sort()
        res = []
        n = len(nums)
        for i in range(n - 2):
            if nums[i] > 0:            # sorted -> rest can't sum to 0
                break
            if i > 0 and nums[i] == nums[i-1]:   # NOTE: skip duplicated 1st element
                continue
            lo, hi = i + 1, n - 1
            while lo < hi:
                s = nums[i] + nums[lo] + nums[hi]
                if s < 0:
                    lo += 1
                elif s > 0:
                    hi -= 1
                else:
                    res.append([nums[i], nums[lo], nums[hi]])
                    # NOTE: skip duplicated 2nd / 3rd element
                    while lo < hi and nums[lo] == nums[lo+1]:
                        lo += 1
                    while lo < hi and nums[hi] == nums[hi-1]:
                        hi -= 1
                    lo += 1
                    hi -= 1
        return res
```

### 1-3) 通用 kSum 遞迴（k Sum → 2 Sum） ⭐⭐⭐⭐

**核心想法**：`kSum(target, start, k)` = 對每個候選 `nums[i]`，把它接在 `kSum(target - nums[i], i+1, k-1)` 的結果前面。
遞迴在 `k == 2` 觸底（改用排序後的雙指標）。同一副骨架就能解 LC 15（`k=3`）和 LC 18（`k=4`）。

```java
// java
// LC 18 - 4Sum  (generic kSum)
// IDEA: recursively peel one element off -> base case is sorted 2 Sum
public List<List<Integer>> fourSum(int[] nums, int target) {
    // time = O(n^(k-1)) = O(n^3), space = O(k) recursion depth (excluding output)
    Arrays.sort(nums);
    return kSum(nums, (long) target, 0, 4);   // NOTE: long target -> avoid int overflow
}

private List<List<Integer>> kSum(int[] nums, long target, int start, int k) {
    List<List<Integer>> res = new ArrayList<>();
    if (start >= nums.length) return res;
    if (k == 2) return twoSumSorted(nums, target, start);   // base case
    for (int i = start; i < nums.length - k + 1; i++) {
        if (i > start && nums[i] == nums[i - 1]) continue;   // skip duplicated element
        for (List<Integer> sub : kSum(nums, target - nums[i], i + 1, k - 1)) {
            List<Integer> cur = new ArrayList<>();
            cur.add(nums[i]);
            cur.addAll(sub);
            res.add(cur);
        }
    }
    return res;
}

private List<List<Integer>> twoSumSorted(int[] nums, long target, int start) {
    List<List<Integer>> res = new ArrayList<>();
    int lo = start, hi = nums.length - 1;
    while (lo < hi) {
        long sum = (long) nums[lo] + nums[hi];
        if (sum < target) {
            lo++;
        } else if (sum > target) {
            hi--;
        } else {
            res.add(Arrays.asList(nums[lo], nums[hi]));
            while (lo < hi && nums[lo] == nums[lo + 1]) lo++;
            while (lo < hi && nums[hi] == nums[hi - 1]) hi--;
            lo++;
            hi--;
        }
    }
    return res;
}
```

```python
# python
# LC 18 - 4Sum  (generic kSum)
# IDEA: recursively peel one element off -> base case is sorted 2 Sum
class Solution(object):
    def fourSum(self, nums, target):
        # time = O(n^(k-1)) = O(n^3), space = O(n) sorting (Timsort) + O(k) recursion depth (excluding output)
        nums.sort()
        return self.kSum(nums, target, 0, 4)

    def kSum(self, nums, target, start, k):
        res = []
        if start >= len(nums):
            return res
        if k == 2:                       # base case
            return self.twoSumSorted(nums, target, start)
        for i in range(start, len(nums) - k + 1):
            if i > start and nums[i] == nums[i-1]:   # skip duplicated element
                continue
            for sub in self.kSum(nums, target - nums[i], i + 1, k - 1):
                res.append([nums[i]] + sub)
        return res

    def twoSumSorted(self, nums, target, start):
        res = []
        lo, hi = start, len(nums) - 1
        while lo < hi:
            s = nums[lo] + nums[hi]
            if s < target:
                lo += 1
            elif s > target:
                hi -= 1
            else:
                res.append([nums[lo], nums[hi]])
                while lo < hi and nums[lo] == nums[lo+1]:
                    lo += 1
                while lo < hi and nums[hi] == nums[hi-1]:
                    hi -= 1
                lo += 1
                hi -= 1
        return res
```

### 1-4) 最接近的和 ⭐⭐⭐

**變化點**：可能**根本沒有剛好命中**的組合，所以不是找到就回傳，而是持續掃描並記錄最好的
`|sum - target|`。指標移動規則不變（`sum < target → lo++`，否則 `hi--`）— 正是這條規則讓貪婪掃描成立。
`sum == target` 時可以提早回傳（距離 0 不可能被超越）。

```java
// java
// LC 16 - 3Sum Closest
// IDEA: same sort + 2 pointers scan, but track min |sum - target| instead of exact match
public int threeSumClosest(int[] nums, int target) {
    // time = O(n^2), space = O(log n) (sorting)
    Arrays.sort(nums);
    int n = nums.length;
    int best = nums[0] + nums[1] + nums[2];
    for (int i = 0; i < n - 2; i++) {
        if (i > 0 && nums[i] == nums[i - 1]) continue;
        int lo = i + 1, hi = n - 1;
        while (lo < hi) {
            int sum = nums[i] + nums[lo] + nums[hi];
            if (Math.abs(sum - target) < Math.abs(best - target)) best = sum;
            if (sum == target) return sum;   // can't get closer
            else if (sum < target) lo++;
            else hi--;
        }
    }
    return best;
}
```

```python
# python
# LC 16 - 3Sum Closest
# IDEA: same sort + 2 pointers scan, but track min |sum - target| instead of exact match
class Solution(object):
    def threeSumClosest(self, nums, target):
        # time = O(n^2), space = O(n) (python sort = Timsort, O(n) aux)
        nums.sort()
        n = len(nums)
        best = nums[0] + nums[1] + nums[2]
        for i in range(n - 2):
            if i > 0 and nums[i] == nums[i-1]:
                continue
            lo, hi = i + 1, n - 1
            while lo < hi:
                s = nums[i] + nums[lo] + nums[hi]
                if abs(s - target) < abs(best - target):
                    best = s
                if s == target:
                    return s          # can't get closer
                elif s < target:
                    lo += 1
                else:
                    hi -= 1
        return best
```

- **變形** — LC 1099（Two Sum Less Than K，見下面 2-4）：同一副骨架，只是把「距離」換成**嚴格上界**：
  當 `nums[lo] + nums[hi] < k` 就記下這個和並 `lo++`（試著更大），否則 `hi--`。

### 1-5) 對前綴和做雜湊表補數（連續版本）

**變化點**：LC 560 要的是**連續子陣列**，不是任意數對 — 所以不能排序。先建前綴和
`P[j]`，注意 `sum(i..j) = P[j] - P[i-1] = k` ⇔ `P[i-1] = P[j] - k`。這正是把 **2 Sum 的補數查表**
搬到前綴和上跑，而且要用**計數**的 map（不是索引 map），因為我們要的是所有配對。

```java
// java
// LC 560 - Subarray Sum Equals K
// IDEA: 2 Sum complement lookup over prefix sums; cnt[0] = 1 seeds the empty prefix
public int subarraySum(int[] nums, int k) {
    // time = O(n), space = O(n)
    Map<Integer, Integer> cnt = new HashMap<>();
    cnt.put(0, 1);                 // NOTE: empty prefix, so subarrays starting at idx 0 count
    int prefix = 0, res = 0;
    for (int x : nums) {
        prefix += x;
        res += cnt.getOrDefault(prefix - k, 0);            // complement lookup
        cnt.put(prefix, cnt.getOrDefault(prefix, 0) + 1);  // NOTE: insert AFTER querying
    }
    return res;
}
```

```python
# python
# LC 560 - Subarray Sum Equals K
# IDEA: 2 Sum complement lookup over prefix sums; cnt[0] = 1 seeds the empty prefix
from collections import defaultdict
class Solution(object):
    def subarraySum(self, nums, k):
        # time = O(n), space = O(n)
        cnt = defaultdict(int)
        cnt[0] = 1          # NOTE: empty prefix
        prefix, res = 0, 0
        for x in nums:
            prefix += x
            res += cnt[prefix - k]   # complement lookup
            cnt[prefix] += 1         # NOTE: insert AFTER querying
        return res
```

> **陷阱**：這題的 `nums` 可以有負數／零，所以滑動視窗**行不通** — 只能用前綴和 + 雜湊表。

## 2) LC 範例

### 2-1) 2 Sum — LC 1 ⭐⭐⭐⭐⭐
```python
# LC 001
class Solution(object):
    def twoSum(self, nums, target):
        lookup = {}
        for i, num in enumerate(nums):
            if target - num in lookup:
                return [lookup[target - num], i]
            lookup[num] = i
        return [-1, -1]
```

### 2-1') 2 Sum II — LC 167
```python
# LC 167 Two Sum II - Input Array Is Sorted
# NOTE : we can also use "2 sum" dict approach
# V0
# IDEA : TWO POINTERS
#       -> l = 0, r = len(numbers) - 1
class Solution(object):
    def twoSum(self, numbers, target):
        l = 0
        ### NOTE this
        r = len(numbers) - 1
        res = []
        while r > l:
            #print ("l, r = {}, {}".format(l, r))
            tmp = numbers[l] + numbers[r]
            if tmp == target:
                return [l+1, r+1]
            ### NOTE this
            elif tmp > target:
                r -= 1
            ### NOTE this
            else:
                l += 1
        return [-1, -1]
```

### 2-2) 3 Sum — LC 15 ⭐⭐⭐⭐⭐
```python
# LC 015
# V0
# IDEA : for loop + 2 sum
class Solution(object):
    def threeSum(self, nums):
        # edge case
        if not nums or len(nums) < 3:
            return []
        if len(nums) == 3:
            return [nums] if sum(nums) == 0 else []
        if nums.count(0) == len(nums):
            return [[0,0,0]]
        res = []
        nums.sort()
        # for loop
        for i in range(len(nums)):
            cur = nums[i]
            # 2 sum
            d = {}
            """
            NOTE !!!
             -> we don't need below implementation (will cause TLE)

                #_nums = nums[:i] + nums[i+1:]
                #for j, x in enumerate(_nums):

             -> this one is enough : for j, x in enumerate(nums[i+1:])
            """
            for j, x in enumerate(nums[i+1:]):
                # cur + x + y = 0
                # -> y = -x - cur
                if -x-cur in d:
                    tmp = [cur, x, -x-cur]
                    tmp.sort()
                    if tmp not in res:
                        res.append(tmp)
                    #res.append([cur, x, -x-cur])
                else:
                    d[x] = j
        return res

# V0'
# IDEA : 2 SUM -> 3 SUM
class Solution(object):
    def threeSum(self, nums):
        if not nums or len(nums) <= 2:
            return []
        res = []
        # optimize, not necessary
        nums.sort()
        # loop over i
        for i in range(len(nums)):
            """
            2 sum
            """
            d = {}
            """
            NOTE !!! here we loop over range(i+1, len(nums))
            #   -> since we need non duplicated results
            """
            for j in range(i+1, len(nums)):
                """
                NOTE : nums[i] + nums[j] + nums[k] = 0
                #    -> so - (nums[i] + nums[j]) = nums[k]
                #    -> and we are trying to find if such k already in the dict
                """
                if -(nums[i] + nums[j]) in d:
                    tmp = [nums[i]] + [nums[j], -(nums[i]+nums[j])]
                    tmp.sort()
                    if tmp not in res:
                        res.append(tmp)
                else:
                    d[nums[j]] = j
        return res

# V0''
# IDEA : 2 SUM -> 3 SUM
class Solution(object):
    def threeSum(self, nums):
        res = []
        if not nums or len(nums) <= 2:
            return res
        # this sort may not be necessary
        nums.sort()
        for i in range(len(nums)):
            # NOTE : set target = -nums[i]
            t = -nums[i]
            d = {}
            ### NOTE : we NEED tp loop from idx = i+1 to len(nums)
            for j in range(i+1, len(nums)):
                if  (-nums[j] + t) in d:
                        tmp = [ nums[i], nums[j], -nums[j] + t ]
                        tmp.sort()
                        # note :  this trick to not append duplicated ans
                        if tmp not in res:
                            res.append(tmp)
                d[nums[j]] = j
        return res
```

### 2-3) 4 Sum — LC 18
```python
# LC 018
# V0 
class Solution(object):
    def fourSum(self, nums, target):
        resultList = []
        nums.sort()
        for num1 in range(0, len(nums)-3):
            for num2 in range(num1 + 1, len(nums)-2):
                num3 = num2 + 1
                num4 = len(nums) -1
                while num3 != num4:
                    summer = nums[num1] + nums[num2] + nums[num3] + nums[num4]
                    if summer == target:
                        list_temp = [nums[num1],nums[num2],nums[num3],nums[num4]]
                        if list_temp not in resultList:
                            resultList.append(list_temp)
                        num3 += 1
                    elif summer > target:
                        num4 -= 1
                    else:
                        num3 += 1
        return resultList
```
### 2-4) Two Sum Less Than K — LC 1099
```python
# LC 1099 Two Sum Less Than K
# V1
# https://goodtecher.com/leetcode-1099-two-sum-less-than-k/
class Solution:
    def twoSumLessThanK(self, nums, k):    
        nums = sorted(nums)
        i = 0
        j = len(nums) - 1
      
        max_sum = -1
        
        while i < j:
            if nums[i] + nums[j] >= k:
                j -= 1
            else:
                max_sum = max(max_sum, nums[i] + nums[j])
                i += 1      
        return max_sum

# V1'
# https://blog.51cto.com/u_15344287/3647641
class Solution:
    def twoSumLessThanK(self, nums: List[int], k: int) -> int:
        nums.sort()
        ans = -1
        for i1 in range(len(nums)):
            n1 = nums[i1]
            i2 = bisect.bisect_left(nums, k - nums[i1], lo=i1 + 1) - 1
            n2 = nums[i2]
            if i2 > i1:
                ans = max(ans, n1 + n2)
        return ans
```
