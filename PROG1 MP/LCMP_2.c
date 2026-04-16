<<<<<<< HEAD
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
/*
Description: The program is a welcome display that shows
shows the options Play(To start the game), Score (show top5 scores), Quit(End Program)
Full Game Description: Each level a certain number of aliens will appear,
                       the players' job is to calculate how many hits it will take to kill them
                       by multiplying the number of aliens by the numerical value of each alien.

Programmed by: <Luis Carlos> <S15B>
Last modified: 11/24/24
Version: <1.0135>
[Acknowledgements: <list of sites or borrowed libraries and sources>]
*/

// Function Prototypes

//Will handle all the game from level generation to keeping track of the score
float nGameplay(int nlevel ,int nlives, int nscore ,float fFACEH , float fCHESTB, float fXENO, float fQUEEN); 

void dGuide();//Displays a guide to show the design and number of lives the aliens have.
void dHeader();//To be able to display "SPACE INVADERS" on all levels
void dGoodBye();//When player chooses to quit/end the program
void dDivider();//A line to separate actions
void clearScreen();//Will clear the screen for the "Play again" feature

//Will display the top 5 highscores and the corresponding IDs
void dHighScores(int nH1, int nID1, int nH2, int nID2, int nH3,int nID3, int nH4 ,int nID4,int nH5, int nID5);
//Will sort the scores and IDs as they are updated
void iHighscores(int *nH1, int *nID1, int *nH2, int *nID2, int *nH3,int *nID3, int *nH4 ,int *nID4,int *nH5, int *nID5 , int *nscore , int *nID);
//Checks if ID already exists
int IDexist(int nID , int nID1 , int nID2 , int nID3 , int nID4 , int nID5);

float dFH(float fFACEH);//Facehugger sprite
float dCB(int nlevel , float fCHESTB);//Chest buster sprite
float dXM(int nlevel , float fXENO);//Xenomorph sprite
float dQN(int nlevel , float fQUEEN);//Queen sprite


int main(){
//Variables
srand(time(0));
int nlives = 3;
int nlevel = 1;
int nscore = 0;
//Top 5 Scores
int nH1 = 0,nH2 = 0,nH3 = 0,nH4 = 0,nH5 = 0;
//Top 5 IDs
int nID1 = 0, nID2 = 0, nID3 = 0, nID4 = 0, nID5 = 0; 

int nID; 
int nChoice;
char cAgain;

//Sprite Values
float fFACEH = 1.0;
float fCHESTB = 2.0;
float fXENO = 4.2;
float fQUEEN = 7.3;

//Welcome Display
do{
dHeader();
printf("\n[1]Play\n[2]Top Scores\n[3]Quit\n");
printf("\nEnter Choice: ");
scanf("%d",&nChoice);

switch (nChoice) {
        case 1:
        nlives = 3;
        nlevel = 1;
        nscore = 0;
        nID = 0;

        // Loop until a valid player ID is entered
        while (nID == 0) {
            printf("\nEnter a player ID (7 - 8 digits only): ");
            scanf("%d", &nID);

            // Validate the player ID
            if ((nID >= 1000000 && nID < 100000000)) {
                // Check if ID already exists
                if (IDexist(nID, nID1, nID2, nID3, nID4, nID5)) {
                    printf("This ID already exists. Please enter a different ID.\n");
                    nID = 0; // Reset ID to prompt for a new one
                } else {
                    // Proceed with gameplay and high score update
                    nscore = nGameplay(nlevel, nlives, nscore, fFACEH, fCHESTB, fXENO, fQUEEN);
                    iHighscores(&nH1, &nID1, &nH2, &nID2, &nH3, &nID3, &nH4, &nID4, &nH5, &nID5, &nscore, &nID);
                    
                    // Play again feature
                    printf("\nDo you wish to return to menu? (Y/N): ");
                    scanf(" %c", &cAgain);
                    clearScreen();
                }
            } else { 
                printf("Invalid ID. Please enter a valid player ID (7 - 8 digits).\n");
                nID = 0; // Reset ID to prompt for a new one
            }
        }
        break;

        case 2:
            //Display top scores and IDs
            dHighScores(nH1, nID1, nH2, nID2, nH3, nID3, nH4, nID4, nH5, nID5);
            //Play again feature
            printf("\nDo you wish to return to menu? (Y/N): ");
            scanf(" %c" , &cAgain);
            getchar();
            clearScreen();
            break;

        case 3:
            dGoodBye();
            break;

        default:
            printf("Invalid choice! Please enter 1, 2, or 3.\n");
            break;
         
        }
                
        // Reset choice for next iteration if not quitting
        if (cAgain != 'Y' && cAgain != 'y') {
           dGoodBye();
           break;
        }
        
    }
 while(cAgain != 'N' && cAgain != 'n');

    return 0;
}

