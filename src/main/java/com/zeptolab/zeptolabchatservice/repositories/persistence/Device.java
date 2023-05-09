package com.zeptolab.zeptolabchatservice.repositories.persistence;

import com.zeptolab.zeptolabchatservice.repositories.type.DeviceType;
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
@Table(name = "t_devices",
        uniqueConstraints = {@UniqueConstraint(name = "uk_devices_id_deleted", columnNames = {"id", "deleted"})}
)
public class Device extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_devices_user_id"),
            nullable = false)
    private User user;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private DeviceType type;


    @Column(name = "push_notification_token")
    private String pushNotificationToken;


    public Device(final String name,
                  final DeviceType type) {
        this.name = name;
        this.type = type;

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Device device = (Device) o;

        return new EqualsBuilder()
                .appendSuper(true)
                .append(this.user, device.user)
                .append(this.name, device.name)
                .append(this.type, device.type)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(INIT_ODD, MULTIPLY_ODD)
                .appendSuper(super.hashCode())
                .append(this.user)
                .append(this.name)
                .append(this.type)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("user", this.user)
                .append("name", this.name)
                .append("type", this.type)
                .toString();
    }
}