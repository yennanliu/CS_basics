"""

# https://www.cnblogs.com/lz87/p/10095363.html

[LeetCode] 340. Longest Substring with At Most K Distinct Characters


Given a string, find the length of the longest substring T that contains at most k distinct characters.

Example 1:

Input: s = "eceba", k = 2
Output: 3
Explanation: T is "ece" which its length is 3.
Example 2:

Input: s = "aa", k = 1
Output: 2
Explanation: T is "aa" which its length is 2.


"""

# V0

# V1
# TODO : validate this approach
# IDEA : SLIDING WINDOW
# https://leetcode.jp/leetcode-340-longest-substring-with-at-most-k-distinct-characters-%E8%A7%A3%E9%A2%98%E6%80%9D%E8%B7%AF%E5%88%86%E6%9E%90/
from collections import Counter
class Solution:
    def lengthOfLongestSubstringKDistinct(self, s, k):
        # edge case
        if not s or not k:
            return 0
        # init
        d = {}
        distinct = 0
        l = 0
        res = 0
        c = Counter()
        # loop over s
        for r in range(len(s)):
            rightChar = s[r]
            rightCount = c.get(rightChar, 0)
            # case 1
            if rightCount ==0:
                distinct += 1
            cnt[rightChar] += 1
            # case 2
            if distinct <= k:
                res = max(res, r - l + 1)
            # case 3
            else:
                while distinct > k:
                    leftChar = s[left]
                    leftCount = c.get(leftChar, 0)
                    cnt[leftChar] -= 1
                    if cnt[leftChar] == 0:
                        distinct -= 1
                    left += 1
        return res

# V1'
# https://www.acwing.com/file_system/file/content/whole/index/content/1783600/
class Solution:
    def lengthOfLongestSubstringKDistinct(self, s, k):
        Dict = {}
        count = 0
        left = 0
        right = 0
        res = 0
        while(right < len(s)):
            if s[right] not in Dict:
                Dict[s[right]] = 1
            else:
                Dict[s[right]] += 1

            if Dict[s[right]] == 1:
                count += 1

            if count <= k:
                res = max(res, right - left + 1)

            else:            
                while(count > k and left <= right):
                    Dict[s[left]] -= 1
                    if Dict[s[left]] == 0:
                        count -= 1
                    left += 1
                res = max(res, right - left + 1)
            right += 1

        return res

# V1''
# C++
# class Solution {
# public:
#     int lengthOfLongestSubstringKDistinct(string s, int k) {
#         unordered_map<char, int> hash;
#         int left = 0, Max = 0;
#         for(int i =0; i < s.size(); i++)
#         {
#             hash[s[i]]++;
#             while(hash.size()>k)
#             {
#                 hash[s[left]]--;
#                 if(hash[s[left]]==0) hash.erase(s[left]);
#                 left++;
#             }
#             Max = max(Max, i-left+1);
#         }
#         return Max;
#     }
# };

# V1'''
# https://leetcode.jp/leetcode-340-longest-substring-with-at-most-k-distinct-characters-%E8%A7%A3%E9%A2%98%E6%80%9D%E8%B7%AF%E5%88%86%E6%9E%90/
# JAVA
# public int lengthOfLongestSubstringKDistinct(String s, int k) {
#     // 如果字符串长度为0或者k为0，返回0
#     if(s.length()==0 || k==0) return 0;
#     // 将字符串转为字符数组
#     char[] arr = s.toCharArray();
#     // 区间内字符种类
#     int distinct = 0;
#     // 记录区间内出现过的每种字符的个数
#     Map<Character, Integer> count=new HashMap<>();
#     // 左指针
#     int left=0;
#     // 返回结果
#     int res=0;
#     // 从0向右移动右指针
#     for(int right=0;right<arr.length;right++){
#         // 当前右指针字符
#         char rightChar=arr[right];
#         // 查看该字符在区间内的个数
#         int c = count.getOrDefault(rightChar,0);
#         // 个数为0，说明当前是第一次出现，字符种类加一
#         if(c==0) distinct++;
#         // 更新当前字符在区间内出现的次数
#         count.put(rightChar, c+1);
#         // 如果字符种类小于等于k，当前区间为合理区间
#         if(distinct<=k){
#             // 更新合理区间最大值
#             res=Math.max(res, right-left+1);
#         }else{
#             // 当前为非合理区间
#             while(distinct>k){
#                 // 左指针指向的字符
#                 char leftChar=arr[left];
#                 // 该字符区间内出现的次数
#                 int leftCount = count.getOrDefault(leftChar,0);
#                 // 移除该字符
#                 count.put(leftChar,leftCount-1);
#                 // 如果该字符数量为0，说明区间内不在包含该字符
#                 // 字符种类减一
#                 if(leftCount-1==0) distinct--;
#                 // 否则继续移动左指针
#                 left++;
#             }
#         }
#     }
#     return res;
# }

# V1''''
# https://www.cnblogs.com/lightwindy/p/8537211.html
# JAVA

# V2