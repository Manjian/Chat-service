package com.zeptolab.zeptolabchatservice.repositories.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("VisibilityModifier")
public abstract class BaseEntity {

    protected static final int INIT_ODD = 17;
    protected static final int MULTIPLY_ODD = 17;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    protected UUID id;


    @Column(name = "created", nullable = false)
    protected LocalDateTime created;

    @Column(name = "updated", nullable = false)
    protected LocalDateTime updated;

    @Column(name = "deleted")
    protected LocalDateTime deleted;

    @PrePersist
    protected void onCreate() {
        this.created = LocalDateTime.now();
        this.updated = this.created;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final BaseEntity ent = (BaseEntity) o;

        return new EqualsBuilder()
                .appendSuper(true)
                .append(this.id, ent.id)
                .append(this.created, ent.created)
                .append(this.updated, ent.updated)
                .append(this.deleted, ent.deleted)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(INIT_ODD, MULTIPLY_ODD)
                .append(id)
                .append(this.id)
                .append(this.created)
                .append(this.updated)
                .append(this.deleted)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.id)
                .append("created", this.created)
                .append("updated", this.updated)
                .append("deleted", this.deleted)
                .toString();
    }
}