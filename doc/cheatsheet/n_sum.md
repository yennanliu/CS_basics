# N Sum

## 0) Concept  

### 0-1) Types

- Types
    - 2 Sum
        - 2 Sum
        - 2 Sum II : array is sorted
        - 2 Sum III - Data structure design
        - 2 Sum IV - Input is a BST
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
//--------------------
// 2 SUM general form
//--------------------
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