"""

1915. Number of Wonderful Substrings
Medium

A wonderful string is a string where at most one letter appears an odd number of times.

For example, "ccjjc" and "abab" are wonderful, but "ab" is not.
Given a string word that consists of the first ten lowercase English letters ('a' through 'j'), return the number of wonderful non-empty substrings in word. If the same substring appears multiple times in word, then count each occurrence separately.

A substring is a contiguous sequence of characters in a string.

 

Example 1:

Input: word = "aba"
Output: 4
Explanation: The four wonderful substrings are underlined below:
- "aba" -> "a"
- "aba" -> "b"
- "aba" -> "a"
- "aba" -> "aba"
Example 2:

Input: word = "aabb"
Output: 9
Explanation: The nine wonderful substrings are underlined below:
- "aabb" -> "a"
- "aabb" -> "aa"
- "aabb" -> "aab"
- "aabb" -> "aabb"
- "aabb" -> "a"
- "aabb" -> "abb"
- "aabb" -> "b"
- "aabb" -> "bb"
- "aabb" -> "b"
Example 3:

Input: word = "he"
Output: 2
Explanation: The two wonderful substrings are underlined below:
- "he" -> "h"
- "he" -> "e"
 

Constraints:

1 <= word.length <= 105
word consists of lowercase English letters from 'a' to 'j'.

"""

# V0

# V1
# IDEA : bit op
# https://leetcode.com/problems/number-of-wonderful-substrings/discuss/1299552/JavaC%2B%2BPython-Bit-Mask-%2B-Prefix
# IDEA :
# Use a mask to count the current prefix string.
# mask & 1 means whether it has odd 'a'
# mask & 2 means whether it has odd 'b'
# mask & 4 means whether it has odd 'c'
# ...
# We find the number of wonderful string with all even number of characters.
# Then we flip each of bits, 10 at most, and doing this again.
# This will help to find string with at most one odd number of characters.
class Solution:
      def wonderfulSubstrings(self, word):
            count = [1] + [0] * 1024
            res = cur = 0
            for c in word:
                cur ^= 1 << (ord(c) - ord('a'))
                res += count[cur]
                res += sum(count[cur ^ (1 << i)] for i in range(10))
                count[cur] += 1
            return res
# V1'
# https://leetcode-cn.com/problems/number-of-wonderful-substrings/solution/zui-mei-zi-zi-fu-chuan-de-shu-mu-by-leet-2j7g/
class Solution:
    def wonderfulSubstrings(self, word: str) -> int:
        freq = Counter([0])
        mask, ans = 0, 0
        
        for ch in word:
            idx = ord(ch) - ord("a")
            mask ^= (1 << idx)
            if mask in freq:
                ans += freq[mask]
            for i in range(10):
                mask_pre = mask ^ (1 << i)
                if (mask_pre := mask ^ (1 << i)) in freq:
                    ans += freq[mask_pre]
            freq[mask] += 1
        
        return ans

# V1''
# IDEA : bit op (bit mask)
# https://leetcode.com/problems/number-of-wonderful-substrings/discuss/1301408/Bitmask-with-pictures-or-Python-or-O(N)
class Solution:
    def wonderfulSubstrings(self, word: str) -> int:
        # count is a bitmask where each bit represent the count of a character from a-j % 2
        # bitmask is 10 bits, each bit 2 values, 2^10 = 1024
        count = [0] * 1024
        # 0 means empty string, which has all its bits 0
        count[0] = 1
        ans = 0
        cur = 0
        for char in word:
            # bitmask of the current string ending in char
            cur ^= 1 << (ord(char) - ord('a'))
            # add the all even case to result
            ans += count[cur]
            # flip each bit and see if there's matching prefix
            # this adds the 'at most one' odd case.
            for i in range(10):
                new_bitmask = cur ^ (1 << i)
                if count[new_bitmask] > 0:
                    ans += count[new_bitmask]
            count[cur] += 1
        return ans

