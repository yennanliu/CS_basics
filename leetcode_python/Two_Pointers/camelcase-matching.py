"""

1023. Camelcase Matching
Medium

Given an array of strings queries and a string pattern, return a boolean array
answer where answer[i] is true if queries[i] matches pattern, and false
otherwise.

A query word queries[i] matches pattern if you can insert lowercase English
letters into the pattern so that it equals the query. You may insert a character
at any position in pattern or you may choose not to insert any characters at all.

Example 1:

Input: queries = ["FooBar","FooBarTest","FootBall","FrameBuffer","ForceFeedBack"], pattern = "FB"
Output: [true,false,true,true,false]
Explanation: "FooBar" can be generated like this "F" + "oo" + "B" + "ar".
"FootBall" can be generated like this "F" + "oot" + "B" + "all".
"FrameBuffer" can be generated like this "F" + "rame" + "B" + "uffer".

Example 2:

Input: queries = ["FooBar","FooBarTest","FootBall","FrameBuffer","ForceFeedBack"], pattern = "FoBa"
Output: [true,false,true,false,false]
Explanation: "FooBar" can be generated like this "Fo" + "o" + "Ba" + "r".
"FootBall" can be generated like this "Fo" + "ot" + "Ba" + "ll".

Example 3:

Input: queries = ["FooBar","FooBarTest","FootBall","FrameBuffer","ForceFeedBack"], pattern = "FoBaT"
Output: [false,true,false,false,false]
Explanation: "FooBarTest" can be generated like this "Fo" + "o" + "Ba" + "r" + "T" + "est".

Constraints:

1 <= pattern.length, queries.length <= 100
1 <= queries[i].length <= 100
queries[i] and pattern consist of English letters.

"""

# V0


# V0-0-1
# IDEA: 2 POINTERS (GPT)
class Solution(object):

    def camelMatch(self, queries, pattern):
        """
        :type queries: List[str]
        :type pattern: str
        :rtype: List[bool]
        """
        res = []

        for q in queries:
            res.append(self._is_match_v2(q, pattern))

        return res

    def _is_match_v2(self, x, p):
        j = 0

        for i in range(len(x)):
            val_x = x[i]

            """
            NOTE !!!

            make sure j is still in boundary
            """
            # Match the next pattern character
            if j < len(p) and val_x == p[j]:
                j += 1

            # Extra uppercase characters are not allowed
            elif val_x.isupper():
                return False

            # Extra lowercase characters can be skipped


        """
        NOTE !!!

        make sure j matched the last char

        ->

        ```
        if j < len(p) and val_x == p[j]:
            j += 1
        ```

        -> 

        so j will be equal to len(p) in the final iteration

        """
        # Every pattern character must be matched
        return j == len(p)


# V0-1
# IDEA: 2 POINTERS (gemini)
class Solution(object):

  def camelMatch(self, queries, pattern):
    """:type queries: List[str]

    :type pattern: str
    :rtype: List[bool]
    """
    res = []
    for q in queries:
      res.append(self._is_match(q, pattern))
    return res

  def _is_match(self, q, pattern):
    i = 0  # query 指標
    j = 0  # pattern 指標

    while i < len(q):
      # 1. 若字元相符，兩者皆前進
      if j < len(pattern) and q[i] == pattern[j]:
        i += 1
        j += 1
      # 2. 若 q[i] 是大寫字母，但無法與 pattern 匹配，代表非法大寫字母
      elif q[i].isupper():
        return False
      # 3. 若 q[i] 是小寫字母，可以安全地略過（視為插入的小寫字元）
      else:
        i += 1

    # 4. 必須確保 pattern 的所有字元都有被匹配到
    return j == len(pattern)



# V0-2
# IDEA: 2 POINTERS (gpt)

class Solution(object):
    def camelMatch(self, queries, pattern):
        """
        :type queries: List[str]
        :type pattern: str
        :rtype: List[bool]
        """
        res = []

        for query in queries:
            i = 0  # Pointer for query
            j = 0  # Pointer for pattern

            matched = True

            for ch in query:
                if j < len(pattern) and ch == pattern[j]:
                    # Match the current pattern character
                    j += 1

                elif ch.isupper():
                    # Extra uppercase character cannot be ignored
                    matched = False
                    break

                # Extra lowercase characters can be ignored

            # All pattern characters must be matched
            res.append(matched and j == len(pattern))

        return res


# V0-3
# IDEA : 2 POINTERS (SUBSEQUENCE CHECK + NO EXTRA UPPERCASE) (claude)
#
#   a query matches iff
#     1) pattern is a SUBSEQUENCE of query, and
#     2) every query char NOT used by the pattern is lowercase
#   (we may only INSERT lowercase letters into the pattern)
#
#   walk the query with i, the pattern with j:
#     - query[i] == pattern[j]  -> consume it (j += 1)
#     - query[i] is lowercase   -> an inserted letter, skip it
#     - query[i] is uppercase   -> an uppercase the pattern never asked for -> False
#
#   greedy matching is safe: taking the FIRST equal char never hurts, since a
#   later copy of it could only be skipped if it were lowercase anyway.
#
#   e.g. pattern = "FB", query = "FootBall"
#        F(match) o o t(skip) B(match) a l(skip) L -> extra uppercase -> False
#
# time = O(Q * L), space = O(1) besides the output
#   (Q = number of queries, L = max query length)
class Solution(object):
    def camelMatch(self, queries, pattern):
        """
        :type queries: List[str]
        :type pattern: str
        :rtype: List[bool]
        """
        answer = []
        for query in queries:
            answer.append(self._matches(query, pattern))
        return answer

    def _matches(self, query, pattern):
        """True if pattern can become query by inserting only lowercase letters."""
        j = 0  # next pattern char still to be matched

        for ch in query:
            # case 1) the char the pattern needs next
            if j < len(pattern) and ch == pattern[j]:
                j += 1
            # case 2) an inserted lowercase letter -> allowed
            elif ch.islower():
                continue
            # case 3) an uppercase letter the pattern does not have -> not allowed
            else:
                return False

        # NOTE !!! j == len(pattern), not len(pattern) - 1: j was incremented
        #          past the last matched char, so a full match leaves it at the end
        return j == len(pattern)


# V1
# IDEA : REGEX — SURROUND EVERY PATTERN CHAR WITH [a-z]*
#
#   "insert any lowercase letters anywhere" is exactly the regex
#       ^[a-z]* p0 [a-z]* p1 [a-z]* ... [a-z]*$
#   so build it once and fullmatch every query against it.
#
#   e.g. pattern = "FoB" -> [a-z]*F[a-z]*o[a-z]*B[a-z]*
#
#   NOTE : short to write, but the interviewer usually wants V0's pointer walk —
#          learn V0 first.
#
# time = O(Q * L), space = O(P) for the compiled pattern
import re


class Solution2(object):
    def camelMatch(self, queries, pattern):
        """
        :type queries: List[str]
        :type pattern: str
        :rtype: List[bool]
        """
        # build "[a-z]*F[a-z]*B[a-z]*" piece by piece
        regex = "[a-z]*"
        for ch in pattern:
            # re.escape is a no-op for letters, but keeps the regex safe
            regex += re.escape(ch) + "[a-z]*"
        matcher = re.compile(regex)

        answer = []
        for query in queries:
            # fullmatch: the whole query must be consumed, not just a prefix
            answer.append(matcher.fullmatch(query) is not None)
        return answer
