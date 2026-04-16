/******************************************************************************
 *  Description     : <short description of the file>
 *  Author/s        : <student1 full name (last name, first name)>
 *                    <student2 full name (last name, first name)>
 *  Section         : <your section>
 *  Last Modified   : <date when last revision was made>
 ******************************************************************************/

#ifndef HELPERS_2_C // Include this to prevent redefinition error
#define HELPERS_2_C // Include this to prevent redefinition error

#include <stdio.h>
#include "defs.h"

/**
 * For demonstrating another function belonging to a separate file
 */
void fnInHelpers2()
{
  printf("This function is from helpers_2.c\n");
  printf("This function includes defs.h, thus has access to its contents\n");
  printf("A constant within defs.h: %.4f\n\n", PI);
}

#endif // HELPERS_2_C; Include this to prevent redefinition error