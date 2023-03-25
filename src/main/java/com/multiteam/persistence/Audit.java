package com.multiteam.persistence;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public class Audit {

    public Audit() {}

    @Id
    @GeneratedValue
    protected UUID id;

    @Column(name = "created_date")
    protected LocalDateTime createdDate;

    @Column(name = "removed_date")
    protected LocalDateTime removedDate;

    @Column(name = "updated_date")
    protected LocalDateTime updatedDate;

    @Column(name = "created_by")
    @CreatedBy
    protected String createdBy;

    @Column(name = "modified_by")
    @LastModifiedBy
    protected String modifiedBy;

    @Column(name = "active")
    protected boolean active = true;

    public UUID getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getRemovedDate() {
        return removedDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public boolean isActive() {
        return active;
    }

    @PrePersist
    public void onPrePersist() {
        this.createdDate = LocalDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedDate = LocalDateTime.now();
    }

    @PreRemove
    public void onPreRemove() {
        this.removedDate = LocalDateTime.now();
    }
}
