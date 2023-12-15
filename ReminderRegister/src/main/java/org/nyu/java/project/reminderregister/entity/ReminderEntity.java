package org.nyu.java.project.reminderregister.entity;



import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "reminders")
public class ReminderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column( length = 10)
    private Integer reminderId;

    @Column(nullable = false)
    private String reminderName;

    @Column(nullable = false)
    private String reminderWay;

    @Column(nullable = false)
    private LocalDate reminderDate;

    @Column(nullable = false)
    private LocalTime reminderTime;

    @Column(nullable = false, length = 500)
    private String reminderDescription;

    @Column
    private Boolean isExpired;

    @Column(nullable = false)
    private String reminderToEvent;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public ReminderEntity() {
    }

    public ReminderEntity(String reminderName, String reminderWay, LocalDate reminderDate, LocalTime reminderTime, String reminderDescription,String reminderToEvent) {
        this.reminderName = reminderName;
        this.reminderWay = reminderWay;
        this.reminderDate = reminderDate;
        this.reminderTime = reminderTime;
        this.reminderDescription = reminderDescription;
        this.isExpired = false;
        this.reminderToEvent  = reminderToEvent;
    }

    public String getReminderToEvent() {
        return reminderToEvent;
    }

    public void setReminderToEvent(String reminderToEvent) {
        this.reminderToEvent = reminderToEvent;
    }

    public Integer getReminderId() {
        return reminderId;
    }

    public void setReminderId(Integer id) {
        this.reminderId = id;
    }

    public String getReminderName() {
        return reminderName;
    }

    public void setReminderName(String reminderName) {
        this.reminderName = reminderName;
    }

    public String getReminderWay() {
        return reminderWay;
    }

    public void setReminderWay(String reminderWay) {
        this.reminderWay = reminderWay;
    }

    public LocalDate getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(LocalDate reminderDate) {
        this.reminderDate = reminderDate;
    }

    public LocalTime getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(LocalTime reminderTime) {
        this.reminderTime = reminderTime;
    }

    public String getReminderDescription() {
        return reminderDescription;
    }

    public void setReminderDescription(String reminderDescription) {
        this.reminderDescription = reminderDescription;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getExpired() {
        return isExpired;
    }

    public void setExpired(Boolean expired) {
        isExpired = expired;
    }

    @Override
    public String toString() {
        return "ReminderEntity{" +
                "reminderName='" + reminderName + '\'' +
                ", reminderWay='" + reminderWay + '\'' +
                ", reminderDate=" + reminderDate +
                ", reminderTime=" + reminderTime +
                ", reminderDescription='" + reminderDescription + '\'' +
                ", isExpired=" + isExpired +
                ", user=" + user +
                '}';
    }
}
