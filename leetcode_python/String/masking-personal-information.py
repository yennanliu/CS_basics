"""

831. Masking Personal Information
Medium

You are given a personal information string s, representing either an email address or a phone number. Return the masked personal information using the below rules.

Email address:

An email address is:

A name consisting of at least two uppercase and lowercase English letters, followed by
The '@' symbol, followed by
The domain consisting of uppercase and lowercase English letters with a dot '.' somewhere in the middle (not the first or last character).

To mask an email:

The uppercase letters in the name and domain must be converted to lowercase letters.
The middle letters of the name (i.e., all but the first and last letters) must be replaced by 5 asterisks "*****".

Phone number:

A phone number is formatted as follows:

The phone number contains 10-13 digits.
The last 10 digits make up the local number.
The remaining 0-3 digits, in the beginning, make up the country code.
Separation characters from the set {'+', '-', '(', ')', ' '} separate the above digits in some way.

To mask a phone number:

Remove all separation characters.
The masked phone number should have the form:

"***-***-XXXX" if the country code has 0 digits.
"+*-***-***-XXXX" if the country code has 1 digit.
"+**-***-***-XXXX" if the country code has 2 digits.
"+***-***-***-XXXX" if the country code has 3 digits.

"XXXX" is the last 4 digits of the local number.

Example 1:

Input: s = "LeetCode@LeetCode.com"
Output: "l*****e@leetcode.com"
Explanation: s is an email address.
The name and domain are converted to lowercase, and the middle of the name is replaced by 5 asterisks.

Example 2:

Input: s = "AB@qq.com"
Output: "a*****b@qq.com"
Explanation: s is an email address.
The name and domain are converted to lowercase, and the middle of the name is replaced by 5 asterisks.
Note that even though "ab" is 2 characters, it still must have 5 asterisks in the middle.

Example 3:

Input: s = "1(234)567-890"
Output: "***-***-7890"
Explanation: s is a phone number.
There are 10 digits, so the local number is 10 digits and the country code is 0 digits.
Thus, the resulting masked number is "***-***-7890".

Constraints:

s is either a valid email or a phone number.
If s is an email:

8 <= s.length <= 40
s consists of uppercase and lowercase English letters and exactly one '@' symbol and '.' symbol.

If s is a phone number:

10 <= s.length <= 20
s consists of digits, spaces, and the symbols '(', ')', '-', and '+'.

"""

# V0 

# V1
# http://bookshadow.com/weblog/2018/05/06/leetcode-masking-personal-information/
# time = O(n), n = len(S)
# space = O(n)
class Solution(object):
    def maskPII(self, S):
        """
        :type S: str
        :rtype: str
        """
        # case 1 : email account. e.g. : xxx@gmail.com 
        if '@' in S:
            left, right = S.lower().split('@')
            return left[0] + '*****' + left[-1] + '@' + right
        # case 2 : phone number. e.g. : 1(234)567-890
        digits = re.sub('\D*', '', S)
        countryCode = len(digits) - 10
        return (countryCode and '+' + '*' * countryCode + '-' or '') + '***-***-' + digits[-4:]

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/80644199
# time = O(n), n = len(S)
# space = O(n)
class Solution(object):
    def convert_phone(self, phone):
        phone = phone.strip().replace(' ', '').replace('(', '').replace(')', '').replace('-', '').replace('+', '')
        if len(phone) == 10:
            return "***-***-" + phone[-4:]
        else:
            return "+" + '*' * (len(phone) - 10) + "-***-***-" + phone[-4:]

    def convert_email(self, email):
        email = email.lower()
        first_name, host = email.split('@')
        return first_name[0] + '*****' + first_name[-1] + '@' + host

    def maskPII(self, S):
        """
        :type S: str
        :rtype: str
        """
        return self.convert_email(S) if '@' in S else self.convert_phone(S)
        
# V2
# time = O(n), n = len(S)
# space = O(n)
class Solution(object):
    def maskPII(self, S):
        """
        :type S: str
        :rtype: str
        """
        if '@' in S:
            first, after = S.split('@')
            return "{}*****{}@{}".format(first[0], first[-1], after).lower()

        digits = filter(lambda x: x.isdigit(), S)
        local = "***-***-{}".format(digits[-4:])
        if len(digits) == 10:
            return local
        return "+{}-{}".format('*' * (len(digits) - 10), local)