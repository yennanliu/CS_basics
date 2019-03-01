



# V1 

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


# V2 
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
        for count in reversed(xrange(len(counts)-1)):
            for c in counts[count]:
                result += c * count

        return result
