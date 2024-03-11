"""

Design an algorithm to encode a list of strings to a string. The encoded string is then sent over the network and is decoded back to the original list of strings.

Machine 1 (sender) has the function:

string encode(vector<string> strs) {
  // ... your code
  return encoded_string;
}
Machine 2 (receiver) has the function:

vector<string> decode(string s) {
  //... your code
  return strs;
}
So Machine 1 does:

string encoded_string = encode(strs);
and Machine 2 does:

vector<string> strs2 = decode(encoded_string);
strs2 in Machine 2 should be the same as strs in Machine 1.

Implement the encode and decode methods.

You are not allowed to solve the problem using any serialize methods (such as eval).

 

Example 1:

Input: dummy_input = ["Hello","World"]
Output: ["Hello","World"]
Explanation:
Machine 1:
Codec encoder = new Codec();
String msg = encoder.encode(strs);
Machine 1 ---msg---> Machine 2

Machine 2:
Codec decoder = new Codec();
String[] strs = decoder.decode(msg);
Example 2:

Input: dummy_input = [""]
Output: [""]
 

Constraints:

1 <= strs.length <= 200
0 <= strs[i].length <= 200
strs[i] contains any possible characters out of 256 valid ASCII characters.
 

Follow up: Could you write a generalized algorithm to work on any possible set of characters?

Note:
The string may contain any possible characters out of 256 valid ascii characters. 
Your algorithm should be generalized enough to work on any possible characters.
Do not use class member/global/static variables to store states. 
Your encode and decode algorithms should be stateless.
Do not rely on any library method such as eval or serialize methods. 
You should implement your own encode/decode algorithm.


https://leetcode.ca/2016-08-27-271-Encode-and-Decode-Strings/

"""

# V0 
# encode : len(element) + "$#"
# decode : find the "#" and get the len(element) -> get original element
# NOTE : find() in python
# https://www.runoob.com/python/att-string-find.html
# >>>info = 'abca'
# >>> print info.find('a')    # from index=0, find the 1st element in string. return 0
# 0
# >>> print info.find('a',1)  # from index=1, find the 1st element in string. return 3
# 3
# >>> print info.find('3')    # if can't find, return -1
# -1
# >>>
class Codec:
    def encode(self, strs):
        s = ""
        for i in strs:
            s += str(len(i)) + "#" + i
        return s

    def decode(self, s):
        i, str = 0, []
        while i < len(s):
            # note here
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

# V1
# https://blog.csdn.net/qq508618087/article/details/50912562
# IDEA : 
# encode : len(element) + "$"
# decode : find the "$" and get the len(element)
# JAVA
# class Codec {
# public:
#
#     // Encodes a list of strings to a single string.
#     string encode(vector<string>& strs) {
#         string result;
#         for(auto str: strs) result += to_string((int)str.size())+"$"+str;   
#         return result;
#     }
# 
#     // Decodes a single string to a list of strings.
#     vector<string> decode(string s) {
#         vector<string> result;
#         int len = s.size(), pos = 0;
#         while(pos < len)
#         {
#             int k = s.find('$', pos), num = stoi(s.substr(pos, k-pos));
#             result.push_back(s.substr(k+1, num));
#             pos = k+num+1;
#         }
#         return result;
#     }
# };

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