# V1'''
# https://leetcode.com/problems/number-of-wonderful-substrings/discuss/1301195/python-easiest-soln-bit-masking
class Solution:
    def wonderfulSubstrings(self, word: str) -> int:
        dp=defaultdict(int)
        ans=2**(26)-1
        kk=ans
        dp[ans]=1
        n=len(word)
        res=0
        l=[2**i for i in range(26)]
        for i in range(n):
            ans^=2**(ord(word[i])-97)
            if (dp[ans]!=0):
                res+=dp[ans]
            ma=0
            for j in l:
                ff=ans
                ff^=j
                ma+=dp[ff]
            res+=ma
            dp[ans]+=1
        return res

# V1''''
# https://leetcode.com/problems/number-of-wonderful-substrings/discuss/1300877/Python-O(n)-solution-with-comments-Prefix-sum-bit-mask
# IDEA :
# This was similar to solving the problem of finding the subarray of sum k. This can be solved using prefix sum concept . For example given an array a= [ 2, 3 ,4 ,5 ,10] and k=9, we can calculate running sum and store Its prefix sum array as dp=[2,5,9,14,24]. Here for a running sum "rs =14 " which occured at index j=3, we can check if "rs - k=14-9=5" is available in dp array. let's say "rs - k=5" was available at index i=1 (i<j) then subarray a[i+1 : j ]=a[2:3] will sum upto k=9.
# Now coming to this problem, let's say we want a subarray which gives 1000000000 and we have a "curr" value( similar to running sum mentioned above ) so we should check if dp[ curr - 1000000000] . But since these are binary numbers dp[ curr - 1000000000] is equivalent to dp[ curr ^ 1000000000] --> dp[ curr^(1<<pos)] here pos=10.
# The reason for checking curr^1000000000 was curr ^ (curr^1000000000) = 1000000000.
# Similarily we want to check all other possibilities like 0100000000, 0010000000,... etc. All these cases corresponds to having one character with odd count and others with even count
class Solution:
    def wonderfulSubstrings(self, word: str) -> int:
        # we are representing each char with a bit, 0 for count being even and 1 for odd
        # 10 char from a to j
        # array to store 2^10 numbers
        dp=[0]*1024
        
        # jihgfedcba -> 0000000000
        curr=0  # 000..(0-> 10 times) 
        
        # since we are starting with curr as 0 make dp[0]=1
        dp[0]=1
        
        # result
        res=0
        
        for c in word:
            # 1<<i sets i th bit to 1 and else to 0
            # xor will toggle the bit
            curr^= (1<<(ord(c)-ord('a')))
            
            # if curr occurred earlier at j and now at i then [j+1: i] has all zeroes
            # this was to count all zeroes case
            res+=dp[curr]
            
            # now to check if these 100000..,010000..,001.. cases  can be acheived using brute force
            # we want to see if curr ^ delta = 10000.. or 010000.. etc
            # curr^delta =1000... then
            # curr ^ 1000.. = delta
            
            for i in range(10):
                res+=dp[curr ^(1<<i)]      
               
            dp[curr]+=1              
        return res

