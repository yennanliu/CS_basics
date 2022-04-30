"""

1027. Longest Arithmetic Subsequence
Medium

Given an array nums of integers, return the length of the longest arithmetic subsequence in nums.

Recall that a subsequence of an array nums is a list nums[i1], nums[i2], ..., nums[ik] with 0 <= i1 < i2 < ... < ik <= nums.length - 1, and that a sequence seq is arithmetic if seq[i+1] - seq[i] are all the same value (for 0 <= i < seq.length - 1).

 

Example 1:

Input: nums = [3,6,9,12]
Output: 4
Explanation: 
The whole array is an arithmetic sequence with steps of length = 3.
Example 2:

Input: nums = [9,4,7,2,10]
Output: 3
Explanation: 
The longest arithmetic subsequence is [4,7,10].
Example 3:

Input: nums = [20,1,15,3,10,5,8]
Output: 4
Explanation: 
The longest arithmetic subsequence is [20,15,10,5].
 

Constraints:

2 <= nums.length <= 1000
0 <= nums[i] <= 500

"""

# V0
# IDEA : DP
class Solution:
    def longestArithSeqLength(self, A):
            dp = {}
            for i in range(len(A)):
                for j in range(i + 1, len(A)):
                    dp[j, A[j] - A[i]] = dp.get((i, A[j] - A[i]), 1) + 1
            return max(dp.values())

# V0'
# IDEA : HASH TABLE
# https://leetcode.com/problems/longest-arithmetic-subsequence/discuss/274657/Short-Python-solution
class Solution:
    def longestArithSeqLength(self, A):
        aux, cnt, prefix = {a : {} for a in A}, {}, set()
        for a in A:
            cnt[a] = cnt[a] + 1 if a in cnt else 1
            for b in prefix:
                if a != b:
                    aux[a][a - b] = 1 + aux[b][a - b] if a - b in aux[b] else 2
            prefix.add(a)
            
        max_const = max(cnt.values())
        max_aux = max(max(d.values()) for a, d in aux.items() if d)
        return max(max_const, max_aux, 2)

# V1
# https://www.796t.com/article.php?id=154559
# http://www.noteanddata.com/leetcode-1027-Longest-Arithmetic-Sequence-Google-Interview-Problem-java-solution-note.html
# https://blog.csdn.net/w5688414/article/details/109696664

# V1
# IDEA : HASH
# https://leetcode.com/problems/longest-arithmetic-subsequence/discuss/274657/Short-Python-solution
class Solution:
    def longestArithSeqLength(self, A):
        aux, cnt, prefix = {a : {} for a in A}, {}, set()
        for a in A:
            cnt[a] = cnt[a] + 1 if a in cnt else 1
            for b in prefix:
                if a != b:
                    aux[a][a - b] = 1 + aux[b][a - b] if a - b in aux[b] else 2
            prefix.add(a)
            
        max_const = max(cnt.values())
        max_aux = max(max(d.values()) for a, d in aux.items() if d)
        return max(max_const, max_aux, 2)

# V1'
# https://leetcode.com/problems/longest-arithmetic-subsequence/discuss/275395/python-O(n**2)-solution
class Solution:
    def longestArithSeqLength(self, A):
        # Constant seq: '0000', O(len(A) )
        ct = collections.Counter(A)
        ans = max(2, max(ct[i] for i in ct))
        
        # Increasing seq:'1234', O(len(A)**2 )
        ansdic = {}
        for i in range(len(A)):
            for j in range(i):
                a0, a1, a2 = A[j]*2-A[i], A[j], A[i]
                if a0 == a1:continue
                if (a0, a1) in ansdic:
                    ansdic[a1, a2] = ansdic[a0, a1] + 1
                    ans = max(ansdic[a1, a2], ans)
                else:
                    ansdic[a1, a2] = 2
        return ans

# V1''
# IDEA : HASH SET
# https://leetcode.com/problems/longest-arithmetic-subsequence/discuss/274625/simple-hash-Set-Python
class Solution(object):
    def longestArithSeqLength(self, A):
        res = 2
        if len(A) <= 2:
            return len(A)
        cnt = {}
        node = {}
        mx = {}
        curr = A[1] - A[0]
        cnt[(curr,1)] = 2
        node[curr] = set()
        node[curr].add(1)
        mx[curr] = 2
        
        res = 2
        for i in range(2,len(A)):
            for j in range(i):
                dis = A[i] - A[j]
                if dis in node:
                    if j in node[dis]:
                        cnt[(dis,i)] = cnt[(dis,j)] + 1                        
                        #node[dis].remove(j)
                        node[dis].add(i)
                        mx[dis] = max(mx[dis], cnt[(dis,i)])
                        res = max(mx[dis],res)
                    else:
                        cnt[(dis,i)] = 2
                        node[dis].add(i)
                else:
                    cnt[(dis,i)] = 2
                    node[dis] = set()
                    node[dis].add(i)
                    mx[dis] = 2
            
        
        return res

# V1'''
# IDEA : DP
# https://leetcode.com/problems/longest-arithmetic-subsequence/discuss/274611/JavaC%2B%2BPython-DP
class Solution:
    def longestArithSeqLength(self, A):
            dp = {}
            for i in range(len(A)):
                for j in range(i + 1, len(A)):
                    dp[j, A[j] - A[i]] = dp.get((i, A[j] - A[i]), 1) + 1
            return max(dp.values())

# V1''''
# IDEA : DP
# https://leetcode.com/problems/longest-arithmetic-subsequence/discuss/514742/Python-DP
class Solution:
    def longestArithSeqLength(self, A):
        DP = {}
        A_len = len(A)
        for right in range(1, A_len):
            for left in range(right):
                diff = A[right] - A[left]
                #if (diff, left) in DP:
                #    DP[(diff, right)] = DP[(diff, left)] + 1
                #else:
                #    DP[(diff, right)] = 2
                DP[(diff, right)] = DP.get((diff,left), 1) + 1
        return max(DP.values())

# V2