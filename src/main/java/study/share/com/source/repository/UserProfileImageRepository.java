package study.share.com.source.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import study.share.com.source.model.UserProfileImage;

public interface UserProfileImageRepository extends JpaRepository<UserProfileImage, Long>{

	UserProfileImage findByFilename(String profileimagePaths);

}