# V1'''''
# IDEA : bit mask
# https://leetcode.com/problems/number-of-wonderful-substrings/discuss/1299556/Python3.-One-pass.-O(N).-Bit-masks.
class Solution:
    # idea: we don't need to know exact substring to define if it's "wonderful" or not
    #   but rather if number of occurrences of each letter is odd/even;
    #   so there are only two states for each letter, so we can use binary mask to represent any substring
    #
    #   for example "0101" represents: ("d" even number, "c" - odd, "b" - even, "a" - odd);
	#   
	#  a mask will represent a prefix of word [0..k] where `0 <= k < len(word)`
	#  if we have two masks that represent prefixes [0..k] and [0..p], where `p > k`,
	#  then substring [k+1..p] is "wonderful" if:
	#     1. two masks are equal (all letters in substring [k+1..p] have even frequency)
	#     2. or two masks are 1 bit different (only one letter in substring [k+1..p] has odd frequency)
	#
	#   we can test this with XOR operator:
	#       if (mask_0_k ^ mask_0_p) == mask_with_0_or_1_bit_set => we have a "wonderful" substring
    #
    #   we can iterate through word and:
	#       1. compute a mask based on previous mask (switch bit for corresponding letter)
	#       2. find all masks that would combine with current one a "wonderful" string (equal mask or one bit different mask)
	#       3. check how many of those masks we saw earlier and add the number to result
	#       4. memorize current mask (so it can be used on further iterations)
	#
    #   example: "ccbbc" (3 letter alphabet)
	#                     binary_index:        [111, 110, 101, 100, 011, 010, 001, 000]
    #       "" -      { mask: 000, mask_count: [  0,   0,   0,   0,   0,   0,   0, 0+1], res: 0 }
    #       "c" -     { mask: 100, mask_count: [  0,   0,   0, 0+1,   0,   0,   0,   1], res: 0 + 0 (equal masks) + 1 (1 bit different masks) = 1 }
    #       "cc" -    { mask: 000, mask_count: [  0,   0,   0,   1,   0,   0,   0, 1+1], res: 1 + 1 (equal masks) + 1 (1 bit different masks) = 3 }
    #       "ccb" -   { mask: 010, mask_count: [  0,   0,   0,   1,   0, 0+1,   0,   2], res: 3 + 0 (equal masks) + 2 (1 bit different masks) = 5 }
    #       "ccbb" -  { mask: 000, mask_count: [  0,   0,   0,   1,   0,   1,   0, 2+1], res: 5 + 2 (equal masks) + 2 (1 bit different masks) = 9 }
    #       "ccbbc" - { mask: 100, mask_count: [  0,   0,   0, 1+1,   0,   1,   0,   3], res: 9 + 1 (equal masks) + 3 (1 bit different masks) = 13 }
	#
    
    def wonderfulSubstrings(self, word: str) -> int:
        mask = 0
        mask_count = [0] * 1024
        mask_count[mask] += 1
        res = 0

        for letter in word:
            mask ^= 1 << ord(letter) - ord("a")
            res += mask_count[mask]
            for i in range(10):
                res += mask_count[mask ^ 1 << i]
            mask_count[mask] += 1
        return res

# V1''''''
# IDEA : prefix bit mask
# https://leetcode.com/problems/number-of-wonderful-substrings/discuss/1300891/Python-3-Prefix-bit-mask
class Solution:
    def wonderfulSubstrings(self, word: str) -> int:
        n = len(word)
        mask = 0
        prefix = defaultdict(int)
        prefix[0] += 1
        ans = 0
        for w in word:
            mask ^= 1 << (ord(w) - ord('a'))
            # no difference
            ans += prefix[mask]
            for i in range(10):
                # only differed by one digit 
                tmp = mask ^ (1 << i)
                ans += prefix[tmp]
            prefix[mask] += 1
        return ans

# V1''''''''
# IDEA : prefix sum (TLE)
# https://leetcode.com/problems/number-of-wonderful-substrings/discuss/1412294/Python-or-Prefix-Sum-No-Bit-Vector-or-Slow-but-easy-to-understand
class Solution:
    def wonderfulSubstrings(self, word: str) -> int:
        prefix = collections.defaultdict(int)
        prefix[(0,0,0,0,0,0,0,0,0,0)] = 1
        count = {c: 0 for c in 'abcdefghij'}
        ans = 0
        for letter in word:
            count[letter]+=1
            current = [count[c]%2 for c in 'abcdefghij']
            currentAsTuple = tuple(current)
            for i, subletter in enumerate('abcdefghij'):
                prev = current[i]
                current[i] = (count[subletter]+1)%2
                ans+=prefix[tuple(current)]
                current[i] = prev
            ans+=prefix[currentAsTuple]
            prefix[currentAsTuple]+=1
        return ans

# V2