package model;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String login;
    private String password;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Dot> dots;



    public User(){

    }

    public User(String login, String password){
        this.login = login;
        this.password = password;

    }

    public void addDot(Dot newDot){
        newDot.setOwner(this);
        dots.add(newDot);
    }

    public String getPassword() {
        return password;
    }
    public String getLogin() {
        return login;
    }

    public List<Dot> getDots() {
        return dots;
    }

    public void setDots(List<Dot> dots) {
        this.dots = dots;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
