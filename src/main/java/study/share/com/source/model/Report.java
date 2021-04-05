package study.share.com.source.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import study.share.com.source.model.DTO.ReportDTO;
import study.share.com.source.model.common.DateAudit;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "report")
@Getter
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private long id;

    @Column(name="CONTENT")
    private String content;

    @Column(name="REG_DATE")
    @CreationTimestamp
    private LocalDateTime regdate;

    @Column(name="MOD_DATE")
    private LocalDateTime moddate;

    @Builder(builderMethodName = "createBuilder", builderClassName = "createBuilder")
    public Report(ReportDTO reportDTO) {
        this.id=reportDTO.getId();
        this.content=reportDTO.getContent();
        this.regdate=reportDTO.getRegdate();
        this.moddate=reportDTO.getModdate();
    }
}
