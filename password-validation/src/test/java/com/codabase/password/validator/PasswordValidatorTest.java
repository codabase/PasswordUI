package com.codabase.password.validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;



@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class PasswordValidatorTest {

    // injected by Spring IoC
    private PasswordValidator passwordValidator;

    @Autowired
    public void setPasswordFailureListI(PasswordValidator passwordValidator) {
        this.passwordValidator = passwordValidator;
    }

    private void testPasswordIsNotValid(UserPassword userPassword, String inValidValue) {
        assertFalse(passwordValidator.isValid(userPassword));
        assertEquals(1,userPassword.getFailureList().size());
        assertTrue(userPassword.getFailureList().contains(inValidValue));
    }

    private void testPasswordIsValid(UserPassword userPassword) {
        assertTrue(passwordValidator.isValid(userPassword));
        assertTrue(userPassword.getFailureList().isEmpty());
    }


    /**
     * password="12345r" Testcase 0 digit with one char at the end (valid)
     *
     */
    @Test
    public void passwordT_1_Valid() {

        // The password
        UserPassword userPassword = new UserPassword("1234r");
        testPasswordIsValid(userPassword);
    }

    /**
     * Testcase 1 (no) lowercase
     */
    @Test
    public void passwordT_2_InValidNoLowercase() {
        // The password
        UserPassword userPassword = new UserPassword("12345");
        testPasswordIsNotValid(userPassword,passwordValidator.passwordHasNoLowercase());
    }


    /**
     * Testcase 2 (no) digit
     */
    @Test
    public void passwordT_3_InValidNoDigit() {

        // The password
        UserPassword userPassword = new UserPassword("abcde");
        testPasswordIsNotValid(userPassword,passwordValidator.passwordHasNoDigit());
    }

    /**
     * Testcase 3 short password
     */
    @Test
    public void passwordT_4_InValidTooShort() {

        // The password
        UserPassword userPassword = new UserPassword("aba1");
        testPasswordIsNotValid(userPassword,passwordValidator.passwordLengthIsTooShort());
    }

    /**
     * Testcase 4 long password
     */
    @Test
    public void passwordT_5_IsValid() {

        // The password
        UserPassword userPassword = new UserPassword("1234567890a234");
        testPasswordIsNotValid(userPassword,passwordValidator.passwordLengthIsTooLong());
    }

    /**
     * Testcase 5 hAS SPACE
     */
    @Test
    public void passwordT_6_InValidContainsNonAlpha() {
        // The password
        UserPassword userPassword = new UserPassword(" 1234a"); //contains space
        testPasswordIsNotValid(userPassword,passwordValidator.passwordContainsNonAlphaCharacter());
    }


    /**
     * Testcase 6 hAS Uppercase
     */
    @Test
    public void passwordT_7_InValidContainsUppercase() {
        // The password
        UserPassword userPassword = new UserPassword("1234Ua");
        testPasswordIsNotValid(userPassword, passwordValidator.passwordContainsUppercase());
    }

    /**
     * Testcase 7 repeating sequence
     */
    @Test
    public void passwordInValid7() {

        // The password
        UserPassword userPassword = new UserPassword("1231234a");
        testPasswordIsNotValid(userPassword, passwordValidator.passwordContainsSequenceFollowedBySameSequence());
    }

}