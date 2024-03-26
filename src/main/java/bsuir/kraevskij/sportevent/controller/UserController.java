package bsuir.kraevskij.sportevent.controller;

import bsuir.kraevskij.sportevent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {


    private final UserService userService;

    public UserController(UserService userService){
        this.userService=userService;
    }


    @PostMapping("/admin/users/change-role/{userId}")
    public ResponseEntity<String> changeUserRole(@PathVariable("userId") Long userId, @RequestParam("roleId") Long roleId) {
        userService.updateUserRole(userId, roleId);
        return ResponseEntity.ok().build();
    }
}
