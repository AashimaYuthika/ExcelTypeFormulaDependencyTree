package dependencytree;

import java.util.Scanner;

/**
 * @author Aashima
 */
public class DependencyTree {

    public static void main(String[] args) {
        Cell[][] cell = new Cell[2][2]; //For easier testing purposed we are taking into consideration a simple matrix of 2 X 2
        EvaluateExpression eVal = new EvaluateExpression(); //Object for calling the function that will evaluate a given expression using Stack
        double content; //content of the cell (numeric value, in case of a formula cell it stores the result of the expression)
        String input = "";
        int indexOfOperator = -1, row = 0, col = 0;

        System.out.println("Enter the cell content \nIt can be a constant value or a formula \nRemember, the matrix should contain atleast 1 constant");
        Scanner read = new Scanner(System.in);

        //Entering values into all cells
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                cell[i][j] = new Cell();
                System.out.println("Enter Row: " + i + " Column: " + j);
                System.out.println("For entering a formula press 0");
                content = read.nextDouble(); //the content of the cell

                //If the user wants to enter a formula into the cell
                if (content == 0) {
                    System.out.println("Enter the expression. Each cell should be written of the form: i,j for i denoting row and j denoting column");
                    System.out.println("For a constant value, simply enter the required constant");
                    input = read.next();
                    String exp = "";
                    String term;
                    indexOfOperator = -1;
                    int tempi, tempj;
                    for (int k = 0; k < input.length(); k++) {
                        char c = input.charAt(k);
                        if (c == '+' || c == '-' || c == '/' || c == '*' || k == input.length() - 1) {
                            if (k != input.length() - 1) {
                                term = input.substring(indexOfOperator + 1, k);
                            } else {
                                term = input.substring(indexOfOperator + 1);
                            }

                            if (term.length() == 1) {
                                exp += term + c;
                            } else {
                                tempi = Character.getNumericValue(term.charAt(0));
                                tempj = Character.getNumericValue(term.charAt(2));
                                exp += Double.toString(cell[tempi][tempj].getContent()) + c;
                                cell[i][j].setCausedBy(cell[tempi][tempj]); //Setting the cells that the current formula cell is dependent on
                                cell[tempi][tempj].setEffects(cell[i][j]); //Setting the cells the formula cells in the "Effects" field of the cells that it depends on
                            }
                            indexOfOperator = k;
                        }
                    }
                    cell[i][j].setContent(eVal.evaluate(exp.substring(0, exp.length() - 1)));
                    cell[i][j].setContentFormula(exp); //Setting the expression that the formula cell contains
                } else { //Otherwise if the user wants to enter a simple constant into the cell
                    cell[i][j].setContent(content);
                    cell[i][j].setContentFormula(null); //There is no formula in the constant cell
                    cell[i][j].setCausedBy(null); //The constant cell does not depend on any other cell
                }
            }
        }

        //Evaluate change in content of any given cell
        int choice;

        while (true) {
            System.out.println("------------------------------------------------------------------");
            System.out.println("1. Edit constant cell");
            System.out.println("2. Edit formula in a formula cell");
            System.out.println("3. Delete Cell");
            System.out.println("4. Display Matrix");
            System.out.println("5. Exit");
            System.out.println("Enter your choice");
            choice = read.nextInt();

            switch (choice) {
                case 1:
                    Cell temp;
                    System.out.println("Enter location of cell that you want to edit(Row and Column consecutively): ");
                    row = read.nextInt();
                    col = read.nextInt();
                    System.out.println("Enter New Value");
                    content = read.nextDouble();
                    cell[row][col].setContent(content);
                    for (int i = 0; i < cell[row][col].getEffects().size(); i++) { //time complexity = c1n
                        //getting the formula cells that are affected by the cell that we have edited
                        temp = cell[row][col].getEffects().get(i);  //time complexity = c2
                        temp.setContent(eVal.evaluate(temp.getContentFormula())); //recalculating the formula in the cell//time complexity = c3
                        int j = 0;
                        //changning values of the cells that the formula cell affects (if any)
                        if (temp.getEffects().size() != 0) { //time complexity = c4
                            while (temp.getEffects().size() < j) { //time complexity = c5n
                                Cell tempEffects = temp.getEffects().get(j); //time complexity = c6
                                tempEffects.setContent(eVal.evaluate(tempEffects.getContentFormula())); //time complexity = c7
                                j++;
                            }
                        }
                    }
                    
                    //Thus, the time complexity here is O(n^2)
                    break;
                case 2:
                    System.out.println("Enter location of cell that you want to edit(Row and Column consecutively): ");
                    row = read.nextInt();
                    col = read.nextInt();
                    System.out.println("Enter the expression. Each cell should be written of the form: i,j for i denoting row and j denoting column");
                    System.out.println("For a constant value, simply enter the required constant");
                    input = read.next();
                    String exp = "";
                    String term;
                    indexOfOperator = -1;
                    int tempi,
                     tempj;
                    for (int k = 0; k < input.length(); k++) {
                        char c = input.charAt(k);
                        if (c == '+' || c == '-' || c == '/' || c == '*' || k == input.length() - 1) {
                            if (k != input.length() - 1) {
                                term = input.substring(indexOfOperator + 1, k);
                            } else {
                                term = input.substring(indexOfOperator + 1);
                            }

                            if (term.length() == 1) {
                                exp += term + c;
                            } else {
                                tempi = Character.getNumericValue(term.charAt(0));
                                tempj = Character.getNumericValue(term.charAt(2));
                                exp += Double.toString(cell[tempi][tempj].getContent()) + c;
                            }
                            indexOfOperator = k;
                        }
                    }
                    cell[row][col].setContent(eVal.evaluate(exp.substring(0, exp.length() - 1)));
                    cell[row][col].setContentFormula(exp); //Setting the expression that the formula cell contains

                    for (int i = 0; i < cell[row][col].getEffects().size(); i++) {
                        temp = cell[row][col].getEffects().get(i);
                        temp.setContent(eVal.evaluate(temp.getContentFormula()));
                        int j = 0;
                        if (temp.getEffects().size() != 0) {
                            while (temp.getEffects().size() < j) {
                                Cell tempEffects = temp.getEffects().get(j);
                                tempEffects.setContent(eVal.evaluate(tempEffects.getContentFormula()));
                                j++;
                            }
                        }
                    }
                    break;
                case 3:
                    System.out.println("Enter location of cell that you want to delete(Row and Column consecutively): ");
                    row = read.nextInt();
                    col = read.nextInt();
                    cell[row][col].getEffects().clear(); //time complexity = c1
                    cell[row][col] = null; //time complexity = c1
                    break;
                    //Thus the time complexity here is a constant, O(1)     
                case 4:
                    for(int i = 0;i<2;i++){
                        for(int j = 0;j<2;j++){
                            System.out.print(cell[i][j].getContent()+"\t");
                        }
                        System.out.println();
                    }
                    break;
                case 5:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Please Enter Correct Choice\n");
            }
        }
    }
}
