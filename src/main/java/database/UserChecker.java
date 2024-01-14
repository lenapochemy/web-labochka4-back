package database;

import exceptions.DBException;
import exceptions.TokenException;
import jakarta.ejb.Stateful;
import model.User;

import jakarta.ejb.EJB;

@Stateful
public class UserChecker {
    @EJB
    private UserBean userBean;
    private User currentUser = null;
    @EJB
    TokenUtils tokenUtils;

    public boolean registration(User user) throws DBException {
        if(userBean.checkLogin(user.getLogin())) return false;
        userBean.addUser(user);
        return true;
    }

    public String login(User badUser) throws DBException{

            if (userBean.checkUser(badUser)) {
                currentUser = userBean.getCurrentUser();
                String token = tokenUtils.generateToken(currentUser);
                return token;
            } else return null;

    }


    public User getUserFromToken(String token) throws TokenException, DBException {
        if(token.isEmpty()) throw  new TokenException();
        if(tokenUtils.verifyToken(token)){
            return tokenUtils.decodeToken(token);
//            return userBean.findUserByLogin(login);

        } else throw new TokenException();
    }

}
