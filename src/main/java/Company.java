import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Company {
    public static Map<String, List<Employee>> mapOfEmployees = new HashMap<String, List<Employee>>();
    static {
        mapOfEmployees.put("программисты", new ArrayList<Employee>());
        mapOfEmployees.put("маркетинг", new ArrayList<Employee>());
        mapOfEmployees.put("менеджмент", new ArrayList<Employee>());
        mapOfEmployees.put("кадры", new ArrayList<Employee>());
        mapOfEmployees.put("бухгалтерия", new ArrayList<Employee>());
    }

    public static BigDecimal getAvrSalary(String nameOfDepartment) {
        nameOfDepartment = nameOfDepartment.trim().toLowerCase();

        if (!mapOfEmployees.containsKey(nameOfDepartment)) {
            System.out.println("При вычислении средней заработной платы для отдела " + nameOfDepartment + " возникла ошибка. Данный отдел не был найден в списке.");
            return null;
        }

        BigDecimal sumSalary = new BigDecimal("0.00");
        List<Employee> list = mapOfEmployees.get(nameOfDepartment);
        for (Employee employee : list) {
            BigDecimal employeeSalary = employee.getSalary();
            sumSalary = sumSalary.add(employeeSalary);
        }
        if (sumSalary.equals(new BigDecimal("0.00"))) return sumSalary;
        return sumSalary.divide(new BigDecimal(list.size()), 2, BigDecimal.ROUND_HALF_UP);
    }
}
