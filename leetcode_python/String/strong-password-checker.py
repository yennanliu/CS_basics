"""

420. Strong Password Checker
Hard

A password is considered strong if the below conditions are all met:

- It has at least 6 characters and at most 20 characters.
- It contains at least one lowercase letter, at least one uppercase letter, and
  at least one digit.
- It does not contain three repeating characters in a row (i.e., "Baaabb0" is
  weak, but "Baabaa0" is strong).

Given a string password, return the minimum number of steps required to make
password strong. if password is already strong, return 0.

In one step, you can:

- Insert one character to password,
- Delete one character from password, or
- Replace one character of password with another character.

Example 1:

Input: password = "a"
Output: 5

Example 2:

Input: password = "aA1"
Output: 3

Example 3:

Input: password = "1337C0d3"
Output: 0

Constraints:

1 <= password.length <= 50
password consists of letters, digits, dot '.' or exclamation mark '!'.

"""

# V0
# IDEA : GREEDY (3 cases on length)
#
#  A run of L equal chars needs L // 3 replacements to be broken up.
#
#  case 1) len < 6   : we must insert (6 - len) chars; each insertion can also
#                      supply a missing char type AND break a run
#                      -> max(6 - len, missing_types)
#
#  case 2) 6 <= len <= 20 : only replacements; each replacement can also supply a
#                      missing type -> max(sum(L // 3), missing_types)
#
#  case 3) len > 20  : we MUST delete (len - 20) chars. Spend them where they
#                      save the most replacements:
#                        - run with L % 3 == 0 : 1 deletion saves 1 replacement
#                        - run with L % 3 == 1 : 2 deletions save 1 replacement
#                        - any other run       : 3 deletions save 1 replacement
#                      -> delete + max(remaining_replacements, missing_types)
#
# time = O(n)
# space = O(n)
class Solution(object):
    def strongPasswordChecker(self, password):
        n = len(password)

        has_lower = has_upper = has_digit = 0
        for ch in password:
            if ch.islower():
                has_lower = 1
            elif ch.isupper():
                has_upper = 1
            elif ch.isdigit():
                has_digit = 1
        missing = 3 - (has_lower + has_upper + has_digit)

        # lengths of the runs of >= 3 identical chars
        runs = []
        i = 0
        while i < n:
            j = i
            while j < n and password[j] == password[i]:
                j += 1
            if j - i >= 3:
                runs.append(j - i)
            i = j

        if n < 6:
            return max(6 - n, missing)

        if n <= 20:
            replace = sum(r // 3 for r in runs)
            return max(replace, missing)

        # n > 20
        delete = n - 20
        left = delete

        # 1 deletion kills one replacement on a run with length % 3 == 0
        for idx in range(len(runs)):
            if left <= 0:
                break
            if runs[idx] % 3 == 0:
                runs[idx] -= 1
                left -= 1

        # 2 deletions kill one replacement on a run with length % 3 == 1
        for idx in range(len(runs)):
            if left <= 1:
                break
            if runs[idx] >= 4 and runs[idx] % 3 == 1:
                runs[idx] -= 2
                left -= 2

        # 3 deletions kill one replacement on any run that is still >= 3
        for idx in range(len(runs)):
            if left <= 0:
                break
            if runs[idx] >= 3:
                d = min(left, runs[idx] - 2)
                runs[idx] -= d
                left -= d

        replace = sum(r // 3 for r in runs)
        return delete + max(replace, missing)
