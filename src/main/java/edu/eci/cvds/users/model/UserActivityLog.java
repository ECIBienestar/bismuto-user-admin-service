package edu.eci.cvds.users.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_activity_logs")
public class UserActivityLog {

    @Id
    @Column(name = "log_id", length = 36)
    private String logId; //e.g. a UUID string

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "Action", nullable = false, length = 100)
    private String action;

    @Column(name = "description", length = 500)
    private String description;

    protected UserActivityLog() {}

    public UserActivityLog(User user, String action,
                           String description) {
        this.logId = UUID.randomUUID().toString();
        this.user = user;
        this.timestamp = LocalDateTime.now();
        this.action = action;
        this.description = description;
    }

    @PrePersist
    private void prePersist() {
        if (this.logId == null) {
            this.logId = UUID.randomUUID().toString();
        }
        if (this.timestamp == null) {
            this.timestamp = LocalDateTime.now();
        }
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}