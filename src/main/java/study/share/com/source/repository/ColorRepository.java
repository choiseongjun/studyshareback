package study.share.com.source.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import study.share.com.source.model.Color;

public interface ColorRepository extends JpaRepository<Color,Long>{

	Color findByColor(String color);

}
