# V0 

# V1 
# http://bookshadow.com/weblog/2018/05/06/leetcode-masking-personal-information/
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
# Time:  O(1)
# Space: O(1)
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