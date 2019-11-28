
import java.math.BigDecimal;

public class Employee {
    private String fullName;
    private BigDecimal salary;

    public Employee(String fullName, BigDecimal salary) {
        this.fullName = fullName;
        setSalary(salary);
    }

    public String getFullName() {
        return fullName;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        if (salary.compareTo(BigDecimal.ZERO) < 0) {
            this.salary = BigDecimal.ZERO;
        } else {
            this.salary = salary.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
    }

    public static boolean isDataOfEmployeeRight(String[] data, int numberOfString) {
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
}
