# V0 

# V1 
# https://www.jiuzhang.com/solution/3sum-with-multiplicity/#tag-highlight-lang-python
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
# Time:  O(n^2), n is the number of disctinct A[i]
# Space: O(n)
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