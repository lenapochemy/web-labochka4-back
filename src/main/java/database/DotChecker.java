package database;

import exceptions.DBException;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import model.Dot;
import model.User;

import java.util.List;

@Stateless
public class DotChecker {

    @EJB
    private DotBean dotBean;

    public void addDot(Dot dot) throws DBException {
        dotBean.addDot(dot);
    }

    public List<Dot> getDotsByUser(User user) throws DBException {
        return dotBean.getDotsByUser(user);
    }


}
