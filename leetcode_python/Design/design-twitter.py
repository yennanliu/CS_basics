# V0
# https://github.com/labuladong/fucking-algorithm/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E7%B3%BB%E5%88%97/%E8%AE%BE%E8%AE%A1Twitter.md
from collections import defaultdict
from heapq import merge
class Twitter(object):
    
    def __init__(self):
        self.follower_followees_map = defaultdict(set)
        self.user_tweets_map = defaultdict(list)
        self.time_stamp = 0

    def postTweet(self, userId, tweetId):
        self.user_tweets_map[userId].append((self.time_stamp, tweetId))
        self.time_stamp -= 1

    def getNewsFeed(self, userId):
        followees = self.follower_followees_map[userId]
        followees.add(userId)
        
        # reversed(.) returns a listreverseiterator, so the complexity is O(1) not O(n)
        candidate_tweets = [reversed(self.user_tweets_map[u]) for u in followees]

        tweets = []
        # complexity is 10lg(n), n is twitter's user number in worst case
        for t in merge(*candidate_tweets):
            tweets.append(t[1])
            if len(tweets) == 10:
                break
        return tweets

    def follow(self, followerId, followeeId):
        self.follower_followees_map[followerId].add(followeeId)

    def unfollow(self, followerId, followeeId):
        self.follower_followees_map[followerId].discard(followeeId)

# V0'
import collections, itertools, heapq
class Twitter(object):

    def __init__(self):
        self.timer = itertools.count(step=-1)
        self.tweets = collections.defaultdict(collections.deque)
        self.followees = collections.defaultdict(set)

    def postTweet(self, userId, tweetId):
        self.tweets[userId].appendleft((next(self.timer), tweetId))

    def getNewsFeed(self, userId):
        tweets = heapq.merge(*(self.tweets[u] for u in self.followees[userId] | {userId}))
        return [t for _, t in itertools.islice(tweets, 10)]

    def follow(self, followerId, followeeId):
        self.followees[followerId].add(followeeId)

    def unfollow(self, followerId, followeeId):
        self.followees[followerId].discard(followeeId)

# V1
# https://leetcode.com/problems/design-twitter/discuss/82943/Use-python-heapq.merge
# https://github.com/labuladong/fucking-algorithm/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E7%B3%BB%E5%88%97/%E8%AE%BE%E8%AE%A1Twitter.md
# IDEA : heapq.merge
from collections import defaultdict
from heapq import merge
class Twitter(object):
    def __init__(self):
        """
        Initialize your data structure here.
        """
        self.follower_followees_map = defaultdict(set)
        self.user_tweets_map = defaultdict(list)
        self.time_stamp = 0

    def postTweet(self, userId, tweetId):
        """
        Compose a new tweet.
        :type userId: int
        :type tweetId: int
        :rtype: void
        """
        self.user_tweets_map[userId].append((self.time_stamp, tweetId))
        self.time_stamp -= 1

    def getNewsFeed(self, userId):
        """
        Retrieve the 10 most recent tweet ids in the user's news feed. Each item in the news feed must be posted by users who the user followed or by the user herself. Tweets must be ordered from most recent to least recent.
        :type userId: int
        :rtype: List[int]
        """
        followees = self.follower_followees_map[userId]
        followees.add(userId)
        
        # reversed(.) returns a listreverseiterator, so the complexity is O(1) not O(n)
        candidate_tweets = [reversed(self.user_tweets_map[u]) for u in followees]

        tweets = []
        # complexity is 10lg(n), n is twitter's user number in worst case
        for t in merge(*candidate_tweets):
            tweets.append(t[1])
            if len(tweets) == 10:
                break
        return tweets

    def follow(self, followerId, followeeId):
        """
        Follower follows a followee. If the operation is invalid, it should be a no-op.
        :type followerId: int
        :type followeeId: int
        :rtype: void
        """
        self.follower_followees_map[followerId].add(followeeId)

    def unfollow(self, followerId, followeeId):
        """
        Follower unfollows a followee. If the operation is invalid, it should be a no-op.
        :type followerId: int
        :type followeeId: int
        :rtype: void
        """
        self.follower_followees_map[followerId].discard(followeeId)

