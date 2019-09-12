package com.hisense.task.repository;


import com.hisense.task.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data JPA repository for the BsFirstPlan entity.
 */
@Repository
public interface UsersRepository extends JpaRepository<Users, Long>, JpaSpecificationExecutor<Users> {

    @Query("select u from Users u where u.userCode like '%'||?1||'%' and u.userName like '%'||?2||'%' and u.userNameCn like '%'||?3||'%'")
    List<Users> findByUserCodeLikeAndUserNameLikeAndUserNameCnLike(String userCode,String userName,String userNameCn);
}
