package Subject.Bank;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

class SavingAccount extends Account {
    private static final Map<Integer, Double> interestRates = new HashMap<>();
    private int depositMonths;
    private boolean isMatured;

    static {
        interestRates.put(1, 3.0);
        interestRates.put(3, 3.35);
        interestRates.put(6, 3.4);
        interestRates.put(9, 3.35);
        interestRates.put(12, 3.35);
        interestRates.put(24, 2.9);
        interestRates.put(36, 2.9);
        interestRates.put(48, 2.9);
        interestRates.put(60, 2.9);
    }

    public SavingAccount(
            int accountNumber,
            String accountName,
            String accountHolder,
            int balance,
            boolean[] isAvailable,
            int depositMonths,
            boolean isMatured) {
        super(
                accountNumber,
                accountName,
                accountHolder,
                balance,
                isAvailable
        );
        this.depositMonths = depositMonths;
        this.isMatured = isMatured;
    }

    public boolean getIsMatured() {
        return isMatured;
    }

    @Override
    public void deposit(int amount) {
        super.setBalance(super.getBalance() + amount);
    }

    public void info(int accountNumber) {
        System.out.printf("정기예금 통장 (계좌번호:  %d, 잔액: %,d원, 예금주: %s)\n",
                getAccountNumber(),
                getBalance(),
                getAccountHolder()
        );
    }

    public void matureAndTransfer(Account targetAccount, int months) {
        Double rate = getInterestRateForMonths(months);

        int interest = (int) Math.round(getBalance() * (rate / 100));
        int maturedBalance = getBalance() + interest;

        targetAccount.deposit(maturedBalance);
        setBalance(0);

        System.out.printf("%s에 %,d원이 입금되었습니다.\n", targetAccount.getAccountName(), maturedBalance);
        System.out.println("정기예금 통장이 해지되었습니다. 감사합니다.");
    }

    Double getInterestRateForMonths(int months) {
        Integer nearestKey = interestRates.keySet().stream()
                .filter(k -> k <= months)
                .max(Integer::compare)
                .orElse(null);

        return nearestKey != null ? interestRates.get(nearestKey) : null;
    }

    public void printInterestRates() {
        DecimalFormat df = new DecimalFormat("0.##"); // 소수점 한 자리는 유지, 두 번째 자리는 필요시 출력

        System.out.println("* 예치 개월에 따른 적용 금리");
        interestRates.entrySet().stream()
                .sorted((entry1, entry2) -> Integer.compare(entry1.getKey(), entry2.getKey())) // 키 기준으로 정렬
                .forEach(entry -> {
                    System.out.printf("                  %5d개월 이상    %s%%\n", entry.getKey(), df.format(entry.getValue()));
                });
    }
}
