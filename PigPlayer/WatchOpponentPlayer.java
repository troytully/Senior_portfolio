/**Troy Tully
  * this player rolls according to how well the other opponent is doing.
  * */
public class WatchOpponentPlayer extends PigPlayer{
  public int hold;
  
  
  public WatchOpponentPlayer(){
    super();
    hold = 20;
  }
  public WatchOpponentPlayer(String name){
    super(name);
    hold = 20;
  }
  public WatchOpponentPlayer(String name, int newHold){
    super(name);
    hold = newHold;
  }
  
  public boolean isRolling(int turnTotal, int opponent){
    if(PigGame.GOAL <= (opponent+25))
      return true;
    else if(this.getScore() < opponent && turnTotal < (hold*1.25))
      return true;
    else if(this.getScore() > opponent && turnTotal <= (hold/1.15))
      return true;
    else if(this.getScore() == opponent && turnTotal<hold)
      return true;
    else
      return false;
  }
  
}