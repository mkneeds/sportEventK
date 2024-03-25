package bsuir.kraevskij.sportevent.service;

import bsuir.kraevskij.sportevent.model.AdminMessage;
import bsuir.kraevskij.sportevent.repository.AdminMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AdminMessageService {

    private final AdminMessageRepository adminMessageRepository;

    @Autowired
    public AdminMessageService(AdminMessageRepository adminMessageRepository) {
        this.adminMessageRepository = adminMessageRepository;
    }

    public AdminMessage saveAdminMessage(AdminMessage adminMessage) {
        return adminMessageRepository.save(adminMessage);
    }

    public List<AdminMessage> getAllAdminMessages() {
        return adminMessageRepository.findAll();
    }

    public AdminMessage getAdminMessageById(Long id) {
        Optional<AdminMessage> adminMessageOptional = adminMessageRepository.findById(id);
        return adminMessageOptional.orElse(null);
    }

    public void deleteAdminMessage(Long id) {
        adminMessageRepository.deleteById(id);
    }
}
