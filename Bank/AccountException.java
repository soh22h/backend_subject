package Subject.Bank;

// 계좌 관련 예외의 상위 클래스
abstract class AccountException extends Exception {
    public AccountException(String message) {
        super(message);
    }
}

// 잔액 부족 예외 클래스
class InsufficientBalanceException extends AccountException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}

// 이체 불가능 예외 클래스
class UnsupportedTransferException extends AccountException {
    public UnsupportedTransferException(String message) {
        super(message);
    }
}

// 출금 불가능 예외 클래스
class UnsupportedWithdrawalException extends AccountException {
    public UnsupportedWithdrawalException(String message) {
        super(message);
    }
}
