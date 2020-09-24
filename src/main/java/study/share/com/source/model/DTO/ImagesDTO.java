package study.share.com.source.model.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import study.share.com.source.model.UploadFile;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ImagesDTO {
    private Long id;
    private String title;
    private String src;
    private String imgFullPath;
    
    public UploadFile toEntity(){
    	UploadFile build = UploadFile.builder()
                .id(id)
                .filename(title)
                .src(src)
                .build();
        return build;
    }

    @Builder
    public ImagesDTO(Long id, String title, String src,String imgFullPath) {
        this.id = id;
        this.title = title;
        this.src = src;
        this.imgFullPath = imgFullPath;
    }
}