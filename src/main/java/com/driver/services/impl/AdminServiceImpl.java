package com.driver.services.impl;

import com.driver.model.Admin;
import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    ServiceProviderRepository serviceProviderRepository;

    @Autowired
    CountryRepository countryRepository;

    public Admin register(String username, String password) {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password);
        return adminRepository.save(admin);
    }

    public Admin addServiceProvider(int adminId, String providerName) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(() -> new RuntimeException("Admin not found"));
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setName(providerName);
        serviceProvider.setAdmin(admin);
        serviceProviderRepository.save(serviceProvider);
        admin.getServiceProviders().add(serviceProvider);
        return adminRepository.save(admin);
    }

    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception {
        ServiceProvider serviceProvider = serviceProviderRepository.findById(serviceProviderId)
                .orElseThrow(() -> new RuntimeException("Service Provider not found"));

        CountryName countryEnum;
        try {
            countryEnum = CountryName.valueOf(countryName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new Exception("Country not found");
        }

        Country country = new Country();
        country.setCountryName(countryEnum);
        country.setCode(countryEnum.toCode());
        country.setServiceProvider(serviceProvider);
        countryRepository.save(country);
        serviceProvider.getCountryList().add(country);
        return serviceProviderRepository.save(serviceProvider);
    }
}
