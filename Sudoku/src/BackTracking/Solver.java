package BackTracking;

class Solver {


  public Sudoku backtracking(Sudoku state, int n) {
    int row = -1;
    int col = -1;
    boolean done = true;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (state.getBoard()[i][j] == 0) {
          row = i;
          col = j;
          done = false;
          break;
        }
      }
    }

    if (done) {
      return state;
    }

    for (int value = 1; value <= n; value++) {
      if (state.verify(row, col, value)) {
        state.makeChange(row, col, value);
        if (backtracking(state, n) != null) {
          return state;
        } else {
          state.makeChange(row, col, 0);
        }
      }
    }
    return null;
  }
}
