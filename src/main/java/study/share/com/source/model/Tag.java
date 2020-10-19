package study.share.com.source.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import study.share.com.source.model.common.DateAudit;

@Entity
@Table(name = "tag")
@Data
public class Tag extends DateAudit{
	   	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private long id;
	   	
	    private String name;
//	    @ManyToMany(mappedBy = "tags")
//	    private Set<Moim> moims = new HashSet<>();
	    @OneToMany(fetch = FetchType.LAZY,orphanRemoval=true,mappedBy = "tag")
		@JsonIgnore 
		private List<FeedTag> tag;
	
}