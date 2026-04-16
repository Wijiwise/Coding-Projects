<<<<<<< HEAD
 /** 
 *  Description     : The goal of this machine project is to simulate a game of "Codename"
                      put 2 teams against each other 
 *  Author/s        : Carlos, Luis Guiller Alberto M.
 *  Section         : S23
 *  Last Modified   : 7/29/2025
 *  Acknowledgments : ChatGPT   was   used   mainly   for   streamlining   and/or   sentence   construction.   The   instructor   validated   the 
                      AI-generated output and modified it as needed.
 */ 
 #include <stdio.h>
 #include <string.h>
 #include <stdlib.h>
 #include <time.h>
 #include "helper.h"
 #include "helper.c"

 int main() 
 { 
  PlayerTag players[50]; //List of all players
  int count = 0;//Player count
  

  int nChoice = 0;
  do
  {
    if (nChoice == 0) {
        nChoice = MainMenu(); 
    }
    switch(nChoice)
    {
      case 1:
      Gameplay(players, &count);
      nChoice = 0;
      break;

      case 2:
      TopScore(players, count);
      nChoice = 0;
      break;

      case 3:
      printf("Terminating Program\n");
      break;

      default:
      printf("INVALID CHOICE!\n");
      nChoice = 0;

    }
    
  }while(nChoice != 3);

 return  0; 
 } 
 
 /** 
 * This is to certify that this project is my/our own work, based on my/our personal 
 * efforts in studying and applying the concepts learned. I/We have constructed the 
 * functions and their respective algorithms and corresponding code by myself/ourselves. 
 * The program was run, tested, and debugged by my/our own efforts. I/We further certify 
 * that I have not copied in part or whole or otherwise plagiarized the work of 
 * other students and/or persons. 
 v0.1.0  7 
* 
* Carlos, Luis Guiller Alberto M.(12305162) 
=======
 /** 
 *  Description     : The goal of this machine project is to simulate a game of "Codename"
                      put 2 teams against each other 
 *  Author/s        : Carlos, Luis Guiller Alberto M.
 *  Section         : S23
 *  Last Modified   : 7/29/2025
 *  Acknowledgments : ChatGPT   was   used   mainly   for   streamlining   and/or   sentence   construction.   The   instructor   validated   the 
                      AI-generated output and modified it as needed.
 */ 
 #include <stdio.h>
 #include <string.h>
 #include <stdlib.h>
 #include <time.h>
 #include "helper.h"
 #include "helper.c"

 int main() 
 { 
  PlayerTag players[50]; //List of all players
  int count = 0;//Player count
  

  int nChoice = 0;
  do
  {
    if (nChoice == 0) {
        nChoice = MainMenu(); 
    }
    switch(nChoice)
    {
      case 1:
      Gameplay(players, &count);
      nChoice = 0;
      break;

      case 2:
      TopScore(players, count);
      nChoice = 0;
      break;

      case 3:
      printf("Terminating Program\n");
      break;

      default:
      printf("INVALID CHOICE!\n");
      nChoice = 0;

    }
    
  }while(nChoice != 3);

 return  0; 
 } 
 
 /** 
 * This is to certify that this project is my/our own work, based on my/our personal 
 * efforts in studying and applying the concepts learned. I/We have constructed the 
 * functions and their respective algorithms and corresponding code by myself/ourselves. 
 * The program was run, tested, and debugged by my/our own efforts. I/We further certify 
 * that I have not copied in part or whole or otherwise plagiarized the work of 
 * other students and/or persons. 
 v0.1.0  7 
* 
* Carlos, Luis Guiller Alberto M.(12305162) 
>>>>>>> 97eb56f353ee26a616327fd05c3c41000ae5bc0f
*/ 