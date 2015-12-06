/*
 * Randy Ram
 * 808000428
 * 
 * Problem: Given an m x n matrix of numbers, find the minimum cost path
 *          column 1 to column n.
 * 
 * Solution: For each cell in the matrix, calculate the minimnum cost to reach that cell from
 *           the 3 previous cells before it. Repeat for all cells. The cell in the
 *           last column with the smallest value is the minimum path from a cell in the first column.
 *           Retrace using the costs 
 * 
 *           For each entry in the initial m x n,  M, we populate an m x n CostMatrix:
 *           Initially declare the first column in both matrices to be the same. 
 *           Calculate the remaining positions for CostMatrix using the below recurrence:
 * 
 *           CostMatrix[i][j] = Min(CostMatrix[i][j-1] + M[i][j], CostMatrix[i+1][j-1] + M[i][j], CostMatrix[m][j-1] + M[i][j])   if i == 0,
 *                            = Min(CostMatrix[i][j-1] + M[i][j], CostMatrix[i-1][j-1] + M[i][j], CostMatrix[0][j-1] + M[i][j])   if i == n,
 *                            = Min(CostMatrix[i][j-1] + M[i][j], CostMatrix[i-1][j-1] + M[i][j], CostMatrix[i+1][j-1] + M[i][j]) if i != 0 && i != n
 * 
 *         Running time of this algorithm is: O(mn) where m and n are the dimensions of the matrix
 */

import java.util.*;
import java.io.*;

public class ramrMinpath
{
    public static void main(String[] args)
    {
        try
        {
            ramrMinpath r = new ramrMinpath();
            Scanner in = new Scanner(new File ("input.txt"));
            int numRows, numCols;
            numRows = in.nextInt();
            numCols = in.nextInt();
            int inputMatrix[][] = new int[numRows][numCols];
            //System.out.println(numRows + " " + numCols);
            for(int i =0; i < numRows; i++)
            {
                for(int j = 0; j < numCols; j++)
                {
                    inputMatrix[i][j] = in.nextInt();
                }
            }
            
            r.findMinpath(numRows, numCols, inputMatrix);
        }
      catch (FileNotFoundException e)
      {
          System.out.println("Cannot find file");
      }
      catch (IOException e) 
      {
          e.printStackTrace();
      }
    }
    
