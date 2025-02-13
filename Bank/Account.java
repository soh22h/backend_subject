package Subject.Bank;

abstract class Account {
    private int accountNumber;
    private String accountName;
    private String accountHolder;
    private int balance;
    private boolean[] isAvailable; // 각 기능의 사용 가능 여부

    public Account(
            int accountNumber,
            String accountName,
            String accountHolder,
            int balance,
            boolean[] isAvailable) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.accountHolder = accountHolder;
        this.balance = balance;
        this.isAvailable = isAvailable;
    }

    public String getAccountName() {
        return accountName;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    // 기능 사용 가능 여부 확인 메소드
    public boolean isFunctionAvailable(int index) {
        return isAvailable[index];
    }

    // 사용 가능 시 출금 실행 메소드
    public void withdrawIfAvailable() throws UnsupportedWithdrawalException {
        if (!isFunctionAvailable(1)) {
            throw new UnsupportedWithdrawalException("출금할 수 없는 통장입니다.");
        }
    }

    // 사용 가능 시 이체 실행 메소드
    public void transferIfAvailable() throws UnsupportedTransferException {
        if (!isFunctionAvailable(2)) {
            throw new UnsupportedTransferException("이체할 수 없는 통장입니다.");
        }
    }

    public void deposit(int amount) {
        balance += amount;
    }

    public void info(int accountNumber){};
    
    // 기본적으로 인출과 이체는 지원하지 않도록 예외 처리
    public void withdraw(int amount) throws InsufficientBalanceException {
        throw new InsufficientBalanceException("잔액이 부족합니다!");
    };

    public void transfer(Account targetAccount, int amount) throws InsufficientBalanceException {
        throw new InsufficientBalanceException("잔액이 부족합니다!");
    };
}
