"""

1889. Minimum Space Wasted From Packaging
Hard

You have n packages that you are trying to place in boxes, one package in each box. There are m suppliers that each produce boxes of different sizes (with infinite supply). A package can be placed in a box if the size of the package is less than or equal to the size of the box.

The package sizes are given as an integer array packages, where packages[i] is the size of the ith package. The suppliers are given as a 2D integer array boxes, where boxes[j] is an array of box sizes that the jth supplier produces.

You want to choose a single supplier and use boxes from them such that the total wasted space is minimized. For each package in a box, we define the space wasted to be size of the box - size of the package. The total wasted space is the sum of the space wasted in all the boxes.

For example, if you have to fit packages with sizes [2,3,5] and the supplier offers boxes of sizes [4,8], you can fit the packages of size-2 and size-3 into two boxes of size-4 and the package with size-5 into a box of size-8. This would result in a waste of (4-2) + (4-3) + (8-5) = 6.
Return the minimum total wasted space by choosing the box supplier optimally, or -1 if it is impossible to fit all the packages inside boxes. Since the answer may be large, return it modulo 109 + 7.

 

Example 1:

Input: packages = [2,3,5], boxes = [[4,8],[2,8]]
Output: 6
Explanation: It is optimal to choose the first supplier, using two size-4 boxes and one size-8 box.
The total waste is (4-2) + (4-3) + (8-5) = 6.
Example 2:

Input: packages = [2,3,5], boxes = [[1,4],[2,3],[3,4]]
Output: -1
Explanation: There is no box that the package of size 5 can fit in.
Example 3:

Input: packages = [3,5,8,10,11,12], boxes = [[12],[11,9],[10,5,14]]
Output: 9
Explanation: It is optimal to choose the third supplier, using two size-5 boxes, two size-10 boxes, and two size-14 boxes.
The total waste is (5-3) + (5-5) + (10-8) + (10-10) + (14-11) + (14-12) = 9.
 

Constraints:

n == packages.length
m == boxes.length
1 <= n <= 105
1 <= m <= 105
1 <= packages[i] <= 105
1 <= boxes[j].length <= 105
1 <= boxes[j][k] <= 105
sum(boxes[j].length) <= 105
The elements in boxes[j] are distinct.

"""

# V0

# V1
# https://leetcode-cn.com/problems/minimum-space-wasted-from-packaging/
# https://www.codeleading.com/article/30365710107/
# https://blog.csdn.net/zml66666/article/details/117854566
# https://icode.best/i/49662541727831

# V1
# IDEA : BINARY SEARCH
# https://leetcode.com/problems/minimum-space-wasted-from-packaging/discuss/1254116/JavaC%2B%2BPython-Binary-Search-should-not-use-prefix-sum
# IDEA :
# Sort packages A,
# Sort boxes[i],
# for each box in box[i] from small to big,
# find out how many packages in A it can covers,
# using binary search.
#
# Assume we alreay cover i packages,
# now we find that we can cover j packages,
# the space we are using (j - i) * box size.
# We accumulate the space size.
#
# No need for prefix sum at all,
# this takes extra unnecessay space.
# The final result is total space - package size sum,
# we just need to calculate the box size sum once,
# and that's enough.
#
# Complexity
# Time O(sort(A) + sort(B) + BlogA)
# Space O(sort)
class Solution:
    def minWastedSpace(self, A, boxes):
            A.sort()
            res = float('inf')
            for B in boxes:
                B.sort()
                if B[-1] < A[-1]: continue
                cur = i = 0
                for b in B:
                    # both of below are OK
                    #j = bisect.bisect(A, b, i)
                    # https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/python_trick.md#1-27-bisect--bisect_right-bisect_left-array-bisection-algorithm
                    j = bisect.bisect_right(A, b, i)
                    cur += b * (j - i)
                    i = j
                res = min(res, cur)
            return (res - sum(A)) % (10**9 + 7) if res < float('inf') else -1


# V1
# IDEA : Binary Search without PrefixSum
# https://leetcode.com/problems/minimum-space-wasted-from-packaging/discuss/1254238/C%2B%2BJavaPython-Binary-Search-without-PrefixSum-Clean-and-Concise
class Solution:
    def minWastedSpace(self, packages: List[int], boxes: List[List[int]]) -> int:
        packages.sort()
        n = len(packages)
        minTotalBoxSize = math.inf

        for boxSizes in boxes:
            boxSizes.sort()
            if boxSizes[-1] < packages[-1]: continue  # if the largest box can't pack the largest package -> skip
            totalBoxSize = 0  # Total box size to pack all n packages
            startIdx = 0
            for boxSize in boxSizes:
                idx = bisect_right(packages, boxSize, startIdx) - 1  # find the largest index of the package which is less or equal to boxSize
                packedCount = (idx - startIdx + 1)  # number of remain packages that this box can be packed.
                totalBoxSize += boxSize * packedCount
                startIdx = idx + 1
            minTotalBoxSize = min(minTotalBoxSize, totalBoxSize)

        if minTotalBoxSize == math.inf:  # can't packed all n packages
            return -1
        return (minTotalBoxSize - sum(packages)) % 1_000_000_007  # minTotalWasted = minTotalBoxSize - totalPackageSize

