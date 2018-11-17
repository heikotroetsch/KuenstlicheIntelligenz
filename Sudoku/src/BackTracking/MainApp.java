package BackTracking;

public class MainApp {

  public static Solver solver = new Solver();
  
  public static int[][] sudoku1 = new int[9][9];
  public static int[][] sudoku2 = new int[9][9];
  public static int[][] sudoku3 = new int[9][9];
  
  public static Sudoku solvedSudoku1;
  public static Sudoku solvedSudoku2;
  public static Sudoku solvedSudoku3;

  public static void main(String[] args) {
    initializeSudokus();

    solvedSudoku1 = solver.backtracking(new Sudoku(sudoku1));
    print(solvedSudoku1);
    
    solvedSudoku2 = solver.backtracking(new Sudoku(sudoku2));
    print(solvedSudoku2);

    solvedSudoku3 = solver.backtracking(new Sudoku(sudoku3));
    print(solvedSudoku3);
    
  }

  private static void print(Sudoku solved){
    System.out.println("Solved Sudoku:");
    if (solved != null) {
      solved.print();
    } else {
      System.out.println("Sudoku can not be solved.\n");
    }
  }

  private static void initializeSudokus() {
    // Sudoku 1
    sudoku1[0][0] = 8;
    sudoku1[2][6] = 2;
    sudoku1[4][6] = 7;
    sudoku1[6][8] = 8;
    sudoku1[8][6] = 4;
    sudoku1[1][2] = 3;
    sudoku1[3][1] = 5;
    sudoku1[5][3] = 1;
    sudoku1[7][2] = 8;
    sudoku1[1][3] = 6;
    sudoku1[3][5] = 7;
    sudoku1[5][7] = 3;
    sudoku1[7][3] = 5;
    sudoku1[2][1] = 7;
    sudoku1[4][4] = 4;
    sudoku1[6][2] = 1;
    sudoku1[7][7] = 1;
    sudoku1[2][4] = 9;
    sudoku1[4][5] = 5;
    sudoku1[6][7] = 6;
    sudoku1[8][1] = 9;

    // Sudoku 2
    sudoku2[0][1] = 7;
    sudoku2[0][5] = 6;
    sudoku2[1][0] = 9;
    sudoku2[1][7] = 4;
    sudoku2[1][8] = 1;
    sudoku2[2][2] = 8;
    sudoku2[2][5] = 9;
    sudoku2[2][7] = 5;
    sudoku2[3][1] = 9;
    sudoku2[3][5] = 7;
    sudoku2[3][8] = 2;
    sudoku2[4][2] = 3;
    sudoku2[4][6] = 8;
    sudoku2[5][0] = 4;
    sudoku2[5][3] = 8;
    sudoku2[5][7] = 1;
    sudoku2[6][1] = 8;
    sudoku2[6][3] = 3;
    sudoku2[6][6] = 9;
    sudoku2[7][0] = 1;
    sudoku2[7][1] = 6;
    sudoku2[7][8] = 7;
    sudoku2[8][3] = 5;
    sudoku2[8][7] = 8;

    // Sudoku 3
    sudoku3[0][0] = 5;
    sudoku3[0][1] = 3;
    sudoku3[0][4] = 7;
    sudoku3[1][0] = 6;
    sudoku3[1][3] = 1;
    sudoku3[1][4] = 9;
    sudoku3[1][5] = 5;
    sudoku3[2][1] = 9;
    sudoku3[2][2] = 8;
    sudoku3[2][7] = 6;
    sudoku3[3][0] = 8;
    sudoku3[3][4] = 6;
    sudoku3[3][8] = 3;
    sudoku3[4][0] = 4;
    sudoku3[4][3] = 8;
    sudoku3[4][5] = 3;
    sudoku3[4][8] = 1;
    sudoku3[5][0] = 7;
    sudoku3[5][4] = 2;
    sudoku3[5][8] = 6;
    sudoku3[6][1] = 6;
    sudoku3[6][6] = 2;
    sudoku3[6][7] = 8;
    sudoku3[7][3] = 4;
    sudoku3[7][4] = 1;
    sudoku3[7][5] = 9;
    sudoku3[7][8] = 5;
    sudoku3[8][4] = 8;
    sudoku3[8][7] = 7;
    sudoku3[8][8] = 9;

  }

}
