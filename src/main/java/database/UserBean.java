package database;


import jakarta.persistence.NoResultException;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.ejb.Stateful;

@Stateful
public class UserBean {
    Transaction transaction = null;

    public User addUser(String login, String password){
        String hash = PasswordHasher.hash(password);
        User user = new User(login, hash);
        addUserToDB(user);
        return user;
    }

    private void addUserToDB(User user){
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (Exception e){
            if(transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public User findUser(String login){
        User user = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){

            user = session.createQuery("from User u where u.login = :login", User.class).setParameter("login", login).getSingleResult();
        } catch (NoResultException r){
          user = null;
        } catch (Exception e){
            e.printStackTrace();
//            if(transaction != null){
//                transaction.rollback();
//            }
        }
        return user;
    }

    //проверяет логин и пароль
    public boolean checkUser(String login, String password){
        User user = findUser(login);
        if(user == null) return false;
        String hash = PasswordHasher.hash(password);
        //System.out.println(hash);
        //System.out.println(user.getPassword());
        return hash.equals(user.getPassword());
    }

    //проверяет существование логина,
    //вернет false если логин не занят
    public boolean checkLogin(String login){
        User user = findUser(login);
        return user != null;
    }

}
