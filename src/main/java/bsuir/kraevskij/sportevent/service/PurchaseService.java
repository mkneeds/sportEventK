package bsuir.kraevskij.sportevent.service;

import bsuir.kraevskij.sportevent.model.Purchase;
import bsuir.kraevskij.sportevent.model.User;
import bsuir.kraevskij.sportevent.model.UserBalance;
import bsuir.kraevskij.sportevent.repository.ProductRepository;
import bsuir.kraevskij.sportevent.repository.PurchaseRepository;
import bsuir.kraevskij.sportevent.repository.UserBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final UserBalanceRepository userBalanceRepository;
    private final JwtService jwtService;
    private final ProductRepository productRepository;

    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository, UserBalanceRepository userBalanceRepository, JwtService jwtService, ProductRepository productRepository) {
        this.purchaseRepository = purchaseRepository;
        this.userBalanceRepository = userBalanceRepository;
        this.jwtService = jwtService;
        this.productRepository = productRepository;
    }

    @Transactional
    public void processPurchase(Map<String, Object> purchaseRequest) throws Exception {
        String title = (String) purchaseRequest.get("title");
        String priceString = (String) purchaseRequest.get("price");
        double price = Double.parseDouble(priceString);
        String quantityString = (String) purchaseRequest.get("quantity");
        int quantity = Integer.parseInt(quantityString);
        String token = (String) purchaseRequest.get("token");

        UserBalance buyerBalance = userBalanceRepository.findByUser_Username(jwtService.extractUsername(token));
        if (buyerBalance == null) {
            throw new IllegalArgumentException("Buyer balance not found for user: " );
        }

        UserBalance sellerBalance = userBalanceRepository.findByUser(productRepository.findByName(title).get().getUser());
        if (sellerBalance == null) {
            throw new IllegalArgumentException("Seller balance not found for user: ");
        }

        double amount = price * quantity;

        if (buyerBalance.getBalance() >= amount) {
            buyerBalance.setBalance(buyerBalance.getBalance() - amount);
            userBalanceRepository.save(buyerBalance);

            sellerBalance.setBalance(sellerBalance.getBalance() + amount);
            userBalanceRepository.save(sellerBalance);

            Purchase purchase = new Purchase();
            purchase.setBuyer(buyerBalance.getUser());
            purchase.setSeller(sellerBalance.getUser());
            purchase.setAmount(amount);
            purchase.setPurchaseDate(new Date());
            purchaseRepository.save(purchase);
        } else {
            throw new Exception("Insufficient balance for purchase.");
        }
    }
    public double getTotalSales() {
        Double totalSales = purchaseRepository.sumAmount();
        return totalSales != null ? totalSales.doubleValue() : 0.0;
    }


    public double calculateUserPercentage(User user) {
        double totalSales = getTotalSales();
        double userSales = getUserSales(user);
        return (userSales / totalSales) * 100;
    }
    private int extractMonthFromDate(Date purchaseDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(purchaseDate);
        return cal.get(Calendar.MONTH);
    }
    @Transactional(readOnly = true)
    public double getUserSales(User user) {
        List<Purchase> userPurchases = purchaseRepository.findBySeller(user);
        double userSales = userPurchases.stream().mapToDouble(Purchase::getAmount).sum();
        return userSales;
    }
    public List<Map<String, Object>> getUserSalesData(User user) {
        List<Map<String, Object>> salesData = new ArrayList<>();

        List<Purchase> userPurchases = purchaseRepository.findBySeller(user);
        for (int i = 0; i < 12; i++) {
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", i);
            int salesCount = 0;
            for (Purchase purchase : userPurchases) {
                if (extractMonthFromDate(purchase.getPurchaseDate()) == i) {
                    salesCount++;
                }
            }
            monthData.put("sales", salesCount);
            salesData.add(monthData);
        }
        return salesData;
    }

}
