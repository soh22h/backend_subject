package Subject.Bank;

class TransferAccount extends Account {
    public TransferAccount(
            int accountNumber,
            String accountName,
            String accountHolder,
            int balance,
            boolean[] isAvailable) {
        super(
                accountNumber,
                accountName,
                accountHolder,
                balance,
                isAvailable
        );
    }

    @Override
    public void deposit(int amount) {
        super.setBalance(super.getBalance() + amount);
    }

    @Override
    public void withdraw(int amount) throws InsufficientBalanceException{
        if(getBalance() < amount){
            String message = String.format("잔액이 부족합니다! (잔액: %,d원)", getBalance());
            throw new InsufficientBalanceException(message);
        }

        super.setBalance(super.getBalance() - amount);
    }

    @Override
    public void transfer(Account targetAccount, int amount) throws InsufficientBalanceException {
        if(getBalance() < amount){
            String message = String.format("잔액이 부족합니다! (잔액: %,d원)", getBalance());
            throw new InsufficientBalanceException(message);
        }

        withdraw(amount);
        targetAccount.deposit(amount);
    }

    public void info(int accountNumber) {
        System.out.printf("자유입출금 통장 (계좌번호:  %d, 잔액: %,d원, 예금주: %s)\n",
                getAccountNumber(),
                getBalance(),
                getAccountHolder()
        );
    }
}
