#ifndef HELPER_C // Include this to prevent redefinition error
#define HELPER_C // Include this to prevent redefinition error

#include <stdio.h>
#include<string.h>
#include <time.h>
#include "helper.h"

 /* Game Proper Flow
  1. Form Blue and Red Team
  2. Select Spy Master for each team
  3. Determine Grid of words
  4. Select Keycard for each words identity
  5.Game Start
    - SM Phase
        * Spy Masters can see the keycard
        * SMs must give the number of words associated with their clue ex("animal, 2", "LION  BEAR")
    -Agent Phase
        *Rest of the team cannot see the keycard
  
  */
/*This function shows displays the user's options and prompts them
 to choose an option.
 @returns the selected option.
*/
int MainMenu(){
    int x;
    do{
    printf("=====================MAIN MENU=====================\n");
    printf("[1] New Game\n[2] Top Spymasters\n[3] Exit Program\n");
    scanf("%d",&x);

       if(x<1 || x>3){
          printf("INVALID CHOICE PLEASE CHOOSE BETWEEN 1,2,3 ONLY\n");
       }
    }while(x<1 || x>3);
    return x;

 }

 /*
Load players will read all the players usernames and the number of wins they have as spy masters
It will loop until it reaches the end of the file
@param char *filename will be the name of the file to be read
@param PlayerTag player[] is the array to place the players data
@param *count  to keep track of the number of players available
*/
void LoadPlayers(char *filename, PlayerTag players[], int *count)
{   

    FILE *fp = fopen(filename, "r");

    if(fp == NULL)
    {
      printf("No such file exists.\n");
      *count = 0;
    } else{
        *count = 0;
        while(*count<50 && fscanf(fp, "%36[^,],%d\n" , players[*count].username , &players[*count].WinsSM) == 2)
        (*count)++;
    }


  fclose(fp);
}

/*
This function updates the player.txt file when new players are added
new players' wins will be set to 0
@param - char *filename will be the name of the file to be updated
@param - PlayerTag player[] is the data to be written
@param - int *numPlayers points to the address where the # of players will be stored
*/
void SavePlayers(char *filename, PlayerTag players[], int *numPlayers)
{
       FILE *fp = fopen(filename, "w");

    if (fp == NULL)
    {
        printf("Could not open %s for writing.\n", filename);
    }
    else
    {
        for (int i = 0; i < *numPlayers; i++)
        {
            fprintf(fp, "%s,%d\n", players[i].username, players[i].WinsSM);
        }
        fclose(fp);
        printf("Player list saved to %s.\n", filename);
    }
}

/*
FormTeams will allow the users to form the blue and red team
- Create an empty team to be filled
- Users will select players from the playerlist
- User can enter a new player which will be added to the player list file
- After members are filled, Users will select who will be the spymaster
- Returns team

  @param - PlayerTag players[] contains the list of players to choose from
  @param - int *numPlayers address of the number of players
  @param - char *Teamcolor address of the current teams color
  @return - a team with at least 2 members and only 1 Spy Master
*/
 TeamTag FormTeams(PlayerTag players[], int *numPlayers, char *Teamcolor)
 {  //Function Variables
    TeamTag temp;
    temp.members = 0;
    temp.spymaster = NULL;
    int z;

    do {
        //displays the current teams members as they are being added
        displayTeamMembers(&temp, Teamcolor);
        //Prompts user to select how they want to form their team
        printf("\nWhat would you like to do?\n");
        printf("[0] Done Adding\n");
        printf("[1] Add from List\n");
        printf("[2] Add New Player\n");
        printf("[3] Remove from Team\n");

        scanf("%d", &z);
        //Every choice corresponds to a dedicated function 
        switch(z) {
            //Prompts the user to select a username from the player list 
            case 1: addFromList(players, numPlayers, &temp); break;
            //Prompts the user to write a new username not found on the player list
            case 2: addNewPlayer(players, numPlayers, &temp); break;
            //Prompts the user to pick a team memeber to remove
            case 3: removeFromTeam(players, numPlayers, &temp); break;
            case 0:
                //Checks if there are less than 2 members on a team
                if (temp.members < 2) {
                    printf("A team must have at least 2 members.\n");
                    z = 1;  // Continue loop
                }
                break;
            //Incase a user enter an invalid number
            default: printf("INVALID CHOICE\n");
        }
      //Loop continues while z is not 0 or there are less than 2 memebers
    } while(z != 0 || temp.members < 2);
    //Prompts the user to select the spy master 
    //from the current members on the team
    selectSpyMaster(&temp, Teamcolor);
    //Will be printed once a team is complete
    printf("%s Team Formed\n", Teamcolor);
    //Returns the completed team
    return temp;
 }
