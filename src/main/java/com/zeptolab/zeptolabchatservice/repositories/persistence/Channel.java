package com.zeptolab.zeptolabchatservice.repositories.persistence;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "t_channel",
        uniqueConstraints = {@UniqueConstraint(name = "uk_channels_id_deleted", columnNames = {"id", "deleted"})}
)
public class Channel extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Setter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "channel", fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();


    @Setter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "channel", fetch = FetchType.LAZY)
    private List<Message> messages = new ArrayList<>();


    public Channel(final String name) {
        this.name = name;
    }

    public void addUser(final User user) {
        this.users.add(user);
    }

    public void addMessage(final Message message) {
        this.messages.add(message);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Channel channel = (Channel) o;

        return new EqualsBuilder()
                .appendSuper(true)
                .append(this.name, name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(INIT_ODD, MULTIPLY_ODD)
                .appendSuper(super.hashCode())
                .append(this.name)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("name", this.getName())
                .toString();
    }
}

