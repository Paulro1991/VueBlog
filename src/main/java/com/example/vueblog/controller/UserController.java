package com.example.vueblog.controller;


import com.example.vueblog.entity.User;
import com.example.vueblog.service.IUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author paulro1991
 * @since 2021-01-27
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final IUserService iUserService;

    public UserController(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @GetMapping({"/{id}"})
    User getUserById(@PathVariable Long id) {
        return iUserService.getById(id);
    }

}
