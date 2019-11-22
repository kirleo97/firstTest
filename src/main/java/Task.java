
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
        Company company = new Company();
        String fileName = args[0];
        if (fileName == null || fileName.isEmpty()) {
            System.out.println("Ошибка: файл не был передан в качестве аргумента. Программа не может быть выполнена.");
            return;
        }
        readEmployees(fileName);
        writeEmployees();
    }

    public static void readEmployees(String fileName) {
        System.out.println("Начинаем считывание сотрудников из файла " + fileName + "...");
        System.out.println();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String s;
            String[] mas;
            int numberOfString = 0;

            while (reader.ready()) {
                numberOfString++;
                s = reader.readLine();

                if (s == null || s.isEmpty()) continue;

                mas = s.trim().split(" ");
                int sizeOfMas = mas.length;
                if (sizeOfMas < 3) {
                    System.out.println("Введены данные неверного формата для данного сотрудника на строке + " + numberOfString + ". Перейдем к следующему сотруднику.");
                    continue;
                }

                String fullNameOfEmployee = "";
                boolean isNameRight = true;
                for (int i = 0; i < sizeOfMas - 2 && isNameRight; i++) {
                    isNameRight = mas[i].trim().matches("[а-яА-ЯёЁ]+");
                    fullNameOfEmployee += mas[i] + " ";
                }
                if (!isNameRight) {
                    System.out.println("Ошибка: для имени сотрудника на строке " + numberOfString + " введены данные неверного формата. Перейдем к следующему сотруднику.");
                    continue;
                }
                fullNameOfEmployee = fullNameOfEmployee.trim();

                String salary = mas[sizeOfMas - 2];
                try {
                    Double.parseDouble(salary);
                } catch (NumberFormatException e) {
                    System.out.println("Введены данные неверного формата для заработной платы работника. Ошибка в строке + " + numberOfString + " для сотрудника с именем " + fullNameOfEmployee + ". Перейдем к следующему сотруднику.");
                    e.printStackTrace();
                    continue;
                }
                BigDecimal decimalSalary = new BigDecimal(salary);
                decimalSalary = decimalSalary.setScale(2, BigDecimal.ROUND_HALF_UP);

                String department = mas[sizeOfMas - 1].toLowerCase();
                if (!Company.mapOfEmployees.containsKey(department)) {
                    Company.mapOfEmployees.put(department, new ArrayList<Employee>());
                }

                Employee employee = new Employee(fullNameOfEmployee, decimalSalary);
                List<Employee> list = Company.mapOfEmployees.get(department);
                list.add(employee);
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
        System.out.println();
        System.out.println("Начинаем выведение информации о сотрудниках...");
        System.out.println();

        for (Map.Entry<String, List<Employee>> pair : Company.mapOfEmployees.entrySet()) {
            String nameOfDepartment = pair.getKey();
            System.out.println("Вывод информации о следующем отделе: " + nameOfDepartment + "...");
            for (Employee employee : pair.getValue()) {
                System.out.printf("%-50s", employee.getFullName());
                System.out.println(employee.getSalary().toString());
            }
            System.out.println("Средняя заработная плата по отделу составляет " + Company.getAvrSalary(pair.getKey()));
            System.out.println();
        }

        System.out.println();
    }
}
