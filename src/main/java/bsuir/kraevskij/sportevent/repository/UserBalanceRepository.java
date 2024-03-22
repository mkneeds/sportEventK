package bsuir.kraevskij.sportevent.repository;

import bsuir.kraevskij.sportevent.model.User;
import bsuir.kraevskij.sportevent.model.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBalanceRepository extends JpaRepository<UserBalance, Long> {

    UserBalance findByUser(User user);
    UserBalance findByUser_Username(String username);
}
