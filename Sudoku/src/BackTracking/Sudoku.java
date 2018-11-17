package BackTracking;

public class Sudoku {

  private int[][] board = new int[9][9];

  public Sudoku(int[][] state) {
    for (int row = 0; row < 9; row++)
      for (int col = 0; col < 9; col++)
        board[row][col] = state[row][col];
  }
  
  public int[][] getBoard(){
    return this.board;
  }
  
  public void makeChange(int row, int col, int num) {
    this.board[row][col] = num;
  }

  public boolean verify(int row, int col, int num) {
    return checkRowClash(row, col, num)&&checkColClash(row, col, num)&&threeByThree(row, col, num);
  }
  
  private boolean checkRowClash(int row, int col, int num) {
    //checken ob die nummer in diese Reihe schon existiert.
    for (int colCount = 0; colCount < board.length; colCount++) {
      if (board[row][colCount] == num) {
        return false;
      }
    }
    return true;
  }
  
  private boolean checkColClash(int row, int col, int num) {
    //checken ob die nummer schon in diese Zeile existiert. 
    for (int rowCount = 0; rowCount < board.length; rowCount++) {
      if (board[rowCount][col] == num) {
        return false;
      }
    }
    return true;
  }
  
  private boolean threeByThree(int row, int col, int num) {
    //checken ob die nummer schon in einem 3 bei 3 Quadrat vorkommt.
    int startX = row - row % 3;
    int startY = col - col % 3;
    
    for (int x = startX; x < startX + 3; x++) {
      for (int y = startY; y < startY + 3; y++) {
        if (board[x][y] == num) {
          return false;
        }
      }
    }
    return true;
  }
  
  

  public void print() {    
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[row].length; col++) {
        System.out.print(board[row][col]);
        System.out.print(" ");
      }
      System.out.print("\n");
    }
    System.out.print("\n");
  }

}
