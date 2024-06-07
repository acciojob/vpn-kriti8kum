package com.driver.services.impl;

import com.driver.model.User;

public interface ConnectionServices {
    User connect(int userId, String countryName) throws Exception;
    User disconnect(int userId) throws Exception;
    User communicate(int senderId, int receiverId) throws Exception;
}
