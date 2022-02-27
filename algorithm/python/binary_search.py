#---------------------------------------------------------------
#  BINARY SEARCH 
#---------------------------------------------------------------

# V0  

# V1 
# http://kuanghy.github.io/2016/06/14/python-bisect
# Binary search via recursion 
def binary_search_recursion(lst, value, low, high):
    if high < low:
        return None
    # better to use mid = low + (high - low) / 2 then mid = (low + high) / 2
    # since "low + high" may cause "stack overflow" issue when low, high are some big number
    mid = low + (high - low) / 2
    if lst[mid] > value:
        return binary_search_recursion(lst, value, low, mid-1)
    elif lst[mid] < value:
        return binary_search_recursion(lst, value, mid+1, high)
    else:
        return mid

# Binary search via for loop 
def binary_search_loop(lst,value):
    low, high = 0, len(lst)-1
    while low <= high:
        mid = (low + high) / 2
        if lst[mid] < value:
            low = mid + 1
        elif lst[mid] > value:
            high = mid - 1
        else:
            return mid
    return None

# V2 
# https://github.com/yennanliu/algorithms/blob/master/algorithms/search/binary_search.py
# Binary search via for loop
def binary_search(array, query):
    lo, hi = 0, len(array) - 1
    while lo <= hi:
        mid = (hi + lo) // 2
        val = array[mid]
        if val == query:
            return mid
        elif val < query:
            lo = mid + 1
        else:
            hi = mid - 1
    return None

#  Binary search via recursion
def binary_search_recur(array, low, high, val):
    if low > high:       # error case
        return -1
    mid = (low + high) // 2
    if val < array[mid]:
        return binary_search_recur(array, low, mid - 1, val)
    elif val > array[mid]:
        return binary_search_recur(array, mid + 1, high, val)
    else:
        return mid