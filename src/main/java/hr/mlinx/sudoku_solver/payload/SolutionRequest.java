package hr.mlinx.sudoku_solver.payload;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SolutionRequest {

    private String puzzle;

    @JsonCreator
    public SolutionRequest(@JsonProperty("puzzle") String puzzle) {
        this.puzzle = puzzle;
    }

    public String getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(String puzzle) {
        this.puzzle = puzzle;
    }

}
