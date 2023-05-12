package com.zeptolab.zeptolabchatservice.repositories.persistence;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "t_users",
        uniqueConstraints = {@UniqueConstraint(name = "uk_users_name", columnNames = {"name"})}
)
public class User extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "channel_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_users_channel_id"))
    private Channel channel;


    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "session_id", nullable = false)
    private String sessionId;


    @Setter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private List<Device> devices = new ArrayList<>();


    public User(final String name, final String password, final String sessionId) {
        this.name = name;
        this.password = password;
        this.sessionId = sessionId;
    }

    public void addDevice(final Device device) {
        device.setUser(this);
        this.devices.add(device);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final User user = (User) o;

        return new EqualsBuilder()
                .appendSuper(true)
                .append(this.name, user.name)
                .append(this.password, user.password)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(INIT_ODD, MULTIPLY_ODD)
                .appendSuper(super.hashCode())
                .append(this.name)
                .append(this.password)
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