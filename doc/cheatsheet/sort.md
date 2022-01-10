# Sort

## 0) Concept  

### 0-1) Types
- Bubble sort
- Quick sort
- Insertion sort
- Heap sort
- Merge sort
- Selection sort
- Topological sort

### 0-2) Pattern

## 1) General form

### 1-1) Basic OP

## 2) LC Example

### 2-1) Pancake Sorting
```python
# python
# LC 969 Pancake Sorting
# V0
# IDEA : pankcake sort + while loop
# IDEA : 3 STEPS
#   -> step 1) Find the maximum number in arr
#   -> step 2) Reverse from 0 to max_idx
#   -> step 3) Reverse whole list
# https://github.com/yennanliu/CS_basics/blob/master/algorithm/python/pancake_sort.py
class Solution(object):
    def pancakeSort(self, arr):
        """Sort Array with Pancake Sort.
        :param arr: Collection containing comparable items
        :return: Collection ordered in ascending order of items
        Examples:
        >>> pancake_sort([0, 5, 3, 2, 2])
        [0, 2, 2, 3, 5]
        >>> pancake_sort([])
        []
        >>> pancake_sort([-2, -5, -45])
        [-45, -5, -2]
        """
        cur = len(arr)
        res = []
        while cur > 1:
            # step 1) Find the maximum number in arr
            max_idx = arr.index(max(arr[0:cur]))
            res = res + [max_idx+1, cur] # idx is 1 based
            # step 2) Reverse from 0 to max_idx
            #arr = arr[max_idx::-1] + arr[max_idx + 1 : len(arr)] # this is OK as well
            arr = arr[:max_idx][::-1] + arr[max_idx + 1 : len(arr)]
            # step 3) Reverse whole list
            #arr = arr[cur - 1 :: -1] + arr[cur : len(arr)] # this is OK as well
            arr = arr[:cur - 1][::-1] + arr[cur : len(arr)]
            cur -= 1
        print ("arr = " + str(arr))
        return res

# V1
# https://leetcode.com/problems/pancake-sorting/discuss/817978/Python-O(n2)-by-simulation-w-Comment
# https://leetcode.com/problems/pancake-sorting/discuss/330990/Python
class Solution:
    def pancakeSort(self, A):

        res = []

        for x in range(len(A), 1, -1):
            # Carry out pancake-sort from largest number n to smallest number 1

            # find the index of x
            i = A.index(x)

            # flip first i+1 elements to put x on A[0]
            # flip first x elements to put x on A[x-1]
            # now, x is on its corresponding position A[x-1] on ascending order
            # 
            """
            # array extend
            In [10]: x = [1,2,3]

            In [11]: x.extend([4])

            In [12]: x
            Out[12]: [1, 2, 3, 4]

            In [13]: x = [1,2,3]

            In [14]: x = x + [4]

            In [15]: x
            Out[15]: [1, 2, 3, 4]

            """
            #res.extend([i + 1, x])
            res = res + [i + 1, x]

            # update A
            """
            https://stackoverflow.com/questions/509211/understanding-slice-notation

            a[::-1]    # all items in the array, reversed
            a[1::-1]   # the first two items, reversed
            a[:-3:-1]  # the last two items, reversed
            a[-3::-1]  # everything except the last two items, reversed

            -> A[:i:-1] : last i items, reversed

            """
            A = A[:i:-1] + A[:i]
        #print ("res = " + str(res))
        return res

# V1
# IDEA : RECURSIVE
# https://leetcode.com/problems/pancake-sorting/discuss/553116/My-python-solution
# https://leetcode.com/problems/pancake-sorting/discuss/274921/PythonDetailed-Explanation-for-This-Problem
class Solution:
    def pancakeSort(self, A):
        pointer = len(A)
        result = []

        while pointer > 1:
            idx = A.index(pointer)
            result.append(idx + 1)
            A = A[idx::-1] + A[idx + 1:]
            result.append(pointer)
            A = A[pointer - 1::-1] + A[pointer:]
            pointer -= 1
            
        return result
```
```java
// java
// aAlgorithm book (labu) p. 347

// record reverse op array
LinkedList<Integer> res = new LinkedList<>{};

List<Integer> pancakeSort(int[] cakes){
	sort(cakes, cakes.length);
	return res;
}

// order first N pancakes
void sort(int[] cakes, int n){
	// base case
	if (n == 1) return;

	// find max index
	int maxCake = 0;
	int maxCakeIndex = 0;
	for (int i = 0; i < n; i ++){
		if (cakes[i] > maxCake){
			maxCakeIndex = i;
			maxCake = cakes[i];
		}
	}
	// after 1st flip, put max pancake to the 1st layer
	reverse(cakes, 0, maxCakeIndex);
	// record this flip
	res.add(maxCakeIndex+1);
	// 2nd flip, make max pancake to the bottom (last layer)
	reverse(cakes, 0, n-1);
	// record this flop
	res.add(n);
	// recursive call : flip the remaining pancakes
	sort(cakes, n-1);
}

/** flip arr[i..j] elements */
void reverse(int[] arr, int i, int j){
	while (i < j){
		int tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
		i++;
		j--;
	}
}
```
