//---------------------------------------------------------------
// BUBBLE SORT
//---------------------------------------------------------------
//
// Repeatedly walk the array swapping adjacent out-of-order pairs, so
// the largest value "bubbles" to the end on each pass.
//
//   pass 1: [5, 1, 4, 2]  ->  [1, 4, 2, 5]   5 is now in place
//   pass 2: [1, 4, 2, 5]  ->  [1, 2, 4, 5]   4 is now in place
//   pass 3: [1, 2, 4, 5]  ->  no swaps       -> already sorted, stop
//
// TWO DETAILS THAT ARE EASY TO GET WRONG:
//   - the inner loop must stop at `length - 1 - i`, both to avoid
//     comparing against a slot past the end AND because the last i
//     values are already sorted -- re-scanning them is wasted work
//   - tracking whether a pass swapped anything gives the O(N) best
//     case; without it, an already-sorted array still costs O(N^2)
//
// Bubble sort is STABLE (equal values keep their relative order) and
// in-place, but it is the slowest of the classic sorts. It is here to
// be understood, not used.
//
// Time  : Best O(N) (sorted, with the early exit), Avg/Worst O(N^2)
// Space : O(1)

function bubbleSort(array) {
  const length = array.length;

  for (let i = 0; i < length - 1; i++) {
    let swapped = false;

    // `- 1 - i`: stay in bounds, and skip the sorted tail
    for (let j = 0; j < length - 1 - i; j++) {
      if (array[j] > array[j + 1]) {
        [array[j], array[j + 1]] = [array[j + 1], array[j]];
        swapped = true;
      }
    }

    if (!swapped) break; // a clean pass means the array is sorted
  }
  return array;
}

// demo
console.assert(
  bubbleSort([99, 44, 6, 2, 1, 5, 63, 87, 283, 4, 0]).join(',') === '0,1,2,4,5,6,44,63,87,99,283',
  'sorts a mixed array'
);
console.assert(bubbleSort([]).join(',') === '', 'empty array');
console.assert(bubbleSort([1]).join(',') === '1', 'single element');
console.assert(bubbleSort([1, 2, 3]).join(',') === '1,2,3', 'already sorted');
console.assert(bubbleSort([3, 2, 1]).join(',') === '1,2,3', 'reversed');
console.assert(bubbleSort([2, 1, 2, 1]).join(',') === '1,1,2,2', 'duplicates');
console.assert(bubbleSort([0, -3, 5, -1]).join(',') === '-3,-1,0,5', 'negatives');

console.log(bubbleSort([99, 44, 6, 2, 1, 5, 63, 87, 283, 4, 0]).join(' '));
console.log('Success.');

module.exports = { bubbleSort };
