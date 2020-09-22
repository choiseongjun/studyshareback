package study.share.com.source.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "uploadfile")
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class UploadFile {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private long categorykey;

	private String filename;

	private String realname;

	private long filesize;

	private String filepath;

	private String ImageExtension;

	public UploadFile() {
	}
	
	@Builder
    public UploadFile(Long id, String filename, String filepath) {
       this.id = id;
       this.filename=filename;
       this.filepath = filepath;
   }
//	@ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "WEBLIST_ID", referencedColumnName = "ID")
//    private WebList weblist;
	
}
