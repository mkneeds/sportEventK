package bsuir.kraevskij.sportevent.repository;

import bsuir.kraevskij.sportevent.model.AdminMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMessageRepository extends JpaRepository<AdminMessage, Long> {

}

