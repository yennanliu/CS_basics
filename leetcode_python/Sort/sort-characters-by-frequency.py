# V0
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
