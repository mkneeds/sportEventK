package bsuir.kraevskij.sportevent.controller;

import bsuir.kraevskij.sportevent.model.User;
import bsuir.kraevskij.sportevent.model.UserBalance;
import bsuir.kraevskij.sportevent.repository.UserBalanceRepository;
import bsuir.kraevskij.sportevent.repository.UserRepository;
import bsuir.kraevskij.sportevent.service.JwtService;
import bsuir.kraevskij.sportevent.service.PurchaseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final UserBalanceRepository userBalanceRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Autowired
    public PurchaseController(PurchaseService purchaseService, UserBalanceRepository userBalanceRepository, JwtService jwtService, UserRepository userRepository) {
        this.purchaseService = purchaseService;
        this.userBalanceRepository = userBalanceRepository;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
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
    public Map<String, Double> getUserBalance(HttpServletRequest request) {
        String token = jwtService.extractTokenFromCookie(request);
        UserBalance userBalance = userBalanceRepository.findByUser_Username(jwtService.extractUsername(token));
        Map<String, Double> response = new HashMap<>();
        if (userBalance != null) {
            response.put("balance", userBalance.getBalance());
        } else {
            throw new RuntimeException("User balance not found for the provided token");
        }
        return response;
    }
    @GetMapping("/purchase/calculateTotalSales")
    public ResponseEntity<Double> calculateTotalSales(HttpServletRequest request) {
        String token = jwtService.extractTokenFromCookie(request);
        Optional<User> user = userRepository.findByUsername(jwtService.extractUsername(token));
        if (user.isPresent()) {
            double totalSales = purchaseService.getUserSales(user.get());
            return ResponseEntity.ok(totalSales);
        }else{
        return null;
        }
    }

    @GetMapping("/purchase/calculatePercantageTotal")
    public ResponseEntity<Double> calculatePercantageTotal(HttpServletRequest request) {
        String token = jwtService.extractTokenFromCookie(request);
        Optional<User> user = userRepository.findByUsername(jwtService.extractUsername(token));
        if (user.isPresent()) {
            double commission = purchaseService.calculateUserPercentage(user.get());
            return ResponseEntity.ok(commission);

        } else {
            return ResponseEntity.ok(null);
        }
    }
    @GetMapping("/purchase/getUserSales")
    public ResponseEntity<List<Integer>> getUserSales(HttpServletRequest request) {
        String token = jwtService.extractTokenFromCookie(request);
        Optional<User> user = userRepository.findByUsername(jwtService.extractUsername(token));
        if (user.isPresent()) {
            List<Map<String, Object>> salesData = purchaseService.getUserSalesData(user.get());
            List<Integer> salesOnly = salesData.stream()
                    .map(entry -> (int) entry.get("sales"))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(salesOnly);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