# V1'
# https://leetcode.com/problems/design-twitter/discuss/82863/Python-solution
# IDEA : collections.defaultdict
# * DEMO : itertools.count
# EXAMPLE 1
# In [23]: import itertools
#     ...: mytimer  = itertools.count(step=-1)
#     ...: print (mytimer)
#     ...: for _ in range(5):
#     ...:     print (next(mytimer))
#     ...:     
# count(0, -1)
# 0
# -1
# -2
# -3
# -4
# EXAMPLE 2 
# In [24]: import itertools
#     ...: mytimer2  = itertools.count(step=1)
#     ...: print (mytimer2)
#     ...: for _ in range(5):
#     ...:     print (next(mytimer2))
#     ...:     
# count(0)
# 0
# 1
# 2
# 3
# 4
# 
# * DEMO collections.defaultdict(collections.deque) :
# In [31]: mytweets = collections.defaultdict(collections.deque)
#     ...: mytweets['001'].appendleft(('1', 'twitterid_1'))
#     ...: mytweets['002'].appendleft(('2', 'twitterid_2'))
#     ...: mytweets['003'].appendleft(('3', 'twitterid_3'))
#     ...: mytweets['001'].appendleft(('10', 'twitterid_10'))
#     ...: print (mytweets)
#     ...: 
# defaultdict(<class 'collections.deque'>, {'001': deque([('10', 'twitterid_10'), ('1', 'twitterid_1')]), '002': deque([('2', 'twitterid_2')]), '003': deque([('3', 'twitterid_3')])})
# * DEMO heapq.merge(*iterables) : 
# https://www.geeksforgeeks.org/merge-two-sorted-arrays-python-using-heapq/
# heapq.merge(*iterables) -> Merge multiple sorted inputs into a single sorted output (for example, merge timestamped entries from multiple log files). Returns an iterator over the sorted values.
# In [46]: mytweets = collections.defaultdict(collections.deque)
#     ...: mytweets['1'].appendleft(('1', 'twitterid_1'))
#     ...: mytweets['2'].appendleft(('2', 'twitterid_2'))
#     ...: mytweets['3'].appendleft(('3', 'twitterid_3'))
#     ...: mytweets['1'].appendleft(('10', 'twitterid_10'))
#     ...: #print (mytweets)
#     ...: print (list(heapq.merge(*(mytweets[u] for u in ['1','2','3']))))
#     ...: print ('='*30)
#     ...: print (list(heapq.merge((mytweets[u] for u in ['1','2','3']))))
#     ...: 
# [('10', 'twitterid_10'), ('1', 'twitterid_1'), ('2', 'twitterid_2'), ('3', 'twitterid_3')]
# ==============================
# [deque([('10', 'twitterid_10'), ('1', 'twitterid_1')]), deque([('2', 'twitterid_2')]), deque([('3', 'twitterid_3')])]
# * DEMO : bitwise OR (a | b)
# ->  a | b  : bitwise OR  Each bit position in the result is the logical OR of the bits in the corresponding position of the operands. (1 if either is 1, otherwise 0.)
# https://realpython.com/python-operators-expressions/
# In [67]: 1 | 1
# Out[67]: 1

# In [68]: 1 | 2
# Out[68]: 3

