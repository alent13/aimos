package com.applexis.aimos.controller;

import com.applexis.aimos.model.UserMinimalInfo;
import com.applexis.aimos.model.entity.ContactList;
import com.applexis.aimos.model.entity.ShowMode;
import com.applexis.aimos.model.entity.User;
import com.applexis.aimos.model.service.ContactListService;
import com.applexis.aimos.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ContactsController {

    @Autowired
    public UserService userService;

    @Autowired
    public ContactListService contactListService;

    @RequestMapping(value = "/searchContact", method = RequestMethod.POST)
    public @ResponseBody List<UserMinimalInfo> searchContact(@RequestParam String login, Principal principal) {
        List<User> usersLike = userService.getContacts(login);

        List<UserMinimalInfo> users = new ArrayList<>();

        for (User user : usersLike) {
            if(user.getUserSettings().getPageShow().getMode().equals(ShowMode.EVERYONE)
                    && !user.getLogin().equals(principal.getName())){
                UserMinimalInfo info = new UserMinimalInfo();
                info.setLogin(user.getLogin());
                info.setName(user.getName());
                info.setSurname(user.getSurname());
                users.add(info);
            }
            else if(user.getUserSettings().getPageShow().getMode().equals(ShowMode.CONTACTSONLY)) {

            }
        }
        return users;
    }

    @RequestMapping(value = "/addToFriendList/{friendLogin}", method = RequestMethod.GET)
    public void addFriend(@PathVariable String friendLogin, Principal principal) {
        ContactList contactList = new ContactList();
        User user = userService.getByLogin(principal.getName());
        User friend = userService.getByLogin(friendLogin);
        contactList.setUserId(user.getId());
        contactList.setFriend(friend);
        contactListService.save(contactList);
    }

}
