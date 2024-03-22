package bsuir.kraevskij.sportevent.controller;

import bsuir.kraevskij.sportevent.model.UserBalance;
import bsuir.kraevskij.sportevent.repository.UserBalanceRepository;
import bsuir.kraevskij.sportevent.service.JwtService;
import bsuir.kraevskij.sportevent.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final UserBalanceRepository userBalanceRepository;
    private final JwtService jwtService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService, UserBalanceRepository userBalanceRepository, JwtService jwtService) {
        this.purchaseService = purchaseService;
        this.userBalanceRepository = userBalanceRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/product/purchase")
    public ResponseEntity<String> makePurchase(@RequestBody List<Map<String, Object>> purchaseRequests) throws Exception {
        for (Map<String, Object> purchaseRequest : purchaseRequests) {
            purchaseService.processPurchase(purchaseRequest);
        }
        return ResponseEntity.ok("Purchase processed successfully.");
    }
    @PostMapping("/product/getUserBalance")
    @ResponseBody
    public Map<String, Double> getUserBalance(@RequestBody Map<String, String> requestBody) {
        String token = requestBody.get("token");
        UserBalance userBalance = userBalanceRepository.findByUser_Username(jwtService.extractUsername(token));
        Map<String, Double> response = new HashMap<>();
        if (userBalance != null) {
            response.put("balance", userBalance.getBalance());
        } else {
            throw new RuntimeException("User balance not found for the provided token");
        }
        return response;
    }

}
