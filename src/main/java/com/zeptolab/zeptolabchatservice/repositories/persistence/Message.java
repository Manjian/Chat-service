package com.zeptolab.zeptolabchatservice.repositories.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "t_message",
        uniqueConstraints = {@UniqueConstraint(name = "uk_messages_id_deleted", columnNames = {"id", "deleted"})}
)
public class Message extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "channel_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_messages_channel_id"),
            nullable = false)
    private Channel channel;

    public Message(final String content, final String messageOwner) {
        this.content = content;
        this.messageOwner = messageOwner;
    }

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "message_owner", nullable = false)
    private String messageOwner;

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
                .append(this.channel, message.channel)
                .append(this.content, message.content)
                .append(this.messageOwner, message.messageOwner)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(INIT_ODD, MULTIPLY_ODD)
                .appendSuper(super.hashCode())
                .append(this.channel)
                .append(this.content)
                .append(this.messageOwner)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("channel", this.channel)
                .append("content", this.content)
                .append("messageOwner", this.messageOwner)
                .toString();
    }


}