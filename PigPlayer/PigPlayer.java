/**Troy Tully
  * this class is the parent class for all of the players
  * */
public abstract class PigPlayer{
  private String name;
  private int score;
  private int totalWins;
  public PigPlayer(){
   name = "player1"; 
  }
  public PigPlayer(String newName){
    name = newName; 
  }
  public void setName(String newName){
    name = newName; 
  }
  public String getName(){
    return name;
  }
  public void reset(){
    score = 0;
  }
  public void addPoints(int turnTotal){
    score = score + turnTotal; 
    if(turnTotal>0){
    //System.out.println( this.getName() + " " + turnTotal);
    }
    this.won();
  }
  public boolean won(){
    if(score >= PigGame.GOAL){
      totalWins++;
      System.out.println(this.getName() +" won!");
      return true;
    }
    else
      return false;
  }
  public int getScore(){
    return score; 
  }
  public int getWinRecord(){
    return totalWins;  
  }
  public String toString(){
    return name + " Score: " + score; 
  }
  
  public abstract boolean isRolling(int turnTotal, int opponentScore);
  
  
  
}