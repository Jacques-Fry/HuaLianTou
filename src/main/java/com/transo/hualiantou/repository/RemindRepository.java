package com.transo.hualiantou.repository;

import com.transo.hualiantou.pojo.Remind;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Jack_YD
 * @create 2019/9/5 14:06
 */

public interface RemindRepository extends JpaRepository<Remind,Long> {

    Page<Remind> findByUserIdAndStatusLikeAndTypeLikeOrderByCreateTimeDesc(long userId,String status,String type,Pageable pageable);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value="UPDATE message_remind SET status=?1 WHERE id=?2")
    void updateStatus(String status,long id);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value="UPDATE message_remind SET status=?1 WHERE user_id=?2 and type=?3")
    void updateStatusByUserId(String status,long userId,String type);

    //获取消息未读条数
    @Query(nativeQuery = true,value="SELECT count(1) FROM message_remind WHERE user_id=?1 AND status=?2")
    Integer countTotal(long userId,String status);

    boolean existsById(long id);

    @Query("select r.id from Remind r where r.userId=?1 and r.status=?2 and r.type=?3")
    List<Long> findByUserIdAndStatus(long userId,String status,String type);

    Integer countByTypeAndStatusAndUserId(String type,String status,long userId);
}
