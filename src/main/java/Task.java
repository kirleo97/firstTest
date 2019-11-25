
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;

public class Task {
    public static void main(String[] args) {
        try {
            String fileName = args[0];
            readEmployees(fileName);
            writeEmployees();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Ошибка: файл не был передан в качестве аргумента. Программа не может быть выполнена.");
            e.printStackTrace();
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
            System.out.println("Средняя заработная плата по отделу составляет " + Company.getInstance().getAvrSalary(department) + "\n");
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
}