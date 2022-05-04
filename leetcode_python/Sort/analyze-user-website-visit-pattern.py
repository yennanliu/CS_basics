"""

1152. Analyze User Website Visit Pattern
Medium

You are given two string arrays username and website and an integer array timestamp. All the given arrays are of the same length and the tuple [username[i], website[i], timestamp[i]] indicates that the user username[i] visited the website website[i] at time timestamp[i].

A pattern is a list of three websites (not necessarily distinct).

For example, ["home", "away", "love"], ["leetcode", "love", "leetcode"], and ["luffy", "luffy", "luffy"] are all patterns.
The score of a pattern is the number of users that visited all the websites in the pattern in the same order they appeared in the pattern.

For example, if the pattern is ["home", "away", "love"], the score is the number of users x such that x visited "home" then visited "away" and visited "love" after that.
Similarly, if the pattern is ["leetcode", "love", "leetcode"], the score is the number of users x such that x visited "leetcode" then visited "love" and visited "leetcode" one more time after that.
Also, if the pattern is ["luffy", "luffy", "luffy"], the score is the number of users x such that x visited "luffy" three different times at different timestamps.
Return the pattern with the largest score. If there is more than one pattern with the same largest score, return the lexicographically smallest such pattern.

 

Example 1:

Input: username = ["joe","joe","joe","james","james","james","james","mary","mary","mary"], timestamp = [1,2,3,4,5,6,7,8,9,10], website = ["home","about","career","home","cart","maps","home","home","about","career"]
Output: ["home","about","career"]
Explanation: The tuples in this example are:
["joe","home",1],["joe","about",2],["joe","career",3],["james","home",4],["james","cart",5],["james","maps",6],["james","home",7],["mary","home",8],["mary","about",9], and ["mary","career",10].
The pattern ("home", "about", "career") has score 2 (joe and mary).
The pattern ("home", "cart", "maps") has score 1 (james).
The pattern ("home", "cart", "home") has score 1 (james).
The pattern ("home", "maps", "home") has score 1 (james).
The pattern ("cart", "maps", "home") has score 1 (james).
The pattern ("home", "home", "home") has score 0 (no user visited home 3 times).
Example 2:

Input: username = ["ua","ua","ua","ub","ub","ub"], timestamp = [1,2,3,4,5,6], website = ["a","b","a","a","b","c"]
Output: ["a","b","a"]
 

Constraints:

3 <= username.length <= 50
1 <= username[i].length <= 10
timestamp.length == username.length
1 <= timestamp[i] <= 109
website.length == username.length
1 <= website[i].length <= 10
username[i] and website[i] consist of lowercase English letters.
It is guaranteed that there is at least one user who visited at least three websites.
All the tuples [username[i], timestamp[i], website[i]] are unique.

"""

# V0

# V1
# IDEA : BRUTE FOECE + itertools.combinations
# https://blog.csdn.net/qq_17550379/article/details/99209763
# itertools.combinations : 
#  -> https://codertw.com/%E7%A8%8B%E5%BC%8F%E8%AA%9E%E8%A8%80/364249/
class Solution:
    def mostVisitedPattern(self, username, timestamp, website):
        data = [[username[i], timestamp[i], website[i]] for i in range(len(username))]
        data.sort()
        d = collections.defaultdict(list)
        for u, t, w in data:
            d[u].append(w)
        
        res = collections.defaultdict(set)
        for i, v in d.items():
            """
            note here !!!

            itertools.combinations(v, 3) : 
                -> will return ALL non-dupliicated combinations from collections with given number counts

            example :
                In [7]: import itertools

                In [8]:  x = itertools.combinations(range(4), 3)

                In [9]: print (list(x))
                [(0, 1, 2), (0, 1, 3), (0, 2, 3), (1, 2, 3)]

            syntax :
                itertools.combinations(collections, number-of-elements)
            """
            for j in itertools.combinations(v, 3):
                res[j].add(i)

        return sorted(res.items(), key=lambda a:(-len(a[1]), a[0]))[0][0]

# V1'
# https://leetcode.com/problems/analyze-user-website-visit-pattern/discuss/355388/Python-Solution
class Solution:
    def mostVisitedPattern(self, username, timestamp, website):
            dp = collections.defaultdict(list)
            for t, u, w in sorted(zip(timestamp, username, website)):
                dp[u].append(w)
            count = sum([collections.Counter(set(itertools.combinations(dp[u], 3))) for u in dp], collections.Counter())
            return list(min(count, key=lambda k: (-count[k], k)))

