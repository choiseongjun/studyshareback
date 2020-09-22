//package study.share.com.source.model;
//
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Getter
//@Entity
//@Table(name = "gallery")
//public class Images {
//    @Id
//    @GeneratedValue(strategy= GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(length = 50, nullable = false)
//    private String title;
//
//    @Column(columnDefinition = "TEXT")
//    private String filePath;
//
//    @Builder
//    public Images(Long id, String title, String filePath) {
//        this.id = id;
//        this.title = title;
//        this.filePath = filePath;
//    }
//}