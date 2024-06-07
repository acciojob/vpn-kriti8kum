package com.driver.services.impl;

import com.driver.models.Country;
import com.driver.models.CountryName;
import com.driver.models.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CountryRepository countryRepository;

    public User register(String username, String password, String countryName) throws Exception {
        CountryName countryEnum;
        try {
            countryEnum = CountryName.valueOf(countryName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new Exception("Country not found");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setConnected(false);
        userRepository.save(user);

        Country country = new Country();
        country.setName(countryEnum.name());
        country.setCode(countryEnum.toCode());
        country.setUser(user);
        countryRepository.save(country);

        user.setOriginalCountry(country);
        user.setOriginalIp(country.getCode() + "." + user.getId());
        return userRepository.save(user);
    }

    public User subscribe(int userId, int serviceProviderId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        ServiceProvider serviceProvider = serviceProviderRepository.findById(serviceProviderId).orElseThrow(() -> new RuntimeException("Service Provider not found"));

        user.getServiceProviderList().add(serviceProvider);
        return userRepository.save(user);
    }
}
