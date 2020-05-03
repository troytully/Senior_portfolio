/**Troy Tully
  * this class performs simulations
  * */
public class Simulations extends PigGame{
  
  public Simulations(){
   super(); 
  }
  
  public static void firstAdvantage(long simulations){
    PigPlayer computer = new SimpleHoldPlayer("Player1");
    PigPlayer computer2 = new SimpleHoldPlayer("Player2");
    PigGame troy = new PigGame(computer,computer2);
    for(int i = 0; i < simulations; i++){
      troy.playGame();
    }
    System.out.println(computer.getWinRecord());
    System.out.println(computer2.getWinRecord());
  }
  
  public static boolean isFirstBetter(long simulations, PigPlayer first, PigPlayer second){
    for(int i = 0; i < simulations; i++){
      if(i <simulations/2){
        PigGame troy = new PigGame(first,second);
         troy.playGame();
      }
      else{
        PigGame troy = new PigGame(second,first);
         troy.playGame();
      }
     
      }
  System.out.println(first.getWinRecord());
   System.out.println(second.getWinRecord());
   double total = first.getWinRecord() + second.getWinRecord();
  System.out.println((first.getWinRecord()/total) + "%");
  System.out.println((second.getWinRecord()/total) + "%");;
   if(first.getWinRecord() > second.getWinRecord()){
     //System.out.println((first.getWinRecord()/total) + "%");
     return true;}
   else
     return false;
   }
    
    
    
public static void main (String[] args){
  Simulations troy = new Simulations();
int firstBest = 0;
int secondBest = 0;
 for(int h = 1; h < 10; h++){
//    System.out.print(h + " -->");
  for(int i = 0; i < 1; i++){
     PigPlayer computer = new StrategicPlayer("Player1");
    PigPlayer computer2 = new WatchOpponentPlayer("Player2");
  //troy.firstAdvantage(10000);
    boolean winner = troy.isFirstBetter(10000, computer, computer2);
    
  if(winner == true)
    firstBest++;
  else 
    secondBest++;
  }}
  System.out.println(firstBest);
System.out.println(secondBest);
 

} }