# In [69]: 1 | 0
# Out[69]: 1
import collections, itertools, heapq
class Twitter(object):

    def __init__(self):
        self.timer = itertools.count(step=-1)
        self.tweets = collections.defaultdict(collections.deque)
        self.followees = collections.defaultdict(set)

    def postTweet(self, userId, tweetId):
        self.tweets[userId].appendleft((next(self.timer), tweetId))

    def getNewsFeed(self, userId):
        tweets = heapq.merge(*(self.tweets[u] for u in self.followees[userId] | {userId}))
        return [t for _, t in itertools.islice(tweets, 10)]

    def follow(self, followerId, followeeId):
        self.followees[followerId].add(followeeId)

    def unfollow(self, followerId, followeeId):
        self.followees[followerId].discard(followeeId)

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/82155420
# IDEA : heapq
class User(object):
    """
    User structure
    """
    def __init__(self, userId):
        self.userId = userId
        self.tweets = set()
        self.following = set()

class Tweet(object):
    """
    Tweet structure
    """
    def __init__(self, tweetId, userId, ts):
        self.tweetId = tweetId
        self.userId = userId
        self.ts = ts
        
    def __cmp__(self, other):
        #call global(builtin) function cmp for int
        return cmp(other.ts, self.ts)

class Twitter(object):
    
    def __init__(self):
        """
        Initialize your data structure here.
        """
        self.ts = 0
        self.userMap = dict()
        

    def postTweet(self, userId, tweetId):
        """
        Compose a new tweet.
        :type userId: int
        :type tweetId: int
        :rtype: void
        """
        if userId not in self.userMap:
            self.userMap[userId] = User(userId)
        tweet = Tweet(tweetId, userId, self.ts)
        self.userMap[userId].tweets.add(tweet)
        self.ts += 1

    def getNewsFeed(self, userId):
        """
        Retrieve the 10 most recent tweet ids in the user's news feed. Each item in the news feed must be posted by users who the user followed or by the user herself. Tweets must be ordered from most recent to least recent.
        :type userId: int
        :rtype: List[int]
        """
        res = list()
        que = []
        if userId not in self.userMap:
            return res
        mainUser = self.userMap[userId]
        for t in mainUser.tweets:
            heapq.heappush(que, t)
        for u in mainUser.following:
            for t in u.tweets:
                heapq.heappush(que, t)
        n = 0
        while que and n < 10:
            res.append(heapq.heappop(que).tweetId)
            n += 1
        return res

    def follow(self, followerId, followeeId):
        """
        Follower follows a followee. If the operation is invalid, it should be a no-op.
        :type followerId: int
        :type followeeId: int
        :rtype: void
        """
        if followeeId not in self.userMap:
            self.userMap[followeeId] = User(followeeId)
        if followerId not in self.userMap:
            self.userMap[followerId] = User(followerId)
        if followerId == followeeId:
            return
        followee = self.userMap[followeeId]
        self.userMap[followerId].following.add(followee)

    def unfollow(self, followerId, followeeId):
        """
        Follower unfollows a followee. If the operation is invalid, it should be a no-op.
        :type followerId: int
        :type followeeId: int
        :rtype: void
        """
        if (followerId == followeeId) or (followerId not in self.userMap) or (followeeId not in self.userMap):
            return
        followee = self.userMap[followeeId]
        if followee in self.userMap.get(followerId).following:
            self.userMap.get(followerId).following.remove(followee)

