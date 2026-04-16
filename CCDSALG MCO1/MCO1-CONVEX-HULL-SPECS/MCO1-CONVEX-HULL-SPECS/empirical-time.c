/*
   empirical-time.c

   Sample program for determining the empirical execution time. Note that the actual numerical results will depend on the platform,
   i.e., the hardware, the compiler, the operating system.
*/

#include <time.h>    // this is needed for calling the clock()
#include <stdio.h>

#define STEP  (1024)
#define LIMIT (32768)

// NOTE: 2^10 = 1024 for the STEP
//       2^15 = 32768 for the LIMIT


//
// this is a sample algo, it is O(n^2) as it contains a loop within a loop
//
// parameter n is the problem (or input) size
void algo(long int n)
{
  long long ctr;     
  clock_t the_start; // start time 
  clock_t the_end;   // end time

  the_start = clock();  // record the start time 

  ctr = 0; 
  for (long int i = 1; i <= n; ++i) {    // outer loop: this is O(n)
      ctr = 0;                           // this is O(n)
      for (long int j = 1; j <= n; j++)  // inner loop: this is O(n^2) as affected by the outer loop
          ctr++;                         // this is O(n^2) 
  }
  
  the_end = clock();  // record the end time

  // prints the input size n and the corresponding elapsed execution time in ms (millisecond)
  printf("%6ld %15lf\n", n, (double)(the_end - the_start));  
}

int main() {
  long int n;        // n is the problem or input size

  // generates the problem size n, each n is a power of 2  
  for (n = 0; n <= LIMIT; n += STEP) {  
     algo(n);
  }  

  return 0;
}
