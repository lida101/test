package com.hisense.task.service;

import com.hisense.task.dto.UsersSearchDTO;
import com.hisense.task.domain.Users;
import com.hisense.task.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {

    private final Logger log = LoggerFactory.getLogger(UsersService.class);

    @Autowired
    private UsersRepository usersRepository;

    public List<Users> findUsers(UsersSearchDTO usersSearchDTO) {
        List<Users> users=
        usersRepository.findByUserCodeLikeAndUserNameLikeAndUserNameCnLike(usersSearchDTO.getUserCode(),usersSearchDTO.getUserName(),usersSearchDTO.getUserNameCn());
        return  users;

    }

    public Users findById(Long id){
        Users user=usersRepository.findOne(id);
        return user;
    }

    public void updateUsers(Users users){
        usersRepository.save(users);
    }

    public void deleteUsers(Long id){
        usersRepository.delete(id);
    }


}
