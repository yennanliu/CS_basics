"""

135. Candy
Hard

There are n children standing in a line. Each child is assigned a rating value given in the integer array ratings.

You are giving candies to these children subjected to the following requirements:

Each child must have at least one candy.
Children with a higher rating get more candies than their neighbors.
Return the minimum number of candies you need to have to distribute the candies to the children.

 

Example 1:

Input: ratings = [1,0,2]
Output: 5
Explanation: You can allocate to the first, second and third child with 2, 1, 2 candies respectively.
Example 2:

Input: ratings = [1,2,2]
Output: 4
Explanation: You can allocate to the first, second and third child with 1, 2, 1 candies respectively.
The third child gets 1 candy because it satisfies the above two conditions.
 

Constraints:

n == ratings.length
1 <= n <= 2 * 104
0 <= ratings[i] <= 2 * 104

"""

# V0

# V1
# https://blog.csdn.net/XX_123_1_RJ/article/details/86636718
# https://blog.csdn.net/qq_37821701/article/details/105237305
# https://leetcode-cn.com/problems/candy/solution/135-fen-fa-tang-guo-python-by-fei-ben-de-cai-zhu-u/
class Solution:
    def candy(self, ratings):
        N = len(ratings)
        candy = [1] * N
        # left -> right scan
        for i in range(N-1):
            if ratings[i] < ratings[i+1]:
                candy[i+1] = candy[i] + 1
        # right -> left scan
        for i in range(N-1, 0, -1):
            if ratings[i] < ratings[i-1] and candy[i] >= candy[i-1]:
                candy[i-1] = candy[i]+1
        return sum(candy)

# V1'
# IDEA : GREEDY
# https://leetcode.com/problems/candy/discuss/1300194/Python-O(n)-time-solution-explained
# IDEA : 
# Go from left to right and while increase, give the the next person +1 candy from previous, if not, leave number of candies as it was. In this way when we make this pass we make sure that condition that person with bigger value gets more candies fulfilled for pairs of adjusent persons where left person is smaller than right. Now, go from right to left and do the same: now we cover pairs of adjacent persons where right is smaller than left. After these two passes all persons are happy.
# Complexity
# Overall time and space complexity is O(n). Remark: there is also O(1) space complexity solution using the idea of peaks and valleys.
class Solution:
    def candy(self, R):
        n, ans = len(R), [1]*len(R)
        
        for i in range(n-1):
            if R[i] < R[i+1]:
                ans[i+1] = max(1 + ans[i], ans[i+1])
                
        for i in range(n-2, -1, -1):
            if R[i+1] < R[i]:
                ans[i] = max(1 + ans[i+1], ans[i])
        
        return sum(ans)

# V1''
# IDEA : min heap
# https://leetcode.com/problems/candy/discuss/1314082/Python-min-heap-solution
class Solution:
    def candy(self, ratings: List[int]) -> int:
        candies = [1] * len(ratings)
        mheap = []
        for i,rating in enumerate(ratings):
            heapq.heappush(mheap,(rating,i))
        #popping the kid with the lowest rating coz he/she not getting anymore than 1 candy
        heapq.heappop(mheap)
        while mheap:
            _,kid = heapq.heappop(mheap)
            if kid == len(candies)-1:
                if ratings[kid] > ratings[kid-1]:
                    candies[kid] = candies[kid-1]+1
            elif kid == 0:
                if ratings[kid] > ratings[kid+1]:
                    candies[kid] = candies[kid+1]+1
            else:
                if ratings[kid] > ratings[kid-1] and ratings[kid] > ratings[kid+1]:
                    candies[kid] = max(candies[kid-1],candies[kid+1]) + 1
                elif ratings[kid] > ratings[kid-1]:
                    candies[kid] = candies[kid-1]+1
                elif ratings[kid] > ratings[kid+1]:
                    candies[kid] = candies[kid+1]+1
                    
        return sum(candies)

# V1'''
# IDEA : BRUTE FORCE
# https://leetcode.com/problems/candy/solution/
# JAVA
# public class Solution {
#     public int candy(int[] ratings) {
#         int[] candies = new int[ratings.length];
#         Arrays.fill(candies, 1);
#         boolean hasChanged = true;
#         while (hasChanged) {
#             hasChanged = false;
#             for (int i = 0; i < ratings.length; i++) {
#                 if (i != ratings.length - 1 && ratings[i] > ratings[i + 1] && candies[i] <= candies[i + 1]) {
#                     candies[i] = candies[i + 1] + 1;
#                     hasChanged = true;
#                 }
#                 if (i > 0 && ratings[i] > ratings[i - 1] && candies[i] <= candies[i - 1]) {
#                     candies[i] = candies[i - 1] + 1;
#                     hasChanged = true;
#                 }
#             }
#         }
#         int sum = 0;
#         for (int candy : candies) {
#             sum += candy;
#         }
#         return sum;
#     }
# }

# V1''''
# IDEA : 2 ARRAY
# https://leetcode.com/problems/candy/solution/
# JAVA
# public class Solution {
#     public int candy(int[] ratings) {
#         int sum = 0;
#         int[] left2right = new int[ratings.length];
#         int[] right2left = new int[ratings.length];
#         Arrays.fill(left2right, 1);
#         Arrays.fill(right2left, 1);
#         for (int i = 1; i < ratings.length; i++) {
#             if (ratings[i] > ratings[i - 1]) {
#                 left2right[i] = left2right[i - 1] + 1;
#             }
#         }
#         for (int i = ratings.length - 2; i >= 0; i--) {
#             if (ratings[i] > ratings[i + 1]) {
#                 right2left[i] = right2left[i + 1] + 1;
#             }
#         }
#         for (int i = 0; i < ratings.length; i++) {
#             sum += Math.max(left2right[i], right2left[i]);
#         }
#         return sum;
#     }
# }

# V2