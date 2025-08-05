package com.hedgerock.api.rest;

import com.hedgerock.api.dto.UserDTO;
import com.hedgerock.api.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        return userService.create(userDTO);
    }

    @GetMapping("{id}")
    public UserDTO getUserById(@PathVariable String id) {
        return userService.get(id);
    }

    @PatchMapping("{id}")
    public UserDTO updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        return userService.update(id, userDTO);
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable String id) {
        userService.delete(id);
    }
}