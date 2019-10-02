# Time:  O((m + n) * l), m is the size of list1, n is the size of list2
# Space: O(m * l), l is the average length of string
# Suppose Andy and Doris want to choose a restaurant for dinner,
# and they both have a list of favorite restaurants represented by strings.
#
# You need to help them find out their common interest with the least list index sum.
# If there is a choice tie between answers, output all of them with no order requirement.
# You could assume there always exists an answer.
#
# Example 1:
# Input:
# ["Shogun", "Tapioca Express", "Burger King", "KFC"]
# ["Piatti", "The Grill at Torrey Pines", "Hungry Hunter Steakhouse", "Shogun"]
# Output: ["Shogun"]
# Explanation: The only restaurant they both like is "Shogun".
# Example 2:
# Input:
# ["Shogun", "Tapioca Express", "Burger King", "KFC"]
# ["KFC", "Shogun", "Burger King"]
# Output: ["Shogun"]
# Explanation: The restaurant they both like and have the least index sum is "Shogun" with index sum 1 (0+1).
# Note:
# The length of both lists will be in the range of [1, 1000].
# The length of strings in both lists will be in the range of [1, 30].
# The index is starting from 0 to the list length minus 1.
# No duplicates in both lists.

# V0 
class Solution:
    """
    @param list1: a list of strings
    @param list2: a list of strings
    @return: the common interest with the least list index sum
    """

    def findRestaurant(self, list1, list2):
        # Write your code here
        ans = len(list1) + len(list2)
        s = []
        for andy in list1:
            if andy in list2:
                idx1 = list1.index(andy)
                idx2 = list2.index(andy)
                if idx1 + idx2 < ans:
                    ans = idx1 + idx2
                    s = []
                    s.append(andy)
                elif idx1 + idx2 == ans:
                    s.append(andy)
        return s

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79138621
class Solution(object):
    def findRestaurant(self, list1, list2):
        """
        :type list1: List[str]
        :type list2: List[str]
        :rtype: List[str]
        """
        commons = [word for word in list1 if word in list2]
        answer = []
        smallest = 1000000
        for common in commons:
            index1 = list1.index(common)
            index2 = list2.index(common)
            index = index1 + index2
            if smallest > index:
                smallest = index
                answer = [common]
            elif smallest == index:
                answer.append(common)
        return answer

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79138621
class Solution(object):
    def findRestaurant(self, list1, list2):
        """
        :type list1: List[str]
        :type list2: List[str]
        :rtype: List[str]
        """
        dic1 = {word:ind for ind,word in enumerate(list1)}
        dic2 = {word:ind for ind,word in enumerate(list2)}
        answer = []
        smallest = 1000000
        for word in dic1:
            if word in dic2:
                _sum = dic1[word] + dic2[word]
                if smallest > _sum:
                    smallest = _sum
                    answer = [word]
                elif smallest == _sum:
                    answer.append(word)
        return answer

# V1''
# http://bookshadow.com/weblog/2017/05/28/leetcode-minimum-index-sum-of-two-lists/
class Solution(object):
    def findRestaurant(self, list1, list2):
        """
        :type list1: List[str]
        :type list2: List[str]
        :rtype: List[str]
        """
        dict1 = {v : i for i, v in enumerate(list1)}
        minSum = len(list1) + len(list2)
        ans = []
        for i, r in enumerate(list2):
            if r not in dict1:
                continue
            currSum = i + dict1[r]
            if currSum < minSum:
                ans = [r]
                minSum = currSum
            elif currSum == minSum:
                ans.append(r)
        return ans

# V1'''
# https://www.jiuzhang.com/solution/minimum-index-sum-of-two-lists/#tag-highlight-lang-python
class Solution:
    """
    @param list1: a list of strings
    @param list2: a list of strings
    @return: the common interest with the least list index sum
    """

    def findRestaurant(self, list1, list2):
        # Write your code here
        ans = len(list1) + len(list2)
        s = []
        for andy in list1:
            if andy in list2:
                idx1 = list1.index(andy)
                idx2 = list2.index(andy)
                if idx1 + idx2 < ans:
                    ans = idx1 + idx2
                    s = []
                    s.append(andy)
                elif idx1 + idx2 == ans:
                    s.append(andy)
        return s

# V2 
# Time:  O((m + n) * l), m is the size of list1, n is the size of list2
# Space: O(m * l), l is the average length of string
class Solution(object):
    def findRestaurant(self, list1, list2):
        """
        :type list1: List[str]
        :type list2: List[str]
        :rtype: List[str]
        """
        lookup = {}
        for i, s in enumerate(list1):
            lookup[s] = i

        result = []
        min_sum = float("inf")
        for j, s in enumerate(list2):
            if j > min_sum:
                break
            if s in lookup:
                if j + lookup[s] < min_sum:
                    result = [s]
                    min_sum = j + lookup[s]
                elif j + lookup[s] == min_sum:
                    result.append(s)
        return result
