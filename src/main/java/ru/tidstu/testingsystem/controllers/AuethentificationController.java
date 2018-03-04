package ru.tidstu.testingsystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ru.tidstu.testingsystem.authentication.Auethentification;
import ru.tidstu.testingsystem.authentication.RoleUser;
import ru.tidstu.testingsystem.controllers.admin_room.AdminRoomController;
import ru.tidstu.testingsystem.data.service.GroupsService;
import ru.tidstu.testingsystem.olympiad.Olympiad;

@Controller
@RequestMapping("/auethentification")
public class AuethentificationController {

    @Autowired
    private Auethentification auethentification;
    @Autowired
    private GroupsService groupsService;
    @Autowired
    private Olympiad olympiad;

    @RequestMapping(value = "/showPage", method = RequestMethod.GET)
    public ModelAndView showPage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("groups", groupsService.getGroups());
        modelAndView.setViewName("auethentification");
        return modelAndView;
    }

    @RequestMapping(value = "/logIn", method = RequestMethod.POST)
    public @ResponseBody String logIn(@RequestParam(value = "login_sign_in") String login, @RequestParam(value = "pass_sign_in") String password){
        // Redirect processes in JS(auethentification-page.js)
        RoleUser roleUser = auethentification.authenticate(login, password);
        switch (roleUser) {
            case ADMIN:
                return "admin_room_page";
            case USER:
                olympiad.startOlympiad(login, password);
                return "tasks_page";
            case UNKNOWN:
                return "Не правильные данные";
        }
        return "Не правильные данные";
    }

    @RequestMapping(value = "/SignUp", method = RequestMethod.POST)
    public @ResponseBody String signUp(@RequestParam(value = "login_sign_up") String login,
                         @RequestParam(value = "pass_sign_up") String password,
                         @RequestParam(value = "name_sign_up") String name,
                         @RequestParam(value = "group_sign_up") String group){
        // Redirect processes in JS(auethentification-page.js)
        if(auethentification.register(login, password, name, group)){
            olympiad.startOlympiad(login, password);
            return "tasks_page";
        } else {
            return "Пользователь с таким логином уже зарегистрирован";
        }
    }

}
