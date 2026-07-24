"""

394. Decode String
Medium

Given an encoded string, return its decoded string.

The encoding rule is: k[encoded_string], where the encoded_string inside the square brackets is being repeated exactly k times. Note that k is guaranteed to be a positive integer.

You may assume that the input string is always valid; No extra white spaces, square brackets are well-formed, etc.

Furthermore, you may assume that the original data does not contain any digits and that digits are only for those repeat numbers, k. For example, there won't be input like 3a or 2[4].

 

Example 1:

Input: s = "3[a]2[bc]"
Output: "aaabcbc"
Example 2:

Input: s = "3[a2[c]]"
Output: "accaccacc"
Example 3:

Input: s = "2[abc]3[cd]ef"
Output: "abcabccdcdcdef"
Example 4:

Input: s = "abc3[cd]xyz"
Output: "abccdcdcdxyz"
 

Constraints:

1 <= s.length <= 30
s consists of lowercase English letters, digits, and square brackets '[]'.
s is guaranteed to be a valid input.
All the integers in s are in the range [1, 300].

"""



# V0
# IDEA : STACK (2 stack) (GPT)
"""
NOTE:

d_st: stack for storage `digit`
s_st: stack for storage `string`

"""
class Solution(object):
    def decodeString(self, s):
        d_st = []
        s_st = []

        prev_digit = 0
        prev_str = ""

        for x in s:
            if x.isdigit():
                prev_digit = prev_digit * 10 + int(x)

            elif x.isalpha():
                prev_str += x

            elif x == "[":
                d_st.append(prev_digit)
                s_st.append(prev_str)
                prev_digit = 0
                prev_str = ""

            else:  # x == "]"
                repeat = d_st.pop()
                prev = s_st.pop()
                prev_str = prev + repeat * prev_str

        return prev_str


# V0-1
# IDEA : STACK (2 stack) (gemini)
"""
NOTE:

d_st: stack for storage `digit`
s_st: stack for storage `string`

"""
class Solution(object):
    def decodeString(self, s):
        """
        :type s: str
        :rtype: str
        """
        d_st = []
        s_st = []
        
        prev_digit = 0
        prev_str = ""
        
        for x in s:
            if x.isdigit():
                # Build the multi-digit number
                prev_digit = prev_digit * 10 + int(x)
                
            elif x.isalpha():
                # Build the current string
                prev_str += x
                
            elif x == "[":
                # Push the current state to the stacks
                d_st.append(prev_digit)
                s_st.append(prev_str)
                
                # Reset them for the new bracket level
                prev_digit = 0
                prev_str = ""
                
            else: # This handles the "]" character
                # Pop the previous string and the multiplier
                tmp_str = s_st.pop()
                tmp_int = d_st.pop()
                
                # The new string is: (what came before) + (multiplier * what was inside)
                prev_str = tmp_str + (tmp_int * prev_str)
                
        # By the end, all brackets are closed and the full string is in prev_str
        return prev_str


# V0
# IDEA : STACK (GPT)
"""
NOTE !!!


# stack: [(curr_str, curr_num)]
"""
# time = O(n), n = length of decoded output string
# space = O(n)
class Solution(object):
    def decodeString(self, s):
        if not s:
            return ""

        # NOTE !!!
        # stack: [(curr_str, curr_num)]
        stack = []

        curr_num = 0
        curr_str = ""

        for ch in s:

            if ch.isdigit():
                curr_num = curr_num * 10 + int(ch)

            elif ch == '[':
                stack.append((curr_str, curr_num))

                curr_str = ""
                curr_num = 0

            elif ch == ']':
                """
                NOTE !!!


                when `ch == ']'`
                    -> we pop prev_str, prev_num

                    -> cur_str = prev_str + curr_str * prev_num

                        -> so we DON'T add `curr_str` stack directly,
                           but update it as prev_str + curr_str * prev_num
                           , then add to stack
                """
                prev_str, prev_num = stack.pop()

                curr_str = prev_str + curr_str * prev_num

            else:
                curr_str += ch

        return curr_str


