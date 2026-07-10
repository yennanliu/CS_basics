"""

524. Longest Word in Dictionary through Deleting
Solved
Medium
Topics
premium lock icon
Companies
Given a string s and a string array dictionary, return the longest string in the dictionary that can be formed by deleting some of the given string characters. If there is more than one possible result, return the longest word with the smallest lexicographical order. If there is no possible result, return the empty string.

 

Example 1:

Input: s = "abpcplea", dictionary = ["ale","apple","monkey","plea"]
Output: "apple"
Example 2:

Input: s = "abpcplea", dictionary = ["a","b","c"]
Output: "a"
 

Constraints:

1 <= s.length <= 1000
1 <= dictionary.length <= 1000
1 <= dictionary[i].length <= 1000
s and dictionary[i] consist of lowercase English letters.
 

"""

# V0
# IDEA: 2 POINTERS +  string comparision (`word < res`)
# time = O(d * (n + l)), d = len(dictionary), n = len(s), l = avg word length
# space = O(1)
class Solution(object):
    def findLongestWord(self, s, dictionary):
        res = ""

        len_s = len(s)

        for d in dictionary:
            idx_s = 0
            idx_d = 0

            len_d = len(d)

            # NOTE !!! 
            # while `idx_s` and `idx_d` are BOTH in range
            while idx_s < len_s and idx_d < len_d:
                if s[idx_s] == d[idx_d]:
                    idx_d += 1
                idx_s += 1

            """
            NOTE !!!!


            simply check `idx_d == len_d`
                -> then we know if cur `d` can be formed by s via deletion 


            # If idx_d reached len_d, we found all characters of 'd' in 's'
            """
            if idx_d == len_d:
                if len(d) > len(res) or (len(d) == len(res) and d < res):
                    res = d

        return res


# V0
# IDEA: 2 POINTERS +  string comparision (`word < res`) (gpt)
# time = O(d * (n + l)), d = len(dictionary), n = len(s), l = avg word length
# space = O(1)
class Solution(object):
    def findLongestWord(self, s, dictionary):
        res = ""

        for word in dictionary:
            i = 0
            j = 0

            while i < len(s) and j < len(word):
                if s[i] == word[j]:
                    j += 1
                i += 1

            # Whole word matched
            if j == len(word):
                if len(word) > len(res):
                    res = word
                elif len(word) == len(res) and word < res:
                    res = word

        return ress


# V0-1
# IDEA: SORT + 2 POINTERS +  string comparision (`word < res`) (gemini)
# time = O(d*log(d) + d * (n + l)), d = len(dictionary), n = len(s), l = avg word length
# space = O(log d) (sort), O(1) extra
class Solution(object):
    def findLongestWord(self, s, dictionary):
        """
        :type s: str
        :type dictionary: List[str]
        :rtype: str
        """
        # Step 1: Sort the dictionary.
        # Primary key: -len(x) sorts by length in descending order (longest first)
        # Secondary key: x sorts alphabetically in ascending order (a-z)
        dictionary.sort(key=lambda x: (-len(x), x))
        
        # Step 2: Test each word in order. The first one that fits is our winner!
        for word in dictionary:
            i = 0  # Pointer for string s
            j = 0  # Pointer for current word
            
            # Scan through s to see if 'word' is a valid subsequence
            while i < len(s) and j < len(word):
                if s[i] == word[j]:
                    j += 1
                i += 1
                
            # If j reached the end of the word, it means the whole word was found in s
            if j == len(word):
                return word
                
        return ""



# V1
# http://bookshadow.com/weblog/2017/02/26/leetcode-longest-word-in-dictionary-through-deleting/
# IDEA : GREEDY + BFS
# time = O(n + total_len_of_d), n = len(s)
# space = O(total_len_of_d)
class Solution(object):
    def findLongestWord(self, s, d):
        """
        :type s: str
        :type d: List[str]
        :rtype: str
        """
        ans = []
        dmap = collections.defaultdict(list)
        for w in d:
            dmap[w[0]].append((0, w))
        for c in s:
            wlist = dmap[c]
            del dmap[c]
            for i, w in wlist:
                if i + 1 == len(w):
                    ans.append(w)
                else:
                    dmap[w[i + 1]].append((i + 1, w))
        if not ans: return ''
        maxl = len(max(ans, key = len))
        return min(w for w in ans if len(w) == maxl)


# V2
# time = O(d*log(d) + d * n), d = len(d), n = len(s)
# space = O(1)
class Solution(object):
    def findLongestWord(self, s, d):
        """
        :type s: str
        :type d: List[str]
        :rtype: str
        """
        d.sort(key = lambda x: (-len(x), x))
        for word in d:
            i = 0
            for c in s:
                if i < len(word) and word[i] == c:
                    i += 1
            if i == len(word):
                return word
        return ""
