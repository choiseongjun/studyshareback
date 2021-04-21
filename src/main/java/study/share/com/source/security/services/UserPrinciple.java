package study.share.com.source.security.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import study.share.com.source.model.User;

public class UserPrinciple implements UserDetails {
	private static final long serialVersionUID = 1L;

	private Long id;

    private String name;

    private String userid;

    private String email;
    
    private String nickname;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrinciple(Long id, String name, 
			    		String userid, String email, String password,String nickname,
			    		Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.userid = userid;
        this.email = email;
        this.nickname=nickname;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserPrinciple build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())
        ).collect(Collectors.toList());

        return new UserPrinciple(
                user.getId(),
                user.getUserid(),
                user.getNickname(),
                user.getEmail(),
                user.getPassword(),
                user.getSex(),
                authorities
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
    	return userid;
    }
    public String getEmail() {
        return email;
    }
    public String getNickname() {
		return nickname;
	}

    @Override
    public String getUsername() {
        return userid;
    }

    @Override
    public String getPassword() {
        return password;
    }
    public void setUserid(String userid) {
		this.userid = userid;
	}


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        UserPrinciple user = (UserPrinciple) o;
        return Objects.equals(id, user.id);
    }
}