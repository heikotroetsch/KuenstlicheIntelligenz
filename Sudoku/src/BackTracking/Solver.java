package BackTracking;

class Solver {


  public Sudoku backtracking(Sudoku state) {
    int row = -1;
    int col = -1;
    boolean done = true;
    for (int x = 0; x < 9; x++) {
      for (int y = 0; y < 9; y++) {
        if (state.getBoard()[x][y] == 0) {
          row = x;
          col = y;
          done = false;
          break;
        }
      }
    }

    if (done) {
      return state;
    }

    for (int value = 1; value <= 9; value++) {
      if (state.verify(row, col, value)) {
        state.makeChange(row, col, value);
        if (backtracking(state) != null) {
          return state;
        } else {
          state.makeChange(row, col, 0);
        }
      }
    }
    return null;
  }
}
