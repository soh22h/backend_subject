package Subject.Company;

import java.text.DecimalFormat;

class TemporaryWorker extends Worker {
    private int payPerHour;
    private int workTime;

    public TemporaryWorker(
            String name,
            int payPerHour,
            int workTime) {
        super(name);
        this.payPerHour = payPerHour;
        this.workTime = workTime;
    }

    @Override
    public int getPay() {
        return payPerHour * workTime;
    }

    @Override
    public void showSalaryInfo() {
        System.out.printf("사원 %s의 근무시간은 %d시간, 시간 수당은 %,d원 급여는 %,d원\n",
                name,
                workTime,
                payPerHour,
                getPay()
        );
    }
}
