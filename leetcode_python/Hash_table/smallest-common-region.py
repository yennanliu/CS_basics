# https://leetcode.ca/all/1257.html

"""

1257. Smallest Common Region
Medium

You are given some lists of regions where the first region of each list
includes all other regions in that list.

Naturally, if a region x contains another region y then x is bigger than y.
Also, by definition, a region x contains itself.

Given two regions: region1 and region2, return the smallest region that
contains both of them.

If you are given regions r1, r2, and r3 such that r1 includes r3, it is
guaranteed there is no r2 such that r2 includes r3.

It is guaranteed the smallest region exists.

Example 1:

Input:
regions = [["Earth","North America","South America"],
["North America","United States","Canada"],
["United States","New York","Boston"],
["Canada","Ontario","Quebec"],
["South America","Brazil"]],
region1 = "Quebec",
region2 = "New York"

Output: "North America"

Example 2:

Input: regions = [["Earth", "North America", "South America"],["North America", "United States", "Canada"],["United States", "New York", "Boston"],["Canada", "Ontario", "Quebec"],["South America", "Brazil"]], region1 = "Canada", region2 = "South America"

Output: "Earth"

Constraints:

2 <= regions.length <= 10^4
2 <= regions[i].length <= 20
1 <= regions[i][j].length, region1.length, region2.length <= 20
region1 != region2
regions[i][j], region1, and region2 consist of English letters.
The input is generated such that there exists a region which contains all the
other regions, either directly or indirectly.

"""

# V0
class Solution(object):
    def findSmallestRegion(self, regions, region1, region2):
        pass


# V0-1
# IDEA : HASH MAP (CHILD -> PARENT) + ANCESTOR SET (LCA)  (LOWEST COMMON ANCESTOR)(GPT)
"""
CORE IDEA:


1. 建立一個 child_to_parent 映射表，記錄每一個子區域的唯一父區域。


2. 從 region1 開始，一路往上追溯所有的祖先，
   並將它們全部存入一個雜湊集合（ancestors）中。

3. 從 region2 開始也一路往上追溯，
   第一個在 ancestors 集合中遇到的區域，
   就是我們要找的「最小共同區域（Lowest Common Ancestor）」！

"""
from collections import defaultdict

class Solution(object):
    def findSmallestRegion(self, regions, region1, region2):
        # Map: {child: parent}
        child_parent = {}

        for info in regions:
            parent = info[0]
            children = info[1:]

            for child in children:
                child_parent[child] = parent

        # Collect all ancestors of region1, including itself
        region1_ancestors = set()
        curr = region1

        """
        NOTE !!!


        從 region1 開始，一路向上收集所有祖先（包含自己）

        """
        while curr:
            region1_ancestors.add(curr)
            curr = child_parent.get(curr)


        """
        NOTE !!!


        從 region2 開始向上追溯，第一個出現在 ancestors 中的節點就是最低共同祖先
        
        """
        # Walk up from region2; the first match is the smallest common region
        curr = region2

        while curr:
            if curr in region1_ancestors:
                return curr
            curr = child_parent.get(curr)

        return None


# V0-2
# 向上追溯法（Path Tracing / Lowest Common Ancestor） (gemini)
class Solution(object):

  def findSmallestRegion(self, regions, region1, region2):
    """:type regions: List[List[str]]

    :type region1: str
    :type region2: str
    :rtype: str
    """
    # 1. 記錄每個子區域對應的唯一父區域
    child_to_parent = {}

    for info in regions:
      parent = info[0]
      children = info[1:]
      for child in children:
        child_to_parent[child] = parent

    # 2. 從 region1 開始，一路向上收集所有祖先（包含自己）
    ancestors = set()
    curr = region1
    while curr:
      ancestors.add(curr)
      curr = child_to_parent.get(curr)

    # 3. 從 region2 開始向上追溯，第一個出現在 ancestors 中的節點就是最低共同祖先
    curr = region2
    while curr:
      if curr in ancestors:
        return curr
      curr = child_to_parent.get(curr)

    return None


# V0-3
# IDEA : HASH MAP (CHILD -> PARENT) + ANCESTOR SET  (LOWEST COMMON ANCESTOR)(claude)
#
#   every region has at most ONE direct parent, so the regions form a tree and
#   "smallest region containing both" is the LCA of region1 and region2.
#
#   1) walk region1 up to the root, recording every ancestor (itself included)
#   2) walk region2 up; the FIRST region already in that set is the answer,
#      because walking up only ever gets bigger -> the first hit is the smallest
#
#   e.g. Quebec -> {Quebec, Canada, North America, Earth}
#        New York -> United States -> North America  (in set) -> "North America"
#
# time = O(n), space = O(n)    (n = total number of region names)
class Solution(object):
    def findSmallestRegion(self, regions, region1, region2):
        """
        :type regions: List[List[str]]
        :type region1: str
        :type region2: str
        :rtype: str
        """
        # child -> its direct parent; the root never appears as a key
        parent = {}
        for region_list in regions:
            big_region = region_list[0]
            for i in range(1, len(region_list)):
                parent[region_list[i]] = big_region

        # every region that contains region1 (a region contains itself)
        ancestors_of_1 = set()
        current = region1
        while current is not None:
            ancestors_of_1.add(current)
            current = parent.get(current)

        # NOTE !!! the first ancestor of region2 found in the set is the
        #          lowest one, since each step up moves to a bigger region
        current = region2
        while current not in ancestors_of_1:
            current = parent[current]

        return current


# V0-4
# IDEA : HASH MAP (CHILD -> PARENT) + TWO POINTERS (SAME AS LC 160 / LC 1650) (claude)
#
#   same tree, but no ancestor set. Move two pointers up; when one passes the
#   root, restart it at the OTHER region. Each pointer then walks
#   (depth1 + depth2) steps in total, so they meet exactly at the LCA.
#
#   e.g. depth(Quebec) = 3, depth(New York) = 3 -> both reach North America
#        on the second step without ever switching.
#
# time = O(n), space = O(n) for the map, O(1) extra for the walk
class Solution2(object):
    def findSmallestRegion(self, regions, region1, region2):
        """
        :type regions: List[List[str]]
        :type region1: str
        :type region2: str
        :rtype: str
        """
        parent = {}
        for region_list in regions:
            big_region = region_list[0]
            for i in range(1, len(region_list)):
                parent[region_list[i]] = big_region

        pointer1 = region1
        pointer2 = region2
        while pointer1 != pointer2:
            # past the root -> continue from the other start, so both pointers
            # cover the same total distance before meeting
            if pointer1 in parent:
                pointer1 = parent[pointer1]
            else:
                pointer1 = region2

            if pointer2 in parent:
                pointer2 = parent[pointer2]
            else:
                pointer2 = region1

        return pointer1


# V1
# https://leetcode.ca/2019-05-10-1257-Smallest-Common-Region
class Solution:
    def findSmallestRegion(
        self, regions: List[List[str]], region1: str, region2: str
    ) -> str:
        m = {}
        for region in regions:
            for r in region[1:]:
                m[r] = region[0]
        s = set()
        while m.get(region1):
            s.add(region1)
            region1 = m[region1]
        while m.get(region2):
            if region2 in s:
                return region2
            region2 = m[region2]
        return region1
