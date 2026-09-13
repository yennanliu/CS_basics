"""

923. 3Sum With Multiplicity
Medium

Given an integer array arr, and an integer target, return the number of tuples i, j, k such that i < j < k and arr[i] + arr[j] + arr[k] == target.

As the answer can be very large, return it modulo 10^9 + 7.

Example 1:

Input: arr = [1,1,2,2,3,3,4,4,5,5], target = 8
Output: 20
Explanation:
Enumerating by the values (arr[i], arr[j], arr[k]):
(1, 2, 5) occurs 8 times;
(1, 3, 4) occurs 8 times;
(2, 2, 4) occurs 2 times;
(2, 3, 3) occurs 2 times.

Example 2:

Input: arr = [1,1,2,2,2,2], target = 5
Output: 12
Explanation:
arr[i] = 1, arr[j] = arr[k] = 2 occurs 12 times:
We choose one 1 from [1,1] in 2 ways,
and two 2s from [2,2,2,2] in 6 ways.

Example 3:

Input: arr = [2,1,3], target = 6
Output: 1
Explanation: (1, 2, 3) occured one time in the array so we return 1.

Constraints:

3 <= arr.length <= 3000
0 <= arr[i] <= 100
0 <= target <= 300

"""

# V0 

# V1
# https://www.jiuzhang.com/solution/3sum-with-multiplicity/#tag-highlight-lang-python
# time = O(n)
# space = O(n)
import collections
class Solution:
    """
    @param A: the given integer array
    @param target: the given integer target
    @return: the number of tuples
    """
    def threeSumMulti(self, A, target):
        # Write your code here
        # count the elements in A, counter[a] : # of a in A 
        counter = collections.Counter(A)
        res = 0
        # go throgh all possible collections, and calculate their values  
        # not that i, j, k here is the value, but not index in A  
        for i in range(101):
            for j in range(i, 101):
                k = target - i - j
                if k > 100 or k < 0:
                    continue
                if i == j == k: 
                    # case 1) :  i = j = k 
                    res += counter[i] * (counter[i] - 1) * (counter[i] - 2) // 6
                elif i == j != k: 
                    # case 2) : i= j != k or i=k !=j j=k != i 
                    res += counter[i] * (counter[i] - 1) // 2 * counter[k]
                elif k > j: 
                    # case 3) : i != j != k. for not depulicated count, we set up the i, j k order (by value) here
                    res += counter[i] * counter[j] * counter[k]
        return res % (10**9 + 7)

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/83045983
# time = O(n^2)
# space = O(n)
class Solution(object):
    def threeSumMulti(self, A, target):
        """
        :type A: List[int]
        :type target: int
        :rtype: int
        """
        count = collections.Counter(A)
        Aset = set(A)
        Alist = list(Aset)
        Alist.sort()
        _lenA = len(Alist)
        res = 0
        for i in range(_lenA):
            for j in range(i, _lenA):
                c = target - Alist[i] - Alist[j]
                if c >= Alist[j] and c in Aset:
                    if Alist[i] != Alist[j] != c:
                        res += count[Alist[i]] * count[Alist[j]] * count[c]
                    elif Alist[i] == Alist[j] and Alist[j] != c:
                        res += count[c] * self.caculate(count[Alist[i]], 2)
                    elif Alist[j] == c and Alist[i] != Alist[j]:
                        res += count[Alist[i]] * self.caculate(count[Alist[j]], 2)
                    elif Alist[i] == c and Alist[j] != c:
                        res += count[Alist[j]] * self.caculate(count[c], 2)
                    else:
                        res += self.caculate(count[Alist[i]], 3)
        return res % (10 ** 9 + 7)
    
    def caculate(self, x, i):
        if i == 2:
            return x * (x - 1) / 2
        elif i == 3:
            return x * (x - 1) * (x - 2) / 6
                       
# V2
# time = O(n^2), n is the number of disctinct A[i]
# space = O(n)
import collections
import itertools
class Solution(object):
    def threeSumMulti(self, A, target):
        """
        :type A: List[int]
        :type target: int
        :rtype: int
        """
        count = collections.Counter(A)
        result = 0
        for i, j in itertools.combinations_with_replacement(count, 2):
            k = target - i - j
            if i == j == k:
                result += count[i] * (count[i]-1) * (count[i]-2) // 6
            elif i == j != k:
                result += count[i] * (count[i]-1) // 2 * count[k]
            elif max(i, j) < k:
                result += count[i] * count[j] * count[k]
        return result % (10**9 + 7)