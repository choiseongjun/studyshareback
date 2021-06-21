package study.share.com.source.model.feed;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import study.share.com.source.model.Tag;
import study.share.com.source.model.feed.FeedList;

@Entity
@Table(name = "feed_tag")
@Data
@Getter
@Setter
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