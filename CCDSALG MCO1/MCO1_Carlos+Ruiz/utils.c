#include <math.h>
#include "utils.h"

/* This file contains utility functions for point geometry operations,
 * used in convex hull algorithms like Graham scan.
 */
 
/**
* Finds the anchor point (lowest Y, then leftmost X) in the array.
* Preconditions:
* - 'arr' must contain at least 'n' valid points.
* - 'n' must be >= 1.
* @param arr Array of points.
* @param n   Number of points.
* @return Index of the anchor point in the array.
*/

int find_anchor(Points arr[], int n) {
    int min_idx = 0;
    for (int i = 1; i < n; i++) {
        if (arr[i].y < arr[min_idx].y || (arr[i].y == arr[min_idx].y && arr[i].x < arr[min_idx].x)) {
            min_idx = i;
        }
    }
    return min_idx;
}


//For Bubble Sort
/**
* Determines the orientation of an ordered triplet (p, q, r).
* Preconditions:
* All three points must be valid.
* @param p	First point.
* @param q	Second point.
* @param r	Third point.
* @return 0 if collinear, 1 if clockwise, 2 if counterclockwise.
*/
int orientation(Points p, Points q, Points r) {
    double val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
    if (val == 0) return 0;
    return (val > 0) ? 1 : 2;
}

/**
* Calculates the squared Euclidean distance between two points.
* Preconditions: 
*	'a' and 'b' must be valid points.
* @param a 	First point.
* @param b 	Second point.
* @return Squared distance.
*/
double distance_sq(Points a, Points b) {
    return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
}


//For Quick Sort
/**
* Calculates the polar angle between pivot and another point.
* Preconditions:
* - 'pivot' and 'p' must be valid.
* @param pivot Reference point.
* @param p     Other point.
* @return Angle in radians.
*/
double polarAngle(Points pivot, Points p)
{ 
  return atan2(p.y - pivot.y, p.x - pivot.x);
}

/**
* Swaps two point structures.
* Preconditions:
* - 'a' and 'b' must be valid pointers.
* @param a Pointer to first point.
* @param b Pointer to second point.
*/
void swap(Points *a, Points *b) {
    Points temp = *a;
    *a = *b;
    *b = temp;
}

/**
* Partitions the array for quicksort based on polar angle.
* Preconditions:
* - 'arr' must contain valid points.
* - 'low' and 'high' must be valid indices within the array.
* @param arr   Array of points.
* @param low   Starting index.
* @param high  Ending index.
* @param pivot Anchor point for polar angle comparison.
* @return Index of the partition.
*/
int partition(Points arr[], int low, int high, Points pivot) {
    double pivot_angle = polarAngle(pivot, arr[high]);
    int i = low - 1;
    for (int j = low; j < high; j++) {
        if (polarAngle(pivot, arr[j]) <= pivot_angle) {
            i++;
            swap(&arr[i], &arr[j]);
        }
    }
    swap(&arr[i + 1], &arr[high]);
    return i + 1;
}
