package org.board;

import org.board.dto.User;
import org.board.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.IntStream;

@Configuration
public class UserDataInitializer {

    @Bean
    CommandLineRunner initializeUsers(UserRepository userRepository) {
        return args -> {
            // 5명의 임의 사용자 데이터 생성
            IntStream.rangeClosed(1, 5).forEach(i -> {
                User user = User.builder()
                        .name("Kim" + i)
                        .email("kim" + i + "@aaa.com")
                        .build();
                userRepository.save(user);
            });
        };
    }
}
