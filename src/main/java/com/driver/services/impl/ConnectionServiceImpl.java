package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ConnectionServiceImpl implements  ConnectionServices{

    @Autowired
    ConnectionRepository connectionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ServiceProviderRepository serviceProviderRepository;

    public User connect(int userId, String countryName) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (Boolean.TRUE.equals(user.getConnected())) {
            throw new Exception("Already connected");
        }

        if(countryName.equalsIgnoreCase(user.getOriginalCountry().getCountryName().toString())){
            return user;
        }

        List<ServiceProvider> serviceProviders = user.getServiceProviderList();
        ServiceProvider selectedProvider = serviceProviders.stream()
                .filter(sp -> sp.getCountryList().stream().anyMatch(c -> c.getCountryName().toString().equalsIgnoreCase(countryName)))
                .min(Comparator.comparingInt(ServiceProvider::getId))
                .orElseThrow(() -> new Exception("Unable to connect"));

        user.setConnected(true);
        user.setMaskedIp(countryName + "." + selectedProvider.getId() + "." + user.getId());
        userRepository.save(user);

        Connection connection = new Connection();
        connection.setUser(user);
        connection.setServiceProvider(selectedProvider);
        connectionRepository.save(connection);

        return user;
    }

    public User disconnect(int userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (Boolean.FALSE.equals(user.getConnected())) {
            throw new Exception("Already disconnected");
        }

        user.setConnected(false);
        user.setMaskedIp(null);
        userRepository.save(user);

        return user;
    }

    public User communicate(int senderId, int receiverId) throws Exception {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new RuntimeException("Receiver not found"));

        String receiverCurrentCountry = receiver.getConnected() ? receiver.getMaskedIp().split("\\.")[0] : receiver.getOriginalCountry().getCountryName().toString();
        if (sender.getOriginalCountry().getCountryName().toString().equalsIgnoreCase(receiverCurrentCountry)) {
            return sender;
        }

        return connect(senderId, receiverCurrentCountry);
    }
}
