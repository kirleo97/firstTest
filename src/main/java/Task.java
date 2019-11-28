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
            if (!Company.getInstance().getMapOfEmployees().isEmpty()) {
                Combination.printCombinations(Company.getInstance().getMapOfEmployees());
            } else {
                System.out.println("Ошибка: карта департаментов и списков сотрудников пуста, вычисление не может быть произведено.");
            }
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
                if (s.isEmpty()) {
                    continue;
                }
                mas = s.split(" ");
                if (!Employee.isDataOfEmployeeRight(mas, numberOfString)) {
                    continue;
                }
                Company.getInstance().addEmployeeToDepartment(mas[mas.length - 1].toLowerCase(), new Employee(Employee.makeUpName(mas), new BigDecimal(mas[mas.length - 2])));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Возникла ошибка: введённый Вами файл с именем " + fileName + " не найден. Дальнейший процесс считывания сотрудников невозможен.");
            e.printStackTrace();
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
}