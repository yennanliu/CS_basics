# V1  : DEV 



# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/79542812
import collections
class Solution(object):
    def originalDigits(self, s):
        """
        :type s: str
        :rtype: str
        """
        number = ['zero', 'one', 'two', 'three', 'four', 'five', 'six', 'seven', 'eight', 'nine']
        count = collections.Counter(s)
        res = ''
        for i, num in enumerate(number):
            while True:
                word_count = 0
                for c in num:
                    if count[c] > 0:
                        word_count += 1
                if word_count == len(num):
                    res += str(i)
                    count.subtract(collections.Counter(num))
                else:
                    break
        return res


# V3 
# http://bookshadow.com/weblog/2016/10/16/leetcode-reconstruct-original-digits-from-english/
class Solution(object):
    def originalDigits(self, s):
        """
        :type s: str
        :rtype: str
        """
        cnts = collections.Counter(s)
        nums = ['six', 'zero', 'two', 'eight', 'seven', 'four', 'five', 'nine', 'one', 'three']
        numc = [collections.Counter(num) for num in nums]
        digits = [6, 0, 2, 8, 7, 4, 5, 9, 1, 3]
        ans = [0] * 10
        for idx, num in enumerate(nums):
            cntn = numc[idx]
            t = min(cnts[c] / cntn[c] for c in cntn)
            ans[digits[idx]] = t
            for c in cntn:
                cnts[c] -= t * cntn[c]
        return ''.join(str(i) * n for i, n in enumerate(ans))

# V4 
# Time:  O(n)
# Space: O(1)
from collections import Counter
class Solution(object):
    def originalDigits(self, s):
        """
        :type s: str
        :rtype: str
        """
        # The count of each char in each number string.
        cnts = [Counter(_) for _ in ["zero", "one", "two", "three", \
                                     "four", "five", "six", "seven", \
                                     "eight", "nine"]]

        # The order for greedy method.
        order = [0, 2, 4, 6, 8, 1, 3, 5, 7, 9]

        # The unique char in the order.
        unique_chars = ['z', 'o', 'w', 't', 'u', \
                        'f', 'x', 's', 'g', 'n']

        cnt = Counter(list(s))
        res = []
        for i in order:
            while cnt[unique_chars[i]] > 0:
                cnt -= cnts[i]
                res.append(i)
        res.sort()

        return "".join(map(str, res))