import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Combination {

    public static void printCombinations(Map<String, List<Employee>> map) {
        System.out.println("Выведем все возможные комбинации переводов сотрудников из одного отдела в другой...");
        int numberOfCombination = 0;
        for (String departmentFrom : map.keySet()) {
            List<Employee> listOfEmployeesOut = map.get(departmentFrom);
            BigDecimal previousAvrSalaryOfDepartmentFrom = Company.getInstance().getAvrSalaryOfDepartment(departmentFrom);
            for (String departmentTo : map.keySet()) {
                if (departmentFrom.equals(departmentTo)) {
                    continue;
                }
                BigDecimal previousAvrSalaryOfDepartmentTo = Company.getInstance().getAvrSalaryOfDepartment(departmentTo);
                int countOfCombinations = (int) Math.pow(2, listOfEmployeesOut.size());
                List<Integer> listOfIndexes;
                for (int i = 1; i < countOfCombinations; i++) {
                    listOfIndexes = generateCombination(i);
                    if (checkCombination(listOfIndexes, departmentFrom, departmentTo, previousAvrSalaryOfDepartmentFrom, previousAvrSalaryOfDepartmentTo)) {
                        numberOfCombination++;
                        System.out.println("Комбинация номер " + numberOfCombination + ":\n" + "следующие сотрудники могут быть переведены из отдела " + departmentFrom + " в отдел " + departmentTo + ": ");
                        for (int index : listOfIndexes) {
                            System.out.print(listOfEmployeesOut.get(index).getFullName() + ", ");
                        }
                        System.out.println();
                    }
                }
            }
        }
    }

    public static List<Integer> generateCombination(int n) {
        String binary = new StringBuilder(Integer.toBinaryString(n)).reverse().toString();
        List<Integer> listOfIndexes = new ArrayList<>();
        for (int i = 0; i < binary.length(); i++) {
            if (binary.charAt(i) == '1') {
                listOfIndexes.add(i);
            }
        }
        return listOfIndexes;
    }

    public static boolean checkCombination(List<Integer> indexes, String depFrom, String depTo, BigDecimal previousAvrSalaryOfDepartmentFrom, BigDecimal previousAvrSalaryOfDepartmentTo) {
        List<Employee> employeesOut = Company.getInstance().getMapOfEmployees().get(depFrom);
        List<Employee> employeesInto = Company.getInstance().getMapOfEmployees().get(depTo);

        BigDecimal sumSalaryOfNewDepartmentTo = previousAvrSalaryOfDepartmentTo.multiply(new BigDecimal(employeesInto.size()));
        BigDecimal sumSalaryOfNewDepartmentFrom = previousAvrSalaryOfDepartmentFrom.multiply(new BigDecimal(employeesOut.size()));

        for (int index : indexes) {
            BigDecimal salaryOfEmployee = employeesOut.get(index).getSalary();
            sumSalaryOfNewDepartmentTo = sumSalaryOfNewDepartmentTo.add(salaryOfEmployee);
            sumSalaryOfNewDepartmentFrom = sumSalaryOfNewDepartmentFrom.subtract(salaryOfEmployee);
        }
        BigDecimal newAvrSalaryOfDepartmentTo = sumSalaryOfNewDepartmentTo.divide(new BigDecimal(employeesInto.size() + indexes.size()), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal newAvrSalaryOfDepartmentFrom;
        if (employeesOut.size() - indexes.size() != 0) {
            newAvrSalaryOfDepartmentFrom = sumSalaryOfNewDepartmentFrom.divide(new BigDecimal(employeesOut.size() - indexes.size()), 2, BigDecimal.ROUND_HALF_UP);
        } else {
            newAvrSalaryOfDepartmentFrom = BigDecimal.ZERO;
        }

        if (newAvrSalaryOfDepartmentFrom.compareTo(previousAvrSalaryOfDepartmentFrom) > 0 && newAvrSalaryOfDepartmentTo.compareTo(previousAvrSalaryOfDepartmentTo) > 0) {
            return true;
        }
        return false;
    }
}
