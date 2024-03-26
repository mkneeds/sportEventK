package bsuir.kraevskij.sportevent.service;

import bsuir.kraevskij.sportevent.model.Role;
import bsuir.kraevskij.sportevent.model.User;
import bsuir.kraevskij.sportevent.repository.RoleRepository;
import bsuir.kraevskij.sportevent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void updateUserRole(Long userId, Long roleId) {
        User user = userRepository.findById(Math.toIntExact(userId)).orElseThrow(() -> new IllegalArgumentException("Пользователь с указанным идентификатором не найден: " + userId));
        Role role = roleRepository.findById(Math.toIntExact(roleId)).orElseThrow(() -> new IllegalArgumentException("Роль с указанным идентификатором не найдена: " + roleId));
        user.setRole(role);
        userRepository.save(user);
    }
}
