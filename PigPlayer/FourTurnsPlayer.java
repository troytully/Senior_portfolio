/**Troy Tully
  * this player will win in 4 turns.
  * */
public class FourTurnsPlayer extends PigPlayer{
  public int hold;
  
  
  public FourTurnsPlayer(){
    super();
    hold = PigGame.GOAL/4;
  }
  public FourTurnsPlayer(String name){
    super(name);
    hold = PigGame.GOAL/4;
  }
  public FourTurnsPlayer(String name, int newHold){
    super(name);
    hold = newHold;
  }
  
  public boolean isRolling(int turnTotal, int opponent){
    if(this.getScore()==0 && turnTotal< hold){
      return true;
    }
    else if(this.getScore() >= 75 && turnTotal < (PigGame.GOAL - this.getScore())/1)
        return true;  
  
   else if(this.getScore() >= 50 && turnTotal < (PigGame.GOAL - this.getScore())/2)
         return true;  
  
   else if(this.getScore() >= 25 && turnTotal < (PigGame.GOAL - this.getScore())/3)
        return true;  
//    else if(this.getScore() >= 75 && turnTotal < PigGame.GOAL - this.getScore())
//      return true;  
//    
//    else if(this.getScore() >= 50 && turnTotal < 22)
//      return true;  
//    
//    else if(this.getScore() >= 25 && turnTotal < 24)
//      return true;
    else
      return false;
  }
} 
