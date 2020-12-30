package com.ex.utilities;

import com.ex.models.User;
import com.ex.persistence.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class to check if a user exists in the db
 */
@Component
public class CheckAuthUtil {
    private UserRepo userRepo;

    @Autowired
    public CheckAuthUtil(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    /**
     * Method simply checks to see if the user exists. If not, return false.
     * @param email the email to check
     * @return
     */
    public boolean checkEmail(String email){
        try{
            User user = this.userRepo.getByEmail(email);
            return true;
        }catch(NullPointerException e){
            return false;
        }
    }

    public User checkEmailAndGetUser(String email){
        User user = null;
        user = this.userRepo.getByEmail(email);
        return user;
    }
}
