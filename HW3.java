
public class VampireNumbers {
	 /* @author Emilee Mapanao
	 *  November 3, 2022
	 *  Multi-threaded program to find all vampire numbers
	 *  between 100,000 and 999,999
	 */
	static Thread t1, t2;
	
	public static void main(String[] args) {
		long startTime = System.nanoTime();
		
		// Make instances and begin threads
		vampireNumberFinder evens = new vampireNumberFinder();
		vampireNumberFinder odds = new vampireNumberFinder();
		t1 = new Thread(evens);
		t1.setName("Even thread");
		t2 = new Thread(odds);
		t2.setName("Odd thread");
		t1.start();
		t2.start();
       // Finish odd and even thread before finishing main
        try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			System.out.println("Exception caught: ");
			e.printStackTrace();
		}
        // find elapsed time 
    	long endTime   = System.nanoTime();
		long elapsedTime = endTime - startTime;
		double elapsedTimeInSeconds = (double) elapsedTime / 1_000_000_000;
       int total = evens.getTotal() + odds.getTotal();
       System.out.print("The total number of vampire numbers is: " + total + " in " + elapsedTimeInSeconds + "seconds");
	}

}
// Class to find all even vampire numbers
class vampireNumberFinder implements Runnable {
	int total = 0;
	int firstFang, secondFang;
	int alreadyPrinted;
	
	// find even numbers and apply rules  
	public void getEvenFangs(int num) {
		 String numString = String.valueOf(num);  // parse  to string
		 int numOfDigits = String.valueOf(num).length(); 
		 if (num%2 == 0 && numOfDigits%2 == 0) { // if i is even and even amount of digits
				isVampire(numString, "", num);
			}	
		
	}	
	
	// find odd numbers and apply rules
	public void getOddFangs(int num) {
		 String numString = String.valueOf(num);  // parse to string
		 int numOfDigits = String.valueOf(num).length(); 
		 if (num%2 == 1 && numOfDigits%2 == 0) { // if i is odd and even amount of digits
				isVampire(numString, "", num);
			}
	}
	
	public void isVampire(String str, String ans, int num)  {
        // If string is empty
        if (str.length() == 0) { 
        	int mid = (String.valueOf(ans).length())/2;// Split in half
    		String sub1 = ans.substring(0, mid); // parse to string for comparison
			String sub2 = ans.substring(mid);
		 	firstFang = Integer.parseInt(sub1);        // parse to int for processing
			secondFang = Integer.parseInt(sub2);
			if (sub1.endsWith("0") && sub2.endsWith("0")) {
				// skip
			}
			else if (firstFang * secondFang == num && num != alreadyPrinted) {
				if (Thread.currentThread().getName() == "Even thread") {
					System.out.print("First worker found:  ");
				}
				else if (Thread.currentThread().getName() == "Odd thread") {
					System.out.print("Second worker found: ");
				}
					System.out.println(num + "\t Fangs: " +"(" + firstFang +", " + secondFang + ")");
					alreadyPrinted = num;
					total++;
			}    
       
            return;
        }
        // find permutations of num
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            String ros = str.substring(0, i) +
                        str.substring(i + 1);
 
            // Recursive call
            isVampire(ros, ans + ch, num);
            
        }
        
    }

	public void run() {
		for (int i = 100000; i < 999998; i++) {
			if (Thread.currentThread().getName() == "Odd thread") {
				getOddFangs(i);
			}
			else if (Thread.currentThread().getName() == "Even thread"){
				getEvenFangs(i);
			}
		}
	}
	public int getTotal() {
		return total;
	}
}
