#################################################################
# ALGORITHM DEMO : BUBBLE SORT 
#################################################################

# V0
# LC 215
def bubble_sort(nums):
    _len = len(nums)
    for i in range(_len - 1):
        # since the last n element (idx = n) already minimum/maximum in every sort iteration
        # -> we don't need to consider them again in next iteration
        # -> so that's why j is in range(_len - 1 - i)
        for j in range(_len - 1 - i):
            if nums[j] > nums[j + 1]:
                nums[j], nums[j + 1] = nums[j + 1], nums[j]
    return nums

# V0' 
# DEMO
# c = [9,1,2,3,0]
# output = bubble_sort(c)
# print (output)
# collection : [1, 9, 2, 3, 0]
# collection : [1, 2, 9, 3, 0]
# collection : [1, 2, 3, 9, 0]
# collection : [1, 2, 3, 0, 9]
# collection : [1, 2, 3, 0, 9]
# collection : [1, 2, 3, 0, 9]
# collection : [1, 2, 0, 3, 9]
# collection : [1, 2, 0, 3, 9]
# collection : [1, 0, 2, 3, 9]
# collection : [0, 1, 2, 3, 9]
def bubble_sort(collection):
    length = len(collection)
    for i in range(length-1):
        swapped = False      # if swapped == True, break the loop (means the array's already sorted)
        for j in range(length-1-i):
            if collection[j] > collection[j+1]:
                swapped = True
                collection[j], collection[j+1] = collection[j+1], collection[j]
        if not swapped: break  # Stop iteration if the collection is sorted.
    return collection

# V1 
# https://github.com/keon/algorithms/blob/master/algorithms/sort/bubble_sort.py
def bubble_sort(arr, simulation=False):
    def swap(i, j):
        arr[i], arr[j] = arr[j], arr[i]

    n = len(arr)
    swapped = True
    
    iteration = 0
    if simulation:
        print(("iteration",iteration,":",*arr))
    x = -1
    while swapped:
        swapped = False
        x = x + 1
        for i in range(1, n-x):
            if arr[i - 1] > arr[i]:
                swap(i - 1, i)
                swapped = True
                if simulation:
                    iteration = iteration + 1
                    print(("iteration",iteration,":",*arr))
                    
    return arr

# V2 
# https://github.com/TheAlgorithms/Python/blob/master/sorts/bubble_sort.py
def bubble_sort(collection):
    """Pure implementation of bubble sort algorithm in Python
    :param collection: some mutable ordered collection with heterogeneous
    comparable items inside
    :return: the same collection ordered by ascending
    Examples:
    >>> bubble_sort([0, 5, 3, 2, 2])
    [0, 2, 2, 3, 5]
    >>> bubble_sort([])
    []
    >>> bubble_sort([-2, -5, -45])
    [-45, -5, -2]
    
    >>> bubble_sort([-23,0,6,-4,34])
    [-23,-4,0,6,34]
    """
    length = len(collection)
    for i in range(length-1):
        swapped = False
        for j in range(length-1-i):
            if collection[j] > collection[j+1]:
                swapped = True
                collection[j], collection[j+1] = collection[j+1], collection[j]
        if not swapped: break  # Stop iteration if the collection is sorted.
    return collection


# if __name__ == '__main__':
#     try:
#         raw_input          # Python 2
#     except NameError:
#         raw_input = input  # Python 3
#     user_input = raw_input('Enter numbers separated by a comma:').strip()
#     unsorted = [int(item) for item in user_input.split(',')]
#     print(*bubble_sort(unsorted), sep=',')
