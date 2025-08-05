package com.hedgerock.api.service.user;

import com.hedgerock.api.dto.UserDTO;

public interface UserService {
    UserDTO create(UserDTO userDTO);
    UserDTO get(String id);
    UserDTO update(String id, UserDTO dto);
    void delete(String id);
}
