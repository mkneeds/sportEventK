package bsuir.kraevskij.sportevent.repository;

import bsuir.kraevskij.sportevent.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
