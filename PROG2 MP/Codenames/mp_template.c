<<<<<<< HEAD
/******************************************************************************
 *  Description     : <short description of the project>
 *  Author/s        : <student1 full name (last name, first name)>
 *                    <student2 full name (last name, first name)>
 *  Section         : <your section>
 *  Last Modified   : <date when last revision was made>
 *  Acknowledgments : <list of references used in the making of this project>
 ******************************************************************************/

/* standard preprocessor directives */
#include <stdio.h>

/* custom preprocessor directives */
#include "defs.h"
#include "helpers_1.c"
#include "helpers_2.c"

#define CONSTANT_VALUE 10

/**
 * Represents a 2D point
 */
typedef struct coordinate
{
  int x; // The x-coordinate of a point
  int y; // The y-coordinate of a point
} Coordinate;

/* other definitions (i.e., constants, typedefs, structs) */

/**
 *  Computes the average of the non-negative numbers from a given list of numbers
 *  @param arr The starting address of the array containing the list of numbers
 *  @param arrSize The size of the array
 *  @return The average of the non-negative numbers from the list of numbers
 *  @pre arr can include positive numbers, negative numbers, and zeros
 *  @pre arrSize is the correct size of arr
 */
float getAverage(int arr[], int arrSize)
{
  float average;
  int sum = 0, n = 0, i;

  for (i = 0; i < arrSize; i++)
    if (arr[i] >= 0)
    {
      sum += arr[i];
      n++;
    }

  if (n == 0) // prevent divide by zero
    average = 0;
  else
    average = sum / (float)n;

  return average;
}

/* other function implementations */

int main()
{
  /* your project code */

  // Sample call of a function from helpers_1.c, wherein its parameter is a value
  // evaluated from a constant within this file and from another file
  fnInHelpers1(PI * CONSTANT_VALUE);

  // Sample call of a function from helpers_2.c
  fnInHelpers2();

  return 0;
}

/**
 * This is to certify that this project is my/our own work, based on my/our personal
 * efforts in studying and applying the concepts learned. I/We have constructed the
 * functions and their respective algorithms and corresponding code by myself/ourselves.
 * The program was run, tested, and debugged by my/our own efforts. I/We further certify
 * that I/we have not copied in part or whole or otherwise plagiarized the work of
 * other students and/or persons.
 *
 * <student1 full name (last name, first name)> (DLSU ID# <number>)
 * <student2 full name (last name, first name)> (DLSU ID# <number>)
 */
=======
/******************************************************************************
 *  Description     : <short description of the project>
 *  Author/s        : <student1 full name (last name, first name)>
 *                    <student2 full name (last name, first name)>
 *  Section         : <your section>
 *  Last Modified   : <date when last revision was made>
 *  Acknowledgments : <list of references used in the making of this project>
 ******************************************************************************/

/* standard preprocessor directives */
#include <stdio.h>

/* custom preprocessor directives */
#include "defs.h"
#include "helpers_1.c"
#include "helpers_2.c"

#define CONSTANT_VALUE 10

/**
 * Represents a 2D point
 */
typedef struct coordinate
{
  int x; // The x-coordinate of a point
  int y; // The y-coordinate of a point
} Coordinate;

/* other definitions (i.e., constants, typedefs, structs) */

/**
 *  Computes the average of the non-negative numbers from a given list of numbers
 *  @param arr The starting address of the array containing the list of numbers
 *  @param arrSize The size of the array
 *  @return The average of the non-negative numbers from the list of numbers
 *  @pre arr can include positive numbers, negative numbers, and zeros
 *  @pre arrSize is the correct size of arr
 */
float getAverage(int arr[], int arrSize)
{
  float average;
  int sum = 0, n = 0, i;

  for (i = 0; i < arrSize; i++)
    if (arr[i] >= 0)
    {
      sum += arr[i];
      n++;
    }

  if (n == 0) // prevent divide by zero
    average = 0;
  else
    average = sum / (float)n;

  return average;
}

/* other function implementations */

int main()
{
  /* your project code */

  // Sample call of a function from helpers_1.c, wherein its parameter is a value
  // evaluated from a constant within this file and from another file
  fnInHelpers1(PI * CONSTANT_VALUE);

  // Sample call of a function from helpers_2.c
  fnInHelpers2();

  return 0;
}

/**
 * This is to certify that this project is my/our own work, based on my/our personal
 * efforts in studying and applying the concepts learned. I/We have constructed the
 * functions and their respective algorithms and corresponding code by myself/ourselves.
 * The program was run, tested, and debugged by my/our own efforts. I/We further certify
 * that I/we have not copied in part or whole or otherwise plagiarized the work of
 * other students and/or persons.
 *
 * <student1 full name (last name, first name)> (DLSU ID# <number>)
 * <student2 full name (last name, first name)> (DLSU ID# <number>)
 */
>>>>>>> 97eb56f353ee26a616327fd05c3c41000ae5bc0f
