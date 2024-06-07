package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CountryRepository countryRepository;
    @Autowired
    ServiceProviderRepository serviceProviderRepository;
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
        country.setCountryName(countryEnum);
        country.setCode(countryEnum.toCode());
        country.setUser(user);
        countryRepository.save(country);

        user.setOriginalCountry(country);
        user.setOriginalIp(country.getCode() + "." + user.getId());
        return userRepository.save(user);
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {
        return null;
    }

    public User subscribe(int userId, int serviceProviderId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        ServiceProvider serviceProvider = serviceProviderRepository.findById(serviceProviderId).orElseThrow(() -> new RuntimeException("Service Provider not found"));

        user.getServiceProviderList().add(serviceProvider);
        return userRepository.save(user);
    }
}
