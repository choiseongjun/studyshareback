package study.share.com.source.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.share.com.source.model.BlockedUser;
import study.share.com.source.model.User;
import study.share.com.source.model.report.ReportFeed;
import study.share.com.source.repository.BlockedUserRepository;
import study.share.com.source.repository.UserRepository;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class BlockedUserService {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BlockedUserRepository blockedUserRepository;

    public BlockedUser blockedUserSave (User user, long blockeduserId)
    {
        BlockedUser blockedUser=new BlockedUser();
        Optional <User> blockeduser = userService.findUserId(blockeduserId);
        if(blockeduser.isPresent())//신고 대상자 아이디가 존재할 때만
        {
            //피신고자 정보 가져오기
            Optional<BlockedUser> result=blockedUserRepository.findByUserIdAndBlockedUserId(user.getId(),blockeduser.get().getId());
            if(result.isPresent())
                blockedUser.setId(-1);//이미존재하는 경우
            else//새로 신고 하는 경우
            {
                blockedUser.setUser(user);
                blockedUser.setBlockedUser(blockeduser.get());
                blockedUserRepository.save(blockedUser);
            }
        }
        else
            blockedUser.setId(-1);//신고 대상자 아이디가 존재하지 않는 경우
        return blockedUser;
    }
    public BlockedUser blockedUserDelete (User user, long blockeduserId)
    {
        Optional <User> blockeduser = userService.findUserId(blockeduserId);
        BlockedUser blockedUser=new BlockedUser();
        if(blockeduser.isPresent())//차단 사용자 아이디가 존재 할때만
        {
            //각 사용자 정보 가져오기
            Optional<BlockedUser> result=blockedUserRepository.findByUserIdAndBlockedUserId(user.getId(),blockeduser.get().getId());
            if (result.isPresent())//차단을 한 경우가 맞을 때만 삭제
            {
                blockedUser.setBlockedUser(result.get().getBlockedUser());
                blockedUser.setUser(user);
                blockedUserRepository.deleteByUserIdAndBlockedUserId(user.getId(),blockeduser.get().getId());
            }
            else
                blockedUser.setId(-1);//차단 되어있지 않은 경우
        }
        else
            blockedUser.setId(-1);//차단 대상자 아이디가 존재하지 않은 경우
        return blockedUser;
    }


    public Page<BlockedUser> reportlist (Pageable pageable, long userid)
    {
        return blockedUserRepository.findAllByUserId(pageable,userid);
    }

    public List<BlockedUser> findReportList (long userid)
    {
        return blockedUserRepository.findAllByUserId(userid);
    }

}
