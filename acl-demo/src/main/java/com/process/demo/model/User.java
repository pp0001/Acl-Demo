package com.process.demo.model;

//import com.maurofokker.security.acl.validation.PasswordMatches;
//import com.maurofokker.security.acl.validation.ValidPassword;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

@Entity
//@PasswordMatches
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotEmpty(message = "Email is required.")
    private String email;

//    @ValidPassword
    @NotEmpty(message = "Password is required.")
    private String password;
    
    @NotEmpty(message = "Role Id is required.")
    private String roleId;

//	@ManyToOne(targetEntity = Ropa.class)
//	public Ropa ropa;
//    @ManyToMany
//	@JoinTable(	name="user_ropa",
//				joinColumns = {@JoinColumn(name="user_id", referencedColumnName = "id")}, 
//				inverseJoinColumns = {@JoinColumn(name = "ropa_id", nullable = false, referencedColumnName = "id")})
//    private Set<Ropa> ropa;

    public User() {
        super();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
    
//	public Ropa getRopa() {
//		return ropa;
//	}
//
//	public void setRopa(Ropa ropa) {
//		this.ropa = ropa;
//	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((email == null) ? 0 : email.hashCode());
        result = (prime * result) + ((id == null) ? 0 : id.hashCode());
        result = (prime * result) + ((password == null) ? 0 : password.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;

        if (email == null) {
            if (other.email != null) {
                return false;
            }
        } else if (!email.equals(other.email)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (password == null) {
            if (other.password != null) {
                return false;
            }
        } else if (!password.equals(other.password)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("User [id=").append(id).append(", email=").append(email).append(", roleId=").append(roleId).append("]");
        return builder.toString();
    }

}
