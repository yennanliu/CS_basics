"""

Note: This is a companion problem to the System Design problem: Design TinyURL.
TinyURL is a URL shortening service where you enter a URL such as https://leetcode.com/problems/design-tinyurl and it returns a short URL such as http://tinyurl.com/4e9iAk. Design a class to encode a URL and decode a tiny URL.

There is no restriction on how your encode/decode algorithm should work. You just need to ensure that a URL can be encoded to a tiny URL and the tiny URL can be decoded to the original URL.

Implement the Solution class:

Solution() Initializes the object of the system.
String encode(String longUrl) Returns a tiny URL for the given longUrl.
String decode(String shortUrl) Returns the original long URL for the given shortUrl. It is guaranteed that the given shortUrl was encoded by the same object.
 

Example 1:

Input: url = "https://leetcode.com/problems/design-tinyurl"
Output: "https://leetcode.com/problems/design-tinyurl"

Explanation:
Solution obj = new Solution();
string tiny = obj.encode(url); // returns the encoded tiny url.
string ans = obj.decode(tiny); // returns the original url after deconding it.
 

Constraints:

1 <= url.length <= 104
url is guranteed to be a valid URL.

# REF : https://leetcode.com/discuss/interview-question/124658/Design-a-URL-Shortener-(-TinyURL-)-System/

"""

# V0 : ARRAY
class Codec:
    def __init__(self):
        self.urls = []

    def encode(self, longUrl):
        self.urls.append(longUrl)
        return "http://tinyurl.com/" + str(len(self.urls) - 1)
        
    def decode(self, shortUrl):
        return self.urls[int(shortUrl.split('/')[-1])]

# V0'
### TODO : optimize below via idea :  
# https://leetcode.com/discuss/interview-question/124658/Design-a-URL-Shortener-(-TinyURL-)-System/
# IDEA : DICT 
class Codec:
    def __init__(self):
        self.prefix = "http://tinyurl.com/"
        self.short_long = {}
        self.long_short = {}

    def encode(self, longUrl):
        if longUrl not in self.long_short:
            self.long_short[longUrl] = self.prefix + str(len(longUrl))
            self.short_long[self.prefix + str(len(longUrl))] = longUrl
            return self.prefix + str(len(longUrl))
        
    def decode(self, shortUrl):
        if shortUrl in self.short_long:
            return self.short_long[shortUrl]
        return False

# V0
class Codec:
    import string
    letters = string.ascii_letters + string.digits
    full_tiny = {}
    tiny_full = {}
    global_counter = 0
    def encode(self, longUrl):
        def decto62(dec):
            ans = ""
            while 1:
                ans = self.letters[dec % 62] + ans
                dec //= 62
                if not dec:
                    break
            return ans
                
        suffix = decto62(self.global_counter)
        if longUrl not in self.full_tiny:
            self.full_tiny[longUrl] = suffix
            self.tiny_full[suffix] = longUrl
            self.global_counter += 1
        return "http://tinyurl.com/" + suffix
        
    def decode(self, shortUrl):
        idx = shortUrl.split('/')[-1]
        if idx in self.tiny_full:
            return self.tiny_full[idx]
        else:
            return None

# V0''
# IDEA : DICT 
class Codec:
    def __init__(self):
        self.count = 0
        self.d = dict()
    
    def encode(self, longUrl):
        self.count += 1
        self.d[self.count] = longUrl
        return str(self.count)

    def decode(self, shortUrl):
        return self.d[int(shortUrl)]

# V0''
import random
class Codec:
    def __init__(self):
        self.dic = {}
        self.dic2 = {}
    def encode(self, longUrl):
        # Encodes a URL to a shortened URL.
        self.dic[longUrl] = str(random.randint(1,100))
        self.dic2["https://tinyurl.com/" + self.dic[longUrl]] = longUrl
        return "https://tinyurl.com/" + self.dic[longUrl]

    def decode(self, shortUrl):
        # Decodes a shortened URL to its original URL.
        return self.dic2[shortUrl]

