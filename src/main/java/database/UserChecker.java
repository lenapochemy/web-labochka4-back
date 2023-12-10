package database;

import model.User;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

@Stateless
public class UserChecker {
    @EJB
    private UserBean userBean;
    private User currentUser;

    public boolean registration(String login, String password){
        if(userBean.checkLogin(login)) return false;

        User user = userBean.addUser(login, password);
        System.out.println("user.login = " + user.getLogin());
        return true;
    }

    public boolean login(String login, String password){
        if(!userBean.checkLogin(login)) return false;

        if(userBean.checkUser(login, password))  {
            currentUser = new User(login, password);
            return true;
        }
            else return false;
    }

    public boolean logout(){
        if(currentUser == null) return false;
        currentUser = null;
        return true;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
