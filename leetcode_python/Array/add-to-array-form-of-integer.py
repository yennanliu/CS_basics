"""

989. Add to Array-Form of Integer
Easy

The array-form of an integer num is an array representing its digits in left to right order.

For example, for num = 1321, the array form is [1,3,2,1].
Given num, the array-form of an integer, and an integer k, return the array-form of the integer num + k.

 

Example 1:

Input: num = [1,2,0,0], k = 34
Output: [1,2,3,4]
Explanation: 1200 + 34 = 1234
Example 2:

Input: num = [2,7,4], k = 181
Output: [4,5,5]
Explanation: 274 + 181 = 455
Example 3:

Input: num = [2,1,5], k = 806
Output: [1,0,2,1]
Explanation: 215 + 806 = 1021
 

Constraints:

1 <= num.length <= 104
0 <= num[i] <= 9
num does not contain any leading zeros except for the zero itself.
1 <= k <= 104

"""

# V0
# IDEA : array op
class Solution:
    def addToArrayForm(self, num, k):
        s = ""
        for i in num:
            s += str(i)       
        answer = int(s) + k
        return  list("".join(str(answer)))

# V1
# IDEA : array op
# https://leetcode.com/problems/add-to-array-form-of-integer/discuss/1433276/Python-solution
class Solution:
    def addToArrayForm(self, num: List[int], k: int) -> List[int]:
        s = ""
        for i in num:
            s += str(i)       
        answer = int(s) + k
        return  list("".join(str(answer)))

# V1
# IDEA : int adding
# https://leetcode.com/problems/add-to-array-form-of-integer/discuss/473808/python
class Solution:
    def addToArrayForm(self, A: List[int], K: int) -> List[int]:
        K = list(map(int,list(str(K))))
        n,m = len(A),len(K)
        carry = 0
        if m > n:
            n,m = m,n
            A,K = K,A
        for i in range(1,n+1):
            if i <= m:
                addednt = K[-i]
            else:
                addednt = 0
            v = A[-i] + addednt + carry 
            A[-i]  = v%10
            carry = v//10
        return [carry] + A if carry else A

# V1
# IDEA :  Schoolbook Addition
# https://leetcode.com/problems/add-to-array-form-of-integer/solution/
class Solution(object):
    def addToArrayForm(self, A, K):
        A[-1] += K
        for i in range(len(A) - 1, -1, -1):
            carry, A[i] = divmod(A[i], 10)
            if i: A[i-1] += carry
        if carry:
            A = map(int, str(carry)) + A
        return A

# V1
# https://leetcode.com/problems/add-to-array-form-of-integer/discuss/874679/Python-one-liner
class Solution:
    def addToArrayForm(self, A: List[int], K: int) -> List[int]:
        return [int(x) for x in str(int(''.join(str(x) for x in A))+K)]

# V2