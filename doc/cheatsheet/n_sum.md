# N Sum

## 0) Concept  

### 0-1) Types

- Types
    - 2 Sum
        - 2 Sum
        - 2 Sum II : array is sorted
        - 2 Sum III - Data structure design
        - 2 Sum IV - Input is a BST
        - Two Sum Less Than K
    - 3 Sum
    - 4 Sum
    - N Sum

- Algorithm
    - sorting
    - 2 pointers
        - sorting + 2 pointers
    - hashmap op
    - array op

- Data structure
    - dict
    - set
    - array
    - Treenode

### 0-2) Pattern

## 1) General form

```c++
// c++
//---------------------------------
// 2 SUM general form (target = 0)
//---------------------------------
// (algorithm book (labu) p.329)
vector<vector<int>> twoSumTarget(vector<int> & nums, int integer){
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
            res.push_back((left, right));
            /** AVOID ADDING duplicated combinations */
            while (lo < hi && nums[lo] == left) lo++;
            while (lo < hi && nums[hi] == right) hi++;
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
            res.push_back((left, right));
            /** AVOID ADDING duplicated combinations */
            while (lo < hi && nums[lo] == left) lo++;
            while (lo < hi && nums[hi] == right) hi++;
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
            int left = nums[lo], int right = nums[hi];
            if (sum < target){
                while (lo < hi && nums[lo] == left) lo ++;
            }else if (sum > target){
                while (lo < hi && nums[hi] == right) hi --;
            }else{
                res.push_back((left, right));
                /** AVOID ADDING duplicated combinations */
                while (lo < hi && nums[lo] == left) lo++;
                while (lo < hi && nums[hi] == right) hi++;
            }
        }
    }else{
        /** n > 2, recursive get (n-1) Sum result */
        for (int i = start; i < sz; i++){
            vector<vector<int>>;
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

### 1-1) Basic OP

## 2) LC Example

### 2-1) 2 Sum
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

### 2-1') 2 Sum II
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

### 2-2) 3 Sum
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

# V0
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

# V0'
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

### 2-3) 4 Sum
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
### 2-4) Two Sum Less Than K
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