//Functions
//Void Functions
void dHeader(){
 printf("%24s\n","SPACE INVADERS\n");
}
void dGoodBye(){
printf("PROGRAM TERMINATED\n");
}

void dGuide(){
  printf("\n%-18s %-13s %s\n","Alien:", "Sprite:","Lives to shoot");
  printf("%-15s %-20s \n","Facehugger"," \033[32m//(~O~)\\\\\033[0m \t\t1.0");
  printf("%-15s %-20s \n","Chestbuster","\033[33m\\\\<^o^>//\033[0m \t\t2.0");
  printf("%-15s %-20s \n","Xenomorph","\033[34m(((((((O,,,o)\033[0m \t\t4.2");
  printf("%-15s %-20s \n","Queen","\033[35m)))O***O(((\033[0m \t \t7.3");
}
void dDivider(){
    printf("----------------------------------------------------------------------------------------");
}
void clearScreen(){
  //Clear Screen Command
  printf("\033[2J\033[1;1H");
}

//Sprite Functions
float dFH(float fFACEH){//Fachugger Sprite ALL LEVELS
    
     int ranNum = rand()%6+1;

  for (int i = 0; i < ranNum; i++ ){

        printf("\033[32m//(~O~)\\\\\033[0m ");
        
  }  
   return ranNum*=fFACEH; //Returns the number generated times the intended sprite value
    }
float dCB(int nlevel , float fCHESTB){//Chestbuster Sprite ALL LEVELS EXCEPT 2 AND 5
    if (nlevel == 2 || nlevel == 5){
      return 0;
    }

     int ranNum = rand()%6+1;
    
  for (int i = 0; i < ranNum; i++ ){

        printf("\033[33m\\\\<^o^>//\033[0m ");
      
  }  
   return ranNum*=fCHESTB; //Returns the number generated times the intended sprite value
}
float dXM(int nlevel , float fXENO){//Xenomorph Sprite EVEN LEVELS
     if (nlevel %2 !=0){
      return 0;
     }
     int ranNum = rand()%6+1;

  for (int i = 0; i < ranNum; i++ ){

        printf("\033[34m(((((((O,,,o)\033[0m ");
      
  }  
  return ranNum*=fXENO; //Returns the number generated times the intended sprite value
    }

float dQN(int nlevel, float fQUEEN){//Queen Sprite LEVEL 10
     if (nlevel != 10){
      return 0;
     }
     int ranNum = rand()%6+1;

  for (int i = 0; i < ranNum; i++ ){

        printf("\033[35m)))O***O(((\033[0m ");
       
  }  
 return ranNum *=fQUEEN; //Returns the number generated times the intended sprite value
 }

//GamePlay Function
float nGameplay(int nlevel ,int nlives, int nscore , float fFACEH , float fCHESTB, float fXENO, float fQUEEN){
    float nAns;
    while(nlevel<= 10 &&  nlives > 0 )
    {
     while(nlives > 0) {
      dDivider();
      dGuide();
      dDivider();

      printf("\nLevel %d\n", nlevel);
      printf("Lives:%d\n", nlives);
      printf("Score:%d\n", nscore);
      
      /*The Correct Answer will also add the values 
      of the other sprites if they appear*/
      float cAns = dFH( fFACEH);
      printf("\n");
      cAns += dCB(nlevel , fCHESTB);
      printf("\n");
      cAns += dXM(nlevel, fXENO);
      printf("\n");
      cAns += dQN(nlevel, fQUEEN);

      printf("\n");
      dDivider();
      printf("\nHow many shots will it take to kill the aliens?\n");
      printf("Answer: ");
      scanf("%f",&nAns);
      
      if ((int)nAns == (int)cAns){
        nlevel++;
        printf("\nCORRECT\n");
        nscore = nscore + 10;
        printf("Updated Score: %d\n", nscore);
        if (nlevel > 10){
           printf("\nCONGRATULATIONS YOU WIN!\n");
           break; //End the Loop
        }
      }
      else{
        nlives--;
        printf("\nINCORRECT\n");
      }
    } 
       if (nlives <= 0)
       {
          printf("GAME OVER");
          break;//End the Loop
       }
       if (nlevel > 10 || nlives == 0 ){
        break;//End the Loop
       }
  }
    return nscore;
}

