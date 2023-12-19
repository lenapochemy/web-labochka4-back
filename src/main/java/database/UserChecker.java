package database;

import exceptions.DBException;
import jakarta.ejb.Stateful;
import model.User;

import jakarta.ejb.EJB;

@Stateful
public class UserChecker {
    @EJB
    private UserBean userBean;
    private User currentUser = null;
    TokenUtils tokenUtils = new TokenUtils();

    public boolean registration(String login, String password) throws DBException {
        if(userBean.checkLogin(login)) return false;

        User user = userBean.addUser(login, password);
      //  System.out.println("user.login = " + user.getLogin());
        return true;
    }

    public boolean login(String login, String password) throws DBException{
            //if(!userBean.checkLogin(login)) return false;
            if (userBean.checkUser(login, password)) {
                currentUser = userBean.findUserByLogin(login);
                String token = tokenUtils.generateToken(currentUser);
                currentUser.setToken(token);
                userBean.updateUser(currentUser);
                //System.out.println("currentUser = " +currentUser.getLogin());
                return true;
            } else return false;

    }


    public boolean logout(User user) throws DBException{
        //if(currentUser == null) return false;
        user.setToken(null);
        userBean.updateUser(user);
//        System.out.println("currentUser = null");
        return true;
    }

    public User getUserByLogin(String login) throws DBException{
        return userBean.findUserByLogin(login);
    }

    public User getUserByToken(String token) throws DBException{
        return userBean.findUserByToken(token);
    }

//    public User getCurrentUser() {
//        return currentUser;
//    }
}
