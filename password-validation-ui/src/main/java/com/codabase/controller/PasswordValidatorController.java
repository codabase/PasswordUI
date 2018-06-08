package com.codabase.controller;


import com.codabase.password.validator.PasswordValidator;
import com.codabase.password.validator.UserPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.*;
import java.util.Set;

@Controller
public class PasswordValidatorController {


    @Autowired
    PasswordValidator passwordValidator;

    @GetMapping("/password")
    public String loadFormPage(Model model) {
        model.addAttribute("onError", "false");
        model.addAttribute("passwodForm", "aa");
        return "passwordForm";
    }

    @GetMapping("/")
    public String loadPage(Model model) {
        model.addAttribute("onError", "false");
        model.addAttribute("passwodForm", "aa");
        return "index";
    }


    @PostMapping("/password")
    public String submitForm(@Valid String password, Model model) {
        password = password.replace(",", "");
        UserPassword userPassword = new UserPassword(password);
        model.addAttribute("password", password);
        if (passwordValidator.isValid(userPassword)) {
            model.addAttribute("onError", "false");
            model.addAttribute("message", "is valid");
        }  else {
            model.addAttribute("onError", "true");
            StringBuilder message = new StringBuilder();
            for (String msg : userPassword.getFailureList()) {
                message.append(msg).append(", ");
            }
            model.addAttribute("message", message.toString());
        }
        return "passwordForm";
    }
}