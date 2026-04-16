#include <stdio.h>
#include <time.h>
#include <math.h>
#include "graham2.h"
#include "stack.h"
#include "qsort.h"

/*
1.Identify the pivot, find the lowest y value, 
  if equal y values, find the smallest x value.

2. Set the polar angles, calculate the angle of the remaining points 
   sort into ascending order, if there are equal angles, 
   chose the farthest one and discard the other

3. Initialize the Stack, Push the pivot and the 
   first two sorted points onto the stack
4. Process remaining points
Use: (xTop−xNext_to_Top)(yPi−yNext_to_Top)−(yTop−yNext_to_Top)(xPi−xNext_to_Top)
 if negative(right turn) pop from stack and repeat
 Push to stack once a left turn is found

5. Finalize Stack
*/
extern double polarAngle(Points pivot, Points p);
extern void quick_sort(Points arr[], int n, Points pivot);

int find_anchor(Points points[], int n)
{
   int min = 0;
   int i; 

   for(i = 1; i < n; i++)
   {
      if(points[i].y < points[min].y || (points[i].y == points[min].y && points[i].x < points[min].x))
      {
        min = i;
      }
   }

   return min;
}

int orientation(Points a, Points b, Points c)
{   
    int flag;
    double val = (b.y - a.y) * (c.x - b.x) - (b.x - a.x) * (c.y - b.y);
    if(fabs(val) < 1e-9)
    {
        flag = 0; //colinear
    }
    else if(val>0)
    {
        flag = 1;//clockwise
    }
    else
    {
        flag = 2;//counterclockwise
    }
    return flag;
}

void graham_scan(Points point[], int n, FILE *output)
{
   if(n<3)
   {
     printf("\nToo few points to create a convex hull\n");
     return;
   }
   //Starts the clock
   clock_t start = clock();

   //Step1 Find the lowest point
   int anchor = find_anchor(point, n);

   //Step 2 swap values
   Points temp = point[0];
   point[0] = point[anchor];
   point[anchor] = temp;

   //Step 3 Quicksort by polar angle
   quick_sort(point + 1, n-1, point[0]);

   //Step 4 Initialize the stack
   Stack s;
   create(&s);
   push(&s, point[0]);
   push(&s, point[1]);
   push(&s, point[2]);

   //Step 5 
  for (int i = 3; i < n; i++) {
    Points top_elem, next_elem;
    pop(&s, &top_elem);
    top(&s, &next_elem);
    
    while (s.top >= 1 && orientation(next_elem, top_elem, point[i]) != 2) {
        top_elem = next_elem;
        pop(&s, &top_elem);
        if (s.top >= 1) 
        {
          top(&s, &next_elem);
        } else {
            break;
        }
        
    }

    push(&s, top_elem);       // restore top_elem
    push(&s, point[i]);       // push the current point
}
   //Step 6 printing

   fprintf(output, "\nConvex Hull\n");
   for(int j = 0; j<= s.top; j++)
   {
       fprintf(output, "%.6f  %.6f\n", s.data[j].x, s.data[j].y);
   }

   //Stop the clock
   clock_t end = clock();
   double elapsed_time_ms = (double)(end - start);
   fprintf(output, "Time Elapsed: %.6f", elapsed_time_ms);
}