/*
This function will display the members as the team is being formed.
TeamTag *team - Pointer to the team to be displayed
char *TeamColor - pointer to which team is being formed

*/
 void displayTeamMembers(TeamTag *team, char *Teamcolor)
 {  //Function Variables
    int i;
    //Show whose team the memebers belong to
    printf("\nCurrent %s Team Members\n", Teamcolor);
    //Uses a for loop to go through ever that's been added
    for (i = 0; i < team->members; i++) {
        printf("+ %s\n", team->players[i]->username);
    }

 }
  /*
  This functions purpose is to add a player to the team for formteams
  @param - PlayerTag players[] - list where players are selected
  @param - int numPlayers - number of players
  @param - TeamTag *team - pointer to the team to be added
  */
 void addFromList(PlayerTag players[], int *numPlayers, TeamTag *team)
 {  
    //Checks if player list is empty
   if (*numPlayers == 0) {
        printf("No players available.\n");
    } else {
        //Displays every player on the list
        int i;
        printf("Available Players:\n");
        for (i = 0; i < *numPlayers; i++) {
            printf("[%d] %s\n", i, players[i].username);
        }
        //Prompts the user to select an index of a player from the list
        int choice;
        printf("Enter player index to add: ");
        scanf("%d", &choice);
        //Checks if player is already on the team
        int isDuplicate = 0;

        if (choice >= 0 && choice < *numPlayers) {
            for (i = 0; i < team->members; i++) {
                if (team->players[i] == &players[choice]) {
                    isDuplicate = 1;
                }
            }
            //Adds only if duplicate is not on the team
            if (isDuplicate) {
                printf("Player is already in the team.\n");
              //Checks if team if already full
            } else if (team->members < 10) {
                team->players[team->members++] = &players[choice];
                printf("%s added to the team!\n", players[choice].username);
            } else {
                printf("Team Full! (max of 10)\n");
            }
        } else {
            printf("Invalid choice\n");
        }
    }
    
 }
 /*
 This function will add a new player via user inputting a new name
 PlayerTag players[] - used to allocates the new player, initializes wins as 0
 int *numPlayers  - points to the current player count
 TeamTag *team - points to the team being formed
 */
 void addNewPlayer(PlayerTag players[], int *numPlayers, TeamTag *team)
 {
     int isDuplicate = 0;
    //Checks if player list is full
    if (*numPlayers >= 50) {
        printf("Player list is full.\n");
    } else if (team->members >= 10) {
        printf("Team is full.\n");
    } else {
        //Prompts the user to enter a username
        Str36 newName;
        printf("Enter username: ");
        scanf(" %36[^\n]", newName);  // Temporarily store input
        //Makes sure user name  doesnt already exist
        for (int i = 0; i < team->members; i++) {
            if (strcmp(team->players[i]->username, newName) == 0) {
                isDuplicate = 1;
            }
        }
        
        if (isDuplicate == 1) {
            printf("Player is already in the team.\n");
        } else {
            //Adds player to the player list and sets their wins to 0
            strcpy(players[*numPlayers].username, newName);
            players[*numPlayers].WinsSM = 0;
            //Successfully adds a player to the team
            team->players[team->members++] = &players[*numPlayers];
            printf("%s was successfully added.\n", players[*numPlayers].username);
             //Increments the total number of players.
            (*numPlayers)++;
        }
    }
 }
 /*
 This function deletes a player from the team
 @param - TeamTag *team - pointer to the team where the player will be kicked from
 @param - int *numPlayers - points to the current player count
 @param - TeamTag *team - points to the team being formed

 */
 void removeFromTeam(PlayerTag players[], int *numPlayers, TeamTag *team)
 {
   int i, choice;
   //Checks if there are no players on the team
   if (team->members == 0) {
        printf("No players to remove.\n");
    } else {
        //Shows the list of players the user can remove
           printf("Select member to remove:\n");
    for ( i = 0; i < team->members; i++) {
        printf("[%d] %s\n", i + 1, team->players[i]->username);
    }

    //Prompt user to select which username to remove
    printf("Select your choice: ");
    scanf("%d", &choice);
    //Checks if choice was valid
    if (choice < 1 || choice > team->members) {
        printf("Invalid choice\n");
        return;
    }
     //Offsets the choice by 1 
    int index = choice - 1;

    if (*numPlayers < 50) {
        // Copy player back to list (if needed, or skip entirely)
        players[*numPlayers] = *(team->players[index]);
        (*numPlayers)++;

        // Shift remaining players left
        for (int i = index; i < team->members - 1; i++) {
            team->players[i] = team->players[i + 1];
        }
        //Decrements the number of memeber on the team
        team->members--;
        printf("Member removed and returned to list.\n");
    } else {
        printf("List is full. Cannot return player.\n");
    }
    }
 }
