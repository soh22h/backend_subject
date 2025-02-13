package org.board.repository;

import lombok.extern.log4j.Log4j2;
import org.board.dto.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Log4j2
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByName(){
        User user2 = userRepository.findByName("Kim2");

        List<User> users = userRepository.findAll();
        Assertions.assertTrue(!users.isEmpty());

        User selectedUser = users.stream().filter(user -> "Kim2".equals(user.getName())).findAny().orElse(null);

        log.info("특정 사용자: Id={}, Name={}, Email={}", user2.getId(), user2.getName(), user2.getEmail());
        Assertions.assertEquals(selectedUser.getId(), user2.getId());
        Assertions.assertEquals(selectedUser.getName(), user2.getName());
        Assertions.assertEquals(selectedUser.getEmail(), user2.getEmail());
    }

    @Test
    public void testFindAll() {
        List<User> users = userRepository.findAll();

        users.forEach(user -> log.info("User: id={}, name={}, email={}", user.getId(), user.getName(), user.getEmail()));

        Assertions.assertEquals(users.size(), 5);
    }
}
