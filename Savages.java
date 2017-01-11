

/**
 * Dinner class provides setter and getter methods for the stewed missionaries
 * @author Corie
 *
 */
class Dinner 
{
    private int dinner;
 
    public void makeDinner(int dinner){//constructor
        this.dinner = dinner;
    }
 
    public int takeDinner() {//dinner getter
    	return dinner;
    }
}
/**
 * The critical section
 * @author corie
 */
class Pot
{
	private static int M;//capacity of the pot, it can hold 10 stewed missionaries
    private Dinner dinner;//dinner object
    private boolean potRefilled;//status of the pot, if it is filled or not
 
    public Dinner takeDinner()//takes a serving
    {
        return dinner;
    }
    
    public boolean isPotFull() //returns a boolean based on the status of the pot
    {
        return potRefilled;
    }
    
    public void fullPot(boolean potRefilled) //refills pot. This is performed by the cook
    {
    	M=3;
        this.potRefilled = potRefilled;
    }
    public int getServings(){//returns M servings to the savages
    	return M;
    }
    public Pot(Dinner Dinner){
        super();
        this.dinner = Dinner;
        this.potRefilled = false;
    }
}

/**
 * SavageChef class handles the cooking of missionaries
 * @author Stougie
 *
 */
class SavChef implements Runnable{
    private Pot pot;
    private static int numMeals = 10;//value mustn't change with each object creation
  
    SavChef(Pot pot)//constructor 
    {
        this.pot = pot;
        System.out.println("The Savage Chef is going to prepare "+ numMeals + " meals total");
    }
    
    public void run() 
    {
        int count =0;//keeps track on the number of meals thus far 
        synchronized (pot)//pot is shared between both so it is taken in sync param
        {
            while (count < numMeals) 
            {
                while (pot.isPotFull()) 
                {
                    try
                    {
                        pot.wait(2000);
                        if (pot.isPotFull()) 
                        {
                            System.out.println("Dinner is over");
                            
                        }
                    } 
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
 
                }
                count++;
                System.out.println("\nSavage cook is cooking meal " + count);
                pot.takeDinner().makeDinner(count);
                pot.fullPot(true);//pot refilled
                pot.notify();
            }
        }
    }
}

/**
 * Class SavEater represents the savages that are eating
 * @author corie
 */
class SavEater implements Runnable 
{
    private Pot pot;
    
    SavEater(Pot pot)//constructor 
    {
        this.pot = pot;
    }
 
    public void run() 
    {
        synchronized (pot){//pot is shared between both so it is taken in sync param
            while (true) 
            {
                while (!pot.isPotFull()) //while pot is not refilled
                {
                    try
                    {
                        pot.wait(3000);
                        if (!pot.isPotFull()) 
                        {
                            System.out.println("\nDinner is over ");
                            return;
                        }
                    } 
                    catch (InterruptedException e) 
                    {
                        e.printStackTrace();
                    }
                }
                	for (int i = 0; i < 3; i++) {//3 savage feasters total
                		System.out.println("Savage feaster is eating meal "  + pot.takeDinner().takeDinner());
					}
                    
                    pot.fullPot(false);
                    pot.notify(); 
            }
 
        }
    }
}

/**
 * Main class Savages 
 * @author Corie
 *
 */
public class Savages {
    public static void main(String arg[]) {
        Pot pot = new Pot(new Dinner());//creates a pot object whose constructor takes a Dinner type argument;The pot is also the critical section
        new Thread(new SavEater(pot)).start();//A thread is created for the savage eater. They both interact with the pot
        new Thread( new SavChef(pot)).start();//A thread is created for the savage chef. They both interact with the pot
    }
}
