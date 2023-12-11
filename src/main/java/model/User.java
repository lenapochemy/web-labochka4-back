package model;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Dot> dots;

    public User(){

    }

    public User(String login, String password){
        this.login = login;
        this.password = password;
    }

    public void addDot(Dot newDot){
        newDot.setUser(this);
        dots.add(newDot);
    }

    public String getPassword() {
        return password;
    }
    public String getLogin() {
        return login;
    }

    public int getId() {
        return id;
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