# V1'''
# https://www.hrwhisper.me/leetcode-design-twitter/
class Twitter(object):
    def __init__(self):
        """
        Initialize your data structure here.
        """
        self.tweets_cnt = 0
        self.tweets = collections.defaultdict(list)
        self.follower_ship = collections.defaultdict(set)

    def postTweet(self, userId, tweetId):
        """
        Compose a new tweet.
        :type userId: int
        :type tweetId: int
        :rtype: void
        """
        self.tweets[userId].append([tweetId, self.tweets_cnt])
        self.tweets_cnt += 1

    def getNewsFeed(self, userId):
        """
        Retrieve the 10 most recent tweet ids in the user's news feed. Each item in the news feed must be posted by users who the user followed or by the user herself. Tweets must be ordered from most recent to least recent.
        :type userId: int
        :rtype: List[int]
        """
        recent_tweets = []
        user_list = list(self.follower_ship[userId]) + [userId]
        userId_tweet_index = [[userId, len(self.tweets[userId]) - 1] for userId in user_list if userId in self.tweets]

        for _ in range(10):
            max_index = max_tweet_id = max_user_id = -1
            for i, (user_id, tweet_index) in enumerate(userId_tweet_index):
                if tweet_index >= 0:
                    tweet_info = self.tweets[user_id][tweet_index]
                    if tweet_info[1] > max_tweet_id:
                        max_index, max_tweet_id, max_user_id = i, tweet_info[1], user_id

            if max_index < 0: break
            recent_tweets.append(self.tweets[max_user_id][userId_tweet_index[max_index][1]][0])
            userId_tweet_index[max_index][1] -= 1

        return recent_tweets

    def follow(self, followerId, followeeId):
        """
        Follower follows a followee. If the operation is invalid, it should be a no-op.
        :type followerId: int
        :type followeeId: int
        :rtype: void
        """
        if followerId != followeeId:
            self.follower_ship[followerId].add(followeeId)

    def unfollow(self, followerId, followeeId):
        """
        Follower unfollows a followee. If the operation is invalid, it should be a no-op.
        :type followerId: int
        :type followeeId: int
        :rtype: void
        """
        if followerId in self.follower_ship and followeeId in self.follower_ship[followerId]:
            self.follower_ship[followerId].remove(followeeId)

# V1''''
# http://bookshadow.com/weblog/2016/06/11/leetcode-design-twitter/
# IDEA : JAVA

# V2 
# https://github.com/kamyu104/LeetCode-Solutions/blob/master/Python/design-twitter.py
# Time:  O(klogu), k is most recently number of tweets,
#                  u is the number of the user's following.
# Space: O(t + f), t is the total number of tweets,
#                  f is the total number of followings.
import collections
import heapq
class Twitter(object):

    def __init__(self):
        """
        Initialize your data structure here.
        """
        self.__number_of_most_recent_tweets = 10
        self.__followings = collections.defaultdict(set)
        self.__messages = collections.defaultdict(list)
        self.__time = 0

    def postTweet(self, userId, tweetId):
        """
        Compose a new tweet.
        :type userId: int
        :type tweetId: int
        :rtype: void
        """
        self.__time += 1
        self.__messages[userId].append((self.__time, tweetId))

    def getNewsFeed(self, userId):
        """
        Retrieve the 10 most recent tweet ids in the user's news feed. Each item in the news feed must be posted by users who the user followed or by the user herself. Tweets must be ordered from most recent to least recent.
        :type userId: int
        :rtype: List[int]
        """
        max_heap = []
        if self.__messages[userId]:
            heapq.heappush(max_heap, (-self.__messages[userId][-1][0], userId, 0))
        for uid in self.__followings[userId]:
            if self.__messages[uid]:
                heapq.heappush(max_heap, (-self.__messages[uid][-1][0], uid, 0))

        result = []
        while max_heap and len(result) < self.__number_of_most_recent_tweets:
            t, uid, curr = heapq.heappop(max_heap)
            nxt = curr + 1
            if nxt != len(self.__messages[uid]):
                heapq.heappush(max_heap, (-self.__messages[uid][-(nxt+1)][0], uid, nxt))
            result.append(self.__messages[uid][-(curr+1)][1])
        return result

    def follow(self, followerId, followeeId):
        """
        Follower follows a followee. If the operation is invalid, it should be a no-op.
        :type followerId: int
        :type followeeId: int
        :rtype: void
        """
        if followerId != followeeId:
            self.__followings[followerId].add(followeeId)

    def unfollow(self, followerId, followeeId):
        """
        Follower unfollows a followee. If the operation is invalid, it should be a no-op.
        :type followerId: int
        :type followeeId: int
        :rtype: void
        """
        self.__followings[followerId].discard(followeeId)