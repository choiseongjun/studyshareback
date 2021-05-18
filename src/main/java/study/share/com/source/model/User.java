package study.share.com.source.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.share.com.source.model.common.DateAudit;

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
public class User extends DateAudit{

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
    @JsonIgnore
    private String sex;
    @JsonIgnore
    private String age;
    


    private boolean verified = false;

    @NotBlank
    @Size(min=6, max = 100)
    @JsonIgnore
    private String password;
    
    @Transient
    private String password2;
    
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
    @OneToMany(fetch = FetchType.LAZY,orphanRemoval=true,mappedBy = "toUser")
	private List<Follow> follow;
   
    @JsonIgnoreProperties({"user"})	
    @OneToOne(mappedBy="user", orphanRemoval = true,fetch = FetchType.LAZY)
	private UserProfileImage userProfileImage;

    @Column(name="reportedCnt")
    @ColumnDefault("0")
    private long reportedCnt;

    @Column(name="account_suspend")
    @ColumnDefault("0")
    private long accountSuspend;
    
    private String fcmToken;
    
    public User(String userid,String nickname, String email,String sex,String password,String age) {
        this.userid = userid;
        this.nickname = nickname;
        this.email = email;
        this.sex=sex;
        this.password = password;
        this.age = age;
    }
//    public User(String userid2, String nickname2, String sex2, String email2, String encode) {
//		// TODO Auto-generated constructor stub
//	}
}