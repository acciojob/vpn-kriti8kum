package com.driver.controllers;

import com.driver.models.User;
import com.driver.services.impl.ConnectionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/connection")
public class ConnectionController {

    @Autowired
    ConnectionServiceImpl connectionService;

    @PostMapping("/connect")
    public ResponseEntity<Void> connect(@RequestParam int userId, @RequestParam String countryName) throws Exception {
        connectionService.connect(userId, countryName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/disconnect")
    public ResponseEntity<Void> disconnect(@RequestParam int userId) throws Exception {
        connectionService.disconnect(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/communicate")
    public ResponseEntity<Void> communicate(@RequestParam int senderId, @RequestParam int receiverId) throws Exception {
        connectionService.communicate(senderId, receiverId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
