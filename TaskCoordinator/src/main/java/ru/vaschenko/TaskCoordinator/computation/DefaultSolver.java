package ru.vaschenko.TaskCoordinator.computation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.TaskCoordinator.annotation.GridComponent;
import ru.vaschenko.TaskCoordinator.annotation.GridMethod;
import ru.vaschenko.TaskCoordinator.annotation.GridParam;
import ru.vaschenko.TaskCoordinator.dto.ResultLatinSquare;
import ru.vaschenko.TaskCoordinator.dto.SubTask;
import ru.vaschenko.TaskCoordinator.enams.TypeComponent;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@GridComponent(TypeComponent.SOLVER)
public class DefaultSolver {

  @GridMethod
  public Map<String, List<ResultLatinSquare>> solve(
      @GridParam(name = "restoreMatrix") List<List<Character>> restoreMatrix,
      @GridParam(name = "subTask") SubTask subTask) {
    List<Integer> emptyCells = getEmptyCells(restoreMatrix);
    int freeCells = emptyCells.size();
    List<Character> alphabet = subTask.alphabet();

    BigInteger totalVariants = BigInteger.valueOf(alphabet.size()).pow(freeCells);
    List<ResultLatinSquare> validSquares = new ArrayList<>();

    for (BigInteger num = BigInteger.ZERO;
        num.compareTo(totalVariants) < 0;
        num = num.add(BigInteger.ONE)) {
      List<List<Character>> newMatrix = fillMatrix(restoreMatrix, emptyCells, num, alphabet);
      if (isLatinSquare(newMatrix, alphabet.size())) {
        validSquares.add(new ResultLatinSquare(newMatrix));
      }
    }
    log.debug(
        "Latin squares: {}",
        validSquares.stream()
            .map(square -> Arrays.deepToString(square.matrix().toArray()))
            .collect(Collectors.toList()));

    return Map.of("re", validSquares);
  }

  private List<Integer> getEmptyCells(List<List<Character>> matrix) {
    List<Integer> emptyCells = new ArrayList<>();
    int index = 0;
    for (List<Character> row : matrix) {
      for (Character cell : row) {
        if (cell == null) emptyCells.add(index);
        index++;
      }
    }
    return emptyCells;
  }

  private List<List<Character>> fillMatrix(
      List<List<Character>> matrix,
      List<Integer> emptyCells,
      BigInteger num,
      List<Character> alphabet) {
    List<List<Character>> newMatrix = new ArrayList<>();
    for (List<Character> row : matrix) {
      newMatrix.add(new ArrayList<>(row));
    }

    for (int i = emptyCells.size() - 1; i >= 0; i--) {
      int alphabetIndex = num.mod(BigInteger.valueOf(alphabet.size())).intValue();
      num = num.divide(BigInteger.valueOf(alphabet.size()));

      int index = emptyCells.get(i);
      newMatrix.get(index / matrix.size()).set(index % matrix.size(), alphabet.get(alphabetIndex));
    }

    return newMatrix;
  }

  private boolean isLatinSquare(List<List<Character>> matrix, int size) {
    Set<Character> rowSet = new HashSet<>();
    Set<Character> colSet = new HashSet<>();

    for (int i = 0; i < size; i++) {
      rowSet.clear();
      colSet.clear();

      for (int j = 0; j < size; j++) {
        if (!rowSet.add(matrix.get(i).get(j)) || !colSet.add(matrix.get(j).get(i))) {
          return false;
        }
      }
    }
    return true;
  }
}
