package hr.mlinx.sudoku_solver.payload;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlacementRequest {

    private String puzzle;
    private String coordinate;
    private String value;

    @JsonCreator
    public PlacementRequest(
            @JsonProperty("puzzle") String puzzle,
            @JsonProperty("coordinate") String coordinate,
            @JsonProperty("value") String value
    ) {
        this.puzzle = puzzle;
        this.coordinate = coordinate;
        this.value = value;
    }

    public String getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(String puzzle) {
        this.puzzle = puzzle;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
