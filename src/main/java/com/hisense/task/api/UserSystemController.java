package com.hisense.task.api;

import com.hisense.task.dto.UsersSearchDTO;
import com.hisense.task.domain.Users;
import com.hisense.task.repository.UsersRepository;
import com.hisense.task.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/")
public class UserSystemController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UsersService usersService;

    private static final String USERS_LIST = "usersList";
    private static final String USERS_ADD = "usersAdd";
    private static final String USERS_EDIT = "usersEdit";

    /**
     * GET users
     */
    @RequestMapping("users")
    public String getUsers(Model model, @ModelAttribute UsersSearchDTO usersSearchDTO) {
        List<Users> data = usersService.findUsers(usersSearchDTO);
        model.addAttribute("users", data);
        model.addAttribute("usersSearchDTO", usersSearchDTO);
        return USERS_LIST;
    }

    @RequestMapping(value = "users/add")
    public String addUsers() {
        return USERS_ADD;
    }

    @RequestMapping(value = "users/edit")
    public String editUsers(Model model, @RequestParam("userId") Long userId) {
        Users users=usersService.findById(userId);
        model.addAttribute("users",users);
        return USERS_EDIT;
    }

    @RequestMapping(value = "users/create")
    public String createUsers(Users users) {
        usersService.updateUsers(users);
        return "redirect:/users";
    }

    @RequestMapping(value = "users/update")
    public String updateUsers(Users users) {
        usersService.updateUsers(users);
        return "redirect:/users";
    }

    @RequestMapping(value = "users/delete")
    public String deleteUsers(@RequestParam("userId") Long userId) {
            usersService.deleteUsers(userId);
        return "redirect:/users";
    }

}
