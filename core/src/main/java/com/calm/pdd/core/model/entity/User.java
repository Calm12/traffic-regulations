package com.calm.pdd.core.model.entity;

import com.calm.pdd.core.model.enums.Role;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@EqualsAndHashCode(exclude = {"id"})
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
	
	//TODO вынести UserDetails из entity!
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(length = 32)
	private String username;
	
	private String email;
	
	@Column(length = 64)
	private String password;
	
	private boolean active;
	
	@ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
	@Enumerated(EnumType.STRING)
	private Set<Role> roles;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime modifiedAt;
	
	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}
	
	public User addRole(Role role) {
		if(roles == null) {
			roles = new HashSet<>();
		}
		roles.add(role);
		
		return this;
	}
	
	public User removeRole(Role role) {
		if(roles != null) {
			roles.remove(role);
		}
		
		return this;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return isActive();
	}
}
