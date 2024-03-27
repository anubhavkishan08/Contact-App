package com.project.ContactManager.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Contact_details")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cId;

    private String cName;

    private String cNickName;

    private String work;

    private String cEmail;
   // private String cImage;

   // private byte[] cImage;

    private String phone;

    @Column(length = 5000)
    private String cAbout;

    @ManyToOne
    private User user;
    public Contact() {
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcNickName() {
        return cNickName;
    }

    public void setcNickName(String cNickName) {
        this.cNickName = cNickName;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getcEmail() {
        return cEmail;
    }

    public void setcEmail(String cEmail) {
        this.cEmail = cEmail;
    }
//
//    public String getcImage() {
//        return cImage;
//    }
//
//    public void setcImage(String cImage) {
//        this.cImage = cImage;
//    }


//    public byte[] getcImage() {
//        return cImage;
//    }
//
//    public void setcImage(byte[] cImage) {
//        this.cImage = cImage;
//    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getcAbout() {
        return cAbout;
    }

    public void setcAbout(String cAbout) {
        this.cAbout = cAbout;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj){
        return this.cId==((Contact)obj).getcId();
    }

//    @Override
//    public String toString() {
//        return "Contact{" +
//                "cId=" + cId +
//                ", cName='" + cName + '\'' +
//                ", cNickName='" + cNickName + '\'' +
//                ", work='" + work + '\'' +
//                ", cEmail='" + cEmail + '\'' +
//                ", phone='" + phone + '\'' +
//                ", cAbout='" + cAbout + '\'' +
//                ", user=" + user +
//                '}';
//    }
}
