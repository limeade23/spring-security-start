package example.jwtstart.service;

import example.jwtstart.model.entity.User;
import example.jwtstart.model.request.JoinRequest;
import example.jwtstart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean join(JoinRequest request) {

        if(userRepository.existsByUsername(request.username())) {
            return false;
        }

        User user = User.builder()
            .username(request.username())
            .password(bCryptPasswordEncoder.encode(request.password()))
            .role("ADMIN")
            .build();

        userRepository.save(user);

        return true;
    }

}
