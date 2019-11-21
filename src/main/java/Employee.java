
import java.math.BigDecimal;

public class Employee {
    private String fullName;
    private BigDecimal salary;

    public Employee(String fullName, BigDecimal salary) {
        this.fullName = fullName;
        salary = salary.setScale(2, BigDecimal.ROUND_HALF_UP);
        this.salary = salary;
    }

    public String getFullName() {
        return fullName;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        salary = salary.setScale(2, BigDecimal.ROUND_HALF_UP);
        if (salary.compareTo(new BigDecimal(0.00)) < 0) {
            this.salary = new BigDecimal("0.00");
            return;
        }
        this.salary = salary;
    }
}
