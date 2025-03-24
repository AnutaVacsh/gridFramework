package ru.vaschenko.TaskCoordinator.computation;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import ru.vaschenko.annotation.GridComponent;
import ru.vaschenko.annotation.GridMethod;
import ru.vaschenko.annotation.GridParam;
import ru.vaschenko.enams.TypeComponent;
import ru.vaschenko.TaskCoordinator.dto.SubTask;
import ru.vaschenko.TaskCoordinator.dto.Task;

@Slf4j
@GridComponent(TypeComponent.DISTRIBUTOR)
public class DefaultDistributor{
    private static final long maxComputationsPerNode = 50000000;
    private static Integer treeLevel;
    private static BigInteger number;
    private static BigInteger subTaskCount;

    @GridMethod
    public Map<String, SubTask> generationSubtasks(@GridParam(name = "task") Task task) {
        if(treeLevel == null && number == null){
            int m = task.alphabet().size();
            treeLevel = calculatingTreeLevel(task.matrix(), m);
            log.debug("calculate tree level = {}", treeLevel);

            subTaskCount = BigInteger.valueOf(m).pow(treeLevel);
            log.debug("calculate subTaskCount = {}", subTaskCount);
            number = BigInteger.ZERO;

            SubTask subTask = new SubTask(treeLevel, number, task.matrix(), task.alphabet());
            log.debug("subtasks {}", subTask);
            return Map.of("subTask", subTask);
        }
        else if(number.compareTo(subTaskCount) >= 0){
            return null;
        }

        number = number.add(BigInteger.ONE);
        SubTask subTask = new SubTask(treeLevel, number, task.matrix(), task.alphabet());
        log.debug("subtasks {}", subTask);
        return Map.of("subTask", subTask);
    }

    /**
     * Метод для вычисления нужного колличества вычислительных узлов, в зависимости от максимально
     * допустимой сложности задачи на каждом узле и глубины построения дерева
     */
    private int calculatingTreeLevel(List<List<Character>> matrix, int m) {
        int filledCells = matrixOccupancy(matrix);
        BigInteger computations = matrixComputations(matrix, m, filledCells);
        int nodeCount = computations.divide(BigInteger.valueOf(maxComputationsPerNode)).intValue();
        log.debug("node count = {}", nodeCount);
        return findTreeLevel(matrix.size(), m, filledCells, nodeCount);
    }

    /**
     * Метод для определения кол-ва вариантов для матрицы по формуде (m)^n, где m - величина
     * алфавита, n - кол-во пустых клеток
     *
     * @param matrix матрицы
     * @param m величина алфавита
     * @param filledCells кол-во заполненных клеток
     */
    private BigInteger matrixComputations(List<List<Character>> matrix, int m, int filledCells) {
        int size = matrix.size();

        int totalCells = size * size;
        int emptyCells = totalCells - filledCells;
        log.debug("totalCells = {}, emptyCells = {}", totalCells, emptyCells);

        BigInteger computations = BigInteger.valueOf(m).pow(emptyCells);
        log.debug("matrix computations = {}", computations);

        return computations;
    }

    private int matrixOccupancy(List<List<Character>> matrix) {
        int occupancy = (int) matrix.stream().flatMap(Collection::stream).filter(Objects::nonNull).count();
        log.debug("matrixOccupancy = {}", occupancy);
        return occupancy;
    }

    private BigInteger factorial(int num) {
        BigInteger result = BigInteger.ONE;
        for (int i = 1; i <= num; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    private int findTreeLevel(int n, int m, int filledCells, int nodeCount) {
        BigInteger treeLevel = BigInteger.valueOf(m).pow((n*n) - filledCells);
        log.debug("treeLevel: 85 {}", treeLevel);

        for (BigInteger i = BigInteger.ONE; i.compareTo(treeLevel) <= 0; i = i.add(BigInteger.ONE)) {
            BigInteger powerValue = BigInteger.valueOf(m).pow(i.intValue());
            if (powerValue.compareTo(BigInteger.valueOf(nodeCount)) >= 0) {
                return i.intValue();
            }
        }

        return treeLevel.intValue();
    }
}
