package bsuir.kraevskij.sportevent.service;



import bsuir.kraevskij.sportevent.model.AuthenticationResponse;
import bsuir.kraevskij.sportevent.model.Role;
import bsuir.kraevskij.sportevent.model.Token;
import bsuir.kraevskij.sportevent.model.User;
import bsuir.kraevskij.sportevent.repository.RoleRepository;
import bsuir.kraevskij.sportevent.repository.TokenRepository;
import bsuir.kraevskij.sportevent.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final UserDetailsServiceImp userDetailsServiceImp;

    private final TokenRepository tokenRepository;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository repository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 RoleRepository roleRepository, UserDetailsServiceImp userDetailsServiceImp, TokenRepository tokenRepository,
                                 AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.roleRepository = roleRepository;
        this.userDetailsServiceImp = userDetailsServiceImp;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(User request) {


        if(repository.findByUsername(request.getUsername()).isPresent()) {
            return new AuthenticationResponse(null, "Данный пользователь уже существует",409);
        }
        initializeRoles();
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        if(isEmailUnique(request.getEmail())){
            user.setEmail(request.getEmail());
        }
        else{
            return new AuthenticationResponse(null, "Данный EMAIL уже существует",409);
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));


        Role role = roleRepository.findByName(request.getRole().getName());

        if (role == null) {

            throw new IllegalArgumentException("Роль '" + request.getRole().getName() + "' не найдена в базе данных.");
        }

        user.setRole(role);


        user = userDetailsServiceImp.createUserWithBalance(user);

        String jwt = jwtService.generateToken(user);

        saveUserToken(jwt, user);

        return new AuthenticationResponse(jwt, "User registration was successful",200);

    }

    public boolean isEmailUnique(String email) {
        Optional<User> user = repository.findByEmail(email);
        return user.isEmpty();
    }

    public AuthenticationResponse authenticate(User request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = repository.findByUsername(request.getUsername()).orElseThrow();
        String jwt = jwtService.generateToken(user);

        revokeAllTokenByUser(user);
        saveUserToken(jwt, user);

        return new AuthenticationResponse(jwt, "User login was successful",200);

    }
    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllTokensByUser(user.getId());
        if(validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t-> {
            t.setLoggedOut(true);
        });

        tokenRepository.saveAll(validTokens);
    }
    private void saveUserToken(String jwt, User user) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }
    private void initializeRoles() {
        Role adminRole = roleRepository.findByName("ADMIN");
        if (adminRole == null) {
            adminRole = roleRepository.save(new Role("ADMIN"));
        }

        Role userRole = roleRepository.findByName("USER");
        if (userRole == null) {
            userRole = roleRepository.save(new Role("USER"));
        }

        Role creatorRole = roleRepository.findByName("CREATOR");
        if (creatorRole == null) {
            creatorRole = roleRepository.save(new Role("CREATOR"));
        }
    }

}
