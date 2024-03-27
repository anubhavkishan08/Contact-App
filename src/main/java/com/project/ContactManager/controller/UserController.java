package com.project.ContactManager.controller;

import com.project.ContactManager.dao.ContactRepository;
import com.project.ContactManager.dao.UserRepository;
import com.project.ContactManager.entity.Contact;
import com.project.ContactManager.entity.User;
import com.project.ContactManager.helper.Message;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

//    @ModelAttribute
//    public void addCommondata(Model model,Principal principal){
//        String username = principal.getName();
//
//        System.out.println(username);
//        //get the user by username
//        User user = this.userRepository.getUserByUserName(username);
//
//
//        System.out.println(user);
//        model.addAttribute("user", user);
//    }

    @RequestMapping("/index")
    public String dashboard(Model model, Principal principal) {

        try {
            String username = principal.getName();


           // System.out.println(username);
            //get the user by username
            User user = this.userRepository.getUserByUserName(username);
            if(user==null) return "login";

          //  System.out.println(user);
            model.addAttribute("title","User-Dashboard");
            model.addAttribute("user", user);


            return "normal/user_dashboard";
        }catch (Exception e){
           // e.printStackTrace();
            return "login";
        }
    }

    // add form handler
    @GetMapping("/add_contact")
    public String openAddContactForm(Model model,Principal principal){
        model.addAttribute("title","Add-Contact");
        model.addAttribute("contact",new Contact());
        try {
            String username = principal.getName();

           // System.out.println(username);
            //get the user by username
            User user = this.userRepository.getUserByUserName(username);


            //System.out.println(user);
            model.addAttribute("user", user);

            return "normal/add_contact_form";
        }catch (Exception e){
            // e.printStackTrace();
            return "login";
        }
    }

    //processing contact form
    @PostMapping("/process_contact")
    public String processContact(@ModelAttribute Contact contact,
                                 Principal principal, HttpSession session) {

        try {
            String name = principal.getName();
            User user = this.userRepository.getUserByUserName(name);
            contact.setUser(user);


            user.getContacts().add(contact);

            this.userRepository.save(user);

            System.out.println(contact);
            session.setAttribute("message",new Message("Your contact is added","success"));
            //session.removeAttribute("message");
           // return "normal/add_contact_form";
        }catch (Exception e){
            e.printStackTrace();
           // return "normal/add_contact_form";
            session.setAttribute("message",new Message("Something went Wrong...","danger"));
            //session.removeAttribute("message");
        }
        return "normal/add_contact_form";
    }

    //show contact handler

    //per page=10
    @GetMapping("/show_contacts/{page}")
    public String showContacts(@PathVariable("page") Integer page, Model m,Principal principal){
        try {

            m.addAttribute("title", "Show-Contacts");

            //show contact data from db
            String user_name = principal.getName();
            User user = this.userRepository.getUserByUserName(user_name);

            Pageable pageable =PageRequest.of(page,10);

            Page<Contact> contactList = this.contactRepository.findContactsByUser(user.getUserId(),pageable);

            m.addAttribute("contacts", contactList);
            m.addAttribute("currentPage",page);

            m.addAttribute("totalPages",contactList.getTotalPages());
        }catch (Exception e){
            e.printStackTrace();
        }

        return "normal/show_contacts";
    }

    //showing particular contact
    @RequestMapping("/contact/{cId}")
    public String showContactDetails(@PathVariable("cId") Integer cId,Model model,Principal principal){

       // System.out.println("Cid" +cId);

        Optional<Contact> contactOptional= this.contactRepository.findById(cId);
        Contact contact=contactOptional.get();

        String userName=principal.getName();
        User user=this.userRepository.getUserByUserName(userName);

        if(user.getUserId()==contact.getUser().getUserId()) {
            model.addAttribute("contact", contact);
        }
        else {
            return "normal/error";
        }
        return "normal/contact_detail";
    }

    @GetMapping("/delete/{cId}")
    public String deleteContact(@PathVariable ("cId") Integer cId,Model model,HttpSession session,Principal principal){

        Contact contact=this.contactRepository.findById(cId).get();

        //contact.setUser(null);
        //find current user
        User user=this.userRepository.getUserByUserName(principal.getName());

        user.getContacts().remove(contact);
        this.userRepository.save(user);


      ///  this.contactRepository.delete(contact);

        session.setAttribute("message",new Message("Contact deleted Successfully...","success"));

        return "redirect:/user/show_contacts/0";
       // return "null";
    }

    // open update form handler
    @PostMapping("/update_contact/{cId}")
    public String updateForm(@PathVariable("cId") Integer cId, Model model){

        model.addAttribute("title","Update Contact");

        Contact contact=this.contactRepository.findById(cId).get();

        model.addAttribute("contact",contact);

        return "normal/update_form";
    }

    //updateContact Handler
    @PostMapping("/process_update")
    public String updateHandler(@ModelAttribute Contact contact,Model model,HttpSession session
    ,Principal principal){

        try{
            //finding old contact details
            //Contact oldContactDetail=this.contactRepository.findById(contact.getcId()).get();


            User user=this.userRepository.getUserByUserName(principal.getName());

           // System.out.println("Contact details"+contact.getcId());
            contact.setUser(user);

           // System.out.println("Contact details update"+contact.getcId());

            this.contactRepository.save(contact);

            //System.out.println("Contact id "+contact.getcId());
          //  System.out.println("contact old id"+oldContactDetail.getcId());


            session.setAttribute("message",new Message("Your contact is updated","success"));


        }catch (Exception e){
            e.printStackTrace();
        }


        return "redirect:/user/contact/"+contact.getcId();
    }

    //your Profile
    @GetMapping("/profile")
    public  String yourProfile(Model model,Principal principal){
        model.addAttribute("title","Profile-page");

        String username = principal.getName();

        System.out.println(username);
        //get the user by username
        User user = this.userRepository.getUserByUserName(username);
        model.addAttribute("user",user);

        return "normal/profile";
    }

}
