# V0 
class Codec:
    def encode(self, strs):
        s = ""
        for i in strs:
            s += str(len(i)) + "#" + i
        return s

    def decode(self, s):
        i, str = 0, []
        while i < len(s):
            sharp = s.find("#", i)
            l = int(s[i:sharp])
            str.append(s[sharp + 1:sharp + l + 1])
            i = sharp + l + 1
        return str

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

### Test case : dev 

# V1'
# https://medium.com/leetcode-%E6%BC%94%E7%AE%97%E6%B3%95%E6%95%99%E5%AD%B8/024-leetcode-271-%E6%BC%94%E7%AE%97%E6%B3%95-encode-and-decode-strings-%E5%AD%97%E4%B8%B2%E5%8A%A0%E8%A7%A3%E5%AF%86-722cafd6238
# IDEA :
# ABC -> 3/ABC 
# ABCD -> 4/ABCD
# A B C D ->1/A1/B1/C1/D
#
# JAVA
# // Encodes a list of strings to a single string.
#     public String encode(List<String> strs) {
#         StringBuilder sb = new StringBuilder();
#         for(String s : strs) {
#             sb.append(s.length()).append('/').append(s);
#         }
#         return sb.toString();
#     }
#
#     // Decodes a single string to a list of strings.
#     public List<String> decode(String s) {
#         List<String> ret = new ArrayList<String>();
#         int i = 0;
#         while(i < s.length()) {
#             int slash = s.indexOf('/', i);// return the 1st '/' index from i
#             int size = Integer.valueOf(s.substring(i, slash)); // the length of encode
#             ret.add(s.substring(slash + 1, slash + size + 1)); // cut it off
#             i = slash + size + 1;// redefine the i index 
#         }
#         return ret;
#     }

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