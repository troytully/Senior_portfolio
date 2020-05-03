/**Troy Tully
  * this class holds a game for two Pig players!
  * */
import java.util.Scanner;
public class PigGame{
  private static boolean verbose=true;
  public static final int GOAL = 100;
  private PigPlayer player1;
  private PigPlayer player2;
  
  
  public PigGame(){
    player1 = new UserPigPlayer("player1");
    player2 = new UserPigPlayer("player2");
  }
  
  public PigGame(String firstPlayer, String secondPlayer){
    player1 = new UserPigPlayer(firstPlayer);
    player2 = new UserPigPlayer(secondPlayer);
  }
  public PigGame(PigPlayer firstPlayer, PigPlayer secondPlayer){
    player1 = firstPlayer;
    player2 = secondPlayer;
  }
  public static int playTurn(PigPlayer player, PigPlayer opponent){
    if(opponent.getScore()>= 100)
      return 0;
    int turnTotal = 0;
    int roll = 0;
    if(verbose){
   System.out.println(player.getName() + " it's your turn!");
    }
    do{
      roll = Die.roll();
      if(roll > 1){
        turnTotal = turnTotal + roll; 
        
        if((turnTotal + player.getScore())>= PigGame.GOAL){
          if(verbose)
          System.out.println("You rolled a " + roll);
          return turnTotal;
        }
        if(verbose){
     System.out.println("You rolled a " + roll);
     System.out.println("Your turn total so far is " + turnTotal);
      System.out.println();
        }
      }
      else{
        if(verbose)
      System.out.println("You rolled a 1!!! Your turn is over!"); 
        return 0;
      }
    }
    while(player.isRolling(turnTotal, opponent.getScore()) == true);
    return turnTotal;
  }
  
  
  public void playGame(){
    while(player1.getScore() < GOAL && player2.getScore() < GOAL){
//    player1.addPoints(99);
      player1.addPoints(this.playTurn(player1, player2));
 System.out.println(player1.toString());
      player2.addPoints(this.playTurn(player2, player1));
 System.out.println(player2.toString());
    }
    player1.reset();
    player2.reset();
    
  }
  public static void userVsUser(){
    Scanner keyboard = new Scanner(System.in);
    String name = new String();
    String name2 = new String();
    double decidingNum = Math.random();
    System.out.println("Yo Yo, Welcome to the game of Pig.");
    System.out.println("Rules:  Roll until your satisfied with your turntotal, If you roll");
    System.out.println("a one, your turntotal resets, and your turn is over, hold to add turntotal to your score!");
    System.out.println("Enter the first player's name?");  
    name = keyboard.next();
    System.out.println("Enter the second player's name?");  
    name2 = keyboard.next();
    if(decidingNum > .50){
      PigGame game = new PigGame(name,name2);
      game.playGame();
    }
    else{
      PigGame game = new PigGame(name2,name);
      game.playGame();
    }
    
    
  }
  public static void userVsComputer(){
    Scanner keyboard = new Scanner(System.in);
    String name = new String();
    double decidingNum = Math.random();
    System.out.println("Yo Yo, Welcome to the game of Pig.");
    System.out.println("Rules:  Roll until your satisfied with your turntotal, If you roll");
    System.out.println("a one, your turntotal resets, and your turn is over, hold to add turntotal to your score!");
    System.out.println("Enter the first player's name?");  
    name = keyboard.next();
    PigPlayer computer = new SimpleHoldPlayer("Computer");
    PigPlayer person = new UserPigPlayer(name);
    if(decidingNum > .50){
      PigGame game = new PigGame(person,computer);
      game.playGame(); 
    }
    else{
PigGame game = new PigGame(computer,person);
      game.playGame();
    }
    
  }
  public static void ComputerVsComputer(){
    double decidingNum = Math.random();
    PigPlayer computer = new SimpleHoldPlayer("Player1");
    PigPlayer computer2 = new SimpleHoldPlayer("Player2");
    if(decidingNum > .50){
      PigGame game = new PigGame(computer,computer2);
      game.playGame(); 
    }
    else{
PigGame game = new PigGame(computer2, computer);
      game.playGame();
    }
    computer.reset();
    computer2.reset();
  }
  public static void main (String[] args){
    
    PigPlayer computer = new FourTurnsPlayer("Player1");
    PigPlayer computer2 = new StrategicPlayer("Player2");
    PigGame troy = new PigGame(computer,computer2);
    for(int i = 0; i < 1; i++){
      troy.playGame();
    }
   System.out.println(computer.getWinRecord());
   System.out.println(computer2.getWinRecord());
  }}