package database;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import model.Dot;
import model.User;
import org.hibernate.JDBCException;

import java.util.List;

@Stateless
public class DotChecker {

    @EJB
    private DotBean dotBean;

    public boolean addDot(Double x, Double y, Double r, User user) throws JDBCException {
        return dotBean.addDot(x, y, r, user);
    }

    public List<Dot> getDotsByUser(User user) throws JDBCException{
        return dotBean.getDotsByUser(user);
    }


}
