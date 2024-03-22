package bsuir.kraevskij.sportevent.service;


import bsuir.kraevskij.sportevent.model.User;
import bsuir.kraevskij.sportevent.model.UserBalance;
import bsuir.kraevskij.sportevent.repository.UserBalanceRepository;
import bsuir.kraevskij.sportevent.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final UserRepository repository;
    private final UserBalanceRepository balanceRepository;

    public UserDetailsServiceImp(UserRepository repository, UserBalanceRepository balanceRepository) {
        this.repository = repository;
        this.balanceRepository = balanceRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }
    @Transactional
    public User createUserWithBalance(User user) {
        User savedUser = repository.save(user);
        UserBalance userBalance = new UserBalance();
        userBalance.setUser(savedUser);
        userBalance.setBalance(0.0);
        balanceRepository.save(userBalance);
        return savedUser;
    }
}
