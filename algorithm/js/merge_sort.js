//---------------------------------------------------------------
// MERGE SORT
//---------------------------------------------------------------
//
// Divide and conquer:
//   1) SPLIT the array in half
//   2) sort each half recursively
//   3) MERGE the two sorted halves into one
//
//              [99, 44, 6, 2, 1]
//             /                 \
//        [99, 44]            [6, 2, 1]
//        /      \            /       \
//     [99]     [44]        [6]     [2, 1]
//        \      /            \      /   \
//        [44, 99]             \   [2]   [1]
//              \               \    \   /
//               \             [1, 2, 6]
//                \            /
//              [1, 2, 6, 44, 99]
//
// The merge is the whole algorithm: two sorted lists combine in linear
// time by repeatedly taking the smaller of the two front values. There
// are log N levels of splitting and each level merges N values, hence
// O(N log N) -- and unlike quicksort that holds in the WORST case too.
//
// Merge sort is STABLE, which is why it is the standard choice for
// sorting objects by a key. The price is O(N) scratch space, which is
// also why quicksort usually wins on raw arrays.
//
// Time  : O(N log N) in all cases
// Space : O(N)

function mergeSort(array) {
  // base case: 0 or 1 element is already sorted
  if (array.length <= 1) return array;

  const middle = Math.floor(array.length / 2);
  const left = mergeSort(array.slice(0, middle));
  const right = mergeSort(array.slice(middle));

  return merge(left, right);
}

// Combine two SORTED arrays into one sorted array, in O(n + m).
function merge(left, right) {
  const result = [];
  let i = 0;
  let j = 0;

  while (i < left.length && j < right.length) {
    // `<=` not `<`: on a tie take from the LEFT, which is what makes
    // the sort stable
    if (left[i] <= right[j]) result.push(left[i++]);
    else result.push(right[j++]);
  }

  // exactly one side still has values; append whatever is left
  return result.concat(left.slice(i)).concat(right.slice(j));
}

// demo
console.assert(
  mergeSort([99, 44, 6, 2, 1, 5, 63, 87, 283, 4, 0]).join(',') === '0,1,2,4,5,6,44,63,87,99,283',
  'sorts a mixed array'
);
console.assert(mergeSort([]).join(',') === '', 'empty array');
console.assert(mergeSort([1]).join(',') === '1', 'single element');
console.assert(mergeSort([1, 2, 3]).join(',') === '1,2,3', 'already sorted');
console.assert(mergeSort([3, 2, 1]).join(',') === '1,2,3', 'reversed');
console.assert(mergeSort([2, 1, 2, 1]).join(',') === '1,1,2,2', 'duplicates');
console.assert(mergeSort([0, -3, 5, -1]).join(',') === '-3,-1,0,5', 'negatives');

console.assert(merge([1, 4, 7], [2, 5]).join(',') === '1,2,4,5,7', 'merge two sorted arrays');
console.assert(merge([], [1, 2]).join(',') === '1,2', 'merge with an empty side');

// mergeSort returns a NEW array -- the input is left untouched
const input = [3, 1, 2];
mergeSort(input);
console.assert(input.join(',') === '3,1,2', 'input is not mutated');

console.log(mergeSort([99, 44, 6, 2, 1, 5, 63, 87, 283, 4, 0]).join(' '));
console.log('Success.');

module.exports = { mergeSort, merge };
