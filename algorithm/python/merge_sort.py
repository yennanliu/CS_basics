#---------------------------------------------------------------
# Merge sort
#---------------------------------------------------------------

# https://www.journaldev.com/31541/merge-sort-algorithm-java-c-python#:~:text=The%20time%20complexity%20of%20MergeSort,time%20to%20merge%20two%20halves.
# Merge sort is one of the most efficient sorting algorithms. 
# It works on the principle of Divide and Conquer based on the 
# idea of breaking down a list into several sub-lists until each 
# sublist consists of a single element and merging those sublists in a manner 
# that results into a sorted list.


# V0 
# https://www.geeksforgeeks.org/merge-sort/
# IDEA : MERGE SORT 
# STEPS : 
# 1) SPLIT THE ARRAY INTO 2 EQUAL SUB ARRAYS
# 2) SORT THEM
# 3) LOOP ABOVE 2 SUB ARRAYS, AND APPEND THE ELEMENTS FROM SMALLEST TO BIGGEST INTO A NEW ARRAY WHICH IS SORTED
def mergeSort(arr): 
    if len(arr) >1: 
        mid = len(arr)//2 #Finding the mid of the array 
        L = arr[:mid] # Dividing the array elements  
        R = arr[mid:] # into 2 halves 
  
        ### NOTE THIS (recursive)  
        mergeSort(L) # Sorting the first half 
        mergeSort(R) # Sorting the second half 
  
        i = j = k = 0
          
        # copy data to temp arrays L[] and R[] 
        while i < len(L) and j < len(R): 
            if L[i] < R[j]: 
                arr[k] = L[i] 
                i+=1
            else: 
                arr[k] = R[j] 
                j+=1
            ### NOTE THIS
            k+=1
          
        # check if any remaining element
        while i < len(L): 
            arr[k] = L[i] 
            i+=1
            k+=1
            
        while j < len(R): 
            arr[k] = R[j] 
            j+=1
            k+=1

# V1
# https://www.journaldev.com/31541/merge-sort-algorithm-java-c-python#:~:text=The%20time%20complexity%20of%20MergeSort,time%20to%20merge%20two%20halves.
def merge_sort(unsorted_array):
    if len(unsorted_array) > 1:
        mid = len(unsorted_array) // 2  # Finding the mid of the array
        left = unsorted_array[:mid]  # Dividing the array elements
        right = unsorted_array[mid:]  # into 2 halves

        merge_sort(left)
        merge_sort(right)

        i = j = k = 0

        #  data to temp arrays L[] and R[]
        while i < len(left) and j < len(right):
            if left[i] < right[j]:
                unsorted_array[k] = left[i]
                i += 1
            else:
                unsorted_array[k] = right[j]
                j += 1
            k += 1

        # Checking if any element was left
        while i < len(left):
            unsorted_array[k] = left[i]
            i += 1
            k += 1

        while j < len(right):
            unsorted_array[k] = right[j]
            j += 1
            k += 1


# Code to print the list
def print_list(array1):
    for i in range(len(array1)):
        print(array1[i], end=" ")
    print()


# driver code to test the above code
# if __name__ == '__main__':
#     array = [12, 11, 13, 5, 6, 7]
#     print("Given array is", end="\n")
#     print_list(array)
#     merge_sort(array)
#     print("Sorted array is: ", end="\n")
#     print_list(array)

# V1 
# https://github.com/TheAlgorithms/Python/blob/master/sorts/merge_sort.py
"""
This is a pure python implementation of the merge sort algorithm
For doctests run following command:
python -m doctest -v merge_sort.py
or
python3 -m doctest -v merge_sort.py
For manual testing run:
python merge_sort.py
"""
def merge_sort(collection):
    """
    Pure implementation of the merge sort algorithm in Python
    :param collection: some mutable ordered collection with heterogeneous
    comparable items inside
    :return: the same collection ordered by ascending
    Examples:
    >>> merge_sort([0, 5, 3, 2, 2])
    [0, 2, 2, 3, 5]
    >>> merge_sort([])
    []
    >>> merge_sort([-2, -5, -45])
    [-45, -5, -2]
    """
    length = len(collection)
    if length > 1:
        midpoint = length // 2
        left_half = merge_sort(collection[:midpoint])
        right_half = merge_sort(collection[midpoint:])
        i = 0
        j = 0
        k = 0
        left_length = len(left_half)
        right_length = len(right_half)
        while i < left_length and j < right_length:
            if left_half[i] < right_half[j]:
                collection[k] = left_half[i]
                i += 1
            else:
                collection[k] = right_half[j]
                j += 1
            k += 1

        while i < left_length:
            collection[k] = left_half[i]
            i += 1
            k += 1

        while j < right_length:
            collection[k] = right_half[j]
            j += 1
            k += 1

    return collection

# if __name__ == '__main__':
#     try:
#         raw_input          # Python 2
#     except NameError:
#         raw_input = input  # Python 3
#     user_input = raw_input('Enter numbers separated by a comma:\n').strip()
#     unsorted = [int(item) for item in user_input.split(',')]
#     print(merge_sort(unsorted))