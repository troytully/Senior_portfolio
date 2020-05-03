/**Troy Tully
  * this player holds at the given hold value.
  * */
public class SimpleHoldPlayer extends PigPlayer{
  public int hold;
  
  
  public SimpleHoldPlayer(){
     super();
    hold = 20;
}
  public SimpleHoldPlayer(String name){
    super(name);
    hold = 20;
  }
   public SimpleHoldPlayer(String name, int newHold){
    super(name);
    hold = newHold;
   }

  public boolean isRolling(int turnTotal, int opponent){
    if(this.getScore() + turnTotal > PigGame.GOAL)
      return false;
      if(turnTotal<hold)
          return true;
      else
        return false;  
  }
   
}