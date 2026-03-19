package com.badzianga.todo.service;

import com.badzianga.todo.exception.EmailAlreadyUsedException;
import com.badzianga.todo.exception.InvalidEmailOrPasswordException;
import com.badzianga.todo.model.User;
import com.badzianga.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void register(String email, String password) throws EmailAlreadyUsedException {
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new EmailAlreadyUsedException();
        }

        User user = new User(email, passwordEncoder.encode(password));

        userRepository.save(user);
    }

    public String login(String email, String password) throws InvalidEmailOrPasswordException {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(InvalidEmailOrPasswordException::new);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidEmailOrPasswordException();
        }

        return jwtService.generateToken(user.getEmail());
    }
}
