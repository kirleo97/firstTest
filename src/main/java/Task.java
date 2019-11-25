
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLOutput;
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
        if (!Company.mapOfEmployees.isEmpty()) {
            printCombinations(Company.mapOfEmployees);
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
                if (s.isEmpty()) continue;

                mas = s.trim().split(" ");
                int sizeOfMas = mas.length;
                if (sizeOfMas < 3) {
                    System.out.println("Введены данные неверного формата для данного сотрудника на строке + " + numberOfString + ". Перейдем к следующему сотруднику.");
                    continue;
                }

                String fullNameOfEmployee = "";
                for (int i = 0; i < sizeOfMas - 2; i++) {
                    fullNameOfEmployee += mas[i] + " ";
                }
                if (!isNameRight(fullNameOfEmployee)) {
                    System.out.println("Ошибка: для имени сотрудника на строке " + numberOfString + " введены данные неверного формата. Перейдем к следующему сотруднику.");
                    continue;
                }

                String salary = mas[sizeOfMas - 2];
                BigDecimal decimalSalary;
                try {
                    decimalSalary = new BigDecimal(salary).setScale(2, BigDecimal.ROUND_HALF_UP);
                } catch (NumberFormatException e) {
                    System.out.println("Введены данные неверного формата для заработной платы работника. Ошибка в строке + " + numberOfString + " для сотрудника с именем " + fullNameOfEmployee + ". Перейдем к следующему сотруднику.");
                    e.printStackTrace();
                    continue;
                }

                Company.getInstance().addEmployeeToDepartment(mas[sizeOfMas - 1].toLowerCase(), new Employee(fullNameOfEmployee.trim(), decimalSalary));
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
        for (String department : Company.mapOfEmployees.keySet()) {
            System.out.println("Вывод информации о следующем отделе: " + department + "...");
            for (Employee employee : Company.mapOfEmployees.get(department)) {
                System.out.printf("%-50s", employee.getFullName());
                System.out.println(employee.getSalary().toString());
            }
            System.out.println("Средняя заработная плата по отделу составляет " + Company.getInstance().getAvrSalaryOfDepartment(department) + "\n");
        }
    }

    public static boolean isNameRight(String name) {
        String[] mas = name.trim().split(" ");
        boolean isNameFormatRight;
        for (int i = 0; i < mas.length; i++) {
            isNameFormatRight = mas[i].trim().matches("[а-яА-ЯёЁ]+");
            if (!isNameFormatRight) {
                return false;
            }
        }
        return true;
    }

    public static void printCombinations(Map<String, List<Employee>> map) {
        System.out.println("Выведем все возможные комбинации переводов сотрудников из одного отдела в другой...");
        Company company = Company.getInstance();
        int numberOfCombination = 0;
        for (String departmentFrom : map.keySet()) {
            List<Employee> listOfEmployeesOut = map.get(departmentFrom);
            BigDecimal previousAvrSalaryOfDepartmentFrom = company.getAvrSalaryOfDepartment(departmentFrom);
            for (String departmentTo : map.keySet()) {
                if (departmentFrom.equals(departmentTo)) {
                    continue;
                }
                BigDecimal previousAvrSalaryOfDepartmentTo = company.getAvrSalaryOfDepartment(departmentFrom);
                for (int m = 1; m <= listOfEmployeesOut.size(); m++) {
                    int[] indexes = null;
                    int COUNT = 0;
                    while ((indexes = generateCombinations(indexes, m, listOfEmployeesOut.size())) != null) {
                        System.out.println(COUNT++);
                        if (checkCombination(indexes, departmentFrom, departmentTo, previousAvrSalaryOfDepartmentFrom, previousAvrSalaryOfDepartmentTo)) {
                            numberOfCombination++;
                            System.out.println("Комбинация номер " + numberOfCombination + ":");
                            System.out.print("следующие сотрудники могут быть переведены из отдела " + departmentFrom + " в отдел " + departmentTo + ": ");
                            for (int index : indexes) {
                                System.out.print(listOfEmployeesOut.get(index - 1).getFullName() + ", ");
                            }
                            System.out.println();
                        }
                    }
                }
            }
        }
    }

    public static int[] generateCombinations(int[] arr, int m, int n) {
        if (arr == null)
        {
            arr = new int[m];
            for (int i = 0; i < m; i++)
                arr[i] = i + 1;
            return arr;
        }
        for (int i = m - 1; i >= 0; i--)
            if (arr[i] < n - m + i + 1)
            {
                arr[i]++;
                for (int j = i; j < m - 1; j++)
                    arr[j + 1] = arr[j] + 1;
                return arr;
            }
        return null;
    }

    public static boolean checkCombination(int[] indexes, String depFrom, String depTo, BigDecimal previousAvrSalaryOfDepartmentFrom, BigDecimal previousAvrSalaryOfDepartmentTo) {
        List<Employee> employeesOut = Company.mapOfEmployees.get(depFrom);
        List<Employee> employeesInto = Company.mapOfEmployees.get(depTo);

        BigDecimal sumSalaryOfNewDepartmentTo = previousAvrSalaryOfDepartmentTo.multiply(new BigDecimal(employeesInto.size()));
        BigDecimal sumSalaryOfNewDepartmentFrom = previousAvrSalaryOfDepartmentFrom.multiply(new BigDecimal(employeesOut.size()));
        int COUNT = 0;
        for (int index : indexes) {
            System.out.println(++COUNT);
            BigDecimal salaryOfEmployee = employeesOut.get(indexes[index] - 1).getSalary();
            sumSalaryOfNewDepartmentTo = sumSalaryOfNewDepartmentTo.add(salaryOfEmployee);
            sumSalaryOfNewDepartmentFrom = sumSalaryOfNewDepartmentFrom.subtract(salaryOfEmployee);
        }
        BigDecimal newAvrSalaryOfDepartmentTo = sumSalaryOfNewDepartmentTo.divide(new BigDecimal(employeesInto.size() + indexes.length));
        BigDecimal newAvrSalaryOfDepartmentFrom = sumSalaryOfNewDepartmentFrom.divide(new BigDecimal(employeesOut.size() - indexes.length));

        if (newAvrSalaryOfDepartmentFrom.compareTo(previousAvrSalaryOfDepartmentFrom) > 0 && newAvrSalaryOfDepartmentTo.compareTo(previousAvrSalaryOfDepartmentTo) > 0) {
            return true;
        }
        return false;
    }

}