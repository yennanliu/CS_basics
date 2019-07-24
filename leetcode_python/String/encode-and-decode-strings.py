# V0 

# V1 
# http://www.voidcn.com/article/p-hpbzcdjd-zo.html
class Codec:
    def encode(self, strs):
        """Encodes a list of strings to a single string.
        
        :type strs: List[str]
        :rtype: str
        """
        s = ""
        for i in strs:
            s += str(len(i)) + "#" + i
        return s

    def decode(self, s):
        """Decodes a single string to a list of strings.
        
        :type s: str
        :rtype: List[str]
        """
        i, str = 0, []
        while i < len(s):
            sharp = s.find("#", i)
            l = int(s[i:sharp])
            str.append(s[sharp + 1:sharp + l + 1])
            i = sharp + l + 1
        return str
# Your Codec object will be instantiated and called as such:
# codec = Codec()
# codec.decode(codec.encode(strs))

# V2 
# Time:  O(n)
# Space: O(1)
class Codec(object):

    def encode(self, strs):
        """Encodes a list of strings to a single string.
        :type strs: List[str]
        :rtype: str
        """
        encoded_str = ""
        for s in strs:
            encoded_str += "%0*x" % (8, len(s)) + s
        return encoded_str


    def decode(self, s):
        """Decodes a single string to a list of strings.
        :type s: str
        :rtype: List[str]
        """
        i = 0
        strs = []
        while i < len(s):
            l = int(s[i:i+8], 16)
            strs.append(s[i+8:i+8+l])
            i += 8+l
        return strs