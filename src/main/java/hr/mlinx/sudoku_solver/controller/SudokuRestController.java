package hr.mlinx.sudoku_solver.controller;

import hr.mlinx.sudoku_solver.payload.*;
import hr.mlinx.sudoku_solver.service.SudokuService;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@RestController
@RequestMapping("api")
public class SudokuRestController {

    private static final Logger log = LoggerFactory.getLogger(SudokuRestController.class);

    private final SudokuService sudokuService;

    public SudokuRestController(SudokuService sudokuService) {
        this.sudokuService = sudokuService;
    }

    @PostMapping("/solve")
    public ResponseEntity<?> solve(@RequestBody SolutionRequest solutionRequest) {
        String puzzle = solutionRequest.getPuzzle();

        log.info("POST request to solve puzzle: {}", puzzle);

        if (StringUtils.isBlank(puzzle))
            return ResponseEntity.ok(new ErrorResponse("Required field missing"));

        if (sudokuService.isNotValidSudokuString(puzzle))
            return ResponseEntity.ok(new ErrorResponse("Invalid puzzle"));

        String solution = sudokuService.solve(puzzle);
        return ResponseEntity.ok(
                solution.isBlank()
                        ? new ErrorResponse("Puzzle cannot be solved")
                        : new SolutionResponse(solution)
        );
    }

    @PostMapping("/check")
    public ResponseEntity<?> check(@RequestBody PlacementRequest placementRequest) {
        String puzzle = placementRequest.getPuzzle(),
                coordinate = placementRequest.getCoordinate(),
                value = placementRequest.getValue();

        log.info("POST request to check {} coordinate for value {} in puzzle {}", coordinate, value, puzzle);

        if (StringUtils.isBlank(puzzle)
        || StringUtils.isBlank(coordinate)
        || StringUtils.isBlank(value))
            return ResponseEntity.ok(new ErrorResponse("Required field(s) missing"));

        if (sudokuService.isNotValidSudokuString(puzzle))
            return ResponseEntity.ok(new ErrorResponse("Invalid puzzle"));

        coordinate = coordinate.toLowerCase();
        if (!Pattern
                .compile("^[a-i][1-9]$")
                .matcher(coordinate)
                .matches())
            return ResponseEntity.ok(new ErrorResponse("Invalid coordinate"));

        if (!Pattern
                .compile("^[1-9]$")
                .matcher(value)
                .matches())
            return ResponseEntity.ok(new ErrorResponse("Invalid value"));

        int row = coordinate.charAt(0) - 'a';
        int column = coordinate.charAt(1) - '1';

        boolean checkRow = sudokuService.checkRowPlacement(puzzle, row, column, value.charAt(0));
        boolean checkColumn = sudokuService.checkColumnPlacement(puzzle, row, column, value.charAt(0));
        boolean checkRegion = sudokuService.checkRegionPlacement(puzzle, row, column, value.charAt(0));

        PlacementResponse placementResponse = new PlacementResponse(true);

        if (checkRow && checkColumn && checkRegion)
            return ResponseEntity.ok(placementResponse);

        placementResponse.setValid(false);

        if (!checkRow) placementResponse.addConflict("row");
        if (!checkColumn) placementResponse.addConflict("column");
        if (!checkRegion) placementResponse.addConflict("region");

        return ResponseEntity.ok(placementResponse);
    }

}
