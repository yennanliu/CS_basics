# Time:  O(m + n), m is the size of banned, n is the size of paragraph
# Space: O(m + n)
# Given a paragraph and a list of banned words,
# return the most frequent word that is not in the list of banned words.
# It is guaranteed there is at least one word that isn't banned, and that the answer is unique.
#
# Words in the list of banned words are given in lowercase, and free of
# punctuation.
# Words in the paragraph are not case sensitive.
# The answer is in lowercase.
#
# Example:
# Input:
# paragraph = "Bob hit a ball, the hit BALL flew far after it was hit."
# banned = ["hit"]
# Output: "ball"
# Explanation:
# "hit" occurs 3 times, but it is a banned word.
# "ball" occurs twice (and no other word does), so it is the most frequent
# non-banned word in the paragraph.
# Note that words in the paragraph are not case sensitive,
# that punctuation is ignored (even if adjacent to words, such as "ball,"),
# and that "hit" isn't the answer even though it occurs more because it is
# banned.
#
# Note:
# - 1 <= paragraph.length <= 1000.
# - 1 <= banned.length <= 100.
# - 1 <= banned[i].length <= 10.
# - The answer is unique, and written in lowercase
#   (even if its occurrences in paragraph may have uppercase symbols,
#   and even if it is a proper noun.)
# - paragraph only consists of letters, spaces,
#   or the punctuation symbols !?',;.
# - Different words in paragraph are always separated by a space.
# - There are no hyphens or hyphenated words.
# - Words only consist of letters, never apostrophes or
#   other punctuation symbols.

# V0 
# IDEA : REGULAR EXPRESSION + COLLECTION
import collections
class Solution:
    def mostCommonWord(self, paragraph, banned):
        p = re.compile(r"[!?',;.]")
        sub_para = p.sub('', paragraph.lower())
        words = sub_para.split(' ')
        words = [word for word in words if word not in banned]
        count = collections.Counter(words)
        return count.most_common(1)[0][0]

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/80472079
import collections
class Solution:
    def mostCommonWord(self, paragraph, banned):
        """
        :type paragraph: str
        :type banned: List[str]
        :rtype: str
        """
        p = re.compile(r"[!?',;.]")
        sub_para = p.sub('', paragraph.lower())
        words = sub_para.split(' ')
        words = [word for word in words if word not in banned]
        count = collections.Counter(words)
        return count.most_common(1)[0][0]

### Test case
s=Solution()
paragraph = "Bob hit a ball, the hit BALL flew far after it was hit."
banned = ["hit"]
assert s.mostCommonWord(paragraph, banned) == "ball"
paragraph = "123"
banned = [""]
assert s.mostCommonWord(paragraph, banned) == "123"
paragraph = "aaa"
banned = ["a"]
assert s.mostCommonWord(paragraph, banned) == "aaa"
paragraph = "Words in the list of banned words are given in the lowercase, and free o"
banned = ["words"]
assert s.mostCommonWord(paragraph, banned) == "in"

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/80472079
# http://bookshadow.com/weblog/2018/04/15/leetcode-most-common-word/
class Solution(object):
    def mostCommonWord(self, paragraph, banned):
        """
        :type paragraph: str
        :type banned: List[str]
        :rtype: str
        """
        paragraph = re.findall(r"\w+", paragraph.lower())
        count = collections.Counter(x for x in paragraph if x not in banned)
        return count.most_common(1)[0][0]

# V1'
# http://bookshadow.com/weblog/2018/04/15/leetcode-most-common-word/
class Solution(object):
    def mostCommonWord(self, paragraph, banned):
        """
        :type paragraph: str
        :type banned: List[str]
        :rtype: str
        """
        tokens = re.sub('[\!\?\'\,;\.]', '', paragraph.lower()).split()
        cnt = collections.Counter(tokens)
        for ban in banned:
            if ban in cnt:
                del cnt[ban]
        return cnt.most_common(1)[0][0]
            
# V2 
import collections
class Solution(object):
    def mostCommonWord(self, paragraph, banned):
        """
        :type paragraph: str
        :type banned: List[str]
        :rtype: str
        """
        lookup = set(banned)
        counts = collections.Counter(
                    word.strip("!?',;.")
                    for word in paragraph.lower().split()
                    )
        result = ''
        for word in counts:
            if (not result or counts[word] > counts[result]) and \
               word not in lookup:
                result = word
        return result