int IDexist(int nID , int nID1 , int nID2 , int nID3 , int nID4 , int nID5)
{
   if(nID == nID1 || nID == nID2 || nID == nID3 || nID == nID4 || nID == nID5){
     return 1; // if ID Exists
   }
   return 0; // if NOT
}

//HighScore Functions
void iHighscores(int *nH1, int *nID1, int *nH2, int *nID2, int *nH3,int *nID3, int *nH4 ,int *nID4,int *nH5, int *nID5 , int *nscore , int *nID)
{
if (*nscore > *nH1){
  //Shift Down
   *nH5 = *nH4; *nID5 = *nID4;
   *nH4 = *nH3; *nID4 = *nID3;
   *nH3 = *nH2; *nID3 = *nID2;
   *nH2 = *nH1; *nID2 = *nID1;
   *nH1 = *nscore; 
   *nID1 = *nID;
   }
  else if (*nscore > *nH2)
  {
    *nH5 = *nH4; *nID5 = *nID4;
    *nH4 = *nH3; *nID4 = *nID3;
    *nH3 = *nH2; *nID3 = *nID2;
    *nH2 = *nscore; 
    *nID2 = *nID;
  }
  else if (*nscore > *nH3)
  {
   *nH5 = *nH4; 
   *nID5 = *nID4;
   *nH4 = *nH3; 
   *nID4 = *nID3;
   *nH3 = *nscore; 
   *nID3 = *nID;
  }
  else if (*nscore > *nH4)
  {
    *nH5 = *nH4; 
    *nID5 = *nID4;
    *nH4 = *nscore; 
    *nID4 = *nID;
  }
    else if (*nscore > *nH5)
  {
   *nH5 = *nscore;
   *nID5 = *nID;
  }
   
}

