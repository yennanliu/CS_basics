# V1 : dev  


# V2 
# https://blog.csdn.net/zjucor/article/details/82721781
class Solution:
    def sumSubarrayMins(self, a):
        """
        :type A: List[int]
        :rtype: int
        """
        n=len(a)
        left,right=[0]*n,[0]*n
        
        st=[]
        for i,v in enumerate(a):
            while st and a[st[-1]]>v:
                idx=st.pop()
                right[idx]=i-idx
            st.append(i)
        while st:
            idx=st.pop()
            right[idx]=n-idx
        
        a=a[::-1]    
        st=[]
        for i,v in enumerate(a):
            while st and a[st[-1]]>=v:
                idx=st.pop()
                left[idx]=i-idx
            st.append(i)
        while st:
            idx=st.pop()
            left[idx]=n-idx
        left=left[::-1]
        
#        print(left,right)
        
        a=a[::-1]
        res=0
        mod=10**9+7
        for i in range(n):
#            print(left[i]*right[i], a[i])
            res+=left[i]*right[i]*a[i]
            res%=mod
        return res

# V3 
# Time:  O(n)
# Space: O(n)
import itertools
# Ascending stack solution
class Solution(object):
    def sumSubarrayMins(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        M = 10**9 + 7

        left, s1 = [0]*len(A), []
        for i in range(len(A)):
            count = 1
            while s1 and s1[-1][0] > A[i]:
                count += s1.pop()[1]
            left[i] = count
            s1.append([A[i], count])

        right, s2 = [0]*len(A), []
        for i in reversed(range(len(A))):
            count = 1
            while s2 and s2[-1][0] >= A[i]:
                count += s2.pop()[1]
            right[i] = count
            s2.append([A[i], count])

        return sum(a*l*r for a, l, r in zip(A, left, right)) % M