/*
This function will allow the users to select which member of the team will be the spymaster
@param - TeamTag *team - points to the team where the user will set the Spy Master
@param - char *Teamcolor - points to the team being formed
*/
 void selectSpyMaster(TeamTag *team, char *Teamcolor)
 {
      int i, choice;
     printf("\nWho will be the Spy Master?\n");

    for (i = 0; i < team->members; i++) {
        printf("[%d] %s\n", i + 1, team->players[i]->username);
    }

    
    do {
        printf("\nEnter your pick (1 to %d): ", team->members);
        scanf("%d", &choice);

        if (choice < 1 || choice > team->members) {
            printf("Invalid choice. Try again.\n");
        }
    } while (choice < 1 || choice > team->members);

    team->spymaster = team->players[choice - 1];

    printf("%s is now the Spy Master of the %s Team.\n", team->spymaster->username, Teamcolor);
     
 }


 /*
 This function will read from a file and randomly select words
 to be added to the grid for the game.
 @@param - char *filename points to a file to be accessed
 @@param - GridWord grid[5][5]  is where the words will be placed
 return 1 for success otherwise 0
 */
 int LoadCodenames(char *filename, GridWord grid[5][5])
 {  int success = 1;
    char wordList[400][21];
    int totalWords = 0;
    //Opens the file
    FILE *fp = fopen(filename, "r");

    if (fp == NULL) {
        printf("No such file exists.\n");
        success = 0;
    } else {
        // Step 1: Save words to array using fscanf
        while (fscanf(fp, "%20s", wordList[totalWords]) == 1 && totalWords < 400) {
            totalWords++;
        }

        if (totalWords < 25) {
            printf("Not enough words in file. Requires at least 25.\n");
        } else {
            // Step 2: Create and shuffle index array (Fisher–Yates Shuffle)
            int indices[400];
            for (int i = 0; i < totalWords; i++) {
                indices[i] = i;
            }

            srand(time(NULL));
            for (int i = totalWords - 1; i > 0; i--) {
                int j = rand() % (i + 1);
                int temp = indices[i];
                indices[i] = indices[j];
                indices[j] = temp;
            }

            // Step 3: Fill the grid
            int count = 0;
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 5; col++) {
                    strcpy(grid[row][col].word, wordList[indices[count]]);
                    grid[row][col].identity = ' ';
                    grid[row][col].reveal = 0;
                    count++;
                }
            }
        }
        //Closes the flag
        fclose(fp);
    }
    //returns 1 for success otherwise 0
    return success;
 }
