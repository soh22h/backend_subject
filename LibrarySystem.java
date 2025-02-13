package Subject;


import java.util.*;
import java.text.SimpleDateFormat;

class Book {
    private String id;
    private String name;
    private int price;
    private String author;
    private String description;
    private String category;
    private String dateIssued;

    public Book(String id,
         String name,
         int price,
         String author,
         String description,
         String category,
         String dateIssued) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.author = author;
        this.description = description;
        this.category = category;
        this.dateIssued = dateIssued;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getDateIssued() {
        return dateIssued;
    }

    public String toString() {
        return String.format("%s | %s | %d원 | %s | %s | %s | %s\n",
                getId(),
                getName(),
                getPrice(),
                getAuthor(),
                getDescription(),
                getCategory(),
                getDateIssued()
        );
    }
}

class SelectedBook {
    private String id;
    private int quantity;
    private int totalPrice;

    public SelectedBook(
            String id,
            int totalPrice) {
        this.id = id;
        this.quantity = 1;
        this.totalPrice = totalPrice;
    }

    public String getId() {
        return id;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice() {
        this.totalPrice += (getTotalPrice() / getQuantity());
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity() {
        this.quantity = getQuantity() + 1;
    }

    public String toString() {
        return String.format("    %s    |      %d      |    %d원",
                getId(),
                getQuantity(),
                getTotalPrice()
        );
    }
}

class Cart {
    private List<SelectedBook> selectedBooks;

    public Cart() {
        selectedBooks = new ArrayList<>();
    }

    public String getCartList() {
        StringBuilder cartList = new StringBuilder();

        selectedBooks.forEach(selectedBook -> cartList.append(selectedBook.toString()).append("\n"));

        return cartList.toString();
    }

    public void addToCart(SelectedBook selectedBook) {
        // 장바구니에 동일한 도서가 있는지 확인
        for (SelectedBook book : selectedBooks) {
            if (book.getId().equals(selectedBook.getId())) {
                book.setTotalPrice();
                book.setQuantity();
                return;
            }
        }
        // 장바구니에 새 도서 추가
        selectedBooks.add(selectedBook);
    }

    public void removeFromCart(String id) {
        // 해당 ID의 책을 제거
        selectedBooks.removeIf(selectedBook -> selectedBook.getId().equals(id));
    }

    public void flushCart() {
        selectedBooks.clear();
    }

    public void printCartList() {
        System.out.println("장바구니 상품 목록 : ");
        System.out.println(this);  // toString 자동 호출
    }

    public void printReceipt() {
        int totalPrice = selectedBooks.stream()
                .mapToInt(SelectedBook::getTotalPrice)
                .sum();

        System.out.printf("총 결재 금액 : %,d원\n", totalPrice);
    }

    public String toString() {
        return String.format("--------------------------------------------------\n     도서 ID     |     수량     |     합계     \n%s--------------------------------------------------\n", getCartList());
    }
}


public class LibrarySystem {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int menuNumber = 0;

        // 프로그램이 실행되면, 보유도서 정보를 적당한 자료구조에 저장
        Book[] books = new Book[3];
        books[0] = new Book(
                "ISBN1234",
                "셜롬홈즈",
                20000,
                "코난 도일",
                "그 누구도 뛰어넘지 못했던 추리 소설의 고전",
                "추리소설",
                "2018/10/08"
        );
        books[1] = new Book(
                "ISBN2345",
                "도리안 그레이의 초상",
                16000,
                "오스카 와일드",
                "예술을 위한 예술!",
                "고전소설",
                "2022/01/22"
        );
        books[2] = new Book(
                "ISBN3456",
                "쥐덫",
                27000,
                "애거서 크리스티",
                "폭설 속에 갇힌 몽스웰 여관 네 명의 손님과 주인 부부, 그리고 한 명의 형사",
                "추리소설",
                "2019/06/10"
        );

        // 보유도서 출력
        for(int i = 0; i < books.length; i++) {
            System.out.println(books[i].toString());
        }

        // 사욛자 정보(이름, 연락처)를 입력 받음
        System.out.print("당신의 이름을 입력하세요 : ");
        String name = scanner.next();

        System.out.print("연락처를 입력하세요 : ");
        String phoneNumber = scanner.next();

        // 장바구니 생성
        Cart cart = new Cart();

        while(menuNumber != 7) {
            System.out.print(
                    "******************************************************\n" +
                    "오늘의 선택, 코난문고\n" +
                    "영원한 스테디셀러, 명탐정 코난시리즈를 만나보세요~\n" +
                    "******************************************************\n" +
                    "1. 고객 정보 확인하기 2. 장바구니 상품 목록 보기\n" +
                    "3. 장바구니에 항목 추가하기 4. 장바구니의 항목 삭제하기\n" +
                    "5. 장바구니 비우기 6. 영수증 표시하기 7. 종료\n" +
                    "메뉴 번호를 선택해주세요\n" +
                    "******************************************************\n" +
                    "메뉴 번호를 선택해주세요 "
            );
            menuNumber = scanner.nextInt();

            if(menuNumber == 1) {
                System.out.println("현재 고객 정보 : ");
                System.out.printf("이름 %s 연락처 %s\n", name, phoneNumber);
            } else if(menuNumber == 2) {
                // 장바구니 목록 호출하는 메소드 호출
                cart.printCartList();
            } else if(menuNumber == 3) {
                for(int i = 0; i < books.length; i++) {
                    books[i].toString();
                }

                System.out.print("장바구니에 추가할 도서의 ID를 입력하세요 : ");
                String id = scanner.next();

                System.out.print("장바구니에 추가하시겠습니까? Y | N ");

                if(scanner.next().trim().toLowerCase().equals("y")) {
                    int price = Arrays.stream(books)
                            .filter(book -> book.getId().equals(id))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 책이 존재하지 않습니다."))
                            .getPrice();

                    // 장바구니에 추가하는 메소드 호출
                    cart.addToCart(new SelectedBook(id, price));
                }
            } else if(menuNumber == 4) {
                cart.printCartList();

                System.out.print("장바구니에서 삭제할 도서의 ID를 입력하세요 : ");
                String id = scanner.next();

                // 장바구니에서 삭제하는 메소드 호출
                cart.removeFromCart(id);
                System.out.printf("장바구니에서 %s가 삭제되었습니다\n", id);
            } else if(menuNumber == 5) {
                // 장바구니 비우는 메소드 호출
                cart.flushCart();
                System.out.println("장바구니를 비웠습니다.");
            } else if(menuNumber == 6) {
                System.out.print("배송받을 분은 고객정보와 같습니까? ");  // y를 받는다는 전제 하

                if(scanner.next().equals("y")) {
                    System.out.print("배송지를 입력해주세요 ");
                    String destination = scanner.next();

                    SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd");

                    System.out.println("-----------------배송 받을 고객 정보-----------------");
                    System.out.printf("고객명 : %s  연락처 : %s\n배송지 : %s  발송일 : %s\n",
                            name,
                            phoneNumber,
                            destination,
                            sdf.format(new Date().getTime())
                    );

                    cart.printCartList();
                    cart.printReceipt();
                }
            }
        }
    }
}
