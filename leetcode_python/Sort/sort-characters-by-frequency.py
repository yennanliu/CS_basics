"""

451. Sort Characters By Frequency
Medium

Given a string s, sort it in decreasing order based on the frequency of the characters. The frequency of a character is the number of times it appears in the string.

Return the sorted string. If there are multiple answers, return any of them.

 

Example 1:

Input: s = "tree"
Output: "eert"
Explanation: 'e' appears twice while 'r' and 't' both appear once.
So 'e' must appear before both 'r' and 't'. Therefore "eetr" is also a valid answer.
Example 2:

Input: s = "cccaaa"
Output: "aaaccc"
Explanation: Both 'c' and 'a' appear three times, so both "cccaaa" and "aaaccc" are valid answers.
Note that "cacaca" is incorrect, as the same characters must be together.
Example 3:

Input: s = "Aabb"
Output: "bbAa"
Explanation: "bbaA" is also a valid answer, but "Aabb" is incorrect.
Note that 'A' and 'a' are treated as two different characters.
 

Constraints:

1 <= s.length <= 5 * 105
s consists of uppercase and lowercase English letters and digits.

"""

# V0
from collections import Counter
class Solution(object):
    def frequencySort(self, s):
        cnt = Counter(s)
        print (cnt)
        res = ""
        for k, v in cnt.most_common():
            res += k * v
        #print (res)
        return res

# V0'
import collections
class Solution(object):
    def frequencySort(self, s):
        d = collections.Counter(s)
        res = ""
        for x in d.most_common():
            res += x[0]*x[1]
        return res

# V0'
import collections
class Solution(object):
    def frequencySort(self, s):
        d = collections.Counter(s)
        d_dict = dict(d)
        res = ""
        ### NOTE : we can sort dict (by value) via below trick
        for x in sorted(d_dict, key=lambda k : -d_dict[k]):
            res += x * d_dict[x]
        return res

# V0'
import collections
class Solution(object):
    def frequencySort(self, s):
        count = collections.Counter(s)
        count_dict = dict(count)
        count_tuple_sorted = sorted(count_dict.items(), key=lambda kv : -kv[1])
        res = ''
        for item in count_tuple_sorted:
            res += item[0] * item[1]
        return res

# V0'
# IDEA : collections.Counter(s).most_common
class Solution(object):
    def frequencySort(self, s):
        return ''.join(c * t for c, t in collections.Counter(s).most_common())

# V1 
# IDEA : SORT 
# https://blog.csdn.net/fuxuemingzhu/article/details/79437548
import collections
class Solution(object):
    def frequencySort(self, s):
        """
        :type s: str
        :rtype: str
        """
        count = collections.Counter(s).most_common()
        res = ''
        for c, v in count:
            res += c * v
        return res

### Test case:
s=Solution()
assert s.frequencySort(['a','b','c','c']) == 'ccab'
assert s.frequencySort(['a']) == 'a'
assert s.frequencySort(['a','A','c','c']) == 'ccaA'
assert s.frequencySort(['c','c','c']) == 'ccc'
assert s.frequencySort([]) == ''
assert s.frequencySort(['','','']) == ''

# V1'
# http://bookshadow.com/weblog/2016/11/02/leetcode-sort-characters-by-frequency/
class Solution(object):
    def frequencySort(self, s):
        """
        :type s: str
        :rtype: str
        """
        return ''.join(c * t for c, t in collections.Counter(s).most_common())

# V2
import collections
class Solution(object):
    def frequencySort(self, s):
    	# sort Counter by value 
    	# https://stackoverflow.com/questions/20950650/how-to-sort-counter-by-value-python
        s_freq_dict = collections.Counter(s).most_common()
        output = ''
        for i in range(len(s_freq_dict)):
            output = output + (s_freq_dict[i][0]*s_freq_dict[i][1])
        return output

# V2' 
# Time:  O(n)
# Space: O(n)
import collections
class Solution(object):
    def frequencySort(self, s):
        """
        :type s: str
        :rtype: str
        """
        freq = collections.defaultdict(int)
        for c in s:
            freq[c] += 1

        counts = [""] * (len(s)+1)
        for c in freq:
            counts[freq[c]] += c

        result = ""
        for count in reversed(range(len(counts)-1)):
            for c in counts[count]:
                result += c * count
        return result