# V1
# https://blog.51cto.com/u_15344287/3648546
# https://blog.csdn.net/qq_21201267/article/details/107784313
class Solution:
    def mostVisitedPattern(self, username: List[str], timestamp: List[int], website: List[str]) -> List[str]:
        size1 = len(username)

        # process with users
        # O(N)
        count1 = collections.defaultdict(list)
        for i in range(size1):
            user, time, site = username[i], timestamp[i], website[i]
            count1[user].append((time, site))

        count2 = collections.defaultdict(list)
        for user, lst in count1.items():
            count2[user] = [i[1] for i in sorted(lst)]

        # process with paths
        count3 = collections.Counter()
        for lst in count2.values():
            paths = set()
            size2 = len(lst)
            for i in range(size2):
                for j in range(i + 1, size2):
                    for k in range(j + 1, size2):
                        paths.add((lst[i], lst[j], lst[k]))

            # de-duplicate with user
            for path in paths:
                count3[path] += 1

        # return result
        ans = list(count3.keys())
        ans.sort(key=lambda x: (-count3[x], x))

        return list(ans[0])

# V1''
# IDEA : SUBSET
# https://leetcode.com/problems/analyze-user-website-visit-pattern/discuss/355385/Python-using-subset3
class Solution:
    def mostVisitedPattern(self, users, t, w):
            def subsets3(a, i, cres, fres):
                if len(cres) == 3:
                    fres.add(tuple(cres))
                    return
                if i == len(a):
                    return
                cres.append(a[i])
                subsets3(a, i+1, cres, fres)
                cres.remove(a[i])        
                subsets3(a, i+1, cres, fres)

            d = defaultdict(list)
            for i in range(len(users)):
                d[users[i]].append((w[i], t[i]))
            res = []
            for user, visits in d.items():
                visits.sort(key=lambda x: x[1])
                fres = set()
                subsets3(list(zip(*visits))[0], 0, [], fres)
                res.extend(fres)
            res.sort()

            count, fcount, fres = 1, 1, res[0]
            for i in range(1, len(res)):
                if res[i] == res[i-1]:
                    count += 1
                    if count > fcount:
                        fcount = count
                        fres = res[i]
                else:
                    count = 1
            return list(fres)

# V1'''
# https://leetcode.com/problems/analyze-user-website-visit-pattern/discuss/1617330/python
class Solution:
    def mostVisitedPattern(self, username, timestamp, website):
        #stores user and patterns
        graph={}
        #zip accepts iterables/arrays and merges them into a single tuple
        #important--need to use sorted 
        utw_tuple=(zip(username,timestamp,website))
        #sort the tuple
        utw_tuple=sorted(utw_tuple)
        #add websites to each user
        for user,time,website in utw_tuple:
            if user not in graph:
                graph[user]=[website]
            else:
                graph[user].append(website)
        #important--initialize the counter object to use for combinarions
        #counter dictioary for pattern counts
        counter={}
        #now count how many times patterns occur
        for user, websites1 in graph.items():
            #here we are creating any possible combinations of websites --of length 3- aa,bb,cc or bb,cc,dd etc
            all_website_patterns=set(itertools.combinations(websites1,3))
            for triple in all_website_patterns:
                if triple in counter:
                    counter[triple]+=1
                else:
                    counter[triple]=1
                #counter[triple]+=1
        maxpattern=None
        maxcount=0
        
        for pattern, count in counter.items():
            if count>maxcount:
                maxpattern=pattern
                maxcount=count
            #if same count,but lexigraphically smaller
            elif count==maxcount and pattern<maxpattern:
                maxpattern=pattern
        return maxpattern

# V1''''
# IDEA : BRUTE FORCE
# https://leetcode.com/problems/analyze-user-website-visit-pattern/discuss/534149/Python-Brute-Force
from collections import defaultdict
class Solution:
    def mostVisitedPattern(self, username, timestamp, website):        
        data = [[username[i], timestamp[i], website[i]] for i in range(len(username))]
        data.sort()
        dic = defaultdict(list)
        for u, t, w in data:
            dic[u].append(w)
        seq = defaultdict(set)
        for u, w in dic.items():
            if len(w)>=3:
                for i in range(len(w)-2):
                    for j in range(i+1,len(w)-1):
                        for k in range(j+1,len(w)):
                            webs = (w[i],w[j],w[k])
                            seq[webs].add(u)
     
        return sorted(seq.items(),key=lambda x:(-len(x[1]),x[0]))[0][0]

# V1'''''
# https://leetcode.com/problems/analyze-user-website-visit-pattern/discuss/1032279/Python-with-Explanation
from itertools import combinations
class Solution(object):
    def mostVisitedPattern(self, username, timestamp, website):
        userToSites = collections.defaultdict(list)
        seqCounter = collections.Counter()
        
        # sort by timestamp
        # joe: [home, about, career]  websites in list are in ascending timestamp order
        for time, user, site in sorted(zip(timestamp, username, website)): 
            userToSites[user].append(site) 
        
        for user, siteLst in userToSites.items():
            comb = set(combinations(siteLst, 3)) # size of combination is set to 3 
            
            # Tuple as key, value,  e.g. ('home', 'about', 'career') : 2
            for seq in comb: 
                seqCounter[seq] += 1
        
        # sort descending by value, then lexicographically
        return sorted(seqCounter, key = lambda x: (-seqCounter[x], x))[0]

# V2