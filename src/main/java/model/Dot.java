package model;

import jakarta.persistence.*;

@Entity
@Table (name = "dots")
public class Dot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "x")
    private Double x;
    @Column(name = "y")
    private Double y;
    @Column(name = "r")
    private Double r;
    @Column(name = "result")
    private boolean result;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    private String time;

    public Dot(Double x, Double y, Double r, User user){
        this.x = x;
        this.y = y;
        this.r = r;
        this.user = user;
    }

    public Dot(){
    }


    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getR() {
        return r;
    }
    public User getUser(){
        return user;
    }

    public String getTime() {
        return time;
    }

    public boolean getResult() {
        return result;
    }
    public int getId(){
        return id;
    }

    public String getResultString(){
        if(result) return "Точка попала";
        else return "Точка не попала";
    }

    public String getResultClass(){
        if(result) return "success";
        else return "fail";
    }
    public void setX(Double x){
        this.x = x;
    }
    public void setY(Double y){
        this.y = y;
    }
    public void setR(Double r){
        this.r = r;
    }
    public void setUser(User user){
        this.user = user;
    }
    public void setResult(boolean isInArea){
        this.result = isInArea;
    }
    public void setTime(String time){
        this.time = time;
    }

    public void setId(int id){
        this.id = id;
    }

}
