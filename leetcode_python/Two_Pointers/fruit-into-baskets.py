

# V1 : DEV 



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
 