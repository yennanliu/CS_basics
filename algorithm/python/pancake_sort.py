#################################################################
# ALGORITHM DEMO : PANCAKE SORT
#################################################################

# V0
# LC 969. Pancake Sorting
# IDEA : pankcake sort + while loop
# IDEA : 3 STEPS
#   -> step 1) Find the maximum number in arr
#   -> step 2) Reverse from 0 to max_idx
#   -> step 3) Reverse whole list
# https://github.com/yennanliu/CS_basics/blob/master/algorithm/python/pancake_sort.py
class Solution(object):
    def pancakeSort(self, arr):
        """Sort Array with Pancake Sort.
        :param arr: Collection containing comparable items
        :return: Collection ordered in ascending order of items
        Examples:
        >>> pancake_sort([0, 5, 3, 2, 2])
        [0, 2, 2, 3, 5]
        >>> pancake_sort([])
        []
        >>> pancake_sort([-2, -5, -45])
        [-45, -5, -2]
        """
        cur = len(arr)
        res = []
        while cur > 1:
            # step 1) Find the maximum number in arr
            max_idx = arr.index(max(arr[0:cur]))
            res = res + [max_idx+1, cur] # idx is 1 based
            # step 2) Reverse from 0 to max_idx
            #arr = arr[max_idx::-1] + arr[max_idx + 1 : len(arr)] # this is OK as well
            arr = arr[:max_idx][::-1] + arr[max_idx + 1 : len(arr)]
            # step 3) Reverse whole list
            #arr = arr[cur - 1 :: -1] + arr[cur : len(arr)] # this is OK as well
            arr = arr[:cur - 1][::-1] + arr[cur : len(arr)]
            cur -= 1
        print ("arr = " + str(arr))
        return res

# V1 
# Pancake sort algorithm 
# Only can reverse array from 0 to i
# https://github.com/TheAlgorithms/Python/blob/master/sorts/pancake_sort.py
def pancakesort(arr):
    cur = len(arr)
    while cur > 1:
        # Find the maximum number in arr
        mi = arr.index(max(arr[0:cur]))
        # Reverse from 0 to mi 
        arr = arr[mi::-1] + arr[mi+1:len(arr)]
        # Reverse whole list 
        arr = arr[cur-1::-1] + arr[cur:len(arr)]
        cur -= 1
    return arr