package com.zeptolab.zeptolabchatservice.repositories.persistence;

import jakarta.persistence.*;
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
        uniqueConstraints = {@UniqueConstraint(name = "uk_users_id_deleted", columnNames = {"id", "deleted"})}
)
public class User extends BaseEntity {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "invited_by")
    private String invitedBy;

    @Setter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private List<Device> devices = new ArrayList<>();


    public User(final String firstName, final String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void addDevice(final Device device) {
        device.setUser(this);
        this.devices.add(device);
    }

    public void setDevices(final List<Device> devices) {
        devices.forEach(device -> device.setUser(this));
        this.devices.clear();
        this.devices.addAll(devices);
    }


    public String getName() {
        return "%s %s".formatted(firstName, lastName);
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
                .append(this.firstName, user.firstName)
                .append(this.lastName, user.lastName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(INIT_ODD, MULTIPLY_ODD)
                .appendSuper(super.hashCode())
                .append(this.firstName)
                .append(this.lastName)
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