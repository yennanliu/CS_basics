# In the following, every capital letter represents some hexadecimal digit from 0 to f.

# The red-green-blue color "#AABBCC" can be written as "#ABC" in shorthand.  For example, "#15c" is shorthand for the color "#1155cc".

# Now, say the similarity between two colors "#ABCDEF" and "#UVWXYZ" is -(AB - UV)^2 - (CD - WX)^2 - (EF - YZ)^2.

# Given the color "#ABCDEF", return a 7 character color that is most similar to #ABCDEF, and has a shorthand (that is, it can be represented as some "#XYZ"

# Example 1:
# Input: color = "#09f166"
# Output: "#11ee66"
# Explanation:  
# The similarity is -(0x09 - 0x11)^2 -(0xf1 - 0xee)^2 - (0x66 - 0x66)^2 = -64 -9 -0 = -73.
# This is the highest among any shorthand color.
# Note:

# color is a string of length 7.
# color is a valid RGB color: for i > 0, color[i] is a hexadecimal digit from 0 to f
# Any answer which has the same (highest) similarity as the best answer will be accepted.
# All inputs and outputs should use lowercase letters, and the output is 7 characters.


# V1  : dev 

# V2 
# http://bookshadow.com/weblog/2018/03/18/leetcode-similar-rgb-color/
### Brute Force ###
class Solution(object):
    def similarRGB(self, color):
        """
        :type color: str
        :rtype: str
        """
        ir, ig, ib = (int(color[x: x+2], 16)
                      for x in (1, 3, 5))
        ans = ()
        delta = 0x7FFFFFFF
        for r in range(16):
            for g in range(16):
                for b in range(16):
                    ndelta = sum((ic - c * 17) ** 2
                                 for ic, c in zip((ir, ig, ib), (r, g, b)))
                    if ndelta < delta:
                        delta = ndelta
                        ans = r, g, b
        return '#' + ''.join(hex(c)[2] * 2 for c in ans)

# V3 
# Time:  O(1)
# Space: O(1)
class Solution(object):
    def similarRGB(self, color):
        """
        :type color: str
        :rtype: str
        """
        def rounding(color):
            q, r = divmod(int(color, 16), 17)
            if r > 8: q += 1
            return '{:02x}'.format(17*q)

        return '#' + \
                rounding(color[1:3]) + \
                rounding(color[3:5]) + \
                rounding(color[5:7])