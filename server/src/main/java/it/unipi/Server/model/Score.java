package it.unipi.Server.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author lucamaffioli
 */
@Entity
@Table(name = "scores")
public class Score implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "list_id", nullable = false)
    private UserList list;
    
    @Column(name = "points", nullable = false)
    private int points;
    
    @Column(name = "questions", nullable = false)
    private int questions;
    
    @Column(name = "time", nullable = false)
    private LocalDateTime time;
    
    public Score() {}

    public Score(User user, UserList list, int points, int questions) {
        this.user = user;
        this.list = list;
        this.points = points;
        this.questions = questions;
        this.time = LocalDateTime.now();
    }

    public UserList getList() {
        return list;
    }

    public void setList(UserList list) {
        this.list = list;
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getQuestions() {
        return questions;
    }

    public void setQuestions(int questions) {
        this.questions = questions;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
    
}
