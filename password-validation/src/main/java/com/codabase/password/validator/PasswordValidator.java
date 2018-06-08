package com.codabase.password.validator;

import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;


import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class PasswordValidator {

    private static int MIN_PASSWORD_LENGTH=5;
    private static int MAX_PASSWORD_LENGTH=12;

    public enum PasswordFailureEnum {
        PASSWORD_HAS_NO_DIGIT,
        PASSWORD_HAS_NO_LOWERCASE,
        PASSWORD_LENGTH_IS_TOO_SHORT,
        PASSWORD_LENGTH_IS_TOO_LONG,
        PASSWORD_CONTAINS_UPPERCASE,
        PASSWORD_CONTAINS_NON_ALPHA_CHARACTER,
        PASSWORD_CONTAINS_A_SEQUENCE_FOLLOWED_BY_THE_SAME_SEQUENCE,
        PASSWORD_IS_BLANK
    }

   public String passwordHasNoDigit() {return getMessage(PasswordFailureEnum.PASSWORD_HAS_NO_DIGIT.name());}
   public String passwordHasNoLowercase() {return getMessage(PasswordFailureEnum.PASSWORD_HAS_NO_LOWERCASE.name());}
   public String passwordLengthIsTooShort() {return getMessage(PasswordFailureEnum.PASSWORD_LENGTH_IS_TOO_SHORT.name()+"<"+MIN_PASSWORD_LENGTH);}
   public String passwordLengthIsTooLong () {return getMessage(PasswordFailureEnum.PASSWORD_LENGTH_IS_TOO_LONG.name()+">"+MAX_PASSWORD_LENGTH);}
   public String passwordContainsUppercase() {return getMessage(PasswordFailureEnum.PASSWORD_CONTAINS_UPPERCASE.name());}
   public String passwordContainsNonAlphaCharacter() {return getMessage(PasswordFailureEnum.PASSWORD_CONTAINS_NON_ALPHA_CHARACTER.name());}
   public String passwordContainsSequenceFollowedBySameSequence() {return getMessage(PasswordFailureEnum.PASSWORD_CONTAINS_A_SEQUENCE_FOLLOWED_BY_THE_SAME_SEQUENCE.name());}
    public String passWordIsBlank() {return getMessage(PasswordFailureEnum.PASSWORD_IS_BLANK.name());}

    public boolean isValid(UserPassword userPassword) {
        userPassword.getFailureList().clear();
        boolean isValid = true;
        if (userPassword.getPassword()==null || userPassword.getPassword().isEmpty())
        {
            userPassword.getFailureList().add(passWordIsBlank());
            isValid = false;
        } else {
            // 2) minimum password length is 5
            // 3) maximum password length is 12
            if (userPassword.getPassword().length() < 5) {
                userPassword.getFailureList().add(passwordLengthIsTooShort());
                isValid = false;
            }
            if (userPassword.getPassword().length() > 12) {
                userPassword.getFailureList().add(passwordLengthIsTooLong());
                isValid = false;
            }

            // 4) If Uppercase or Non Letter Character Found the password is not valid
            boolean hasDigit = false;
            boolean hasLowercase = false;
            for (char c : userPassword.getPassword().toCharArray()) {
                if (Character.isAlphabetic(c) && Character.isUpperCase(c)) {
                    isValid = false;
                    userPassword.getFailureList().add(passwordContainsUppercase());
                    //break;
                }
                if (Character.isLetterOrDigit(c)) {
                    // make sure at least has one digit and one lower case
                    if (!hasDigit && Character.isDigit(c)) {
                        hasDigit = true;
                    }
                    if (!hasLowercase && Character.isLowerCase(c)) {
                        hasLowercase = true;
                    }
                } else {
                    userPassword.getFailureList().add(passwordContainsNonAlphaCharacter());
                    isValid = false;
                    //break;
                }
            }
            if (hasDigit && hasLowercase) {
                // this the rule no problem
                ;
            } else {
                if (!hasDigit) {
                    userPassword.getFailureList().add(passwordHasNoDigit());
                }
                if (!hasLowercase) {
                    userPassword.getFailureList().add(passwordHasNoLowercase());
                }
                isValid = false;
            }


            // 4) can not have the same sequence of character 1 or more followed by the same

            String regex = "(\\w{1,})\\1"; //  Regular Expression if any sequence followed by same
            Pattern p = Pattern.compile(regex);
            Matcher matcher = p.matcher(userPassword.getPassword());
            if (matcher.find()) {
                userPassword.getFailureList().add(passwordContainsSequenceFollowedBySameSequence());
                isValid = false;
            }
        }
        return isValid;
    }


    private String getMessage(String text) {
        return text.toLowerCase().replace("_"," ");
    }

}