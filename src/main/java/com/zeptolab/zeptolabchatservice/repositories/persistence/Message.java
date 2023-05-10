package com.zeptolab.zeptolabchatservice.repositories.persistence;

import com.zeptolab.zeptolabchatservice.repositories.type.MessageType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_message",
        uniqueConstraints = {@UniqueConstraint(name = "uk_messages_id_deleted", columnNames = {"id", "deleted"})}
)
public class Message extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_messages_user_id"),
            nullable = false)
    private User user;


    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "room", nullable = false)
    private String room;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Message message = (Message) o;

        return new EqualsBuilder()
                .appendSuper(true)
                .append(this.user, message.user)
                .append(this.content, message.content)
                .append(this.messageType, message.messageType)
                .append(this.room, message.room)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(INIT_ODD, MULTIPLY_ODD)
                .appendSuper(super.hashCode())
                .append(this.user)
                .append(this.content)
                .append(this.messageType)
                .append(this.room)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("user", this.user)
                .append("content", this.content)
                .append("room", this.room)
                .toString();
    }


}