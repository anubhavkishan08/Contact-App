package com.project.ContactManager.controller;

import com.project.ContactManager.dao.UserRepository;
import com.project.ContactManager.entity.User;
import com.project.ContactManager.helper.Message;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    private UserRepository userRepository;
    @Autowired
    public HomeController(UserRepository theUserRepository){
        userRepository=theUserRepository;
    }


    @RequestMapping("/")
    public String home(Model model){
        model.addAttribute("title","Home-Contact Manger");
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("title","Login-Contact Manger");
        return "login";
    }

    @RequestMapping("/about")
    public String about(Model model){
        model.addAttribute("title","About-Contact Manger");
        return "about";
    }

    @RequestMapping("/signup")
    public String signup(Model model){
        model.addAttribute("title","Register-Contact Manger");
        model.addAttribute("user",new User());
        return "signup";
    }

    //handler for registering user
    @PostMapping("/do_register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult,
                               @RequestParam(value = "agreement",defaultValue = "false")
                               boolean agreement, Model model,HttpServletRequest session){

      try {
          if(bindingResult.hasErrors())
          {
              System.out.println(bindingResult.toString());
              model.addAttribute("user",user);
              return "signup";
          }
//          if(!agreement){
//              System.out.println("You have not agreed to Terms & Conditions");
//              throw  new Exception("You have not agreed to Terms & Conditions");
//          }

          user.setRole("Role_Developer");
          user.setEnabled(true);
          user.setUseImage("default.png");
          user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

          System.out.println("User"+user);


          User result=userRepository.save(user);
          model.addAttribute("user",new User());
          session.setAttribute("message",new Message("Sucessfully Registered !!","alert-success"));
          return "signup";

      }catch (Exception e){
          e.printStackTrace();
          model.addAttribute("user",new User());
          session.setAttribute("message",new Message("Something went Wrong!!"+e.getMessage(),"alert-danger"));


          return "signup";
      }

    }
}
