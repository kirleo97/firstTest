import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Company {
    private static Company company;
    private Map<String, List<Employee>> mapOfEmployees = new HashMap<String, List<Employee>>();

    private Company() {
    }

    public Map<String, List<Employee>> getMapOfEmployees() {
        return mapOfEmployees;
    }

    public static Company getInstance() {
        if(company == null){
            company = new Company();
        }
        return company;
    }

    public void addEmployeeToDepartment(String department, Employee employee) {
        if (!mapOfEmployees.containsKey(department)) {
            mapOfEmployees.put(department, new ArrayList<Employee>());
        }
        mapOfEmployees.get(department).add(employee);
    }

    public BigDecimal getAvrSalaryOfDepartment(String nameOfDepartment) {
        if (!mapOfEmployees.containsKey(nameOfDepartment)) {
            System.out.println("При вычислении средней заработной платы для отдела " + nameOfDepartment + " возникла ошибка. Данный отдел не был найден в списке.");
            return null;
        }

        BigDecimal sumSalary = new BigDecimal("0.00");
        List<Employee> list = mapOfEmployees.get(nameOfDepartment);
        for (Employee employee : list) {
            sumSalary = sumSalary.add(employee.getSalary());
        }
        if (sumSalary.compareTo(BigDecimal.ZERO) == 0) {
            return sumSalary;
        }
        return sumSalary.divide(new BigDecimal(list.size()), 2, BigDecimal.ROUND_HALF_UP);
    }
}