void dHighScores(int nH1, int nID1, int nH2, int nID2, int nH3,int nID3, int nH4 ,int nID4,int nH5, int nID5)
{
 printf("\nHIGHSCORES\tIDs\n");
  printf("1.%d\t\t%d\n" , nH1, nID1);
  printf("2.%d\t\t%d\n" , nH2, nID2);
  printf("3.%d\t\t%d\n" , nH3, nID3);
  printf("4.%d\t\t%d\n" , nH4, nID4);
  printf("5.%d\t\t%d\n" , nH5, nID5);
=======
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
/*
Description: The program is a welcome display that shows
shows the options Play(To start the game), Score (show top5 scores), Quit(End Program)
Full Game Description: Each level a certain number of aliens will appear,
                       the players' job is to calculate how many hits it will take to kill them
                       by multiplying the number of aliens by the numerical value of each alien.

Programmed by: <Luis Carlos> <S15B>
Last modified: 11/24/24
Version: <1.0135>
[Acknowledgements: <list of sites or borrowed libraries and sources>]
*/

// Function Prototypes

//Will handle all the game from level generation to keeping track of the score
float nGameplay(int nlevel ,int nlives, int nscore ,float fFACEH , float fCHESTB, float fXENO, float fQUEEN); 

void dGuide();//Displays a guide to show the design and number of lives the aliens have.
void dHeader();//To be able to display "SPACE INVADERS" on all levels
void dGoodBye();//When player chooses to quit/end the program
void dDivider();//A line to separate actions
void clearScreen();//Will clear the screen for the "Play again" feature

//Will display the top 5 highscores and the corresponding IDs
void dHighScores(int nH1, int nID1, int nH2, int nID2, int nH3,int nID3, int nH4 ,int nID4,int nH5, int nID5);
//Will sort the scores and IDs as they are updated
void iHighscores(int *nH1, int *nID1, int *nH2, int *nID2, int *nH3,int *nID3, int *nH4 ,int *nID4,int *nH5, int *nID5 , int *nscore , int *nID);
//Checks if ID already exists
int IDexist(int nID , int nID1 , int nID2 , int nID3 , int nID4 , int nID5);

float dFH(float fFACEH);//Facehugger sprite
float dCB(int nlevel , float fCHESTB);//Chest buster sprite
float dXM(int nlevel , float fXENO);//Xenomorph sprite
float dQN(int nlevel , float fQUEEN);//Queen sprite


int main(){
//Variables
srand(time(0));
int nlives = 3;
int nlevel = 1;
int nscore = 0;
//Top 5 Scores
int nH1 = 0,nH2 = 0,nH3 = 0,nH4 = 0,nH5 = 0;
//Top 5 IDs
int nID1 = 0, nID2 = 0, nID3 = 0, nID4 = 0, nID5 = 0; 

int nID; 
int nChoice;
char cAgain;

//Sprite Values
float fFACEH = 1.0;
float fCHESTB = 2.0;
float fXENO = 4.2;
float fQUEEN = 7.3;

//Welcome Display
do{
dHeader();
printf("\n[1]Play\n[2]Top Scores\n[3]Quit\n");
printf("\nEnter Choice: ");
scanf("%d",&nChoice);

switch (nChoice) {
        case 1:
        nlives = 3;
        nlevel = 1;
        nscore = 0;
        nID = 0;

        // Loop until a valid player ID is entered
        while (nID == 0) {
            printf("\nEnter a player ID (7 - 8 digits only): ");
            scanf("%d", &nID);

            // Validate the player ID
            if ((nID >= 1000000 && nID < 100000000)) {
                // Check if ID already exists
                if (IDexist(nID, nID1, nID2, nID3, nID4, nID5)) {
                    printf("This ID already exists. Please enter a different ID.\n");
                    nID = 0; // Reset ID to prompt for a new one
                } else {
                    // Proceed with gameplay and high score update
                    nscore = nGameplay(nlevel, nlives, nscore, fFACEH, fCHESTB, fXENO, fQUEEN);
                    iHighscores(&nH1, &nID1, &nH2, &nID2, &nH3, &nID3, &nH4, &nID4, &nH5, &nID5, &nscore, &nID);
                    
                    // Play again feature
                    printf("\nDo you wish to return to menu? (Y/N): ");
                    scanf(" %c", &cAgain);
                    clearScreen();
                }
            } else { 
                printf("Invalid ID. Please enter a valid player ID (7 - 8 digits).\n");
                nID = 0; // Reset ID to prompt for a new one
            }
        }
        break;

        case 2:
            //Display top scores and IDs
            dHighScores(nH1, nID1, nH2, nID2, nH3, nID3, nH4, nID4, nH5, nID5);
            //Play again feature
            printf("\nDo you wish to return to menu? (Y/N): ");
            scanf(" %c" , &cAgain);
            getchar();
            clearScreen();
            break;

        case 3:
            dGoodBye();
            break;

        default:
            printf("Invalid choice! Please enter 1, 2, or 3.\n");
            break;
         
        }
                
        // Reset choice for next iteration if not quitting
        if (cAgain != 'Y' && cAgain != 'y') {
           dGoodBye();
           break;
        }
        
    }
 while(cAgain != 'N' && cAgain != 'n');

    return 0;
}

//Functions
//Void Functions
void dHeader(){
 printf("%24s\n","SPACE INVADERS\n");
}
void dGoodBye(){
printf("PROGRAM TERMINATED\n");
}

void dGuide(){
  printf("\n%-18s %-13s %s\n","Alien:", "Sprite:","Lives to shoot");
  printf("%-15s %-20s \n","Facehugger"," \033[32m//(~O~)\\\\\033[0m \t\t1.0");
  printf("%-15s %-20s \n","Chestbuster","\033[33m\\\\<^o^>//\033[0m \t\t2.0");
  printf("%-15s %-20s \n","Xenomorph","\033[34m(((((((O,,,o)\033[0m \t\t4.2");
  printf("%-15s %-20s \n","Queen","\033[35m)))O***O(((\033[0m \t \t7.3");
}
void dDivider(){
    printf("----------------------------------------------------------------------------------------");
}
void clearScreen(){
  //Clear Screen Command
  printf("\033[2J\033[1;1H");
}

//Sprite Functions
float dFH(float fFACEH){//Fachugger Sprite ALL LEVELS
    
     int ranNum = rand()%6+1;

  for (int i = 0; i < ranNum; i++ ){

        printf("\033[32m//(~O~)\\\\\033[0m ");
        
  }  
   return ranNum*=fFACEH; //Returns the number generated times the intended sprite value
    }
float dCB(int nlevel , float fCHESTB){//Chestbuster Sprite ALL LEVELS EXCEPT 2 AND 5
    if (nlevel == 2 || nlevel == 5){
      return 0;
    }

     int ranNum = rand()%6+1;
    
  for (int i = 0; i < ranNum; i++ ){

        printf("\033[33m\\\\<^o^>//\033[0m ");
      
  }  
   return ranNum*=fCHESTB; //Returns the number generated times the intended sprite value
}
float dXM(int nlevel , float fXENO){//Xenomorph Sprite EVEN LEVELS
     if (nlevel %2 !=0){
      return 0;
     }
     int ranNum = rand()%6+1;

  for (int i = 0; i < ranNum; i++ ){

        printf("\033[34m(((((((O,,,o)\033[0m ");
      
  }  
  return ranNum*=fXENO; //Returns the number generated times the intended sprite value
    }

float dQN(int nlevel, float fQUEEN){//Queen Sprite LEVEL 10
     if (nlevel != 10){
      return 0;
     }
     int ranNum = rand()%6+1;

  for (int i = 0; i < ranNum; i++ ){

        printf("\033[35m)))O***O(((\033[0m ");
       
  }  
 return ranNum *=fQUEEN; //Returns the number generated times the intended sprite value
 }

//GamePlay Function
float nGameplay(int nlevel ,int nlives, int nscore , float fFACEH , float fCHESTB, float fXENO, float fQUEEN){
    float nAns;
    while(nlevel<= 10 &&  nlives > 0 )
    {
     while(nlives > 0) {
      dDivider();
      dGuide();
      dDivider();

      printf("\nLevel %d\n", nlevel);
      printf("Lives:%d\n", nlives);
      printf("Score:%d\n", nscore);
      
      /*The Correct Answer will also add the values 
      of the other sprites if they appear*/
      float cAns = dFH( fFACEH);
      printf("\n");
      cAns += dCB(nlevel , fCHESTB);
      printf("\n");
      cAns += dXM(nlevel, fXENO);
      printf("\n");
      cAns += dQN(nlevel, fQUEEN);

      printf("\n");
      dDivider();
      printf("\nHow many shots will it take to kill the aliens?\n");
      printf("Answer: ");
      scanf("%f",&nAns);
      
      if ((int)nAns == (int)cAns){
        nlevel++;
        printf("\nCORRECT\n");
        nscore = nscore + 10;
        printf("Updated Score: %d\n", nscore);
        if (nlevel > 10){
           printf("\nCONGRATULATIONS YOU WIN!\n");
           break; //End the Loop
        }
      }
      else{
        nlives--;
        printf("\nINCORRECT\n");
      }
    } 
       if (nlives <= 0)
       {
          printf("GAME OVER");
          break;//End the Loop
       }
       if (nlevel > 10 || nlives == 0 ){
        break;//End the Loop
       }
  }
    return nscore;
}

int IDexist(int nID , int nID1 , int nID2 , int nID3 , int nID4 , int nID5)
{
   if(nID == nID1 || nID == nID2 || nID == nID3 || nID == nID4 || nID == nID5){
     return 1; // if ID Exists
   }
   return 0; // if NOT
}

//HighScore Functions
void iHighscores(int *nH1, int *nID1, int *nH2, int *nID2, int *nH3,int *nID3, int *nH4 ,int *nID4,int *nH5, int *nID5 , int *nscore , int *nID)
{
if (*nscore > *nH1){
  //Shift Down
   *nH5 = *nH4; *nID5 = *nID4;
   *nH4 = *nH3; *nID4 = *nID3;
   *nH3 = *nH2; *nID3 = *nID2;
   *nH2 = *nH1; *nID2 = *nID1;
   *nH1 = *nscore; 
   *nID1 = *nID;
   }
  else if (*nscore > *nH2)
  {
    *nH5 = *nH4; *nID5 = *nID4;
    *nH4 = *nH3; *nID4 = *nID3;
    *nH3 = *nH2; *nID3 = *nID2;
    *nH2 = *nscore; 
    *nID2 = *nID;
  }
  else if (*nscore > *nH3)
  {
   *nH5 = *nH4; 
   *nID5 = *nID4;
   *nH4 = *nH3; 
   *nID4 = *nID3;
   *nH3 = *nscore; 
   *nID3 = *nID;
  }
  else if (*nscore > *nH4)
  {
    *nH5 = *nH4; 
    *nID5 = *nID4;
    *nH4 = *nscore; 
    *nID4 = *nID;
  }
    else if (*nscore > *nH5)
  {
   *nH5 = *nscore;
   *nID5 = *nID;
  }
   
}

void dHighScores(int nH1, int nID1, int nH2, int nID2, int nH3,int nID3, int nH4 ,int nID4,int nH5, int nID5)
{
 printf("\nHIGHSCORES\tIDs\n");
  printf("1.%d\t\t%d\n" , nH1, nID1);
  printf("2.%d\t\t%d\n" , nH2, nID2);
  printf("3.%d\t\t%d\n" , nH3, nID3);
  printf("4.%d\t\t%d\n" , nH4, nID4);
  printf("5.%d\t\t%d\n" , nH5, nID5);
>>>>>>> 97eb56f353ee26a616327fd05c3c41000ae5bc0f
}