# V0
# IDEA : STACK (GEMINI)
# time = O(n), n = length of decoded output string
# space = O(n)
class Solution(object):
    def decodeString(self, s):
        """
        :type s: str
        :rtype: str
        """
        stack = []
        current_str = ""
        current_num = 0
        
        for char in s:
            if char.isdigit():
                current_num = current_num * 10 + int(char)
                
            elif char == '[':
                # Push the string first, then the multiplier onto the SAME stack
                stack.append(current_str)
                stack.append(current_num)
                
                # Reset trackers
                current_str = ""
                current_num = 0
                
            elif char == ']':
                # The top item is guaranteed to be the multiplier
                repeat_times = stack.pop()
                # The next item popped is guaranteed to be the preceding string text
                prev_str = stack.pop()
                
                # Decode this layer and update our ongoing string tracking
                current_str = prev_str + (current_str * repeat_times)
                
            else:
                current_str += char
                
        return current_str


# V0
# IDEA : STACK
# NOTE : treat before cases separately
#        1) isdigit
#        2) isalpha
#        3) "["
#        4) "]"
# and define num = 0 for dealing with "100a[b]", "10abc" cases
# time = O(n), n = length of decoded output string
# space = O(n)
class Solution:
    def decodeString(self, s):
        num = 0
        string = ''
        stack = []
        """
        NOTE : we deal with 4 cases
            1) digit
            2) "["
            3) alphabet
            4) "]"

        NOTE : 
            we use pre_num, pre_string for dealing with previous result
        """
        for c in s:
            # case 1) : digit
            if c.isdigit():
                num = num*10 + int(c)
            # case 2) : "["
            elif c == "[":
                stack.append(string)
                stack.append(num)
                string = ''
                num = 0
            # case 3) : alphabet
            elif c.isalpha():
                string += c
            # case 4) "]"
            elif c == ']':
                pre_num = stack.pop()
                pre_string = stack.pop()
                string = pre_string + pre_num * string
        return string


# V0-1
# IDEA: STACK (GPT)
# time = O(n), n = length of decoded output string
# space = O(n)
class Solution(object):
    def decodeString(self, s):

        digit_st = []
        alpha_st = []

        current_str = ""
        current_num = 0

        for ch in s:

            # Build multi-digit number
            if ch.isdigit():
                current_num = current_num * 10 + int(ch)

            elif ch == "[":
                # Save current context
                digit_st.append(current_num)
                alpha_st.append(current_str)

                # Reset for nested section
                current_num = 0
                current_str = ""

            elif ch == "]":
                repeat = digit_st.pop()
                prev_str = alpha_st.pop()

                current_str = prev_str + current_str * repeat

            else:
                # Normal character
                current_str += ch

        return current_str


# V1
# https://leetcode.com/problems/decode-string/discuss/378711/Easy-understand-python-solution-88-beat
# IDEA : STACK 
# IDEA :
#  -> When meet '[' append the previous num and string to stack, 
#  -> when meet the ']' pop the previous num and string to calculate. 
#  -> Notice the num*10 + num is for some case like "100[leetcode]" that num is greater than 10.
# time = O(n), n = length of decoded output string
# space = O(n)
class Solution:
    def decodeString(self, s):
        num = 0
        string = ''
        stack = []
        for c in s:
            if c.isdigit():
                ### NOTICE HERE 
                # num = num*10 + int(c) is for the greater than 10 cases
                # e.g. 
                # input = 100 
                # -> when idx=0
                #  -> num=0*10 + 1
                # -> when idx=1
                #  -> num += 1*10 + 0
                # -> when idx=2
                #  -> num += 10*10 + 0 
                # -> so num = 100 
                num = num*10 + int(c)
            elif c == "[":
                # when c == "["
                # then cache string and num
                # and init string, num again
                stack.append(string)
                stack.append(num)
                string = ''
                num = 0
            elif c.isalpha():
                string += c
            elif c == ']':
                # when c == ']',
                # pop pre num, pre string 
                # do pre_string + pre_num and add it back to string  
                pre_num = stack.pop()
                pre_string = stack.pop()
                string = pre_string + pre_num * string
        return string

