"""

904. Fruit Into Baskets
Solved
Medium
Topics
premium lock icon
Companies
You are visiting a farm that has a single row of fruit trees arranged from left to right. The trees are represented by an integer array fruits where fruits[i] is the type of fruit the ith tree produces.

You want to collect as much fruit as possible. However, the owner has some strict rules that you must follow:

You only have two baskets, and each basket can only hold a single type of fruit. There is no limit on the amount of fruit each basket can hold.
Starting from any tree of your choice, you must pick exactly one fruit from every tree (including the start tree) while moving to the right. The picked fruits must fit in one of your baskets.
Once you reach a tree with fruit that cannot fit in your baskets, you must stop.
Given the integer array fruits, return the maximum number of fruits you can pick.

 

Example 1:

Input: fruits = [1,2,1]
Output: 3
Explanation: We can pick from all 3 trees.
Example 2:

Input: fruits = [0,1,2,2]
Output: 3
Explanation: We can pick from trees [1,2,2].
If we had started at the first tree, we would only pick from trees [0,1].
Example 3:

Input: fruits = [1,2,3,2,2]
Output: 4
Explanation: We can pick from trees [2,3,2,2].
If we had started at the first tree, we would only pick from trees [1,2].
 

Constraints:

1 <= fruits.length <= 105
0 <= fruits[i] < fruits.length
 


"""

# V0
class Solution(object):
    def totalFruit(self, fruits):
        """
        :type fruits: List[int]
        :rtype: int
        """
        pass


# V0-1
# IDEA: SLIDE WINDOW (gemini)
class Solution(object):
    def totalFruit(self, fruits):
        """
        :type fruits: List[int]
        :rtype: int
        """
        if not fruits:
            return 0
            
        # Map to track the frequency of fruit types in the active window
        basket = {}
        left = 0
        max_fruit = 0
        
        # Expand the window using the right pointer
        for right in range(len(fruits)):
            current_fruit = fruits[right]
            basket[current_fruit] = basket.get(current_fruit, 0) + 1
            
            # If we have collected more than 2 distinct types, shrink the window from the left
            while len(basket) > 2:
                left_fruit = fruits[left]
                basket[left_fruit] -= 1
                
                # If a fruit type's count hits 0, remove it completely from the basket
                if basket[left_fruit] == 0:
                    del basket[left_fruit]
                    
                left += 1  # Move left pointer forward
                
            # The length of the valid continuous window is (right - left + 1)
            max_fruit = max(max_fruit, right - left + 1)
            
        return max_fruit


# V0-2
# IDEA: SLIDE WINDOW (gpt)
class Solution(object):
    def totalFruit(self, fruits):
        if not fruits:
            return 0

        left = 0
        count = {}
        max_fruit = 0

        for right in range(len(fruits)):
            fruit = fruits[right]
            count[fruit] = count.get(fruit, 0) + 1

            # Shrink the window until there are at most 2 fruit types
            while len(count) > 2:
                left_fruit = fruits[left]
                count[left_fruit] -= 1

                if count[left_fruit] == 0:
                    del count[left_fruit]

                left += 1

            max_fruit = max(max_fruit, right - left + 1)

        return max_fruit


# V1


# V2 
# https://blog.csdn.net/XX_123_1_RJ/article/details/82828570
class Solution:
    def totalFruit(self, tree):
        cnt = {}
        i = res = 0
        for j, v in enumerate(tree):
            cnt[v] = cnt.get(v, 0) + 1  # get number from a dict, set as 0 if the number is not exist 
            while len(cnt) > 2:  # if amount of the elements in dict > 2 
                cnt[tree[i]] -= 1
                if cnt[tree[i]] == 0:
                    del cnt[tree[i]]
                i += 1
            res = max(res, j - i + 1)  # end one loop, update the outcome 
        return res

# if __name__ == '__main__':
#     solu = Solution()
#     tree = [3, 3, 3, 1, 2, 1, 1, 2, 3, 3, 4]
#     print(solu.totalFruit(tree))




# V3 
# Time:  O(n)
# Space: O(1)

import collections


class Solution(object):
    def totalFruit(self, tree):
        """
        :type tree: List[int]
        :rtype: int
        """
        count = collections.defaultdict(int)
        result, i = 0, 0
        for j, v in enumerate(tree):
            count[v] += 1
            while len(count) > 2:
                count[tree[i]] -= 1
                if count[tree[i]] == 0:
                    del count[tree[i]]
                i += 1
            result = max(result, j-i+1)
        return result
