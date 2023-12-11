package database;

import jakarta.ejb.Stateful;
import jakarta.ejb.StatefulTimeout;
import model.User;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

@Stateful
public class UserChecker {
    @EJB
    private UserBean userBean;
    private User currentUser = null;

    public boolean registration(String login, String password){
        if(userBean.checkLogin(login)) return false;

        User user = userBean.addUser(login, password);
      //  System.out.println("user.login = " + user.getLogin());
        return true;
    }

    public boolean login(String login, String password){
        if(!userBean.checkLogin(login)) return false;

        if(userBean.checkUser(login, password))  {
            currentUser = new User(login, password);
            System.out.println("currentUser = " +currentUser.getLogin());
            return true;
        }
            else return false;
    }

    public boolean logout(){
        //if(currentUser == null) return false;
        currentUser = null;
        System.out.println("currentUser = null");
        return true;
    }

    public User getUserByLogin(String login){
        User user = userBean.findUser(login);
        return user;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
