# V0 

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
        
# V2
