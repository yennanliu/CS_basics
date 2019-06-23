
# V1 : DEV 
# class Solution(object):
#     def pancakeSort(self, A):
#         # Slide spatula under pancake at index and flip to top                                              
#         def flip(stack, index):
#             newStack = stack[:(index + 1)]
#             newStack.reverse()
#             newStack += stack[(index + 1):]
#             return newStack

#         result = []
#         for n in reversed(range(1, len(A)+1)):
#             i = A.index(n)
#             flip(A, i+1)
#             result.append(i+1)
#             flip(A, n)
#             result.append(n)
#         return result

# V1'
# class Solution(object):
#     def pancakeSort(self, A):
#         """
#         :type A: List[int]
#         :rtype: List[int]
#         """
#         N = len(A)
#         stack = []
#         for i in A:
#             stack = stack[:i][::-1] + stack[i:]
#         return stack 





# V2 
# http://blog.jobbole.com/74263/

# Sorts Pancakes                                                                                    
def sortPancakes(stack):
 
    sorted_index = 10
    for i in reversed(list(range(len(stack)))):
        stack = flip(stack, findLargestPancake(stack, i))
        print(("Flip Up", stack))
        stack = flip(stack, i)
        print(("Flip Down", stack))
    return stack
 
# All of the pancakes are sorted after index                                                        
# Returns the index of largest unsorted pancake                                                     
def findLargestPancake(stack, index):
 
    largest_pancake = stack[index]
    largest_index = index;
 
    for i in range(index):
        if stack[i] > largest_pancake:
            largest_pancake = stack[i]
            largest_index = i
 
    print("")
    print(("Insert Spatula in index", largest_index, "Size", largest_pancake))
    return largest_index
 
# Slide spatula under pancake at index and flip to top                                              
def flip(stack, index):
    newStack = stack[:(index + 1)]
    newStack.reverse()
    newStack += stack[(index + 1):]
    return newStack



# V3 
# https://blog.csdn.net/fuxuemingzhu/article/details/85937314
class Solution(object):
    def pancakeSort(self, A):
        """
        :type A: List[int]
        :rtype: List[int]
        """
        N = len(A)
        res = []
        for x in range(N, 0, -1):
            i = A.index(x)
            res.extend([i + 1, x])
            A = A[:i:-1] + A[:i]
        return res

# V4
# Time:  O(n^2)
# Space: O(1)

class Solution(object):
    def pancakeSort(self, A):
        """
        :type A: List[int]
        :rtype: List[int]
        """
        def reverse(l, begin, end):
            for i in range((end-begin) // 2):
                l[begin+i], l[end-1-i] = l[end-1-i], l[begin+i]

        result = []
        for n in reversed(list(range(1, len(A)+1))):
            i = A.index(n)
            reverse(A, 0, i+1)
            result.append(i+1)
            reverse(A, 0, n)
            result.append(n)
        return result