package com.hou.mail.controller;

import com.hou.mail.bean.User;
import com.hou.mail.response.RelationRes;
import com.hou.mail.response.UserRes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
@Controller
public class HFriendsController {
    private static ArrayList<User> friendInfos, searchResult;
    @RequestMapping("/friends.html")
    public String MainToFriends(HttpSession httpSession){
        User user = (User)httpSession.getAttribute("user");
        friendInfos = UserRes.getFriendsByUserID(user.getId());
        httpSession.setAttribute("friends", friendInfos);
        return "friends";
    }

    @PostMapping("/sendEmail-friends")
    public String FriendsToSend( @RequestParam("friend-address") String index,
                                 HttpSession httpSession){
        System.out.println("INDEX IS " + index);
        String addr = friendInfos.get(Integer.parseInt(index)).getEmail();
        httpSession.setAttribute("toAdd", addr);
        return "sendEmail";
    }

    @PostMapping("/sendEmail-search")
    public String SearchToSend( @RequestParam("friend-address") String index,
                                 HttpSession httpSession){
        System.out.println("INDEX IS " + index);
        String addr = searchResult.get(Integer.parseInt(index)).getEmail();
        httpSession.setAttribute("toAdd", addr);
        return "sendEmail";
    }

    @PostMapping("/friends-delete")
    @ResponseBody
    public String onDeleteCall(@RequestParam("index") String index, HttpSession httpSession){
        System.out.println("AT DELETE index " + index);
        int ind = Integer.parseInt(index);
        User user = (User)httpSession.getAttribute("user");
        int toID = friendInfos.get(ind).getId();
        int res = RelationRes.deleteFriendByID(user.getId(), toID);
        friendInfos.remove(ind);
        httpSession.setAttribute("friends", friendInfos);
        if(res != 0){
            System.out.println("ERR AT DEL RELATION ");
            return "ERR";
        }else {
            return "SUCCESS";
        }
    }

    @PostMapping("/search-friends")
    public String onSearchCall(@RequestParam("address") String address
            , HttpSession httpSession){
        System.out.println("SEA add!");
        User user = (User)httpSession.getAttribute("user");

        searchResult = UserRes.findMailAddQZ(address);
//        if(searchResult.size() > 10){// limit size
//            while (searchResult.size() >10 ){
//                searchResult.remove(searchResult.size()  -1);
//            }
//        }
        for (int t = 0; t < searchResult.size(); t++){
            if(searchResult.get(t).getId() == user.getId()){
                searchResult.remove(t);
            }
        }
        httpSession.setAttribute("search", searchResult);

        return "searchFriends";
    }

    @PostMapping("/search-friends-deep")
    public String onDeepSearchCall(@RequestParam("address") String address
            , HttpSession httpSession){
        System.out.println("SEA add deep!");
        User user = (User)httpSession.getAttribute("user");

        searchResult = UserRes.findMailAddDeep(address);
//        searchResult = UserRes.findMailAddQZ(address);
//        if(searchResult.size() > 10){// limit size
//            while (searchResult.size() >10 ){
//                searchResult.remove(searchResult.size()  -1);
//            }
//        }
        for (int t = 0; t < searchResult.size(); t++){
            if(searchResult.get(t).getId() == user.getId()){
                searchResult.remove(t);
            }
        }
        httpSession.setAttribute("search", searchResult);

        return "searchFriends";
    }

    @PostMapping("/friends-add")
    public String onAddFriendsCall(@RequestParam("index") String index
            , HttpSession httpSession){
        System.out.println(" add! fri");
        User user = (User)httpSession.getAttribute("user");
        int res = RelationRes.addNewRelation(user.getId(), searchResult.get(Integer.parseInt(index)).getId());
        System.out.println("Res in add friends: " + res);
        return res == 0 ? "friends" : "ERR " + res;
    }
}
