package database;

import exceptions.DBException;
import jakarta.persistence.NoResultException;
import model.Dot;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.ejb.Stateful;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Stateful
public class DotBean {

    Transaction transaction = null;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void addDot(Dot dot)throws DBException{
        dot.setResult(isInArea(dot));
        Date d = new Date();
        dot.setTime(formatter.format(d));
        addDotToDB(dot);
    }

    private void addDotToDB(Dot dot) throws DBException {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(dot);
            transaction.commit();
            System.out.println("добавили точку в бд");
        } catch (NullPointerException e){
            if(transaction != null) transaction.rollback();
            System.out.println("не добавили точку в бд");
            throw new DBException();
        } catch (Exception e){
            if(transaction != null) transaction.rollback();
            e.printStackTrace();
            System.out.println("не добавили точку в бд");
            throw new DBException();
        }
    }

    public List<Dot> getDotsByUser(User user)throws DBException{
        List<Dot> dots = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            dots = session.createQuery("from Dot d where d.user = :user", Dot.class).setParameter("user", user).getResultList();
        } catch (NullPointerException e) {
            throw new DBException();
        } catch (NoResultException e){
            dots = null;
        } catch (Exception e){
            e.printStackTrace();
        }
        return dots;
    }

    private boolean isInArea(Dot dot){
        Double x = dot.getX();
        Double y = dot.getY();
        Double r = dot.getR();

        return ((0 <= x && x <= r && 0 <= y && y <= r/2) ||
                (x <= 0 && y >= 0 && y <= x + r/2) ||
                (x * x + y * y <= (r/2) * (r/2) && x >= 0 && y <= 0)
                );

    }
}
