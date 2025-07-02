# Bracktrack 

> Brute force via  `decision tree process`

- [Fuck algorithm : BackTrack](https://labuladong.online/algo/essential-technique/backtrack-framework/#%E4%B8%80%E3%80%81%E5%85%A8%E6%8E%92%E5%88%97%E9%97%AE%E9%A2%98)
- [Fuck algorithm : BackTrack different view](https://labuladong.online/algo/practice-in-action/two-views-of-backtrack/)
    - https://labuladong.online/algo/practice-in-action/sudoku/
    - https://labuladong.online/algo/practice-in-action/generate-parentheses/
    - https://labuladong.online/algo/practice-in-action/partition-to-k-equal-sum-subsets/
- [BackTrack Java LC General approach](https://leetcode.com/problems/subsets/solutions/27281/a-general-approach-to-backtracking-questions-in-java-subsets-permutations-combination-sum-palindrome-partitioning/)

- Backtrack (brute force) -> DP (dynamic programming)
- optimization:
    - remove duplicated sub cases
    - have a cache (e.g. hash table) list finished cases (not re-compute them)
    - "cut the sub-tree" via conditions such as `contains`, `start`

- <img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/backtrack1.png" ></p>


- pattern

```
# pseudo code 

result = []

def backtrack(路徑, 選擇清單):
 if 滿足結束條件:
     result.add(路徑)
     return

 for 選擇 in 選擇清單:
     做選擇
     backtrack(路徑, 選擇列表)
     撤銷選擇


- start_idx
- early quit
```

## 0) Concept  

- 3 things to consider : 
 - `Route` : The choices have made
 - `Choice list` : The choices we can select currently
 - `End condition` : bottom of decision tree, meet this point then can't do any other choise 

- Algorithm
    - dfs
    - recursive

- Data structure
    - dict
    - set
    - array


### 0-0) `start_idx` — When & Why?

## 

`start_idx` (or `index`, or similar) is **used to control the search space** — to **avoid duplicates** and maintain order in the generated result.

-> Use `start_idx` when:

- You're generating **combinations/subsets**
- You want to **avoid duplicates**
- You want to **preserve order** of choices


---

### ✅ **Problems that NEED `start_idx`**

These typically involve **combinations**, **subsets**, or **multi-use elements**, where:

- Order doesn't matter (e.g., `[2,3]` is same as `[3,2]`)
- You want to avoid revisiting earlier choices
- You may reuse elements **or** pick each element **once**

#### 🔹 Examples:
| Problem | Use of `start_idx` | Why? |
|--------|------------------|------|
| `Subsets` (Leetcode 78) | ✅ Yes | To avoid duplicate subsets |
| `Combination Sum` (Leetcode 39) | ✅ Yes | Reuse allowed, but in order |
| `Combination Sum II` (Leetcode 40) | ✅ Yes | No reuse, skip duplicates |
| `Combinations` (Leetcode 77) | ✅ Yes | Choose k out of n, in order |
| `Palindrome Partitioning` | ✅ Yes | Explore substrings from `start` |

---

### ❌ **Problems that DO NOT use `start_idx`**

These are often **permutation problems**, where:

- Order **does** matter
- You want to try **all possible orders**
- You **should revisit** earlier choices (sometimes)


-> Don't use `start_idx` when:
- You're generating **permutations**
- You need **all orderings**
- Choices are not sequential (e.g., trying all positions)

#### 🔹 Examples:
| Problem | Use of `start_idx` | Why Not? |
|--------|------------------|---------|
| `Permutations` (Leetcode 46) | ❌ No | All orderings are valid |
| `Permutations II` (Leetcode 47) | ❌ No | Just skip duplicates smartly |
| `N-Queens` | ❌ No | One row per recursion depth |
| `Word Break II` | ❌ No | Choices depend on substring matches |


---

## 🧭 Summary Table

| Problem Type      | Use `start_idx`? | Example Problem |
|-------------------|------------------|-----------------|
| Subsets           | ✅ Yes           | Leetcode 78     |
| Combinations      | ✅ Yes           | Leetcode 77     |
| Combination Sum   | ✅ Yes           | Leetcode 39     |
| Permutations      | ❌ No            | Leetcode 46     |
| N-Queens          | ❌ No            | Leetcode 51     |
| Partitioning      | ✅ Yes           | Leetcode 131    |

---


### 0-1) `i+1` or `i` as start_idx in recursion call ?

- LC 39, 78, 518

Let's walk through it systematically by comparing well-known **Leetcode problems** where this pattern matters.

---

## 🧭 **Core Difference Between `i` and `i + 1` in Recursion**

| Pattern | Meaning                       | Use Case                                |
| ------- | ----------------------------- | --------------------------------------- |
| `i`     | Reuse the **same item again** | **Unbounded Knapsack** (`infinite` items) |
| `i + 1` | Use **each item once only**   | **0/1 Knapsack**, subsets               |

---

## 🔍 Problem Comparison by Recursion Pattern

| Leetcode # | Problem                           | Recursion Index        | Coin Usage       | Explanation                                                 |
| ---------- | --------------------------------- | ---------------------- | ---------------- | ----------------------------------------------------------- |
| **518**    | Coin Change II                    | `i`                    | Infinite         | You can reuse coins unlimited times → use `i`               |
| **377**    | Combination Sum IV                | `i`                    | Infinite         | Order matters → use all i, don't restrict reuse             |
| **39**     | Combination Sum                   | `i`                    | Infinite         | Choose the same number again → `i`                          |
| **40**     | Combination Sum II                | `i + 1`                | Once             | Each number can be used **once only**                       |
| **46**     | Permutations                      | no index (`visited[]`) | Once             | Full permutations → can't reuse → use boolean visited array |
| **47**     | Permutations II (with duplicates) | no index + dedup       | Once             | Same as LC 46 + dedup logic                                    |
| **90**     | Subsets II                        | `i + 1`                | Once             | Use each number once, skip duplicates                       |
| **78**     | Subsets                           | `i + 1`                | Once             | Classic 0/1 inclusion/exclusion                             |
| **322**    | Coin Change (min # of coins)      | `i`                    | Infinite         | Optimization version of coin change                         |
| **494**    | Target Sum                        | `i + 1`                | Once per element | Only one + or - per number                                  |

---

## 🎯 Rules of Thumb

| Situation                       | Use `i` | Use `i + 1`          |
| ------------------------------- | ------- | -------------------- |
| Reuse allowed (infinite supply) | ✅       | ❌                    |
| Use item only once              | ❌       | ✅                    |
| Generating combinations         | ✅       | ✅ (depends)          |
| Generating permutations         | ❌       | ✅ (with `visited[]`) |

---

## 🔧 Quick Example

### Example 1: **Coin Change II** (518)

```java
// Unlimited coin usage
for (int i = start; i < coins.length; i++) {
    backtrack(i, currentSum + coins[i]); // use coin[i] again
}
```

### Example 2: **Combination Sum II** (40)

```java
// Use each number at most once
for (int i = start; i < candidates.length; i++) {
    backtrack(i + 1, currentSum + candidates[i]); // can't reuse candidates[i]
}
```

---

## 🧠 Key Takeaway

> **Use `i` when repetition is allowed (unbounded knapsack style)**
> **Use `i + 1` when repetition is not allowed (0/1 knapsack, subset style)**


---

## 🧠 Visual Guide: Recursion Index Patterns in LeetCode Problems

### 🔄 **Unbounded Knapsack (Reuse Items)**

* **Recursion Pattern:** `i`
* **Use Case:** Items can be used multiple times.
* **Examples:**

  * **518. Coin Change II**: ([AlgoMonster][1])
  * **377. Combination Sum IV**
  * **39. Combination Sum**
  * **322. Coin Change (Minimum Coins)**

### 🔒 **0/1 Knapsack (Use Each Item Once)**

* **Recursion Pattern:** `i + 1`
* **Use Case:** Each item can be used at most once.
* **Examples:**

  * **40. Combination Sum II**: ([AlgoMonster][2])
  * **78. Subsets**
  * **90. Subsets II**
  * **46. Permutations**
  * **47. Permutations II (with duplicates)**
  * **494. Target Sum**

---

## 📊 Quick Reference Table

| Problem # | Problem Name                      | Recursion Index | Coin Usage       | Notes                                          |
| --------- | --------------------------------- | --------------- | ---------------- | ---------------------------------------------- |
| 518       | Coin Change II                    | `i`             | Infinite         | Reuse coins; loop from `i`                     |
| 377       | Combination Sum IV                | `i`             | Infinite         | Order matters; loop from `i`                   |
| 39        | Combination Sum                   | `i`             | Infinite         | Reuse elements; loop from `i`                  |
| 40        | Combination Sum II                | `i + 1`         | Once per element | Use each element once; loop from `i + 1`       |
| 78        | Subsets                           | `i + 1`         | Once per element | Classic inclusion/exclusion; loop from `i + 1` |
| 90        | Subsets II                        | `i + 1`         | Once per element | Handle duplicates; loop from `i + 1`           |
| 46        | Permutations                      | `visited[]`     | Once per element | Full permutations; use visited array           |
| 47        | Permutations II (with duplicates) | `visited[]`     | Once per element | Handle duplicates; use visited array           |
| 494       | Target Sum                        | `i + 1`         | Once per element | Only one + or - per number; loop from `i + 1`  |

---

## 🧭 Summary

* **Use `i`** when:

  * You can reuse items (unlimited supply).
  * Order matters (e.g., combinations with repetition).

* **Use `i + 1`** when:

  * Each item can be used at most once.
  * You're generating combinations or subsets without repetition.

* **Use `visited[]`** when:

  * Generating permutations.
  * Handling duplicates within the input.

---


### 0-1) Types

- Conclusion:
    -  `NO` NEED `start idx` : 全排列 (`Permutations`)
        - Permutations (排列組合)

    - NEED `start idx` : other backtrack problems
        - Subsets (子集)
        - Combinations (組成)
        - combination Sum (LC 39)
        - partitioning

- Problems types

    - Type 1) : `Subsets` (子集)
        - Problems : LC 78, 90, 17
        - [代碼隨想錄 - 0078.子集](https://github.com/youngyangyang04/leetcode-master/blob/master/problems/0078.%E5%AD%90%E9%9B%86.md)
        - (for loop call help func) +  start_idx + for loop + pop(-1)
        - backtrack. find minumum case. transform the problem to `tree-problem`. via `start` remove already used numbers and return all cases
        - Need `!cur.contains(nums[i])` -> to NOT add duplicated element
        ```python
        # V1
        # ...
        cur = []
        res = []
        def help(start_idx, cur):
            # ....
            """
            NOTE !!! start_idx

            we need start_idx here to AVOID re-select previous element
            """
            for i in range(start_idx, len(wordDict)):
                cur.append(wordDict[i])

                # NOTE !!! `start_idx + 1`
                help(start_idx + 1, cur)
                """
                NOTE !!! pop(-1)
                """
                cur.pop(-1)
        # ...
        ```
        ```java
        // java
        public List<List<Integer>> subsets(int[] nums) {
            // ...
            this.getSubSet(start_idx, nums, cur, res);
            //System.out.println("(after) res = " + res);
            return res;
        }

        public void getSubSet(int start_idx, int[] nums, List<Integer> cur, List<List<Integer>> res){

            if (!res.contains(cur)){
                // NOTE !!! init new list via below
                res.add(new ArrayList<>(cur));
            }

            if (cur.size() > nums.length){
                return;
            }

            for (int i = start_idx; i < nums.length; i++){
                /**
                 * NOTE !!!
                 *
                 *  for subset,
                 *  we need "!cur.contains(nums[i])"
                 *  -> to NOT add duplicated element
                 */
                if (!cur.contains(nums[i])){
                    cur.add(nums[i]);
                    /**
                     *  NOTE !!!
                     *
                     *   at LC 78 subset, we need to use `i+1` idx
                     *   in recursive call
                     *
                     *   while at LC 39 Combination Sum,
                     *   we use `i` directly
                     *
                     *
                     *   e.g. next start_idx is ` i+1`
                     */
                    this.getSubSet(i+1, nums, cur, res);
                    // undo
                    cur.remove(cur.size()-1);
                }
            }
        }
        ```

    - `Subsets I`
        - LC 78
        - start idx + backtrack
        ```java
        // java
        // LC 78
        public void getSubSet(int start_idx, int[] nums, List<Integer> cur, List<List<Integer>> res){

                if (!res.contains(cur)){
                    // NOTE !!! init new list via below
                    res.add(new ArrayList<>(cur));
                }

                if (cur.size() > nums.length){
                    return;
                }

                for (int i = start_idx; i < nums.length; i++){
                    /**
                     * NOTE !!!
                     *
                     *  for subset,
                     *  we need "!cur.contains(nums[i])"
                     *  -> to NOT add duplicated element
                     */
                    if (!cur.contains(nums[i])){
                        cur.add(nums[i]);
                        /**
                         *  NOTE !!!
                         *
                         *   at LC 78 subset, we need to use `i+1` idx
                         *   in recursive call
                         *
                         *   while at LC 39 Combination Sum,
                         *   we use `i` directly
                         *
                         *
                         *   e.g. next start_idx is ` i+1`
                         */
                        this.getSubSet(i+1, nums, cur, res);
                        // undo
                        cur.remove(cur.size()-1);
                    }
                }
            }

        ```

    - `Subsets II`
        - LC 90
        - start idx + backtrack + dedup (seen)
        - dedup : can use dict counter or idx
        ```java
        // java
        // LC 90
        private void backtrack(List<List<Integer>> list, List<Integer> tempList, int [] nums, int start){
            list.add(new ArrayList<>(tempList));
            for(int i = start; i < nums.length; i++){
                // skip duplicates
                /**
                 *  NOTE !!!
                 *
                 *   below is the key shows how to simply skip duplicates
                 *   (instead of using hashmap counter)
                 */
                if(i > start && nums[i] == nums[i-1]){
                    continue;
                }
                tempList.add(nums[i]);
                backtrack(list, tempList, nums, i + 1);
                tempList.remove(tempList.size() - 1);
            }
        }
        ```

    - Type 2) : `Permutations (排列組合)` (全排列)
        - Problems : LC 46, 47
        - (for loop call help func) + contains + pop(-1)
        - backtrack. via `contains` remove already used numbers and return all cases
        - `NO NEED` to use start_idx
        ```python
        # ...
        res = []
        cur = []
        def help(cur):
            if len(cur) == len(s):
                res.append(cur[:])
                return
            if len(cur) > len(s):
                return
            for i in nums:
                # NOTE this !!!
                if i not in cur:
                    cur.append(i)
                    help(cur)
                    cur.pop(-1)
        # ...
        ```

    - `Permutations I (排列組合)`
        - LC 46
        ```python
        # python
        class Solution(object):
            def permute(self, nums):
                def help(cur):
                    if len(cur) == n_len:
                        if cur not in res:
                            res.append(list(cur))
                            return
                    if len(cur) > n_len:
                        return
                    for i in nums:
                        #print ("i = " + str(i) + " cur = " + str(cur))
                        if i not in cur:
                            cur.append(i)
                            help(cur)
                            """
                            NOTE !!! : we UNDO the last op we just made (pop last element we put into array)
                            """
                            cur.pop(-1)
                # edge case
                if not nums:
                    return [[]]
                n_len = len(nums)
                res = []
                help([])
                #print ("res = " + str(res))
                return res
            ```

    - `Permutations II (排列組合)`
        - LC 47
        ```python
        # python
        class Solution(object):
            def permuteUnique(self, nums):
                def help(res, cur, cnt):
                    if len(cur) == len(nums):
                        if cur not in res:
                            res.append(cur[:])
                            return
                    if len(cur) > len(nums):
                        return
                    for x in _cnt:
                        #print ("i = " + str(i) + " cur = " + str(cur))
                        #if i not in cur:
                        if _cnt[x] > 0:
                            cur.append(x)
                            _cnt[x] -= 1
                            help(res, cur, _cnt)
                            """
                            NOTE !!! : we UNDO the last op we just made (pop last element we put into array)
                            """
                            cur.pop(-1)
                            _cnt[x] += 1
                # edge case
                if not nums:
                    return [[]]
                _cnt = Counter(nums)
                #print ("_cnt = " + str(_cnt))
                res = []
                cur = []
                help(res, cur, _cnt)
                return res
        ```

    - Type 3) : `Combinations (組成)` 
        - LC 77
        - (for loop call help func) +  start_idx + for loop + + check if len == k + pop(-1)
        ```python
        # ...
        cur = []
        res = []
        def help(idx, cur):
            if len(cur) == k:
                res.append(cur)
                return
            for i in range(idx, n+1):
                cur.append(i)
                help(idx+1, cur)
                cur.pop(-1)
        # ...
        ```
   

    - Combination sum


    - Type 3) : Others
    
    - Parentheses (括弧)
        - LC 20, LC 22

    - Combination Sum
        - LC 39
        ```java
        // java
        // https://leetcode.com/problems/subsets/solutions/27281/a-general-approach-to-backtracking-questions-in-java-subsets-permutations-combination-sum-palindrome-partitioning/
        public List<List<Integer>> combinationSum(int[] nums, int target) {
            List<List<Integer>> list = new ArrayList<>();
            Arrays.sort(nums);
            backtrack(list, new ArrayList<>(), nums, target, 0);
            return list;
        }

        private void backtrack(List<List<Integer>> list, List<Integer> tempList, int [] nums, int remain, int start){
            if(remain < 0) return;
            else if(remain == 0) list.add(new ArrayList<>(tempList));
            else{ 
                for(int i = start; i < nums.length; i++){
                    tempList.add(nums[i]);
                    /** NOTE !!!
                     *
                     *   use i, since we need to use start from current (i) index in recursion call
                     *    (reuse current index)
                     */
                    backtrack(list, tempList, nums, remain - nums[i], i);
                    tempList.remove(tempList.size() - 1);
                }
            }
        }
        ```

    - Combination Sum II
        - LC 40
        ```java
        // java
        // https://leetcode.com/problems/subsets/solutions/27281/a-general-approach-to-backtracking-questions-in-java-subsets-permutations-combination-sum-palindrome-partitioning/
         public List<List<Integer>> combinationSum2(int[] nums, int target) {
            List<List<Integer>> list = new ArrayList<>();
            Arrays.sort(nums);
            backtrack(list, new ArrayList<>(), nums, target, 0);
            return list;
            
        }

        private void backtrack(List<List<Integer>> list, List<Integer> tempList, int [] nums, int remain, int start){
            if(remain < 0) return;
            else if(remain == 0) list.add(new ArrayList<>(tempList));
            else{
                for(int i = start; i < nums.length; i++){
                    if(i > start && nums[i] == nums[i-1]) continue; // skip duplicates
                    tempList.add(nums[i]);
                    backtrack(list, tempList, nums, remain - nums[i], i + 1);
                    tempList.remove(tempList.size() - 1); 
                }
            }
        }        
        ```

    - Palindrome Partitioning
        - LC 131
        ```java
        // java
        // https://leetcode.com/problems/subsets/solutions/27281/a-general-approach-to-backtracking-questions-in-java-subsets-permutations-combination-sum-palindrome-partitioning/
        public List<List<String>> partition(String s) {
           List<List<String>> list = new ArrayList<>();
           backtrack(list, new ArrayList<>(), s, 0);
           return list;
        }

        public void backtrack(List<List<String>> list, List<String> tempList, String s, int start){
           if(start == s.length())
              list.add(new ArrayList<>(tempList));
           else{
              for(int i = start; i < s.length(); i++){
                 if(isPalindrome(s, start, i)){
                    tempList.add(s.substring(start, i + 1));

                    // NOTE !!! `i+1`
                    backtrack(list, tempList, s, i + 1);
                    tempList.remove(tempList.size() - 1);
                 }
              }
           }
        }

        public boolean isPalindrome(String s, int low, int high){
           while(low < high)
              if(s.charAt(low++) != s.charAt(high--)) return false;
           return true;
        } 
        ```


### 0-2) Pattern

```python
# python pseudo code 1
# https://leetcode.com/explore/learn/card/recursion-ii/472/backtracking/2793/
def backtrack(candidate):
    if find_solution(candidate):
        output(candidate)
        return
    
    # iterate all possible candidates.
    for next_candidate in list_of_candidates:
        if is_valid(next_candidate):
            # try this partial candidate solution
            place(next_candidate)
            # given the candidate, explore further.
            backtrack(next_candidate)
            # backtrack
            remove(next_candidate)
```

```python
# python pseudo code 2
for choice in choice_list:
    # do choice
    routes.add(choice)
    backtrack(routes, choice_list)
    # undo choice
    routes.remove(choice)
```

```python
# python pseudo code 3
result = []
def backtrack(route, choice_list):
    if end_condition:
        result.add(route)
        return
    
    for choice in choice_list:
        ### core of backtrack
        do_choice   ### this one is necessary
        backtrack(route, choice_list)
        undo_choice ### this one is necessary
```

## 1) General form

### 1-1) Basic OP

### 1-2) Trick


#### 1-2-1) append to cache with idx

```python
# LC 131. Palindrome Partitioning

# ...
def help(s, res, path):
    if not s:
        res.append(path)
    for i in range(1, len(s)):
        if s[:i] == s[:i][::-1]:
            """
            NOTE below !!!
                -> we call help recursively with s[i:] subset
                -> we append [s[:i]] to tmp cache (path) 
            """
            help(s[i:], res, path + [s[:i]])
# ...
```

#### 1-2-2) avoid add `duplicated` element in `same level` (same recursion call)


```java
// LC 40
// java
// ...

/**
*  NOTE !!!   skip a duplicate at the same recursive level.
*
*   -> Key idea of the duplicate-skipping logic
*       •    We do not skip all duplicates.
*       •    We only skip a duplicate at the same recursive level.
*/

if (i > startIdx && candidates[i] == candidates[i - 1]) {
    continue;
}

// ...
```

```java
// LC 90
// java

// ...
for (int j = i; j < nums.length; j++) {
    /** 
     *  NOTE !!! below !!
     * 
     *  via below, we avoid add `duplicated` element
     * 
     */
    if (j > i && nums[j] == nums[j - 1]) {
        continue;
    }
    // ...
}
// ...
```

```java
// java
// LC 47
// ...

// Skip duplicates in the same recursion layer
if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1])
    continue;
            
// ...
```

#### 1-2-3) NOT do `undo` on primary variable

```java
// java
// LC 79

// https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/BackTrack/WordSearch.java#L133


// In Java, primitive types like int are passed by value. This means when you do:

// dfsFind(board, word, x+1, y, visited, start_idx + 1)

//  1) You're passing a copy of start_idx + 1 to the recursive function. So, inside the recursive call, start_idx is a new variable, and changes to it won't affect the start_idx in the calling function.


// 2) We don't need start_idx -= 1; because start_idx is passed by value, not by reference. So modifying it in the recursive call doesn't affect the caller's start_idx. We're already handling the correct index in each recursive call by passing start_idx + 1.

```

### 1-3)  if `true`, return true right after recursive call

```java
// java
// LC  698

// ...
if (backtrack_(nums, j + 1, k, subsetSum + nums[j], used)){
            return true;
        }

// ...
```

## 2) LC Example

### 2-1) Letter Combinations of a Phone Number 

```java
// java
// LC 17
// V0
// IDEA: BACKTRACK + start_idx (on digit)
List<String> _res = new ArrayList<String>();
public List<String> letterCombinations(String _digits) {

    if (_digits.length() == 0){
        return new ArrayList<>();
    }

    HashMap<java.lang.String, java.lang.String> letters = new HashMap<>();
    letters.put("2", "abc");
    letters.put("3", "def");
    letters.put("4", "ghi");
    letters.put("5", "jkl");
    letters.put("6", "mno");
    letters.put("7", "pqrs");
    letters.put("8", "tuv");
    letters.put("9", "wxyz");

    _letter_builder(letters, 0, _digits, new StringBuilder());
    return this._res;
}

private void _letter_builder(HashMap<String, String> map, int start_idx, String digits, StringBuilder builder){

    /**
     *  NOTE !!!
     *
     *   if builder (StringBuilder) length equals digits length,
     *   -> means we first one of the `all digit visit`
     *   -> we should add this cur to our result
     */
    if (builder.length() == digits.length()){
        this._res.add(builder.toString()); // NOTE this
        return;
    }

    /**
     *  NOTE !!!
     *
     *
     *   1) the `start_idx`  is for `digits` .
     *   e.g.
     *
     *    -> if digits = "23",
     *       the start_idx is 0,
     *       and could become 1, ...
     *
     *
     *   2) via `start_idx` we can focus on specific digit (e.g. "2" only, from "23")
     *      then we can loop over its `alphabet` in recursive call
     *      e.g. "abc" for "2"
     *
     *      letters.put("2", "abc");
     *
     */
    String _digit = String.valueOf(digits.toCharArray()[start_idx]); // NOTE this
    String _alphabets = map.get(_digit);

    // backtrack
    /**
     *  NOTE !!!
     *
     *   we loop over `_alphabets` (digit with idx),
     *   (instead of digit)
     *
     *   -> so we can build our cur string accordingly
     *
     */
    for (char a : _alphabets.toCharArray()){
        builder.append(a);
        _letter_builder(map, start_idx + 1, digits, builder);


        // undo
        // builder.deleteCharAt(0); // NOTE !!! in backtrack, we remove LAST element (idx = len - 1), instead of first element
        builder.deleteCharAt(builder.toString().length() - 1);
        // no need to `undo` start_idx, since it's primary type
        // in java, it is copied as `new var` when pass the recursive call
        // https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/backtrack.md#1-2-3-not-do-undo-on-primary-variable
        // start_idx -= 1; // this is WRONG!!!
    }
}
```

```python
# 017   Letter Combinations of a Phone Number
# V0
# IDEA : backtracking
class Solution(object):
    def letterCombinations(self, digits):
        # help func
        def help(idx, cur):
            if len(cur) == len(digits):
                tmp = "".join(cur[:])
                res.append(tmp)
                cur = []
                return
            if len(cur) > len(digits):
                cur = []
                return
            for a in d[digits[idx]]:
                cur.append(a)
                help(idx+1, cur)
                cur.pop(-1)  # NOTE this !!! : we pop last element
        # edge case
        if not digits:
            return []
        res = []
        cur = []
        idx = 0
        d =  {'2': 'abc', '3': 'def', '4': 'ghi', '5': 'jkl', '6': 'mno', '7': 'pqrs', '8': 'tuv', '9': 'wxyz'}
        help(idx, cur)
        return res

# V0'
# IDEA : dfs + backtracking
class Solution(object):
    def letterCombinations(self, digits):

        def dfs(idx, tmp):

            """
            NOTE : if idx == len(digits)
               -> if tmp is not null, then we append tmp to our result (res)
               -> and we out of the loop
            """
            if idx == len(digits):
                if tmp != "":
                    res.append(tmp)
                return

            ### NOTE : we loop alphabets in d map per number rather than loop over number !!!
            for alpha in d[digits[idx]]:
                """
                NOTE !!!!
                idex+1 : for loop to next number
                tmp+j : for collect cur update
                """
                print ("digits = " + str(digits), " tmp = " + str(tmp) + " alpha = " + str(alpha))
                dfs(idx+1, tmp + alpha)

        d = {'2' : "abc", '3' : "def", '4' : "ghi", '5' : "jkl", '6' : "mno", '7' : "pqrs", '8' : "tuv", '9' : "wxyz"}
        res = []
        dfs(0,"")
        return 

# V1 
# idea : for loop
class Solution(object):
    def letterCombinations(self, digits):
        """
        :type digits: str
        :rtype: List[str]
        """
        if digits == "": return []
        d = {'2' : "abc", '3' : "def", '4' : "ghi", '5' : "jkl", '6' : "mno", '7' : "pqrs", '8' : "tuv", '9' : "wxyz"}
        res = ['']
        for e in digits:
            res = [w + c for c in d[e] for w in res]
        return res
```

### 2-2) combination-sum
```python
# LC 039, LC 040 combination-sum
# V0
# IDEA : DFS + BACKTRACK
class Solution(object):
    def combinationSum(self, candidates, target):

        def dfs(tmp):
            if sum(tmp) == target:
                tmp.sort()
                if tmp not in res:
                    res.append(tmp)
                return
            if sum(tmp) > target:
                return
            for c in candidates:
                dfs(tmp + [c])

        res = []
        tmp = []
        dfs(tmp)
        return res
``` 

### 2-3) Word Search
```python
# LC 079 Word Search

# V0
# IDEA : DFS + backtracking
class Solution(object):
 
    def exist(self, board, word):
        ### NOTE : construct the visited matrix
        visited = [[False for j in range(len(board[0]))] for i in range(len(board))]

        ### NOTE : we visit every element in board and trigger the dfs
        for i in range(len(board)):
            for j in range(len(board[0])):
                if self.dfs(board, word, 0, i, j, visited):
                    return True

        return False

    def dfs(self, board, word, cur, i, j, visited):
        # if "not false" till cur == len(word), means we already found the wprd in board
        if cur == len(word):
            return True

        ### NOTE this condition
        # 1) if idx out of range
        # 2) if already visited
        # 3) if board[i][j] != word[cur] -> not possible to be as same as word
        if i < 0 or i >= len(board) or j < 0 or j >= len(board[0]) or visited[i][j] or board[i][j] != word[cur]:
            return False

        # mark as visited
        visited[i][j] = True
        ### NOTE THIS TRICK (run the existRecu on 4 directions on the same time)
        result = self.dfs(board, word, cur + 1, i + 1, j, visited) or\
                 self.dfs(board, word, cur + 1, i - 1, j, visited) or\
                 self.dfs(board, word, cur + 1, i, j + 1, visited) or\
                 self.dfs(board, word, cur + 1, i, j - 1, visited)
        # mark as non-visited
        visited[i][j] = False

        return result
```

```java
// java
// LC 079
// V0'
// IDEA : DFS + BACKTRACK (modified by GPT)
public boolean exist_0(char[][] board, String word) {
    if (board == null || board.length == 0) {
        return false;
    }

    int l = board.length;
    int w = board[0].length;

    boolean[][] visited = new boolean[l][w];

    for (int i = 0; i < l; i++) {
        for (int j = 0; j < w; j++) {
            if (dfs_(board, i, j, 0, word, visited)) {
                return true;
            }
        }
    }

    return false;
}

private boolean dfs_(char[][] board, int y, int x, int idx, String word, boolean[][] visited) {

    if (idx == word.length()) {
        return true;
    }

    int l = board.length;
    int w = board[0].length;

    if (y < 0 || y >= l || x < 0 || x >= w || visited[y][x] || board[y][x] != word.charAt(idx)) {
        return false;
    }

    /** NOTE !!! we update visited on x, y here */
    visited[y][x] = true;

    int[][] dirs = { {0, 1}, {0, -1}, {1, 0}, {-1, 0} };
    /**
     *  NOTE !!!
     *
     *   instead of below structure:
     *
     *       boolean didFindNextCharacter =
     *                 dfs2(row + 1, col, word, lvl + 1, visited, board) ||
     *                 dfs2(row - 1, col, word, lvl + 1, visited, board) ||
     *                 dfs2(row, col + 1, word, lvl + 1, visited, board) ||
     *                 dfs2(row, col - 1, word, lvl + 1, visited, board);
     *
     *   we can use below logic as well:
     *
     *          for (int[] dir : dirs) {
     *             if (dfs_(board, y + dir[0], x + dir[1], idx + 1, word, visited)) {
     *                 return true;
     *             }
     *         }
     *
     */
    for (int[] dir : dirs) {
        if (dfs_(board, y + dir[0], x + dir[1], idx + 1, word, visited)) {
            return true;
        }
    }

    /** NOTE !!! we undo (backtrack) updated x, y here */
    visited[y][x] = false;

    return false;
}
```

### 2-4) Subsets
```python
# LC 078 Subsets
# V0
# IDEA : Backtracking
class Solution:
    def subsets(self, nums):
        def backtrack(first = 0, curr = []):
            # if the combination is done
            if len(curr) == k:  
                output.append(curr[:])
                return
            for i in range(first, n):
                # add nums[i] into the current combination
                curr.append(nums[i])
                # use next integers to complete the combination
                backtrack(i + 1, curr)
                # backtrack
                curr.pop()
        
        output = []
        n = len(nums)
        for k in range(n + 1):
            backtrack()
        return output

# V0'
# brack tracking
class Solution(object):
    def subsets(self, nums):
        def help(start, tmp, res):
            tmp.sort()
            if tmp not in res:
                res.append(tmp)
            for i in range(start, len(nums)):
                if nums[i] not in tmp:
                    help(start+1, tmp + [nums[i]], res)
        res = []
        start = 0
        tmp = []
        if len(nums) == 1:
            res = [[]]
            res.append(nums)
            return res
        help(start, tmp, res)
        return res

# V0''
# IDEA : DFS 
class Solution(object):
    def subsets(self, nums):
        def dfs(layer, start, tmp):
            if tmp not in res:
                res.append(tmp)
            if layer == len(nums):
                return
            ### NOTE : we have if condition first, then for loop
            for i in range(start, len(nums)):
                ### NOTE below can make loop start `start idx` updated each time
                dfs(layer+1, i+1, tmp + [nums[i]])
        nums.sort()
        res = []
        dfs(0, 0, [])
        return res
```

```java
// java
// LC 78
// V0'
// IDEA : Backtracking
// https://leetcode.com/problems/subsets/editorial/
    List<List<Integer>> output = new ArrayList<>();
    int n, k;

    public void backtrack(int first, ArrayList<Integer> curr, int[] nums) {
        // if the combination is done
        if (curr.size() == k) {
            output.add(new ArrayList<>(curr));
            return;
        }
        /** NOTE HERE !!!
         *
         *  ++i : i+1 first,  then do op
         *  i++ : do op first, then i+1
         *
         *  -> i++ or ++i is both OK here
         */
        for (int i = first; i < n; i++) {
            // add i into the current combination
            curr.add(nums[i]);
            // use next integers to complete the combination
            backtrack(i + 1, curr, nums);
            // backtrack
            curr.remove(curr.size() - 1);
        }
    }

    public List<List<Integer>> subsets(int[] nums) {
        n = nums.length;
        /** NOTE HERE !!!
         *
         *  ++k : k+1 first,  then do op
         *  k++ : do op first, then k+1
         *
         *  -> k++ or ++k is both OK here
         */
        for (k = 0; k < n + 1; k++) {
            backtrack(0, new ArrayList<Integer>(), nums);
        }
        return output;
    }



// V1
// IDEA : BACKTRACK
// https://www.youtube.com/watch?v=REOH22Xwdkk&t=4s
// https://github.com/neetcode-gh/leetcode/blob/main/java/0078-subsets.java
    public List<List<Integer>> subsets_1_2(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        helper(ans, 0, nums, list);
        return ans;
    }

    public void helper(
            List<List<Integer>> ans,
            int start,
            int[] nums,
            List<Integer> list
    ) {
        if (start >= nums.length) {
            ans.add(new ArrayList<>(list));
        } else {

            // decision tree :  add the element and start the  recursive call
            list.add(nums[start]);
            helper(ans, start + 1, nums, list);

            // decision tree :  remove the element and do the backtracking call.
            list.remove(list.size() - 1);
            helper(ans, start + 1, nums, list);
        }
    }    
```

```c++
// c++
// backtrack
// (algorithm book (labu) p.303)

// save all subset
vector<vector<int>> res;

/* main func */
vector<vector<int>> subsets(vector<int> & nums){
    // record visited routes
    vector<int> tracks;
    backtrack(nums, 0, track);
    return res;
}

/* use backtrack pattern */
void backtrack(vector<int> & nums, int start, vector<int> & track){
    // pre-order tranverse
    res.push_back(track);
    // start from `start`, avoid duplivated subset
    for (int i = start; i < nums.size(); i++){
        // make choice
        track.push_back(nums[i]);
        // iteration backtrack
        backtrack(nums, i+1, track);
        // undo choice
        track.pop_back();
    }
}
```

### 2-4') Subsets II
```python
# LC 90 Subsets II
# V0
# IDEA : BACKTRACKING + LC 078 Subsets
from collections import Counter
class Solution(object):
    def subsetsWithDup(self, nums):
        def help(start, tmp, _cnt):
            tmp.sort()
            if tmp not in res:
                res.append(tmp)
            if start >= len(nums):
                return
            for i in range(start, len(nums)):
                if _cnt[nums[i]]  > 0:
                    _cnt[nums[i]] -= 1
                    help(start+1, tmp + [nums[i]], _cnt)
                    """
                    NOTE : here we "undo" the "_cnt[nums[i]] -= 1" op,
                          -> so next recursive can still have the "capacity" of such element
                    """
                    _cnt[nums[i]] += 1

        # edge case
        if not nums:
            return []

        # edge case
        if len(nums) == 1:
            res = [[]]
            res.append([nums[0]])
            return res

        res = [[]]
        _cnt = Counter(nums)
        help(0, [], _cnt)
        print ("res = " + str(res))
        return res

# V0
# IDEA : BRUTE FORCE
class Solution:
    def subsetsWithDup(self, nums):
        # small trick (init with a null array)
        ans=[[]]
        for i in nums:
            for l in list(ans):
                # sorted here, since we want to the "non-duplicated" power set
                temp=sorted(l+[i])
                # avoid duplicated
                if temp not in ans:
                    ans.append(temp) 
        return ans

```

### 2-5) Combinations
```python
# LC 77. Combinations
# V0
# BACKTRACK
class Solution(object):
    def combine(self, n, k): 
        def dfs(current, start):
            if(len(current) == k):
                """
                Both of below approach are OK
                
                list(current) : transform current reference to list
                current[:] : shallow copy
                """
                result.append(list(current))
                return
            
            for i in range(start, n + 1):
                current.append(i)
                dfs(current, i + 1)
                current.pop()
            
        result = []
        dfs([], 1)
        return result

# V0'
# IDEA : BACKTRACK
class Solution:
    def combine(self, n, k):
        res=[]
        def go(i,ma,ans):
            if ma==k:
                res.append(list(ans))
                return
            if i>n:
                return
            ans.append(i)
            go(i+1,ma+1,ans)
            ans.pop()
            go(i+1,ma,ans)
        go(1,0,[])
        return res
```
```c++
// c++
// backtrack
// (algorithm book (labu) p.305)

// record all combinations
vector<vector<int>> res;

/* main func */
vector<vector<int>> combine(int n, int k){
    if (k <= 0 || n <= 0) return res;
    vector<int> track;
    backtrack(n, k, 1, track);
    return res;
}

/* use backtrack pattern */
void backtrack(int n, int k, int start, vector<int> & track){
    // not update res till visit leaf node
    if (k == track.size()){
        res.push_back(track);
        return;
    }

    // increase from i
    for (int i = start; i <= n; i ++){
        // do choice
        track.push_back(i);
        // backtrack
        backtrack(n, k, i+1, track);
        // undo choice
        track.pop_back();
    }
}
```

### 2-6) Permutations
```python
# LC 46. Permutations
# V0
# IDEA : BACKTRACK, 
# similar idea as LC 77 -> difference : contains VS start
class Solution(object):
    def permute(self, nums):
        def help(cur):
            if len(cur) == n_len:
                if cur not in res:
                    res.append(list(cur))
                    return
            if len(cur) > n_len:
                return
            for i in nums:
                #print ("i = " + str(i) + " cur = " + str(cur))
                if i not in cur:
                    cur.append(i)
                    help(cur)
                    cur.pop(-1)
        # edge case
        if not nums:
            return [[]]
        n_len = len(nums)
        res = []
        help([])
        #print ("res = " + str(res))
        return res

# V0 
class Solution(object):
    def permute(self, nums):
        visited = [0] * len(nums)
        res = []
        path = []
        self.dfs(path)
        return res
        
    def dfs(self, path):
        if len(path) == len(nums):
            res.append(path)
        else:
            for i in range(len(nums)):
                if not visited[i]:
                    visited[i] = 1
                    dfs(path + [nums[i]])
                    visited[i] = 0    ### to check if "visited[i] = 0" is necessary
```
```java
// LC 46. Permutations
    List<List<Integer>> ans = new ArrayList<>();

    // V0
    // IDEA : BACKTRACK
    public List<List<Integer>> permute(int[] nums) {

        if (nums.length == 1){
            List<List<Integer>> _ans = new ArrayList<>();
            List<Integer> cur = new ArrayList<>();
            cur.add(nums[0]);
            _ans.add(cur);
            return _ans;
        }

        List<Integer> cur = new ArrayList<>();
        /** NOTE !!! we don't need to set idx param */
        helper(nums, cur);

        return this.ans;
    }

    private void helper(int[] nums, List<Integer> cur){

        if (cur.size() > nums.length){
            return;
        }

        if (!this.ans.contains(cur) && cur.size() == nums.length){

            /** NOTE !!! we use below to add current ArrayList instance to ans */
            this.ans.add(new ArrayList<>(cur));
        }

        
        for (int i = 0; i < nums.length; i++){
            int val = nums[i];
            // input nums is array with distinct integers
            /** NOTE !!! ONLY do recursive, backtrack when meet distinct element */
            if(!cur.contains(val)){
                cur.add(val);
                // recursive call
                helper(nums, cur);
                // undo last op
                cur.remove(cur.size()-1); // NOTE !!! remove last element
            }
        }
    }
```

### 2-7) Generate Parentheses
```python
# python
# LC 022 Generate Parentheses
# V0
# IDEA : bracktrack + Valid Parentheses (LC 020)
class Solution(object):
    def generateParenthesis(self, n):
        # help func for backtracking
        def help(tmp, res, n):
            if len(tmp) == n * 2 and check(tmp):
                res.append(tmp)
                return
            if len(tmp) == n * 2:
                return
            for l in _list:
                print ("l = " + str(l))
                help(tmp + l, res, n)

        """
        LC 020 Valid Parentheses
        """
        def check(s):
            lookup = {"(":")", "[":"]", "{":"}"}
            q = []
            for i in s:
                if i not in lookup and len(q) == 0:
                    return False
                elif i in lookup:
                    q.append(i)
                else:
                    tmp = q.pop()
                    if lookup[tmp] != i:
                        return False
            return True if len(q) == 0 else False

        _list = ['(', ')']
        if n == 1:
            return ["()"]
        res = []
        help("", res, n)
        return res

# V0'
# https://blog.csdn.net/fuxuemingzhu/article/details/79362373
# IDEA: BACKTRACKING + DFS 
# NOTE : KEEP DFS WHEN MEAT 2 CONDTIONS:
#  1) len(path) < n 
#  2) # of "("  > # of ")" (means it's still possible to form a "paratheses" as expected)
class Solution(object):
    def generateParenthesis(self, n):
        res = []
        self.dfs(res, n, n, '')
        return res
        
    def dfs(self, res, left, right, path):
        if left == 0 and right == 0:
            res.append(path)
            return
        if left > 0:
            self.dfs(res, left - 1, right, path + '(')
        if left < right:
            self.dfs(res, left, right - 1, path + ')')
```
```java
// java
// LC 022 Generate Parentheses
// (algorithm book (labu) p.316)

/* main func */
vector<String> generateParentheses(int n){
    if (n == 0) return {};
    // record all legal collections
    vector<string> res;
    // backtrack the routes (in process)
    string track;
    // init : available left Parentheses and right Parentheses counts as n
    backtrack(n, n, track, res);
    return res;
}

/* remain left Parentheses count : left ;.. remain right Parentheses : right */
void backtrack(int left, int right, string& track, vector<string> & res){
    // if count < 0 : illegal
    if (left < 0 || right < 0) return;
    // if remain  left Parentheses count >  right Parentheses count : illegal
    if (right < left) return;
    // if all Parentheses are used : legal, we got one OK solution
    if (left == 0 && right == 0){
        res.push_back(track);
        return;
    }

    // add one more left Parentheses
    track.push_back('(') // do choice
    backtrack(left - 1, right, track, res);
    track.push_back(); // undo choice

    // add one more right Parentheses
    track.push_back(')') // do choice
    backtrack(left, right - 1, track, res);
    track.push_back(); // undo choice
}
```

```java
// java
// V2
// IDEA :  Backtracking, Keep Candidate Valid
// https://leetcode.com/problems/generate-parentheses/editorial/
public List<String> generateParenthesis_3(int n) {
    List<String> answer = new ArrayList<>();
    backtracking(answer, new StringBuilder(), 0, 0, n);

    return answer;
}

private void backtracking(List<String> answer, StringBuilder curString, int leftCount, int rightCount, int n) {
    if (curString.length() == 2 * n) {
        answer.add(curString.toString());
        return;
    }
    if (leftCount < n) {
        curString.append("(");
        backtracking(answer, curString, leftCount + 1, rightCount, n);
        curString.deleteCharAt(curString.length() - 1);
    }
    if (leftCount > rightCount) {
        curString.append(")");
        backtracking(answer, curString, leftCount, rightCount + 1, n);
        curString.deleteCharAt(curString.length() - 1);
    }
}
```

### 2-8) Palindrome Partitioning

```java
// java
// LC 131
// V0-1
// IDEA: BACKTRACK + start_idx (fixed by gpt)
List<List<String>> partitionRes = new ArrayList<>();

public List<List<String>> partition_0_1(String s) {
    if (s == null || s.isEmpty()) {
        return new ArrayList<>();
    }

    backtrack(s, 0, new ArrayList<>());
    return partitionRes;
}

private void backtrack(String s, int start, List<String> currentList) {
/**
 *
 *  •   This is the base case of the recursion.
 *
 *  •   It means: "If we've reached the end of the string,
 *       then the current list of substrings (currentList)
 *       forms a valid full partition of s into palindromes."
 *
 *  •   -> So we add a copy of currentList into
 *         the final result list partitionRes.
 */
/**
 *  - Why start == s.length()?
 *
 *  •   Because start is the index from which
 *      we're currently trying to partition.
 *
 *  •   If start == s.length(), it means we've
 *       used up all characters in s, and currentList is now a full,
 *       valid partition.
 */
if (start == s.length()) {
        partitionRes.add(new ArrayList<>(currentList));
        return;
    }

/**
*  NOTE !!!
*
*   1) we loop from `start + 1` to `s.length()`
*   2) get sub string via s.substring(a, b)
*   3) check if current sub string is Palindrome
*       - if yes,
*          - add sub string to current cache
*          - recursive call backtrack
*          - undo cache add
*/
for (int end = start + 1; end <= s.length(); end++) {
        String sub = s.substring(start, end);
        if (isPalindrome(sub)) {
            currentList.add(sub);
            backtrack(s, end, currentList);
            currentList.remove(currentList.size() - 1); // undo
        }
    }

}

// helper func check if a string is `Palindrome`
public boolean isPalindrome(String x) {
    int l = 0;
    int r = x.length() - 1;
    while (r > l) {
        if (x.charAt(l) != x.charAt(r)) {
            return false;
        }
        r--;
        l++;
    }
    return true;
}
```

```python
# LC 131 Palindrome Partitioning
# V0
# IDEA : BACKTRCK, similar as LC 046 permutations
class Solution(object):
    def partition(self, s):
        def help(s, res, path):
            if not s:
                res.append(path)
                return
            for i in range(1, len(s)+1):
                if s[:i] == s[:i][::-1]:
                    help(s[i:], res, path + [s[:i]])
        # edge case
        if not s:
            return
        res = []
        path = []
        help(s, res, path)
        return res

# V0'
# IDEA : BACKTRCK, similar as LC 046 permutations
class Solution(object):
    def partition(self, s):
        res = []
        self.helper(s, res, [])
        return res
        
    def helper(self, s, res, path):
        if not s:
            res.append(path)
            return
        # beware of the start and the end index
        for i in range(1, len(s) + 1): 
            if self.isPalindrome(s[:i]):
                """
                ### backtrcking 
                if s[:i] is palindrome, then check if there is palindrome in s[i:] as well
                e.g.  
                    a a b b a 
                  => 
                    if 'aa' (<-) is palindrome, then check a b b a (->)
                """
                self.helper(s[i:], res, path + [s[:i]])

    def isPalindrome(self, x):
        return x == x[::-1]
```

### 2-9) Restore IP Addresses
```python
# 093 Restore IP Addresses
# V0 
# IDEA : DFS
class Solution(object):
    def restoreIpAddresses(self, s):
        # if not valid input form (ip address length should < 12)
        if len(s) > 12:
            return []
        res = []
        self.dfs(s, [], res)
        return res
        
    def dfs(self, s, path, res):
        # if not remaining elments (not s) and path is in "xxx.xxx.xxx.xxx" form
        if not s and len(path) == 4:
            res.append('.'.join(path))
            return
        for i in [1,2,3]:
            # avoid "out of index" error
            if i > len(s):
                continue
            number = int(s[:i])
            # str(number) == s[:i] for checking if digit is not starting from "0"
            # e.g. 030 is not accepted form, while 30 is OK
            if str(number) == s[:i] and number <= 255:
                self.dfs(s[i:], path + [s[:i]], res)
```

### 2-9) Word Break
```python
# LC 139 Word Break
# V0
# IDEA : BFS
class Solution:
    def wordBreak(self, s, wordDict):
        if not s or not wordDict:
            return
        q = collections.deque()
        q.append(0)
        visited = [None]*len(s)
        while q:
            i = q.popleft()
            if not visited[i]:
                for j in range(i+1,len(s)+1):                 
                    if s[i:j] in wordDict:                    
                        if j == len(s):
                            return True  
                        q.append(j)
                visited[i]=True
```

### 2-10) Word Break II
```python
# LC 140 Word Break II
# NOTE : there is also dfs, dp approaches
# V0
# IDEA : BACKTRCK, LC 078 Subsets
class Solution(object):
    def wordBreak(self, s, wordDict):
        def help(cur):
            """
            NOTE this !!! : 
                -> shallow copy cur[:]
            """
            if "".join(cur[:]) == s:
                res.append(" ".join(cur[:]))
                return
            if len("".join(cur[:])) > len(s):
                return
            for i in range(len(wordDict)):
                cur.append(wordDict[i])
                help(cur)
                # NOTE this
                cur.pop()

        # edge case
        if not wordDict:
            return []
        res = []
        cur = []
        cnt = 0
        help(cur)
        print ("res = " + str(res))
        return res

# V1
# IDEA : RECURSION
# https://leetcode.com/problems/word-break-ii/discuss/1426014/Python-interview-friendly-simple-recursion
class Solution:
    def wordBreak(self, s: str, wordDict: List[str]) -> List[str]:
        def recur(s, path):
            if not s:
                out.append(' '.join(path))
                return
            for i in range(1,len(s)+1):
                w,new_s = s[:i], s[i:]
                if w in wordDict:
                    recur(new_s, path + [w])
        wordDict, out = set(wordDict), []
        recur(s,[])
        return out

# V1'
# IDEA : BACKTRCK
# https://leetcode.com/problems/word-break-ii/discuss/44404/Python-backtracking
class Solution:
    def wordBreak(self, s, dic):
        if not dic:
            return []
        n = max(len(d) for d in dic)
        stack, parents = [0], collections.defaultdict(set)
        while stack:
            parent = stack.pop()
            for child in range(parent+1, parent+n+1):
                if s[parent:child] in dic:
                    if child not in parents:
                        stack.append(child)
                    parents[child].add(parent)
        stack, res = [[len(s)]], []
        while stack:
            r = stack.pop()
            if r[0] == 0:
                r = [s[i:j] for i, j in zip(r[:-1], r[1:])]
                res.append(' '.join(r))
            for parent in parents[r[0]]:
                stack.append([parent]+r)
        return res
```

### 2-11) Course Schedule
```java
// java
// LC 207
// V0
// IDEA : DFS (fix by gpt)
// NOTE !!! instead of maintain status (0,1,2), below video offers a simpler approach
//      -> e.g. use a set, recording the current visiting course, if ANY duplicated (already in set) course being met,
//      -> means "cyclic", so return false directly
// https://www.youtube.com/watch?v=EgI5nU9etnU
public boolean canFinish(int numCourses, int[][] prerequisites) {
    // Initialize adjacency list for storing prerequisites
    /**
     *  NOTE !!!
     *
     *  init prerequisites map
     *  {course : [prerequisites_array]}
     *  below init map with null array as first step
     */
    Map<Integer, List<Integer>> preMap = new HashMap<>();
    for (int i = 0; i < numCourses; i++) {
        preMap.put(i, new ArrayList<>());
    }

    // Populate the adjacency list with prerequisites
    /**
     *  NOTE !!!
     *
     *  update prerequisites map
     *  {course : [prerequisites_array]}
     *  so we go through prerequisites,
     *  then append each course's prerequisites to preMap
     */
    for (int[] pair : prerequisites) {
        int crs = pair[0];
        int pre = pair[1];
        preMap.get(crs).add(pre);
    }

    /** NOTE !!!
     *
     *  init below set for checking if there is "cyclic" case
     */
    // Set for tracking courses during the current DFS path
    Set<Integer> visiting = new HashSet<>();

    // Recursive DFS function
    for (int c = 0; c < numCourses; c++) {
        if (!dfs(c, preMap, visiting)) {
            return false;
        }
    }
    return true;
}

private boolean dfs(int crs, Map<Integer, List<Integer>> preMap, Set<Integer> visiting) {
    /** NOTE !!!
     *
     *  if visiting contains current course,
     *  means there is a "cyclic",
     *  (e.g. : needs to take course a, then can take course b, and needs to take course b, then can take course a)
     *  so return false directly
     */
    if (visiting.contains(crs)) {
        return false;
    }
    /**
     *  NOTE !!!
     *
     *  if such course has NO preRequisite,
     *  return true directly
     */
    if (preMap.get(crs).isEmpty()) {
        return true;
    }

    /**
     *  NOTE !!!
     *
     *  add current course to set (Set<Integer> visiting)
     */
    visiting.add(crs);
    for (int pre : preMap.get(crs)) {
        if (!dfs(pre, preMap, visiting)) {
            return false;
        }
    }
    /**
     *  NOTE !!!
     *
     *  remove current course from set,
     *  since already finish visiting
     *
     *  e.g. undo changes
     */
    visiting.remove(crs);
    preMap.get(crs).clear(); // Clear prerequisites as the course is confirmed to be processed
    return true;
}