/*
This function will randomly select a number between 1-20
to pick a keycard file(01.txt), the keycard will select who gets turn 1 
and the identity of the words already on the grid.
@param Keycard *key points to the key
@param GridWord grid[5][5]  where the identities will be attached

1. use rng to get a number between 1-20 
2. set number to fopen a file with that number in read mode
3. read the first char and set it to be the team the takes the first turn (R/B)
4. read all entries  (R/B/I/A)
5. access grid via nested for loops
6. set identities based on the file
7. return 1 for success otherwise 0

*/
 int SelectKeyCard(GridWord grid[5][5], Keycard *key)
 { 
  int i,j;
  int success = 1;
  //Randomly generate a number between 1-20
   srand(time(NULL));
   int nKey = (rand() % 20) + 1;
   char filename[100] = "";

// Convert int nKey to a 2-digit string (manual padding)
    char numStr[3]; // Holds "01" to "20"

    if (nKey < 10) {
        numStr[0] = '0';
        numStr[1] = '0' + nKey;
    } else {
        numStr[0] = '0' + (nKey / 10);  // tens
        numStr[1] = '0' + (nKey % 10);  // ones
    }
    numStr[2] = '\0';  // null-terminate 

    // Build filename 
    strcat(filename, "keycards/");   // e.g., "keycard/"
    strcat(filename, numStr);       // e.g., "keycard/07"
    strcat(filename, ".txt");      // becomes "keycard/07.txt"
    //sprintf(filename, "keycard/%02d.txt", nKey);
   
   //Open generated file
   FILE *fp = fopen(filename, "r");

   if(fp == NULL)
   {
    printf("File does not exist\n");
    success = 0;
   } else {
      //set char to first team
      fscanf(fp, "%c\n", &key->firstTeam);

      //Set the rest of the identity
      for(i=0; i<5; i++)
      {
        for(j=0; j<5; j++)
        {
          fscanf(fp, " %c", &key->identity[i][j]);
          grid[i][j].identity = key->identity[i][j];
        }
      }

   }

    fclose(fp);
    return success;
 }
 /*
 This function will initialize all of the neccessary game data needed to run the game.
 It will call upon all previous functions above before being called to Gameplay.

 @param GameState *game points to where the game data will be stored
 @param PlayerTag players[] contains the total list of players
 @param int *count points to the number players available  

 */
 void InitGameState(GameState *game, PlayerTag players[], int *count)
{
    int i, j;
    int success = 1;  // Track setup status

    printf("\n======= Starting New Game =======\n");

    // Step 1: Load Players
    LoadPlayers("players.txt", players, count);
    if (*count < 4) {
        printf("Not enough players to form two teams. At least 4 are needed.\n");
        success = 0;
    }

    // Step 2: Form Teams
    if (success) {
        game->BlueTeam = FormTeams(players, count, "Blue");
        game->RedTeam = FormTeams(players, count, "Red");

        if (game->BlueTeam.members == 0 || game->RedTeam.members == 0) {
            printf("Team formation failed. Check player roles.\n");
            success = 0;
        }
    }

    // Step 3: Load Grid Words (now returns 0 on failure)
    if (success) {
        if (!LoadCodenames("codenames.txt", game->grid)) {
            printf("Failed to load codenames.\n");
            success = 0;
        }
    }

    // Step 4: Select Keycard (now returns 0 on failure)
    if (success) {
        if (!SelectKeyCard(game->grid, &game->key)) {
            printf("Failed to load keycard.\n");
            success = 0;
        }
    }

    // Step 5: Final Game Setup
    if (success) {
        printf("\nBlue Team Members:\n");
        for (i = 0; i < game->BlueTeam.members; i++) {
            printf(" + %s\n", game->BlueTeam.players[i]->username);
        }

        printf("\nRed Team Members:\n");
        for (i = 0; i < game->RedTeam.members; i++) {
            printf(" + %s\n", game->RedTeam.players[i]->username);
        }

        game->currTurn = game->key.firstTeam;
        game->gameOver = 0;

        // Count agents
        int r = 0, b = 0;
        for (i = 0; i < 5; i++) {
            for (j = 0; j < 5; j++) {
                if (game->key.identity[i][j] == 'R') r++;
                else if (game->key.identity[i][j] == 'B') b++;
            }
        }

        game->redLeft = r;
        game->blueLeft = b;
    }
    else {
        printf("\nGame setup failed. Fix the errors above and try again.\n");
    }
}

 /*
 This function will show the current grid and the keycard being used
 @param GateState *game points to keycard and grid to be displayed
 */
 void ViewSpyMasterPhase(GameState *game)
 {   
     int i,j;
     //Grid from the POV of the Spymaster
     printf("\n========GRID(Spymaster View)========\n\n");

     for(i=0; i<5; i++)
     {
        for(j=0; j<5; j++)
        { //Displays the grid of words
          GridWord cell = game->grid[i][j];
          //Shows the identity of the word when selected by agents
          if(cell.reveal){
             switch(cell.identity)
            {
             case 'B': printf("\033[34m[---BLUE----]\033[0m"); break;
             case 'R': printf("\033[31m[----RED----]\033[0m"); break;
             case 'I': printf("\033[37m[-BYSTANDER-]\033[0m"); break;
             case 'A': printf("\033[35m[--ASSASSIN-]\033[0m"); break;
            }
          } else {printf("%-13s ",cell.word);}
  
        }
        printf("\n");
     }
     //Displays the identity of all cards on the grid
     printf("\n========KEYCARD(Spymaster View)========\n\n");
     for(i=0; i<5; i++)
     {
        for(j=0; j<5; j++)
        {
           char id = game->key.identity[i][j];
           switch(id)
           {
             case 'B': printf("\033[34m[---BLUE----]\033[0m"); break;
             case 'R': printf("\033[31m[----RED----]\033[0m"); break;
             case 'I': printf("\033[37m[-BYSTANDER-]\033[0m"); break;
             case 'A': printf("\033[35m[--ASSASSIN-]\033[0m"); break;
           }

        }
        printf("\n");
     }
 }
