# https://leetcode.com/problems/brace-expansion/description/

# https://leetcode.ca/all/1087.html

"""

1087. Brace Expansion
A string S represents a list of words.

Each letter in the word has 1 or more options.  If there is one option, the letter is represented as is.  If there is more than one option, then curly braces delimit the options.  For example, "{a,b,c}" represents options ["a", "b", "c"].

For example, "{a,b,c}d{e,f}" represents the list ["ade", "adf", "bde", "bdf", "cde", "cdf"].

Return all words that can be formed in this manner, in lexicographical order.

 

Example 1:

Input: "{a,b}c{d,e}f"
Output: ["acdf","acef","bcdf","bcef"]
Example 2:

Input: "abcd"
Output: ["abcd"]
 

Note:

1 <= S.length <= 50
There are no nested curly brackets.
All characters inside a pair of consecutive opening and ending curly brackets are different.
Difficulty:
Medium
Lock:
Prime
Company:
Google


"""


# V0
# time = O(1)  # not implemented
# space = O(1)  # not implemented
class Solution:
    def expand(self, s):
        pass


# V1-2
# IDEA: BFS (GPT)
"""
NOTE !!!


for "{a,b}c{d,e}f",

-> we need to collect things a below:

```
["a","b"]
["c"]
["d","e"]
["f"]
```



"""
# time = O(p * n)  # p = total number of expanded strings, n = length of s
# space = O(p * n)
class Solution:
    def expand(self, s):
        # groups will store every "choice group"
        #
        # Example:
        # s = "{a,b}c{d,e}f"
        #
        # groups becomes:
        # [
        #     ["a", "b"],
        #     ["c"],
        #     ["d", "e"],
        #     ["f"]
        # ]
        groups = []

        # i is the current position while scanning the string
        i = 0

        # Parse the input string into groups
        while i < len(s):

            # -------------------------------
            # Case 1: Current character is '{'
            # -------------------------------
            if s[i] == "{":

                # Find the matching '}'
                j = i
                while s[j] != "}":
                    j += 1

                # Example:
                # "{a,b,c}"
                #  ^
                #  i
                #
                # j points at '}'
                #
                # s[i+1:j] = "a,b,c"

                # Split by comma
                #
                # "a,b,c".split(",")
                # -> ["a","b","c"]
                #
                # sort() is required because
                # LeetCode wants lexicographical order
                groups.append(sorted(s[i + 1:j].split(",")))

                # Continue scanning after '}'
                i = j + 1

            # -------------------------------
            # Case 2: Normal character
            # -------------------------------
            else:
                # A normal character is also treated
                # as a choice group with only one choice.
                #
                # Example:
                # c
                #
                # becomes
                #
                # ["c"]
                groups.append([s[i]])

                i += 1

        ###################################################
        # BFS / Build all strings
        ###################################################

        # res stores all partial answers
        #
        # Initially we start with an empty string.
        #
        # res = [""]
        res = [""]

        # Process every group one by one
        #
        # Example:
        #
        # groups =
        # [
        #   ["a","b"],
        #   ["c"],
        #   ["d","e"],
        #   ["f"]
        # ]
        for group in groups:

            # next level strings
            nxt = []

            # Try every current partial string
            for prefix in res:

                # Append every possible choice
                for ch in group:

                    # Build a new string
                    #
                    # Example:
                    #
                    # prefix = "ac"
                    # ch = "d"
                    #
                    # => "acd"
                    nxt.append(prefix + ch)

            # Move to next level
            res = nxt

        # Final answer
        return res


# V1-2
# IDEA: BFS (GEMINI)
# time = O(p * n)  # p = total number of expanded strings, n = length of s
# space = O(p * n)
class Solution:
    def expand(self, s: str) -> list[str]:
        if not s:
            return []
            
        # Step 1: Parse the string into structured token blocks
        # Example: "{a,b}c{d,e}f" -> [['a', 'b'], ['c'], ['d', 'e'], ['f']]
        blocks = []
        i = 0
        n = len(s)
        
        while i < n:
            if s[i] == '{':
                # Find the matching closing bracket
                j = i
                while j < n and s[j] != '}':
                    j += 1
                # Extract options inside brackets, strip commas, and sort them lexicographically
                options = sorted(s[i+1:j].split(','))
                blocks.append(options)
                i = j + 1
            else:
                # Plain single character block
                blocks.append([s[i]])
                i += 1
                
        # Step 2: "BFS" style generation (sequential combination growth)
        # We start with an empty string choice
        q = [""]
        
        for block in blocks:
            next_q = []
            for current_str in q:
                for option in block:
                    next_q.append(current_str + option)
            q = next_q # Update our layer queue to the newly combined strings
            
        return q




# V2
# https://leetcode.ca/2018-11-21-1087-Brace-Expansion/
# time = O(p * n)  # p = total number of expanded strings, n = length of s
# space = O(p * n)
class Solution:
    def expand(self, s: str) -> List[str]:
        def convert(s):
            if not s:
                return
            if s[0] == '{':
                j = s.find('}')
                items.append(s[1:j].split(','))
                convert(s[j + 1 :])
            else:
                j = s.find('{')
                if j != -1:
                    items.append(s[:j].split(','))
                    convert(s[j:])
                else:
                    items.append(s.split(','))

        def dfs(i, t):
            if i == len(items):
                ans.append(''.join(t))
                return
            for c in items[i]:
                t.append(c)
                dfs(i + 1, t)
                t.pop()

        items = []
        convert(s)
        ans = []
        dfs(0, [])
        ans.sort()
        return ans



