package database;


import exceptions.DBException;
import jakarta.persistence.NoResultException;
import model.User;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.ejb.Stateful;
import org.hibernate.exception.JDBCConnectionException;

import java.net.ConnectException;

@Stateful
public class UserBean {
    Transaction transaction = null;

    public User addUser(String login, String password) throws DBException{
        String hash = PasswordHasher.hash(password);
        User user = new User(login, hash);
        addUserToDB(user);
        return user;
    }

    private void addUserToDB(User user) throws DBException {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (NullPointerException e){
            throw new DBException();
        } catch (Exception e){
            if(transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void updateUser(User user) throws DBException{
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
        } catch (NullPointerException e){
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
        } catch (NullPointerException e){
            throw new DBException();
        } catch (Exception e){
          // e.printStackTrace();
//            if(transaction != null){
//                transaction.rollback();
//            }
         user = null;
            throw new DBException();
        }
        return user;
    }

    public User findUserByToken(String token) throws DBException{
        User user;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            user = session.createQuery("from User u where u.token = :token", User.class).setParameter("token", token).getSingleResult();
        } catch (NoResultException r){
            user = null;
        } catch (NullPointerException e){
            throw new DBException();
        } catch (Exception e){
           // e.printStackTrace();
//            if(transaction != null){
//                transaction.rollback();
//            }
            user = null;
            throw new DBException();
        }
        return user;
    }

    //проверяет логин и пароль
    public boolean checkUser(String login, String password) throws DBException{
        User user = findUserByLogin(login);
        if(user == null) return false;
        String hash = PasswordHasher.hash(password);
        //System.out.println(hash);
        //System.out.println(user.getPassword());
        return hash.equals(user.getPassword());
    }

    //проверяет существование логина,
    //вернет false если логин не занят
    public boolean checkLogin(String login)throws DBException{
        User user = findUserByLogin(login);
        return user != null;
    }

}
