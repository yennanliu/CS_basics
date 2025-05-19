# Intervals

- [fucking algorithm : interval merge](https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/%E5%8C%BA%E9%97%B4%E8%B0%83%E5%BA%A6%E9%97%AE%E9%A2%98%E4%B9%8B%E5%8C%BA%E9%97%B4%E5%90%88%E5%B9%B6.md)
- [fucking algorithm : interval overlap](https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/%E5%8C%BA%E9%97%B4%E4%BA%A4%E9%9B%86%E9%97%AE%E9%A2%98.md)
- [Visualization explaination](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/array_overlap_explaination.md
)


## 0) Concept  

### 0-1) Types

- Types

    - Merge intervals
        - LC 56
        - sort on `1st element`

    - Insert intervala
        - LC 57
        - sort on `1st element`

    - Overlap intervals

    - Non-overlap intervals
        - LC 435
        - sort on `2nd element`

    - Max length of pair chain
        - LC 646

    - Courses problems
        - LC 207, 210

    - Meeting room problems
        - LC 252, 253

    - Check if overlap existed
        - LC 729

- Algorithm
    - array op
    - dict op

- Data structure
    - array
    - dict


### 0-2) Pattern

- Step 1) sort on elements
- Step 2) loop over sorted elements, check if `overlap`, updated val (1st, 2nd element)
- Step 3  append updated window to array


#### 0-2-1) Check if 2 intervals are overlap

- LC 729
- https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/Array/MyCalendar1.java


- *Conclusion*:

if `start < date.get(1) and end > date.get(0)`
-> then intervals are OVERLAP


##### `NON OVERLAP` case (sort on 1st element first)

-> or, we can also use `NO OVERLAP` perspectives
-> if we first sort array on `1st element`,
   then the ONLY `overlap` case is as below
   e.g. `old[1] < new[0]`, we can use this as condition
   - LC 56, LC 57

```
|------| old
             
             |------| new
```

##### Overlap cases

```
# case 1

New:       |-------|
Existing:     |------|


# case 2

New:         |---|
Existing:   |-------|

# case 3

New:     |-----------|
Existing:   |-----|



-> All of cases above are with `start < date.get(1) and end > date.get(0)` condition
```


```java
// java
// LC 57

// ...
Arrays.sort(intervals, new Comparator<int[]>() {
    @Override
    public int compare(int[] o1, int[] o2) {
        int diff = o1[0] - o2[0];
        return diff;
    }
});

// ...

for(int[] x: intervals){
    // case 1) stack is empty
    if(st.isEmpty()){
        st.add(x);
    }else{
        /**
         *  LC 57
         *
         *  since we already sorted intervals on
         *  1st element as increasing order (small -> big)
         *  the ONLY non overlap case is as below:
         *
         *    |----|  old
         *            |------| new
         */
        int[] prev = st.pop();
        // case 2) if NOT overlap
        if(prev[1] < x[0]){
            st.add(prev);
            st.add(x);
        }
        // case 3) OVERLAP
        else{
            st.add(new int[]{ Math.min(prev[0], x[0]), Math.max(prev[1], x[1]) });
        }
    }
}

```

## 1) General form

### 1-1) Basic OP

#### 1-1-1) `List` -> `Array`

```java
// java

// LC 56, 57

List<int[]> myList = new ArrayList();
int[][] myArr = myList.toArray(new int[myList.size()][]);


// example
LinkedList<int[]> res = new LinkedList<>();
// ...
int[][] ans = res.toArray(new int[res.size()][]);
```


#### 1-2-0) merge intervals

```java
// java
// LC 56, LC 57

// ...
// sort
intervalList.sort(Comparator.comparingInt(a -> a[0]));
// ...

List<int[]> merged = new ArrayList<>();

// ...

for (int[] x : intervalList) {
       if (merged.isEmpty() || merged.get(merged.size() - 1)[1] < x[0]) {
                merged.add(x);
            }
        else{
            // NOTE : we set 0 idx as SMALLER val from merged last element (0 idx), input
            merged.get(merged.size() - 1)[0] = Math.min(merged.get(merged.size() - 1)[0], x[0]);
            // NOTE : we set 1 idx as BIGGER val from merged last element (1 idx), input
            merged.get(merged.size() - 1)[1] = Math.max(merged.get(merged.size() - 1)[1], x[1]);
        }
}

// ...
```


#### 1-3-0) Non overlapping intervals

```java
// java
// LC 435

// ...
 Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1]));

// ...
```


## 2) LC Example

### 2-1) Merge Intervals

- TODO : move it to "interval merge cheatsheet"
- [fucking-algorithm - intervals](https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/%E5%8C%BA%E9%97%B4%E8%B0%83%E5%BA%A6%E9%97%AE%E9%A2%98%E4%B9%8B%E5%8C%BA%E9%97%B4%E5%90%88%E5%B9%B6.md)

