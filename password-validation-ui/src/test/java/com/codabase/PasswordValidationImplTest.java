package com.codabase;

import com.codabase.password.validator.PasswordValidator;
import com.codabase.password.validator.UserPassword;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com.codabase/config/user-beans.xml"})
public class PasswordValidationImplTest {

    @Autowired
    PasswordValidator passwordValidator;

    @Test
    public void has3Issue()
    {
        UserPassword userPassword = new UserPassword("xxxVVV&");
        assertFalse(passwordValidator.isValid(userPassword));
        assertTrue(userPassword.getFailureList().contains(passwordValidator.passwordHasNoDigit()));
        assertTrue(userPassword.getFailureList().contains(passwordValidator.passwordContainsSequenceFollowedBySameSequence()));
        assertTrue(userPassword.getFailureList().contains(passwordValidator.passwordContainsNonAlphaCharacter()));
        assertTrue(userPassword.getFailureList().contains(passwordValidator.passwordContainsUppercase()));
    }

    @Test
    public void noLowerCaesIssue()
    {
        UserPassword userPassword = new UserPassword("567845");
        passwordValidator.isValid(userPassword);
        assertFalse( passwordValidator.isValid(userPassword));
        assertTrue(userPassword.getFailureList().contains(passwordValidator.passwordHasNoLowercase()));
    }
}