# V1
# IDEA :  Binary Search & Prefix Sum
# https://leetcode.com/problems/minimum-space-wasted-from-packaging/discuss/1332294/Python-Binary-Search-and-Prefix-Sum
class Solution:
    def minWastedSpace(self, packages: List[int], boxes: List[List[int]]) -> int:
        mod=10**9+7
        packages.sort()
        n=len(packages)
        preSum=list(accumulate(packages))
        Min_waste=sys.maxsize
        for L in boxes:
            L.sort()
            waste=0
            preIndex=-1
            for i in L:
                index=bisect_right(packages,i)
                if index==0:
                    continue
                waste+=((index-preIndex)*i)-(preSum[index-1]-preSum[preIndex-1]) if preIndex!=-1 else (index)*i-preSum[index-1]
                preIndex=index
                if index==n:
                    break
            if index==n:
                Min_waste=min(Min_waste,waste) 
        return Min_waste%mod if Min_waste!=sys.maxsize else -1

# V1
# IDEA : HEAP
# https://leetcode.com/problems/minimum-space-wasted-from-packaging/discuss/1253912/Python3.-Sort-packages-%2B-heapify-all-boxes
class Solution:
    # idea:
    #   1. sort packages, so we can process them from smaller to bigger size
    #   2. push boxes to heap (min priority queue), so that when we process all packages
    #       with smaller size than the heap top, we will be able to compute waste space
    #   3. heap element will have form of (size, i, j) that's boxes[i][j] = size
    #   example:
    #       heaptop is (5, i, j)
    #       we processed 3 packages with sizes 2, 3 and 5, the fourth package has size of 6
    #       package_count = 3 (number of packages)
    #       total_box_size = 3 (package_count) * 5 (box_size) = 15
    #       so write: cache[i] = (3, 15) as (package_count, total_box_size)
    #
    #       when we update: cache[i] = [package_count, cache[i][1] + box_size * (package_count - cache[i][0])]
    def minWastedSpace(self, packages: List[int], boxes: List[List[int]]) -> int:
        packages.sort()
        h = []
        for i in range(len(boxes)):
            for box in boxes[i]:
                heappush(h, (box, i))

        package_count = 0
        cache = [[0, 0] for _ in boxes]
        while h:
            while package_count < len(packages) and h[0][0] >= packages[package_count]:
                package_count += 1
            
            size, box_index = heappop(h)
            cache[box_index][1] += size * (package_count - cache[box_index][0])
            cache[box_index][0] = package_count

        if package_count == len(packages):
            total_package_size = sum(packages)
            return min(total_box_size - total_package_size for box_count, total_box_size in cache if box_count == package_count) % (10 ** 9 + 7)
        else:
            return -1

# V1
# IDEA : BINARY SEARCH
# https://leetcode.com/problems/minimum-space-wasted-from-packaging/discuss/1253949/python-Simple-Binary-Search-updating-explanation
import bisect
def find_waste(boxes, packages, s, n):
    if packages[-1] > boxes[-1]:
        # print(packages, boxes)
        return -1
    pi = 0
    total_waste = -1
    for b in boxes:
        if b < packages[0]:
            continue
        if total_waste == -1:
            total_waste = 0
        if b > packages[-1]:
            ni = n
        else:
            ni = bisect.bisect_right(packages, b, lo=pi) # imp (lo)
        waste = ((ni-pi)*b) - (s[ni]-s[pi])
        #print(f"b={b} pi={pi} ni={ni} waste={waste}")
        total_waste += waste
        pi = ni
    return total_waste


class Solution:
    def minWastedSpace(self, packages: List[int], boxes: List[List[int]]) -> int:
        M = 10**9 + 7
        ret = float('inf')
        packages.sort()
        n = len(packages)
        for box in boxes:
            box.sort()
        cum = [0]
        s = 0 
        for x in packages:
            s += x
            cum.append(s)
        for box in boxes:
            waste = find_waste(box, packages, cum, n)
            if waste == -1:
                continue
            ret = min(ret, waste) 
        if ret == float('inf'):
            return -1
        return ret % M

# V1
# IDEA : Solution: Greedy + Binary Search
# https://zxi.mytechroad.com/blog/algorithms/binary-search/leetcode-1889-minimum-space-wasted-from-packaging/
# C++
# IDEA :
# sort packages and boxes
# for each box find all (unpacked) packages that are smaller or equal to itself.
# Time complexity: O(nlogn) + O(mlogm) + O(mlogn)
# Space complexity: O(1)
# class Solution {
# public:
#   int minWastedSpace(vector<int>& packages, vector<vector<int>>& boxes) {
#     constexpr int kMod = 1e9 + 7;
#     const int n = packages.size();        
#     sort(begin(packages), end(packages));    
#     const auto bit = begin(packages);
#     const auto eit = end(packages);
#     long sum = accumulate(bit, eit, 0L);
#     long ans = LLONG_MAX;
#     for (auto& box : boxes) {
#       sort(begin(box), end(box));
#       int l = 0;
#       long cur = 0;
#       for (long b : box) {
#         int r = upper_bound(bit + l, eit, b) - bit;
#         cur += b * (r - l);
#         if (r == n) {
#           ans = min(ans, cur - sum);
#           break;
#         }
#         l = r;
#       }      
#     }
#     return ans == LLONG_MAX ? -1 : ans % kMod;
#   }
# };

# V2