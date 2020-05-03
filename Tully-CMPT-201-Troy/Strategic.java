public class StrategicPlayer extends PigPlayer{
  public int hold;
  
  
  public StrategicPlayer(){
    super();
    hold = 20;
  }
  public StrategicPlayer(String name){
    super(name);
    hold = 20;
  }
  public StrategicPlayer(String name, int newHold){
    super(name);
    hold = newHold;
  }
  
  public boolean isRolling(int turnTotal, int opponent){
    if(PigGame.GOAL <= (opponent+35))
      return true;
    else if(this.getScore() < opponent && turnTotal < (hold*1.25))
      return true;
    else if(this.getScore() > opponent && this.getScore()-opponent >= 25 && turnTotal < hold/2)
      return true;
    else if(this.getScore() > opponent &&  turnTotal < (hold))
      return true;
    else if(this.getScore() == opponent && turnTotal<hold)
      return true;
    else
      return false;
  }
  
}