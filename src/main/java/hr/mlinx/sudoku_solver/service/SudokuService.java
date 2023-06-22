package hr.mlinx.sudoku_solver.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class SudokuService {

    private static final int N = 9; // # of rows/columns, also # of non-zero digits
    private static final int M = 3;  // 3x3 regions

    public boolean isNotValidSudokuString(String sudokuString) {
        return !Pattern
                .compile("^[1-9.]{81}$")
                .matcher(sudokuString)
                .matches();
    }
    
    public boolean checkRowPlacement(String sudokuString, int row, int column, char value) {
        for (int j = 0; j < N; ++j)
            if (j != column && sudokuString.charAt(row * N + j) == value)
                return false;

        return true;
    }

    public boolean checkColumnPlacement(String sudokuString, int row, int column, char value) {
        for (int i = 0; i < N; ++i)
            if (i != row && sudokuString.charAt(i * N + column) == value)
                return false;

        return true;
    }

    public boolean checkRegionPlacement(String sudokuString, int row, int column, char value) {
        int regRowStart = Math.floorDiv(row, M);
        int regColStart = Math.floorDiv(column, M);

        for (int i = regRowStart * M; i < (regRowStart + 1) * M; ++i)
            for (int j = regColStart * M; j < (regColStart + 1) * M; ++j)
                if ((i != row || j != column) && sudokuString.charAt(i * N + j) == value)
                    return false;

        return true;
    }

    public String solve(String sudokuString) {
        char[][] sudoku = new char[N][N];
        List<List<Character>> rows = new ArrayList<>();
        List<List<Character>> columns = new ArrayList<>();
        List<List<Character>> regions = new ArrayList<>();

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j)
                sudoku[i][j] = sudokuString.charAt(i * N + j);

            rows.add(new ArrayList<>());
            columns.add(new ArrayList<>());
            regions.add(new ArrayList<>());
        }

        char cell;
        for (int i = 0, regRow, regCol; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (sudoku[i][j] != '.') {
                    regRow = Math.floorDiv(i, M);
                    regCol = Math.floorDiv(j, M);
                    cell = sudoku[i][j];

                    if (rows.get(i).contains(cell)
                    || columns.get(j).contains(cell)
                    || regions.get(regRow * M + regCol).contains(cell))
                        return "";

                    rows.get(i).add(cell);
                    columns.get(j).add(cell);
                    regions.get(region(regRow, regCol)).add(cell);
                }
            }
        }

        if (depthFirstSearch(sudoku, 0, rows, columns, regions))
            return Arrays
                    .stream(sudoku)
                    .map(String::valueOf)
                    .reduce("", String::concat);

        return "";
    }

    private int region(int regRow, int regCol) {
        return regRow * M + regCol;
    }

    private boolean depthFirstSearch(
            char[][] sudoku,
            int idx,
            List<List<Character>> rows,
            List<List<Character>> columns,
            List<List<Character>> regions
    ) {
        if (idx == N * N)
            return true;

        int row = Math.floorDiv(idx, N);
        int column = idx % N;
        int regRow = Math.floorDiv(row, M);
        int regCol = Math.floorDiv(column, M);

        if (sudoku[row][column] != '.')
            return depthFirstSearch(sudoku, idx + 1, rows, columns, regions);

        char cell;
        for (int i = 1; i <= N; ++i) {
            cell = (char) ('0' + i);

            if (rows.get(row).contains(cell)) continue;
            if (columns.get(column).contains(cell)) continue;
            if (regions.get(region(regRow, regCol)).contains(cell)) continue;

            rows.get(row).add(cell);
            columns.get(column).add(cell);
            regions.get(region(regRow, regCol)).add(cell);
            sudoku[row][column] = cell;

            if (depthFirstSearch(sudoku, idx + 1, rows, columns, regions))
                return true;

            rows.get(row).remove((Character) cell);
            columns.get(column).remove((Character) cell);
            regions.get(region(regRow, regCol)).remove((Character) cell);
            sudoku[row][column] = '.';
        }

        return false;
    }

}
