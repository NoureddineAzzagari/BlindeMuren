package nl.avans.ivh11.BlindWalls.controller;

/**
 * Created by thomasdelhez on 12-03-18.
 */
import nl.avans.ivh11.BlindWalls.domain.mural.Mural;
import nl.avans.ivh11.BlindWalls.domain.user.User;
import nl.avans.ivh11.BlindWalls.model.LoginViewModel;
import nl.avans.ivh11.BlindWalls.repository.UserRepository;
import nl.avans.ivh11.BlindWalls.services.concrete.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;

@Controller
@RequestMapping(value = "/login")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private ArrayList<User> users = new ArrayList<>();
    private User loggedinUser = null;
    private LoginViewModel loggedinUser2 = null;

    //Views Constants
    private final String VIEW_CREATE_USER = "views/login/newUser";
    private final String VIEW_LIST_USERS = "views/login/list";
    private final String VIEW_LOGIN_USER = "views/login/userLogin";
    private final String VIEW_EDIT_USER = "views/login/editUser";

    @Autowired
    private UserRepository userRepository = null;

    // Constructor with Dependency Injection
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    //UserService userService = new UserService(userRepository);


    @GetMapping
    public String listUsers(
            @RequestParam(value="category", required=false, defaultValue="all") String category,
            @RequestParam(value="size", required=false, defaultValue="10") String size,
            Model model) {

        logger.debug("listUsers called.");
        Iterable<User> users = userRepository.findAll();

        model.addAttribute("category", category);
        model.addAttribute("size", size);
        model.addAttribute("users", users);
        return VIEW_LIST_USERS;
    }

    @RequestMapping(value = "/userLogin")
    public String login(Model model) {
        logger.info("login method was called.");

        model.addAttribute("title", "Hier de titel");
        logger.debug("returning views/login/login.");
        return VIEW_LOGIN_USER;
    }

    @RequestMapping(value="/userLogin", method = RequestMethod.POST)
    public ModelAndView validateAndLoginUser(
            @Valid User user,
            final BindingResult bindingResult,
            RedirectAttributes redirect) {

        logger.debug("validateAndLoginUser - login in user " + user.getUsername());
        if (bindingResult.hasErrors()) {
            logger.debug("validateAndLoginUser - not logged in, bindingResult.hasErrors");
            return new ModelAndView(VIEW_LOGIN_USER, "formErrors", bindingResult.getAllErrors());
        }

        UserService userService = new UserService(userRepository);
        loggedinUser = userService.getUser(user.getUsername(), user.getPassword());

        if (loggedinUser != null){
            logger.debug("user ingelogd " + loggedinUser.getFirstname() + " " + loggedinUser.getLastname());
            userService.authenticate(user.getUsername(), user.getPassword());
            logger.debug("user authenticated " + loggedinUser.getFirstname() + " " + loggedinUser.getLastname());

            users = (ArrayList<User>) this.userRepository.findAll();

            return new ModelAndView(VIEW_LIST_USERS, "users", users);}

        else {
            String error = "error";
            return new ModelAndView(VIEW_LOGIN_USER, "error", error);
        }
    }

    @RequestMapping(value="/newUser", method = RequestMethod.GET)
    public String showNewUserForm(final User user, final ModelMap model) {
        logger.debug("showNewUserForm");
        return VIEW_CREATE_USER;
    }

    @RequestMapping(value="/newUser", method = RequestMethod.POST)
    public ModelAndView validateAndSaveUser(
            @Valid User user,
            final BindingResult bindingResult,
            RedirectAttributes redirect) {

        logger.debug("validateAndSaveUser - adding mural " + user.getFirstname());
        if (bindingResult.hasErrors()) {
            logger.debug("validateAndSaveUser - not added, bindingResult.hasErrors");
            return new ModelAndView(VIEW_CREATE_USER, "formErrors", bindingResult.getAllErrors());
        }

        UserService userService = new UserService(userRepository);
        userService.addUser(user);

        users = (ArrayList<User>) this.userRepository.findAll();
        return new ModelAndView(VIEW_LIST_USERS, "users", users);
    }
}