package database;

import exceptions.DBException;
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

    public boolean addDot(Double x, Double y, Double r, User user) throws DBException {
        return dotBean.addDot(x, y, r, user);
    }

    public List<Dot> getDotsByUser(User user) throws DBException {
        return dotBean.getDotsByUser(user);
    }


}
