# V0 
class Codec:
    def __init__(self):
        self.urls = []

    def encode(self, longUrl):
        self.urls.append(longUrl)
        return "http://tinyurl.com/" + str(len(self.urls) - 1)
        
    def decode(self, shortUrl):
        return self.urls[int(shortUrl.split('/')[-1])]

# V0'
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

# V1' 
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

# V1''
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

# V1'''
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

# V2