//Tentaizu P2, Ryan Ramdihal, COP 3503, Professor Guha. 01/25/2022
import java.util.*;
public class tentaizu
{
static int[][] square = new int[7][7];
static int[][] t = new int[7][7];
static int[][] t2 = new int[7][7];
static int fill;
public static void main(String[] args)throws Exception
{
Scanner stdin = new Scanner(System.in);
int cases = stdin.nextInt(); //read cases
for(int i = 1; i <= cases; i++)
{
for(int y = 0; y < 7; y++)
{
char[] row = stdin.next().toCharArray(); //read in lines from
scanner 7 by 7 square
for(int x = 0; x < 7; x++)
{
square[y][x] = 0;
t2[y][x] = 0;
t[y][x] = -1;
if(Character.isDigit(row[x])) //if index is number or dot
t[y][x] = row[x] - '0';
}
}
fill = 0;
System.out.printf("Tentaizu Board #%d:%n", i); //print
if(!solve(0, 0))
System.out.printf("No solution found!%n");
System.out.printf("%n");
}
stdin.close();
}
static boolean solve(int row, int col) {
if(col == 7)
{ //to next row
col = 0;
row++;
}
if(row == 7)
{ //diagonal + checking
if(fill != 10)
return false;
if(t[5][5] != -1 && t[5][5] != t2[5][5])
return false;
if(t[5][6] != -1 && t[5][6] != t2[5][6])
return false;
for(col = 0; col < 7; col++)
{
if(t[6][col] != -1 && t[6][col] != t2[6][col])
return false;
}
for(row = 0; row < 7; row++)
{
for(col = 0; col < 7; col++)
{
//print board
if(square[row][col] != 0)
System.out.printf("*");
else if(t[row][col] != -1)
System.out.printf("%d", t[row][col]);
else
System.out.printf(".");
}
System.out.printf("%n");
}
return true;// ends after recursive functions is complete
}
if(row >= 1 && col >= 2) //checks diagnols
{
if(t[row - 1][col - 2] != -1 && t[row - 1][col - 2] != t2[row -
1][col - 2])
return false;
}
if(row >= 2 && col == 0)
{
if(t[row - 2][5] != -1 && t[row - 2][5] != t2[row - 2][5])
return false;
if(t[row - 2][6] != -1 && t[row - 2][6] != t2[row - 2][6])
return false;
}
if(t[row][col] != -1)
//recursive/backtracking
return solve(row, col + 1);
for(int diax = -1; diax <= 1; diax++) //checking
{
for(int diay = -1; diay <= 1; diay++)
{
if(diax == 0 && diay == 0)
continue;
if(row + diay < 0 || col + diax < 0)
continue;
if(row + diay >= 7 || col + diax >= 7)
continue;
t2[row + diay][col + diax]++;
}
}
fill++;
square[row][col] = 1;
if(solve(row, col + 1)) //recursive/backtracking
return true;
fill--;
square[row][col] = 0; //reset
for(int diax = -1; diax <= 1; diax++)
{
for(int diay = -1; diay <= 1; diay++) //checking
{
if(diax == 0 && diay == 0)
continue;
if(row + diay < 0 || col + diax < 0)
continue;
if(row + diay >= 7 || col + diax >= 7)
continue;
t2[row + diay][col + diax]--;
}
}
return solve(row, col + 1);
}
}