/*
This function is where the spymaster will input a clue to words on the grid
as well as how many words fit the clue.
@param - GameState *game - points to gamestate to be checked
@param - Str20 clueWord - clue which the user will enter
@param - int *clueNumber - points to the number of words that fit the clue
*/
 void EnterClue(GameState *game, Str20 clueWord, int *clueNumber)
 {

     if(game->currTurn == 'B'){
       printf("\n\033[34m=======BLUE TEAM'S TURN=======\033[0m\n");
     } else {
      printf("\n\033[31m=======RED TEAM'S TURN=======\033[0m\n");
     }
     // Flush newline before reading clue
    int ch;
    while ((ch = getchar()) != '\n' && ch != EOF){
        // do nothing, just flush
    }
     //Input Clue
     printf("Please Enter Clue:\n");
     scanf("%20s",clueWord);

     //Input Cluenumber
     printf("Enter number of words related to the clue:\n");
     scanf("%d",clueNumber);

 }
/*
This function will implement the Agent phase, Displays the current grid.
Manages guesses based on the clue and rules
@param - GameState *game - points to the  gamestate that will be updated
@param - Str20 clueWord - Clue entered by the spy master
@param - int *clueNumber - points to the number of words associated with the clue
*/
 void AgentPhase(GameState *game, Str20 clueWord,int *clueNumber)
 {   
     int i,j;
     Str20 Guess;
     int validGuess;
     int guessesLeft = *clueNumber + 1; // Add 1 extra guess as per Codenames rules

    //While loop to make sure the turn does not end 
    while(guessesLeft > 0 && !game->gameOver){ 
     //Display Grid
     printf("\n============GRID(Agent View)==============\n\n");
     for(i=0; i<5; i++)
     {
        for(j=0; j<5; j++)
        {
          GridWord cell = game->grid[i][j];
          if(cell.reveal){
             switch(cell.identity)
            {
             case 'B': printf("\033[34m[---BLUE----]\033[0m"); break;
             case 'R': printf("\033[31m[----RED----]\033[0m"); break;
             case 'I': printf("\033[37m[-BYSTANDER-]\033[0m"); break;
             case 'A': printf("\033[35m[--ASSASSIN-]\033[0m"); break;
            }
          } else {printf("%-13s ",cell.word);}
  
        }
        printf("\n");
     }
     printf("\nClue: %s | Allowed Guesses Left: %d\n", clueWord, guessesLeft);
     //Guess a word
    int found;
    validGuess = 0;
    int turnFlag = 0;
    //Ensures the user inputs a guess
    while(!validGuess){
      
     printf("\nEnter a guess:\n");
     // Flush newline before reading clue
    int ch;
    while ((ch = getchar()) != '\n' && ch != EOF){
        // do nothing, just flush
    }
       scanf("%20s", Guess);

       // Capitalize the guess
       for(int k = 0; Guess[k]; k++) {
           if(Guess[k] >= 'a' && Guess[k] <= 'z') {
               Guess[k] = Guess[k] - 32; // Convert to uppercase
           }
       }
       found = 0;

         for(i=0; i<5; i++)
       {
          for(j=0; j<5; j++)
        {  //Check if guess is on the grid
           if(strcmp(game->grid[i][j].word,Guess) == 0)
           { found = 1;
              
              if(game->grid[i][j].reveal){
                printf("Word has already been revealed. Try Again\n");
                guessesLeft--; 
              } else {

                // Reveal the card
                game->grid[i][j].reveal = 1;
                char identity = game->grid[i][j].identity;
                switch(identity)
                {
                    case 'I': printf("You revealed a BYSTANDER. Turn ends\n"); 
                              turnFlag = 1;
                              break;

                    case 'A': printf("You revealed an ASSASIN. Game Over.\n"); 
                              game->gameOver = 1; 
                              turnFlag = 1;
                              break;

                    case 'B': if(game->currTurn == 'B'){
                               printf("You found a BLUE AGENT.");
                              game->blueLeft--;  
                              guessesLeft--; 
                              if(game->blueLeft == 0){
                                printf("All blue agents found! Blue Team wins!\n");
                                game->gameOver = 1;
                              }
                             } else {
                                    printf("Oops! That was a blue agent. Turn ends.\n");
                                    game->blueLeft--;
                                    if (game->blueLeft == 0) {
                                            printf("All blue agents have been revealed! Blue Team wins!\n");
                                            game->gameOver = 1;
                                        }
                                    turnFlag = 1;
                                } break;

                    case 'R': if(game->currTurn == 'R'){
                               printf("You found a RED AGENT.");
                              game->redLeft--;  
                              guessesLeft--; 
                              if(game->redLeft == 0){
                                printf("All Red agents found! Red Team wins!\n");
                                game->gameOver = 1;
                              }
                             } else {
                                    printf("Oops! That was a red agent. Turn ends.\n");
                                    game->redLeft--;
                                    if (game->redLeft == 0) {
                                            printf("All red agents have been revealed! Red Team wins!\n");
                                            game->gameOver = 1;
                                        }
                                    turnFlag = 1;
                                }  break;

                }
                      validGuess = 1;
                      if (turnFlag || game->gameOver) {
                          guessesLeft = 0; // End the outer while loop
                      } else {
                         if(*clueNumber != guessesLeft - 1)
                         {
                            char choice;
                            printf("\nDo you want to continue guessing? (Y/N): ");

                            while ((ch = getchar()) != '\n' && ch != EOF); // flush
                            scanf(" %c", &choice);
                            if (choice == 'N' || choice == 'n') {
                             guessesLeft = 0;  // End loop without using break
                             }
                         }

                      }

              }

           }
         }
       
      }
      if (!found) {
        printf("Invalid guess. Word not on the grid. Try again.\n");
        guessesLeft--; //Decrements guesses when an invalid guess is entered
    }

    }

  }

 }
