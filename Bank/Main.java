package Subject.Bank;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Map<Integer, Account> accounts = new HashMap<>();

    public static void main(String[] args) {
        accounts.put(1, new TransferAccount(
                1,
                "자유입출금",
                "홍길동",
                0,
                new boolean[]{true, true, true}
        ));
        accounts.put(2, new SavingAccount(
                2,
                "정기예금",
                "홍길동",
                50000000,
                new boolean[]{true, false, false},
                0,
                true
        ));
        accounts.put(3, new OverdraftAccount(
                3,
                "마이너스",
                "홍길동",
                0,
                new boolean[]{true, true, true}
        ));
        System.out.println("OneHana Bank에 방문해 주셔서 감사합니다.");

        while (true) {
            System.out.print(getAccountList());

            String input = scanner.nextLine();
            // 입력 없거나 0 입력 시 종료
            if (input.isEmpty() || input.equals("0")) {
                System.out.println("금일 OneHana Bank 업무를 종료합니다. 감사합니다.");
                break;
            }

            // 아예 다른 값을 입력 시 재입력 요청
            if (!input.matches("[123]")) {
                System.out.println("잘못된 입력입니다. 다시 시도해주세요.");
                continue;
            }

            int accountNumber = Integer.parseInt(input);
            Account selectedAccount = accounts.get(accountNumber);
            if (selectedAccount == null) {
                System.out.println("잘못된 선택입니다. 다시 시도해 주세요.");
                continue;
            }

            selectedAccount.info(accountNumber);

            if(selectedAccount.getAccountName().equals("정기예금")) {
                runSavingAccountOperations((SavingAccount) selectedAccount);
            }
            else {
                runAccountOperations(selectedAccount);
            }
        }
    }

    private static void runSavingAccountOperations(SavingAccount account) {
        if (account.getIsMatured()) {
            while (true) {
                System.out.print("> 정기예금이 만기되었습니다. (+: 만기처리, -: 인출, T: 이체, I: 정보) ");
                String action = scanner.nextLine();

                if (action.equals("0") || action.isEmpty()) {
                    break;
                }

                try {
                    switch (action) {
                        case "+" -> {
                            while (true) {
                                System.out.print("예치 개월 수를 입력하세요? (1 ~ 60개월) ");
                                int months = Integer.parseInt(scanner.nextLine());

                                if ((months < 1 && months != 0) || months > 60) {
                                    System.out.println("올바른 예치 개월 수를 입력하세요.");
                                    continue;
                                } else if (months >= 1 && months <= 60) {
                                    DecimalFormat df = new DecimalFormat("0.##"); // 소수점 한 자리는 유지, 두 번째 자리는 필요시 출력

                                    System.out.printf("%d개월(적용 금리 %s%%)로 만기 처리하시겠어요? (y/n) ", months, df.format(account.getInterestRateForMonths(months)));
                                    String confirmation = scanner.nextLine();
                                    if (confirmation.equals("y")) {
                                        System.out.print(getTargetAccountList(account));
                                        int targetAccountType = Integer.parseInt(scanner.nextLine());
                                        Account targetAccount = accounts.get(targetAccountType);
                                        account.matureAndTransfer(targetAccount, months);

                                        // 만기 통장 삭제
                                        accounts.remove(account.getAccountNumber());
                                        return; // 만기처리가 끝나면 종료
                                    } else if (confirmation.equals("n")) {
                                        continue;
                                    } else {
                                        System.out.println("잘못된 입력입니다.");
                                        return;
                                    }
                                }
                                break;
                            }
                        }
                        case "-" -> {
                            account.withdrawIfAvailable();

                            while (true) {
                                System.out.print("출금 하실 금액은? ");
                                int withdrawAmount = Integer.parseInt(scanner.nextLine());
                                if (withdrawAmount <= 0) {
                                    break;
                                }

                                try {
                                    account.withdraw(withdrawAmount);
                                    System.out.printf("%s 통장에서 %,d원이 출금되었습니다.\n", account.getAccountName(), withdrawAmount);
                                    System.out.printf("%s 통장의 잔액은 %,d원입니다.\n", account.getAccountName(), account.getBalance());
                                    break;
                                } catch (InsufficientBalanceException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                        }
                        case "T" -> {
                            account.transferIfAvailable();

                            System.out.print(getTargetAccountList(account));
                            int targetAccountType = Integer.parseInt(scanner.nextLine());
                            Account targetAccount = accounts.get(targetAccountType);

                            if (targetAccount != null && targetAccount != account) {
                                while (true) {
                                    System.out.printf("%s에 보낼 금액은? ", targetAccount.getAccountName());
                                    int transferAmount = Integer.parseInt(scanner.nextLine());
                                    if (transferAmount <= 0) {
                                        break;
                                    }

                                    try {
                                        account.transfer(targetAccount, transferAmount);
                                        System.out.printf("%s 통장에 %,d원이 입금되었습니다.\n", targetAccount.getAccountName(), transferAmount);
                                        System.out.printf("%s 통장의 잔액은 %,d원입니다.\n", account.getAccountName(), account.getBalance());
                                        break; // 성공적으로 이체가 완료되면 루프 종료
                                    } catch (InsufficientBalanceException e) {
                                        System.out.println(e.getMessage());
                                    }
                                }
                            } else {
                                System.out.println("잘못된 선택입니다.");
                            }
                        }
                        case "I" -> {
                            account.info(account.getAccountNumber());
                            account.printInterestRates();
                        }
                        default -> {
                            System.out.println("올바른 업무를 선택해 주세요.");
                        }
                    }
                } catch (AccountException e) {
                    System.out.println(e.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println("잘못된 입력입니다. 다시 시도해 주세요.");
                }
            }
        } else {
            runAccountOperations(account);  // 만기가 되지 않은 경우에는 일반적인 계좌 처리
        }
    }

    private static void runAccountOperations(Account account) {
        while (true) {
            System.out.print("> 원하시는 업무는? (+: 입금, -: 출금, T: 이체, I: 정보) ");
            String action = scanner.nextLine();

            if (action.equals("0") || action.isEmpty()) {
                break;
            }

            try {
                switch (action) {
                    case "+" -> {
                        System.out.print("입금 하실 금액은? ");
                        int depositAmount = Integer.parseInt(scanner.nextLine());
                        if (depositAmount > 0) {
                            account.deposit(depositAmount);
                            System.out.printf("%s 통장에 %,d원이 입금되었습니다!\n", account.getAccountName(), depositAmount);
                        }
                    }
                    case "-" -> {
                        account.withdrawIfAvailable();

                        while (true) {
                            System.out.print("출금 하실 금액은? ");
                            int withdrawAmount = Integer.parseInt(scanner.nextLine());
                            if (withdrawAmount <= 0) {
                                break;
                            }

                            try {
                                account.withdraw(withdrawAmount);
                                System.out.printf("%s 통장에서 %,d원이 출금되었습니다.\n", account.getAccountName(), withdrawAmount);
                                System.out.printf("%s 통장의 잔액은 %,d원입니다.\n", account.getAccountName(), account.getBalance());
                                break;
                            } catch (InsufficientBalanceException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                    case "T" -> {
                        account.transferIfAvailable();

                        System.out.print(getTargetAccountList(account));
                        int targetAccountType = Integer.parseInt(scanner.nextLine());
                        Account targetAccount = accounts.get(targetAccountType);

                        if (targetAccount != null && targetAccount != account) {
                            while (true) {
                                System.out.printf("%s에 보낼 금액은? ", targetAccount.getAccountName());
                                int transferAmount = Integer.parseInt(scanner.nextLine());
                                if (transferAmount <= 0) {
                                    break;
                                }
//짜주기도 했고, 이게 문제가 없었어
                                try {
                                    account.transfer(targetAccount, transferAmount);
                                    System.out.printf("%s 통장에 %,d원이 입금되었습니다.\n", targetAccount.getAccountName(), transferAmount);
                                    System.out.printf("%s 통장의 잔액은 %,d원입니다.\n", account.getAccountName(), account.getBalance());
                                    break; // 성공적으로 이체가 완료되면 루프 종료
                                } catch (InsufficientBalanceException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                        } else {
                            System.out.println("잘못된 선택입니다.");
                        }
                    }
                    case "I" -> {
                        account.info(account.getAccountNumber());
                    }
                    default -> {
                        System.out.println("올바른 업무를 선택해 주세요.");
                    }
                }
            } catch (AccountException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("잘못된 입력입니다. 다시 시도해 주세요.");
            }
        }
    }

    private static String getTargetAccountList(Account account) {
        StringBuilder listString = new StringBuilder();
        listString.append("어디로 보낼까요? (");

        for (Map.Entry<Integer, Account> entry : accounts.entrySet()) {
            if (!entry.getValue().equals(account)) {
                listString.append(String.format("%d: %s", entry.getKey(), entry.getValue().getAccountName()));
                listString.append(", ");
            }
        }

        // 마지막 콤마와 공백 제거
        if (listString.length() > 2) {
            listString.setLength(listString.length() - 2);
        }

        listString.append(") ");

        return listString.toString();
    }

    private static String getAccountList() {
        StringBuilder listString = new StringBuilder();
        listString.append(">> 통장을 선택하세요 (");

        for (Map.Entry<Integer, Account> entry : accounts.entrySet()) {
            listString.append(String.format("%d: %s", entry.getKey(), entry.getValue().getAccountName()));
            listString.append(", ");
        }

        // 마지막 콤마와 공백 제거
        if (listString.length() > 2) {
            listString.setLength(listString.length() - 2);
        }

        listString.append(") ");

        return listString.toString();
    }
}