   private void findMinpath(int numRows, int numCols, int inputMatrix[][])
   {
       try
       {
           BufferedWriter output = null;
           File file = new File("output.txt");
           output = new BufferedWriter(new FileWriter(file));
           int costMatrix[][] = new int[numRows][numCols];
           for(int i = 0; i < numRows; i++)
           {
               costMatrix[i][0] = inputMatrix[i][0];
           }
           
           //Get and populate min cost array
           for(int j = 1; j < numCols; j++)
           {
               for(int i = 0; i < numRows; i++)
               {
                   //If we're evaluating the top row, we do wrap around checks
                   if(i == 0)
                   {
                       int num1 = inputMatrix[i][j] + costMatrix[i][j-1]; //horizontal
                       int num2 = inputMatrix[i][j] + costMatrix[i+1][j-1]; //lower diagonal
                       int num3 = inputMatrix[i][j] + costMatrix[numRows - 1][j-1]; //wrap around to below
                       int minVal = Math.min(num1, Math.min(num2, num3));
                       costMatrix[i][j] = minVal;
                       //System.out.println(minVal);
                   }
                   
                   else if(i == numRows - 1)
                   {
                       int num1 = inputMatrix[i][j] + costMatrix[i][j-1]; //horizontal
                       int num2 = inputMatrix[i][j] + costMatrix[i-1][j-1]; //upper diagonal
                       int num3 = inputMatrix[i][j] + costMatrix[0][j-1]; //wrap around to top
                       int minVal = Math.min(num1, Math.min(num2, num3));
                       costMatrix[i][j] = minVal;
                   }
                   else
                   {
                       int num1 = inputMatrix[i][j] + costMatrix[i][j-1]; //horizontal
                       int num2 = inputMatrix[i][j] + costMatrix[i-1][j-1]; //upper diagonal
                       int num3 = inputMatrix[i][j] + costMatrix[i+1][j-1]; //lower diagonal
                       int minVal = Math.min(num1, Math.min(num2, num3));
                       costMatrix[i][j] = minVal;
                   }
                   
               }
           }
           
           //Get the minimum cost of path from the last column
           int minPathCost = costMatrix[0][numCols - 1];
           int row = 0;
           for(int i = 1; i < numRows; i++)
           {
               if(costMatrix[i][numCols-1] < minPathCost)
               {
                   minPathCost = costMatrix[i][numCols - 1];
                   row = i;
               }
           }
           
           //Find the path back to the beginning.
           int rowPaths[] = new int[numCols];
           int currentIndex = numCols - 1; //The current column that we're considering
           rowPaths[currentIndex] = row;
           currentIndex--;
           
           int curVal = minPathCost;
           int currentRow = row;
           int currentCol = numCols - 1;
           while(currentCol > 0)
           {
               if(currentRow == 0)
               {
                   if(costMatrix[currentRow][currentCol] - inputMatrix[currentRow][currentCol] 
                          == costMatrix[currentRow][currentCol - 1]) //Horizontal
                   {
                       rowPaths[currentIndex] = currentRow;
                       
                   }
                   else if(costMatrix[currentRow][currentCol] - inputMatrix[currentRow][currentCol] 
                               == costMatrix[currentRow + 1][currentCol - 1]) //Lower Diagonal
                   {
                       rowPaths[currentIndex] = currentRow + 1;
                       currentRow = currentRow + 1;
                   }
                   else if(costMatrix[currentRow][currentCol] - inputMatrix[currentRow][currentCol] 
                               == costMatrix[numRows - 1][currentCol - 1]) //upper-diagonal wrap around
                   {
                       rowPaths[currentIndex] = numRows - 1;
                       currentRow = numRows - 1;
                   }
                   currentIndex--;
                   currentCol--;
               }
               
               else if(currentRow == numRows - 1)
               {
                   if(costMatrix[currentRow][currentCol] - inputMatrix[currentRow][currentCol] 
                          == costMatrix[currentRow][currentCol - 1]) //Horizontal
                   {
                       rowPaths[currentIndex] = currentRow;
                   }
                   else if(costMatrix[currentRow][currentCol] - inputMatrix[currentRow][currentCol] 
                               == costMatrix[currentRow - 1][currentCol - 1]) //Upper diagonal
                   {
                       rowPaths[currentIndex] = currentRow - 1;
                       currentRow = currentRow - 1;
                   }
                   else if(costMatrix[currentRow][currentCol] - inputMatrix[currentRow][currentCol] 
                               == costMatrix[0][currentCol - 1]) //lower diagonal wrap around
                   {
                       rowPaths[currentIndex] = 0;
                       currentRow = 0;
                   }
                   currentIndex--;
                   currentCol--;
               }
               else
               {
                   if(costMatrix[currentRow][currentCol] - inputMatrix[currentRow][currentCol] 
                          == costMatrix[currentRow][currentCol - 1]) //Horizontal
                   {
                       rowPaths[currentIndex] = currentRow;
                   }
                   else if(costMatrix[currentRow][currentCol] - inputMatrix[currentRow][currentCol] 
                               == costMatrix[currentRow - 1][currentCol - 1]) //Upper diagonal
                   {
                       rowPaths[currentIndex] = currentRow - 1;
                       currentRow = currentRow - 1;
                   }
                   else if(costMatrix[currentRow][currentCol] - inputMatrix[currentRow][currentCol] 
                               == costMatrix[currentRow + 1][currentCol - 1]) //lower diagonal
                   {
                       rowPaths[currentIndex] = currentRow + 1;
                       currentRow = currentRow + 1;
                   }
                   currentIndex--;
                   currentCol--;
               }
           }
           
           //System.out.println(minPathCost);
           output.write(minPathCost + "\n");
           for(int i = 0; i < rowPaths.length; i++)
           {
               //System.out.print((rowPaths[i] + 1) + " "); //We add one to it since we used 0 based array indexing instead of 1 based.
               output.write((rowPaths[i] + 1) + " ");
           }
           output.close();
       }
      catch (IOException e)
       {
           e.printStackTrace();
       }
   }
}