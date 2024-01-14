package database;
import exceptions.DBException;
import jakarta.persistence.NoResultException;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.ejb.Stateful;

@Stateful
public class UserBean {
    Transaction transaction = null;
    User currentUser = null;

    public User addUser(User user) throws DBException{
        String hash = PasswordHasher.hash(user.getPassword());
        user.setPassword(hash);
        addUserToDB(user);
        return user;
    }

    private void addUserToDB(User user) throws DBException {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (NullPointerException e){
            if(transaction != null) transaction.rollback();
            throw new DBException();
        } catch (Exception e){
            if(transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }


    public User findUserByLogin(String login) throws DBException{
        User user;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            user = session.createQuery("from User u where u.login = :login", User.class).setParameter("login", login).getSingleResult();
        } catch (NoResultException r) {
            user = null;
        } catch (Exception e){
            e.printStackTrace();
            throw new DBException();
        }
        return user;
    }


    //проверяет логин и пароль
    public boolean checkUser(User badUser) throws DBException{
        User findUser = findUserByLogin(badUser.getLogin());
        if(findUser == null) return false;
        String hash = PasswordHasher.hash(badUser.getPassword());
        //System.out.println("password: " + hash);

        if(hash.equals(findUser.getPassword())){
            currentUser = findUser;
            return true;
        }
        return false;
    }

    //проверяет существование логина,
    //вернет false если логин не занят
    public boolean checkLogin(String login)throws DBException{
        User user = findUserByLogin(login);
        return user != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
