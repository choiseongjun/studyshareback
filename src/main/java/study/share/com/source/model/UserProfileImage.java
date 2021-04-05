package study.share.com.source.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "userprofileimage")
@Getter
@Setter
public class UserProfileImage {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@JsonIgnore
	private String filename;
	@JsonIgnore
	private String realname;
	@JsonIgnore
	private long filesize;

	private String src;
	@JsonIgnore
	private String ImageExtension;
	@JsonIgnore
	private String ipaddress;
	@OneToOne
	@JsonIgnore
	private User user;
}
