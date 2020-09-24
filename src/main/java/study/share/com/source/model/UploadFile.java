package study.share.com.source.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import study.share.com.source.model.common.DateAudit;

@Entity
@Table(name = "uploadfile")
@Data
public class UploadFile extends DateAudit{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@JsonIgnore
	private long userkey;
	@JsonIgnore
	private String filename;
	@JsonIgnore
	private String realname;
	@JsonIgnore
	private long filesize;

	private String src;
	@JsonIgnore
	private String ImageExtension;

	@ManyToOne(optional = true)
	@JoinColumn(name = "feedlist_id")
	@JsonIgnore
	private FeedList feedlist;
	
	public UploadFile() {
	}
	
	@Builder
    public UploadFile(Long id, String filename, String src) {
       this.id = id;
       this.filename=filename;
       this.src = src;
   }

	
}
