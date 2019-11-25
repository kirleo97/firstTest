
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
            this.salary = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
            return;
        }
        this.salary = salary.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
