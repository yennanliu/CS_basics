"""

1797. Design Authentication Manager
Medium

There is an authentication system that works with authentication tokens. For each session, the user will receive a new authentication token that will expire timeToLive seconds after the currentTime. If the token is renewed, the expiry time will be extended to expire timeToLive seconds after the (potentially different) currentTime.

Implement the AuthenticationManager class:

AuthenticationManager(int timeToLive) constructs the AuthenticationManager and sets the timeToLive.
generate(string tokenId, int currentTime) generates a new token with the given tokenId at the given currentTime in seconds.
renew(string tokenId, int currentTime) renews the unexpired token with the given tokenId at the given currentTime in seconds. If there are no unexpired tokens with the given tokenId, the request is ignored, and nothing happens.
countUnexpiredTokens(int currentTime) returns the number of unexpired tokens at the given currentTime.
Note that if a token expires at time t, and another action happens on time t (renew or countUnexpiredTokens), the expiration takes place before the other actions.

 

Example 1:


Input
["AuthenticationManager", "renew", "generate", "countUnexpiredTokens", "generate", "renew", "renew", "countUnexpiredTokens"]
[[5], ["aaa", 1], ["aaa", 2], [6], ["bbb", 7], ["aaa", 8], ["bbb", 10], [15]]
Output
[null, null, null, 1, null, null, null, 0]

Explanation
AuthenticationManager authenticationManager = new AuthenticationManager(5); // Constructs the AuthenticationManager with timeToLive = 5 seconds.
authenticationManager.renew("aaa", 1); // No token exists with tokenId "aaa" at time 1, so nothing happens.
authenticationManager.generate("aaa", 2); // Generates a new token with tokenId "aaa" at time 2.
authenticationManager.countUnexpiredTokens(6); // The token with tokenId "aaa" is the only unexpired one at time 6, so return 1.
authenticationManager.generate("bbb", 7); // Generates a new token with tokenId "bbb" at time 7.
authenticationManager.renew("aaa", 8); // The token with tokenId "aaa" expired at time 7, and 8 >= 7, so at time 8 the renew request is ignored, and nothing happens.
authenticationManager.renew("bbb", 10); // The token with tokenId "bbb" is unexpired at time 10, so the renew request is fulfilled and now the token will expire at time 15.
authenticationManager.countUnexpiredTokens(15); // The token with tokenId "bbb" expires at time 15, and the token with tokenId "aaa" expired at time 7, so currently no token is unexpired, so return 0.
 

Constraints:

1 <= timeToLive <= 108
1 <= currentTime <= 108
1 <= tokenId.length <= 5
tokenId consists only of lowercase letters.
All calls to generate will contain unique values of tokenId.
The values of currentTime across all the function calls will be strictly increasing.
At most 2000 calls will be made to all functions combined.

"""

# V0

# V1
# IDEA : hashmap
# https://leetcode.com/problems/design-authentication-manager/discuss/1118881/Clean-Python-3-hashmap
class AuthenticationManager:
    def __init__(self, timeToLive: int):
        self.time = timeToLive
        self.unexpired = {}

    def generate(self, tokenId: str, currentTime: int):
        self.unexpired[tokenId] = currentTime + self.time

    def renew(self, tokenId: str, currentTime: int):
        if self.unexpired.get(tokenId, 0) > currentTime:
            self.unexpired[tokenId] = currentTime + self.time

    def countUnexpiredTokens(self, currentTime: int):
        for token in tuple(self.unexpired.keys()):
            if self.unexpired[token] <= currentTime:
                self.unexpired.pop(token)
        return len(self.unexpired)

# V1'
# IDEA : hashmap
# https://leetcode.com/problems/design-authentication-manager/discuss/1118827/Clean-Python-with-explanation
class AuthenticationManager(object):

    def __init__(self, timeToLive):
        self.token = dict()
        self.time = timeToLive    # store timeToLive and create dictionary

    def generate(self, tokenId, currentTime):
        self.token[tokenId] = currentTime    # store tokenId with currentTime

    def renew(self, tokenId, currentTime):
        limit = currentTime-self.time        # calculate limit time to filter unexpired tokens
        if tokenId in self.token and self.token[tokenId]>limit:    # filter tokens and renew its time
            self.token[tokenId] = currentTime

    def countUnexpiredTokens(self, currentTime):
        limit = currentTime-self.time       # calculate limit time to filter unexpired tokens
        c = 0
        for i in self.token:
            if self.token[i]>limit:         # count unexpired tokens
                c+=1
        return c

# V1''
# IDEA : linkedHashmap
# https://leetcode.com/problems/design-authentication-manager/discuss/1127288/Python-using-LinkedHashmap
class AuthenticationManager:

    def __init__(self, timeToLive: int):
        self.ttl = timeToLive
        self.cache = OrderedDict() #linkedHashmap using in LRU cache

    def generate(self, tokenId: str, currentTime: int):
        self.cache[tokenId] = currentTime + self.ttl
        
    def renew(self, tokenId: str, currentTime: int):
        if tokenId not in self.cache:
            return
        expiryTime = self.cache[tokenId]
        if expiryTime > currentTime:
            self.cache[tokenId] = currentTime + self.ttl
            self.cache.move_to_end(tokenId)
        
    def countUnexpiredTokens(self, currentTime: int):
        while self.cache and next(iter(self.cache.values())) <= currentTime:
            self.cache.popitem(last=False)
        return len(self.cache)

# V1''''
# IDEA : deque + hashmap
# https://github.com/kaiwensun/leetcode/blob/master/1501-2000/1797.Design%20Authentication%20Manager.py
from collections import deque
class AuthenticationManager(object):

    def __init__(self, timeToLive):
        """
        :type timeToLive: int
        """
        self.timeToLive = timeToLive
        self.queue = deque()
        self.latest = {}


    def generate(self, tokenId, currentTime):
        """
        :type tokenId: str
        :type currentTime: int
        :rtype: None
        """
        self.queue.append((tokenId, currentTime))
        self.latest[tokenId] = currentTime


    def renew(self, tokenId, currentTime):
        """
        :type tokenId: str
        :type currentTime: int
        :rtype: None
        """
        if self.latest.get(tokenId, float("-inf")) + self.timeToLive > currentTime:
            self.generate(tokenId, currentTime)


    def countUnexpiredTokens(self, currentTime):
        """
        :type currentTime: int
        :rtype: int
        """
        while self.queue and self.queue[0][1] <= currentTime - self.timeToLive:
            tokenId, thenTime = self.queue.popleft()
            if self.latest[tokenId] == thenTime:
                del self.latest[tokenId]
        return len(self.latest)

# V1''''
# https://blog.csdn.net/qq_21201267/article/details/115037663
# C++

# V2