/*
This function handles that entire game from start to finish
@param - PlayerTag players[] accesses the list of players need
@param - int *count points to the current number of players

*/
 void Gameplay(PlayerTag players[], int *count)
 {     
    GameState Game;   // Contains all game info
    Str20 clueWord;
    int clueNumber;
    char lastTurn;

    InitGameState(&Game, players, count);  // Sets up game state with real player pointers

    while (!Game.gameOver) {
        lastTurn = Game.currTurn;  // Save before switch

        // Spymaster Phase
        ViewSpyMasterPhase(&Game);
        EnterClue(&Game, clueWord, &clueNumber);

        // Agent Phase
        AgentPhase(&Game, clueWord, &clueNumber);

        // Switch turns
        if (Game.currTurn == 'B') {
            Game.currTurn = 'R';
        } else {
            Game.currTurn = 'B';
        }
    }

    // Determine winning team
    char winningTeam;
    if (Game.redLeft == 0) {
        winningTeam = 'R';
    } else if (Game.blueLeft == 0) {
        winningTeam = 'B';
    } else {
        // Assassin or fallback condition: last team caused game over
        if (lastTurn == 'B') {
            winningTeam = 'R';
        } else {
            winningTeam = 'B';
        }
    }
    // Increment only the winning team's spymaster's WinsSM
    if (winningTeam == 'R' && Game.RedTeam.spymaster != NULL) {
        // Announce the winner
          printf("\nRED Team wins the game!\n");

        Game.RedTeam.spymaster->WinsSM++;
    } else if (winningTeam == 'B' && Game.BlueTeam.spymaster != NULL) {
        printf("\nBLUE Team wins the game!\n");
        Game.BlueTeam.spymaster->WinsSM++;
    }

    // Save updated player records (includes spymaster updates)
    SavePlayers("players.txt", players, count);

    printf("Game Over. Thanks for playing!\n");
 }
 /*
 This function's job is to arrange the top 5 Spy Masters in order
 @param - PLayerTag players[]: array to access all the players on the list
 @param - int count: number of players to sort
 */
 void TopScore(PlayerTag players[], int count)
 {
   int choice;
   LoadPlayers("players.txt", players, &count);
   //Selection Sort
   int i,j, max;
   PlayerTag temp;
   for(i=0; i<count-1; i++)
   {
      max = i;
      for(j=i+1; j<count; j++)
      {
        if(players[j].WinsSM > players[max].WinsSM)
         {
            max = j;

         }
      }

        if(max != i)
        {
          temp = players[i];
          players[i] = players[max];
          players[max] = temp;

        }
   }
   //Prints the Top 5 Spymasters
   do
   {
    printf("\n+----------TOP SPYMASTERS-------------+\n");
        
        for(i=0; i<5; i++)
        {
         printf("%d. %-20s %5d\n" ,i+1, players[i].username , players[i].WinsSM);
        }
    //Prompts the user if they wish to go back to the main menu
    printf("\nReturn to Main Menu?\n");
    printf("[1] Yes\n");
    printf("[2] No\n");
    scanf("%d",&choice);

    if(choice != 1 && choice != 2)
    {
      printf("INVALID CHOICE PLEASE PICK 1 OR 2 ONLY\n");
    }

   }while (choice != 1);

 }

#endif // HELPERS_C