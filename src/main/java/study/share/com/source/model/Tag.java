package study.share.com.source.model;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import study.share.com.source.model.common.DateAudit;
import study.share.com.source.model.feed.FeedTag;

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
	    @OneToMany(fetch = FetchType.LAZY,orphanRemoval=true,mappedBy = "tag",cascade = CascadeType.ALL)
		@JsonIgnore 
		private List<FeedTag> tag;
	
}