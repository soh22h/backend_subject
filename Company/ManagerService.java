package Subject.Company;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

class ManagerService {
    private List<Worker> workers;

    public ManagerService() {
        workers = new ArrayList<Worker>(){};
    }

    public void addWorker(Worker worker) {
        workers.add(worker);
    }

    public void showAllSalaryInfo() {
        workers.forEach((worker) -> worker.showSalaryInfo());
    }

    public void showSalaryInfo(String name) {
        int salary = workers.stream()
                .filter(worker -> worker.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 이름의 사원이 존재하지 않습니다."))
                .getPay();

        System.out.printf("사원 %s의 급여는 %,d원\n", name, salary);
    }

    public void showTotalSalary() {
        int total = workers.stream()
                .mapToInt(Worker::getPay)
                .sum();

        System.out.printf("모든 사원들의 급여 총액은 : %,d원", total);
    }
}
