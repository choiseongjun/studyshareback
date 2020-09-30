package study.share.com.source.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
            "nickname"
        }),
        @UniqueConstraint(columnNames = {
            "userid"
        })
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank
    @Size(min=3, max = 50)
    private String nickname;

    @NaturalId
    @NotBlank
    private String userid;
    
    @Size(max = 50)
    @Email
    private String email;
    
    private String sex;

    @NotBlank
    @Size(min=6, max = 100)
    @JsonIgnore
    private String password;
    @Lob
	private String introduce;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", 
    	joinColumns = @JoinColumn(name = "user_id"), 
    	inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    @OneToMany(fetch = FetchType.LAZY,orphanRemoval=true,mappedBy = "user")
	private List<FeedLike> feedlike;
    @OneToMany(fetch = FetchType.LAZY,orphanRemoval=true,mappedBy = "user")
	private List<TodoList> todolist;
    
    @OneToOne(mappedBy="user", orphanRemoval = true,fetch = FetchType.LAZY)
	private UserProfileImage userProfileImage;
    
    public User(String userid,String nickname, String email,String sex,String password) {
        this.userid = userid;
        this.nickname = nickname;
        this.email = email;
        this.sex=sex;
        this.password = password;
    }
//    public User(String userid2, String nickname2, String sex2, String email2, String encode) {
//		// TODO Auto-generated constructor stub
//	}
}