### Test case
s=Solution()
assert s.decodeString("3[a]2[bc]")=="aaabcbc"
assert s.decodeString("3[a2[c]]")=="accaccacc"
assert s.decodeString("2[abc]3[cd]ef")=="abcabccdcdcdef"
assert s.decodeString("")==""
assert s.decodeString("[]")==""
assert s.decodeString("a")=="a"
assert s.decodeString("abc")=="abc"
assert s.decodeString("[[]]")==""
assert s.decodeString("[[[]]]")==""
assert s.decodeString("[[a]]")==""
assert s.decodeString("[[ab]]")==""
assert s.decodeString("[[[a]]b]")==""

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79332894
# IDEA : STACK
# curstring = prestring + prenum * curstring 
# ord() is a way transforming string -> integer 
# e.g. 
# c = '3' -> ord(c) - ord('0') = 3 
# c= '9'  -> ord(c) - ord('0') = 9
# we can do that via int() as well. i.e. int('9') -> 9
# time = O(n), n = length of decoded output string
# space = O(n)
class Solution(object):
    def decodeString(self, s):
        """
        :type s: str
        :rtype: str
        """
        curnum = 0
        curstring = ''
        stack = []
        for char in s:
            if char == '[':
                stack.append(curstring)
                stack.append(curnum)
                curstring = ''
                curnum = 0
            elif char == ']':
                prenum = stack.pop()
                prestring = stack.pop()
                curstring = prestring + prenum * curstring
            elif char.isdigit():
                curnum = curnum * 10 + int(char)
            else:
                curstring += char
        return curstring

# V1''
# https://leetcode.com/problems/decode-string/discuss/163479/Python-short-and-simple-stack-solution-beats-100-with-explanation
# time = O(n), n = length of decoded output string
# space = O(n)
class Solution:
      def decodeString(self, s):
        stack = []
        for i in range(len(s)):
            if s[i] == "]":
                current = ''
                while stack:
                    val = stack.pop()
                    if val ==  "[":
                        break
                    current = val + current
                num = ''
                while stack and stack[-1].isdigit():
                    num = stack.pop() + num
                stack.append(int(num)*current)
            else:
                stack.append(s[i])
        return ''.join(stack)

# V1'''
# https://leetcode-cn.com/problems/decode-string/solution/decode-string-fu-zhu-zhan-fa-di-gui-fa-by-jyd/
# time = O(n), n = length of decoded output string
# space = O(n)
class Solution:
    def decodeString(self, s: str) -> str:
        stack, res, multi = [], "", 0
        for c in s:
            if c == '[':
                stack.append([multi, res])
                res, multi = "", 0
            elif c == ']':
                cur_multi, last_res = stack.pop()
                res = last_res + cur_multi * res
            elif '0' <= c <= '9':
                multi = multi * 10 + int(c)            
            else:
                res += c
        return res

# V1''''
# https://yq.aliyun.com/articles/714166
# time = O(n), n = length of decoded output string
# space = O(n)
class Solution:
    def decodeString(self, s: str) -> str:
        #init
        stack, res, num = [], '', 0
        for c in s:
            if c.isdigit():
                num = num * 10 + int(c)
            elif c.isalpha():
                res += c
            elif c == '[':
                # insert into stack as tuple (res, num )
                stack.append((res, num))
                # update res and num 
                res, num = '', 0
            else:
                # if c == ']', then pop var and repeat times
                last_str, this_num = stack.pop()
                res = last_str + this_num * res
        return res

# V1''''''
# http://bookshadow.com/weblog/2016/09/04/leetcode-decode-string/
# time = O(n), n = length of decoded output string
# space = O(n)
class Solution(object):
    def decodeString(self, s):
        """
        :type s: str
        :rtype: str
        """
        k = 1
        parts = collections.defaultdict(str)
        digits = collections.defaultdict(int)
        for c in s:
            if c.isdigit():
                digits[k] = digits[k] * 10 + int(c)
            elif c == '[':
                k += 1
            elif c == ']':
                parts[k - 1] += digits[k - 1] * parts[k]
                digits[k - 1] = 0
                parts[k] = ''
                k -= 1
            else:
                parts[k] += c
        return parts[1]

# V2
# time = O(n)
# space = O(n)
class Solution(object):
    def decodeString(self, s):
        """
        :type s: str
        :rtype: str
        """
        curr, nums, strs = [], [], []
        n = 0

        for c in s:
            if c.isdigit():
                n = n * 10 + ord(c) - ord('0')
            elif c == '[':
                nums.append(n)
                n = 0
                strs.append(curr)
                curr = []
            elif c == ']':
                strs[-1].extend(curr * nums.pop())
                curr = strs.pop()
            else:
                curr.append(c)

        return "".join(strs[-1]) if strs else "".join(curr)