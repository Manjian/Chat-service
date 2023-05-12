package com.zeptolab.zeptolabchatservice.repositories.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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

    @Column(name = "address", nullable = false)
    private String address;

    public Device(final String address) {
        this.address = address;

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
                .append(this.address, device.address)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(INIT_ODD, MULTIPLY_ODD)
                .appendSuper(super.hashCode())
                .append(this.user)
                .append(this.address)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("user", this.user)
                .append("address", this.address)
                .toString();
    }
}