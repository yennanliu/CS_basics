# Binary Search Cheatsheet

### 1) General form 
```python
def binary_search(nums, target):
	l, r = 0, len(nums)-1
	while r >= l:
		mid = l + (r-l)//2
		if nums[mid] == target:
			return mid 
		elif nums[mid] < target:
			l = mid+1
		else:
			r = mid-1 
	return -1

```

### 2-1) For loop + binary search
```python
# 167 Two Sum II - Input array is sorted
class Solution(object):
    def twoSum(self, numbers, target):
        for i in range(len(numbers)):
            l, r = i+1, len(numbers)-1
            tmp = target - numbers[i]
            while l <= r:
                mid = l + (r-l)//2
                if numbers[mid] == tmp:
                    return [i+1, mid+1]
                elif numbers[mid] < tmp:
                    l = mid+1
                else:
                    r = mid-1
```