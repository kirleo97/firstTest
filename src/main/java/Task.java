import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Task {
    public static void main(String[] args) {
        try {
            String fileName = args[0];
            readEmployees(fileName);
            writeEmployees();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Ошибка: файл не был передан в качестве аргумента. Программа не может быть выполнена.");
            e.printStackTrace();
            System.exit(0);
        }
        if (!Company.getInstance().getMapOfEmployees().isEmpty()) {
            printCombinations(Company.getInstance().getMapOfEmployees());
        } else {
            System.out.println("Ошибка: карта департаментов и списков сотрудников пуста, вычисление не может быть произведено.");
        }
    }


    public static void readEmployees(String fileName) {
        System.out.println("Начинаем считывание сотрудников из файла " + fileName + "...\n");

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String s;
            String[] mas;
            int numberOfString = 0;
            while (reader.ready()) {
                numberOfString++;
                s = reader.readLine();
                if (s.isEmpty()) {
                    continue;
                }
                mas = s.split(" ");
                if (!isDataRight(mas, numberOfString)) {
                    continue;
                }
                Company.getInstance().addEmployeeToDepartment(mas[mas.length - 1].toLowerCase(), new Employee(makeUpName(mas), new BigDecimal(mas[mas.length - 2])));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Возникла ошибка: введённый Вами файл с именем " + fileName + " не найден. Дальнейший процесс считывания сотрудников невозможен.");
            e.printStackTrace();
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Возникла ошибка ввода/вывода данных: при чтении данных из файла. Дальнейшее чтение данных невозможно.");
            e.printStackTrace();
        }
        System.out.println("Считывание сотрудников из файла завершено.");
    }

    public static void writeEmployees() {
        System.out.println("\nНачинаем выведение информации о сотрудниках...\n");
        for (String department : Company.getInstance().getMapOfEmployees().keySet()) {
            System.out.println("Вывод информации о следующем отделе: " + department + "...");
            for (Employee employee : Company.getInstance().getMapOfEmployees().get(department)) {
                System.out.printf("%-50s", employee.getFullName());
                System.out.println(employee.getSalary().toString());
            }
            System.out.println("Средняя заработная плата по отделу составляет " + Company.getInstance().getAvrSalaryOfDepartment(department) + "\n");
        }
    }

    public static boolean isDataRight(String[] data, int numberOfString) {
        int sizeOfMas = data.length;
        if (sizeOfMas < 3) {
            System.out.println("Введены данные неверного формата для данного сотрудника на строке + " + numberOfString + ". Перейдем к следующему сотруднику.");
            return false;
        }

        String fullNameOfEmployee = "";
        for (int i = 0; i < sizeOfMas - 2; i++) {
            if (!(data[i].matches("[а-яА-ЯёЁ]+"))) {
                System.out.println("Ошибка: для имени сотрудника на строке " + numberOfString + " введены данные неверного формата. Перейдем к следующему сотруднику.");
                return false;
            }
            fullNameOfEmployee += data[i] + " ";
        }

        try {
            new BigDecimal(data[sizeOfMas - 2]);
        } catch (NumberFormatException e) {
            System.out.println("Введены данные неверного формата для заработной платы работника. Ошибка в строке + " + numberOfString + " для сотрудника с именем " + fullNameOfEmployee + ". Перейдем к следующему сотруднику.");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String makeUpName(String[] mas) {
        String fullNameOfEmployee = "";
        for (int i = 0; i < mas.length - 2; i++) {
            fullNameOfEmployee += mas[i] + " ";
        }
        return fullNameOfEmployee;
    }

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