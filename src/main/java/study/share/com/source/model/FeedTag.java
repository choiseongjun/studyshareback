package study.share.com.source.model;

import javax.persistence.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "feed_tag")
@Data
public class FeedTag{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "feed_id")
	@JsonIgnore
	private FeedList feedlist;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "tag_id")
	@JsonIgnore
	private Tag tag;
}