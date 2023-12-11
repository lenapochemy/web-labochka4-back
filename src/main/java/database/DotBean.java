package database;

import model.Dot;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.ejb.Stateful;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateful
public class DotBean {

    Transaction transaction = null;
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public boolean addDot(Double x, Double y, Double r, User user){
        Dot dot = new Dot(x, y, r, user);
        dot.setResult(isInArea(dot));
        Date d = new Date();
        dot.setTime(formatter.format(d));
        return addDotToDB(dot);
    }

    private boolean addDotToDB(Dot dot){
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(dot);
            transaction.commit();
            System.out.println("добавили точку в бд");
            return true;
        } catch (Exception e){
            //if(transaction != null) transaction.rollback();
            e.printStackTrace();
            System.out.println("не добавили точку в бд");
            return false;
        }
    }

    public List<Dot> getDotsByUser(User user){
        List<Dot> dots = new ArrayList<>();
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            dots = session.createQuery("from Dot d where d.user = :user", Dot.class).setParameter("user", user).getResultList();
        } catch (Exception e){
            e.printStackTrace();
            if(transaction != null){
                transaction.rollback();
            }
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
