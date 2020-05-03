/**Troy Tully
  * this class is for users to play
  * */
import java.util.Scanner;
public class UserPigPlayer extends PigPlayer{
  public UserPigPlayer(){
    super();
  }
  public UserPigPlayer(String newName){
    super(newName); 
  }
  public boolean isRolling(int player, int opponent){
    Scanner keyboard = new Scanner(System.in);
    System.out.println("Would you like to roll again? Press enter to roll, or any letter to hold!");
      String ans = keyboard.nextLine();
      if(ans.equals(""))
          return true;
      else
        return false;
      
  }
//  public static void main (String[] args){
//    UserPigPlayer troy = new UserPigPlayer("troy");
//    troy.isRolling(5,10);
    
    
    
    
  
  
  
}