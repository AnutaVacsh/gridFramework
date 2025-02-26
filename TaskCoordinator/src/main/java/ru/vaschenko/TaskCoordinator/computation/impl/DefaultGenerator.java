package ru.vaschenko.TaskCoordinator.computation.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.TaskCoordinator.computation.Generator;
import ru.vaschenko.TaskCoordinator.dto.SubTask;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DefaultGenerator implements Generator<List<List<Character>>, SubTask> {

    @Override
    public List<List<Character>> generate(SubTask subTask) {
        List<Integer> indices = convertToBaseM(subTask.number(), subTask.alphabet().size(), subTask.treeLevel());
        return fillMatrix(subTask.matrix(), indices, subTask.alphabet());
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
