package hr.mlinx.sudoku_solver.payload;

public class SolutionResponse {

    private String solution;

    public SolutionResponse(String solution) {
        this.solution = solution;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

}
