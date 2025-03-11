package ru.vaschenko.TaskCoordinator.computation;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import ru.vaschenko.TaskCoordinator.annotation.GridComponent;
import ru.vaschenko.TaskCoordinator.annotation.GridMethod;
import ru.vaschenko.TaskCoordinator.annotation.GridParam;
import ru.vaschenko.TaskCoordinator.dto.SubTask;
import ru.vaschenko.TaskCoordinator.enams.TypeComponent;

@Slf4j
@GridComponent(TypeComponent.GENERATOR)
public class DefaultGenerator {

  @GridMethod
  public Map<String, Object> generate(@GridParam(name = "subTask") SubTask subTask) {
    List<Integer> indices =
        convertToBaseM(subTask.number(), subTask.alphabet().size(), subTask.treeLevel());
    return Map.of(
        "restoreMatrix",
        fillMatrix(subTask.matrix(), indices, subTask.alphabet()),
        "subTask",
        subTask);
  }

  private List<Integer> convertToBaseM(BigInteger number, int base, int length) {
    List<Integer> result = new ArrayList<>();
    for (int i = 0; i < length; i++) {
      result.add(0, number.mod(BigInteger.valueOf(base)).intValue());
      number = number.divide(BigInteger.valueOf(base));
    }
    return result;
  }

  private List<List<Character>> fillMatrix(
      List<List<Character>> originalMatrix, List<Integer> indices, List<Character> alphabet) {
    List<List<Character>> newMatrix = new ArrayList<>();
    int index = 0;

    for (List<Character> row : originalMatrix) {
      List<Character> newRow = new ArrayList<>();
      for (Character cell : row) {
        if (index < indices.size() && cell == null) {
          newRow.add(alphabet.get(indices.get(index++)));
        } else {
          newRow.add(cell);
        }
      }
      newMatrix.add(newRow);
    }
    log.debug("restoreSubTaskMatrix = {}", newMatrix);
    return newMatrix;
  }
}
