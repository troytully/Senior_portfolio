/**Troy Tully
  * this class rolls a die
  * */
public class Die{
  
  private int sides;
  
  public Die(int newSide){
    sides = newSide;
  }
  public Die(){
    sides = 6;
  }
  public static int roll(){
    double num;
    int end = 0;
    num = Math.random();
    if(num < 0.17)
      end = 1;
    else if (num < 0.33)
      end = 2;
    else if (num < 0.50)
      end = 3;
    else if (num < 0.67)
      end = 4;
    else if (num < 0.84)
      end = 5;
    else if (num < 1)
      end = 6;
    
    return end;
  }
  public void setSides(int newSide){
    sides = newSide;
  }
  public int getSides(){
    return sides; 
  }
  public int rollDie(){
    double num;
    int end;
    end  = 0;
    int side = this.getSides() - 1 ;
    num = Math.random() * (side + 1);
    while(side >= 0){
      if (num > side){
        end = side + 1;
        side = -1;
      }
      else
        side--;
    }
    return end;
  }
  
  
  public static void main (String[] args){
    int one,two,three,four,five,six, seven, eight, i;
    one = two = three = four = five = six = seven = eight = i = 0;
    Die troy = new Die(8);
    while(i<1000){
      int x = troy.rollDie();
      if(x == 1)
        one++;
      else if(x==2)
        two++;
      else if(x==3)
        three++;
      else if(x==4)
        four++;
      else if(x==5)
        five++;
      else if (x==6)
        six++;
      else if (x==7)
        seven++;
      else
        eight++;
      i++;
    }
    System.out.println(one);
    System.out.println(two);
    System.out.println(three);
    System.out.println(four);
    System.out.println(five);
    System.out.println(six);
    System.out.println(seven);
    System.out.println(eight);
  }
  
}