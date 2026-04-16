#ifndef HELPER_H // Include this to prevent redefinition error
#define HELPER_H // Include this to prevent redefinition error
#include <stdio.h>
#include <string.h>
 typedef char Str36[37];
 typedef char Str20[21];  

 typedef struct  //Every players data 
 {
   Str36 username; //Username;
   int WinsSM; //Wins as spymaster
 }PlayerTag;

 typedef struct   //Struct for both blue/red team
 {
  int members;   //no. of players per team
  PlayerTag *players[10];//Points to player's wins and name
  PlayerTag *spymaster; //Points to the spy master
 }TeamTag;

 typedef struct // represents the codenames and their status on the board
 {
   char word[21];   // Words on the grid have at most 20 characters
   char identity;   //B for blue , R for red and A for assassin
   int reveal;     //0 if hidden , 1 for reveled
 }GridWord;

 typedef struct //Key card struct determines which team goes first and determines roles
 {
  char firstTeam;  //B or R will make the first move
  char identity[5][5];  //5x5 grid to assign the roles of each word in the grid
 }Keycard;


 typedef struct  //Reflects the current game state on the board
 {
    TeamTag BlueTeam;
    TeamTag RedTeam;
    GridWord grid[5][5]; //5X5 grid where the game takes place
    Keycard key;    
    char currTurn;    //R or B
    int blueLeft;     // how many blue agents left unrevealed
    int redLeft;      // how many red agents left unrevealed
    int gameOver;     // 1 if game ended, 0 otherwise
 }GameState;


 int MainMenu();
 //Main Menu Functions
 void Gameplay(PlayerTag players[], int *count);
 void TopScore(PlayerTag players[], int count);

 //FormTeams Functions
 void displayTeamMembers(TeamTag *team, char *Teamcolor);
 void selectSpyMaster(TeamTag *team, char *Teamcolor);
 void addFromList(PlayerTag players[], int *numPlayers, TeamTag *team);
 void addNewPlayer(PlayerTag players[], int *numPlayers, TeamTag *team);
 void removeFromTeam(PlayerTag players[], int *numPlayers, TeamTag *team);

 //Gameplay Functions
 TeamTag FormTeams(PlayerTag players[], int *numPlayers, char *Teamcolor); 
 void LoadPlayers(char *filename, PlayerTag players[] , int *count); 
 void SavePlayers(char *filename, PlayerTag players[], int *numPlayers);
                                                                   
 int LoadCodenames(char *filename, GridWord grid[5][5]);
 int SelectKeyCard(GridWord grid[5][5], Keycard *key);
 void InitGameState(GameState *game, PlayerTag players[], int *count);
 //SpyMaster Phase
 void ViewSpyMasterPhase(GameState *game);
 void EnterClue(GameState *game, Str20 clueWord, int *clueNumber);
//Agent Phase
 void AgentPhase(GameState *game, Str20 clueWord,int *clueNumber);

#endif // _H; Include this to prevent redefinition error