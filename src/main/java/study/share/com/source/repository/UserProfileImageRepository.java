package study.share.com.source.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import study.share.com.source.model.User;
import study.share.com.source.model.UserProfileImage;

public interface UserProfileImageRepository extends JpaRepository<UserProfileImage, Long>{

	UserProfileImage findByFilename(String profileimagePaths);

	boolean existsByUser(User user);

	@Query("SELECT id from UserProfileImage WHERE user=(:user)")
	long selectProfileId(@Param("user") User user);



}