# V1
# https://leetcode.com/discuss/interview-question/124658/Design-a-URL-Shortener-(-TinyURL-)-System/
# C++
# string idToShortURL(long int n)
# {
#     // Map to store 62 possible characters
#     char map[] = "abcdefghijklmnopqrstuvwxyzABCDEF"
#                  "GHIJKLMNOPQRSTUVWXYZ0123456789";
#  
#     string shorturl;
#  
#     // Convert given integer id to a base 62 number
#     while (n)
#     {
#         shorturl.push_back(map[n%62]);
#         n = n/62;
#     }
#  
#     // Reverse shortURL to complete base conversion
#     reverse(shorturl.begin(), shorturl.end());
#  
#     return shorturl;
# }
#  
# // Function to get integer ID back from a short url
# long int shortURLtoID(string shortURL)
# {
#     long int id = 0; // initialize result
#  
#     // A simple base conversion logic
#     for (int i=0; i < shortURL.length(); i++)
#     {
#         if ('a' <= shortURL[i] && shortURL[i] <= 'z')
#           id = id*62 + shortURL[i] - 'a';
#         if ('A' <= shortURL[i] && shortURL[i] <= 'Z')
#           id = id*62 + shortURL[i] - 'A' + 26;
#         if ('0' <= shortURL[i] && shortURL[i] <= '9')
#           id = id*62 + shortURL[i] - '0' + 52;
#     }
#     return id;
# }

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79264976
# IDEA : ARRAY
class Codec:

    def __init__(self):
        self.urls = []

    def encode(self, longUrl):
        """Encodes a URL to a shortened URL.

        :type longUrl: str
        :rtype: str
        """
        self.urls.append(longUrl)
        return "http://tinyurl.com/" + str(len(self.urls) - 1)
        
    def decode(self, shortUrl):
        """Decodes a shortened URL to its original URL.
        
        :type shortUrl: str
        :rtype: str
        """
        return self.urls[int(shortUrl.split('/')[-1])]

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/79264976
# IDEA : DICT  
class Codec:
    def __init__(self):
        self.count = 0
        self.d = dict()
    
    def encode(self, longUrl):
        """Encodes a URL to a shortened URL.
        
        :type longUrl: str
        :rtype: str
        """
        self.count += 1
        self.d[self.count] = longUrl
        return str(self.count)

    def decode(self, shortUrl):
        """Decodes a shortened URL to its original URL.
        
        :type shortUrl: str
        :rtype: str
        """
        return self.d[int(shortUrl)]

# V1'''
# https://www.jiuzhang.com/solution/encode-and-decode-tinyurl/#tag-highlight-lang-python
import random
class Solution:
    def __init__(self):
        self.dic = {}
        self.dic2 = {}
    def encode(self, longUrl):
        # Encodes a URL to a shortened URL.
        self.dic[longUrl] = str(random.randint(1,100))
        self.dic2["https://tinyurl.com/" + self.dic[longUrl]] = longUrl
        return "https://tinyurl.com/" + self.dic[longUrl]

    def decode(self, shortUrl):
        # Decodes a shortened URL to its original URL.
        return self.dic2[shortUrl]

# V1''''
# https://leetcode.com/problems/encode-and-decode-tinyurl/discuss/100341/Easy-to-Understand-in-Python
# string DEMO
# In [27]: import string
#
# In [28]: string.ascii_letters
# Out[28]: 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'
#
# In [29]: string.digits
# Out[29]: '0123456789'
class Codec:
    import string
    letters = string.ascii_letters + string.digits
    full_tiny = {}
    tiny_full = {}
    global_counter = 0
    def encode(self, longUrl):
        """Encodes a URL to a shortened URL.
        
        :type longUrl: str
        :rtype: str
        """
        def decto62(dec):
            ans = ""
            while 1:
                ans = self.letters[dec % 62] + ans
                dec //= 62
                if not dec:
                    break
            return ans
                
        suffix = decto62(self.global_counter)
        if longUrl not in self.full_tiny:
            self.full_tiny[longUrl] = suffix
            self.tiny_full[suffix] = longUrl
            self.global_counter += 1
        return "http://tinyurl.com/" + suffix
        
    def decode(self, shortUrl):
        """Decodes a shortened URL to its original URL.
        
        :type shortUrl: str
        :rtype: str
        """
        idx = shortUrl.split('/')[-1]
        if idx in self.tiny_full:
            return self.tiny_full[idx]
        else:
            return None 