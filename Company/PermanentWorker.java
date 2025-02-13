package Subject.Company;

import java.text.DecimalFormat;

class PermanentWorker extends Worker {
    private int salary;

    public PermanentWorker(String name, int salary) {
        super(name);
        this.salary = salary;
    }

    @Override
    public int getPay() {
        return this.salary;
    }

    @Override
    public void showSalaryInfo() {
        System.out.printf("사원 %s의 급여는 %,d원\n", name, salary);
    }
}
