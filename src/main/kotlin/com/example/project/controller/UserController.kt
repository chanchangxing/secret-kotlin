package com.example.project.controller

import com.example.project.bean.UserBean
import com.example.project.mapper.UserMapper
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.*

@RestController
@EnableAutoConfiguration
@RequestMapping("/user")
class UserController(var userMapper: UserMapper) {
    @PostMapping("/register")
    fun register(@RequestParam("name") name: String, @RequestParam("password") password: String): UserBean {
        val id = userMapper.register(name, password)
        return userMapper.getUserInfoById(id)
    }
}