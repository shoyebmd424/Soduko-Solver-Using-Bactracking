package hr.mlinx.sudoku_solver.payload;

import java.util.ArrayList;
import java.util.List;

public class PlacementResponse {

    private boolean valid;
    private final List<String> conflicts;

    public PlacementResponse(boolean valid) {
        this.valid = valid;
        conflicts = new ArrayList<>();
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public List<String> getConflicts() {
        return conflicts;
    }

    public void addConflict(String conflict) {
        conflicts.add(conflict);
    }

}
