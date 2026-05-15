//Ternarian Weights, Kattis, COP 3503, Ryan Ramdihal
import java.util.*;

class weights {
	public static void main(String[] args) 
	{
		Scanner stdin = new Scanner(System.in);
		int n = stdin.nextInt();

		int threes[] = new int[20];			//to the power of three
		threes[0] = 1; //change val to 1



		for (int i = 1; i < 20; i++)
			threes[i] = threes[i - 1] + threes[i - 1] + threes[i - 1];

		for (int cases = 0; cases < n; cases++)  //cases
		{
			int weights = stdin.nextInt();
			int r[] = new int[20]; 
			for (int i = 19; i >= 0; i--) //base 3
			{   
				while (weights >= threes[i]) 
				{
					weights -= threes[i];
					r[i]++;
				}
			}



			for (int i = 0; i < 19; i++) 
			{

				if (r[i] == 3) 
				{
					r[i + 1]++;
					r[i] = 0;
				} 

				else if (r[i] == 2)		// values with base 2 cant be negative
				{
					r[i + 1]++;
					r[i] = -1;
				}
				

			}

			ArrayList<Integer> left = new ArrayList<Integer>();		// add from array list for output
			ArrayList<Integer> right = new ArrayList<Integer>();

			for (int i = 19; i >= 0; i--) 			//evens out scale
			{
				if (r[i] < 0)
					left.add(i);
				else if (r[i] > 0)
					right.add(i);
			}

			System.out.print("left pan:");

			for (Integer i : left)
				System.out.print(" " + threes[i]);
			System.out.println();

			System.out.print("right pan:");

			for (Integer i : right)
				System.out.print(" " + threes[i]);
			System.out.println();

			System.out.println();
		}

		stdin.close();
	}
}