```python
# 056 Merge Intervals
# V0
# IDEA : interval op, LC 57
class Solution(object):
    def merge(self, intervals):
        # edge case
        if not intervals:
            return
        intervals.sort(key=lambda x : x[0])
        res = []
        for i in range(len(intervals)):
            if not res or res[-1][1] < intervals[i][0]:
                res.append(intervals[i])
            else:
                res[-1][1] = max(intervals[i][1], res[-1][1])
        return res

# V0'
# IDEA : interval op
# https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/%E5%8C%BA%E9%97%B4%E8%B0%83%E5%BA%A6%E9%97%AE%E9%A2%98%E4%B9%8B%E5%8C%BA%E9%97%B4%E5%90%88%E5%B9%B6.md
class Solution:
    def merge(self, intervals):
        intervals = sorted(intervals, key=lambda x: x[0])
        result = []
        for interval in intervals:
            if len(result) == 0 or result[-1][1] < interval[0]:
                result.append(interval)
            else:
                result[-1][1] = max(result[-1][1], interval[1])
        return result
```

### 2-2) Non-overlapping-intervals

```java
// java
// LC 435

// V0-3
// IDEA: GREEDY + SORTING (gpt)
public int eraseOverlapIntervals_0_3(int[][] intervals) {
    // Edge case: empty or single interval, nothing to remove
    if (intervals == null || intervals.length <= 1) {
        return 0;
    }

    // Sort intervals by end time (ascending) — Greedy strategy
    Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1]));

    int count = 0;
    int prevEnd = intervals[0][1];

    for (int i = 1; i < intervals.length; i++) {
        if (intervals[i][0] < prevEnd) {
            // Overlapping: remove current interval
            count++;
        } else {
            // Non-overlapping: update end marker
            prevEnd = intervals[i][1];
        }
    }

    return count;
}
```

### 2-3) Insert Interval

```python
# LC 57 Insert Interval
# V0
# IDEA : compare merged[-1][1]. interval[0]
# https://leetcode.com/problems/insert-interval/discuss/1236101/Python3-Easy-to-Understand-Solution
### NOTE : there are only 2 cases
# case 1) no overlap -> append interval directly
# case 2) overlap -> MODIFY 2nd element in last merged interval with the bigger index
class Solution:
    def insert(self, intervals, newInterval):
        ### NOTE THIS TRICK!!! : APPEND newInterval to intervals
        intervals.append(newInterval)
        # need to sort first (by 1st element)
        intervals.sort(key=lambda x:x[0])
        merged = []
        for interval in intervals:
            ### NOTE this condition
            # if not merged : append directly
            # if merged[-1][1] < interval[0] : means no overlap : append directly as well
            if not merged or merged[-1][1] < interval[0]:
                merged.append(interval)
            else:
                ### NOTE this op, if there is overlap, we ONLY modify the 2nd element in last interval with BIGGER digit
                merged[-1][1]= max(merged[-1][1],interval[1])
        return merged
```

### 2-4) Maximum Length of Pair Chain

```python
# LC 646 Maximum Length of Pair Chain
# V0 
# IDEA : GREEDY + sorting
# ->  we sort on pair's "2nd" element -> possible cases that we can get sub pairs with max length with the needed conditions
# ->  we need to find the "max length" of "continous or non-continous" sub pairs (with condition)
#      -> so start from the "sorted 1st pair" CAN ALWAYS MAKE US GET THE MAX LENGTH of sub pairs with the condition ( we define a pair (c, d) can follow another pair (a, b) if and only if b < c. Chain of pairs can be formed in this fashion.)
class Solution(object):
    def findLongestChain(self, pairs):
        pairs = sorted(pairs, key=lambda x : x[1])
        ### NOTICE HERE
        currTime, ans = float('-inf'), 0
        for x, y in pairs:
            ### NOTICE HERE
            if currTime < x:
                currTime = y
                ans += 1
        return ans

# V0'
class Solution(object):
    def findLongestChain(self, pairs):
        pairs = sorted(pairs, key=lambda x : x[1])
        ### NOTICE HERE
        currTime, ans = float('-inf'), 0
        for x, y in pairs:
            ### NOTICE HERE
            if currTime < x:
                currTime = y
                ans += 1
        return ans
```

### 2-5) Minimum Number of Arrows to Burst Balloons

```python
# 452 Minimum Number of Arrows to Burst Balloons
# https://blog.csdn.net/MebiuW/article/details/53096708
class Solution(object):
    def findMinArrowShots(self, points):
        if not points: return 0
        points.sort(key = lambda x : x[1])
        curr_pos = points[0][1]
        ans = 1
        for i in range(len(points)):
            if curr_pos >= points[i][0]:
                continue
            curr_pos = points[i][1]
            ans += 1
        return ans
```