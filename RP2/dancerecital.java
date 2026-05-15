//Ryan Ramdihal, dancerecital kattis problem, Professor Arup Guha, 02/15/2022
import java.util.*;
public class dancerecital {
public static void main(String[] args) {
Scanner stdin = new Scanner(System.in);
int routines = stdin.nextInt();// input number of routines
String[] input = new String[routines]; // dance routine
for(int i = 0; i<routines; i++)
{
input[i] = stdin.next();//filling array of dance routines
}
int[] test = new int[routines]; //test diff permutations // setting index
for array
for(int buffer = 0; buffer < routines; buffer++)
{
test[buffer] = -1; //if empty set to -1
}
boolean[] tryRoutine = new boolean[routines];//keeps track of what dance is
present in test
int[] quick = new int[routines]; //to find lowest amount of changes
int high = 12345; // max number
int routineNum = 0; //tracking
while(true)
{
boolean dance = false; // dance routine works or not
int s = 0;//amount of changes comparison
for(int r = test[routineNum]+1; r < routines; r++) //check routines
{
if(!tryRoutine[r])
{
if(test[routineNum]>-1) //try
{
tryRoutine[test[routineNum]]=false;
}
test[routineNum] = r; // which routine we are in
tryRoutine[r] = true;
dance = true;
if(routineNum > 0)
{
quick[routineNum-1] = 0; //initialize
for(int character = 0;character<
input[test[routineNum]].length();character++) //loop each char of each routine
{
if(input[test[routineNum-
1]].indexOf(input[test[routineNum]].charAt(character)) != -1) //quick change if
necessary
{
quick[routineNum-1]++;// found change
}
}
for(int i = 0;i<routineNum;i++)
{
s +=quick[i];// changes
}
}
break; // break loop to backtrack if necessary
}
}
if(dance) //check
{
if(s < high)
{
if(routineNum == routines-1) //end
{
high = s;
}
else
{
routineNum++;// get to last routine
}
}
}
else //backtrack if not found
{
if(test[routineNum] > -1)
{//this means it not be empty so we have danceNum in there
//we've chosen a dance so have to clear
tryRoutine[test[routineNum]] = false;
test[routineNum] = -1;
}
routineNum--;//backtrack
if(routineNum<0)
{
break; // no possible backtrack
}
}
}
stdin.close();
System.out.println(high);
}
}

