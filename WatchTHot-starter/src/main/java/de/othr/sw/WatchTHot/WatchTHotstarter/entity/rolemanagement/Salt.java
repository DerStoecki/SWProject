package de.othr.sw.WatchTHot.WatchTHotstarter.entity.rolemanagement;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.User;

import javax.persistence.*;

@Entity
@Table(name="SALT", schema = "swwatchthot")
@Access(AccessType.FIELD)
public class Salt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="SALT_ID")
    private Long id;

    @OneToOne
    private User user;

    @Column(name = "SALT_VALUE")
    private String saltValue;

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!obj.getClass().equals(Salt.class)) {
            return false;
        }
        Salt otherSalt = (Salt) obj;
        return this.id.equals(otherSalt.id);
    }
    }
}
