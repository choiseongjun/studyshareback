package study.share.com.source.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.share.com.source.model.Tag;

import java.util.Optional;


public interface TagRepository extends JpaRepository<Tag,Long> {

    Optional <Tag> findByname(String name);
}
