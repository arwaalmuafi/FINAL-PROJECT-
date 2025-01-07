package com.example.finalprojectbond.Model;

import com.example.finalprojectbond.Service.MyUserDetailsService;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MyUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


@Column(unique = true, nullable = false)
    private String username;


    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "varchar(50) not null")
    private String name;


    @Column(columnDefinition = "int not null")
    private Integer age;


    @Column(columnDefinition = "varchar(30) not null")
    private String city;

//    @Size(max = 70,message = "The length of the health status must be at most 70 characters")
//    @NotEmpty(message = "health status cannot be null")
    @Column(columnDefinition = "varchar(70) not null")
    private String healthStatus;
//
//    @Size(max = 40,message = "The length of the city must be at most 40 characters")
//    @NotEmpty(message = "City cannot be null")
//    @Email(message = "Invalid email format")
    @Column(columnDefinition = "varchar(40) not null unique")
    private String email;

//    @NotEmpty(message = "Gender cannot be empty")
//    @Pattern(regexp = "^(Male|Female)$", message = "Gender must be either Male or Female")
    @Column(columnDefinition = "varchar(6) not null")
    private String gender;

//    @Size(max = 15, message = "Role must be at most 15 characters")
    @Column(nullable = false)
    private String role;

//    @NotEmpty(message = "Phone number cannot be null")
//    @Pattern(regexp = "^05[0-9]{8}$", message = "Phone number must start with 05 and be 10 digits")
    @Column(columnDefinition = "varchar(10) not null unique")
    private String phoneNumber;


    @Column()
    private String photoURL;

    @Column(columnDefinition = "double")
    private double rating;



    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Explorer explorer;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Organizer organizer;


    public MyUser(@Size(max = 25,message = "The length of the user name must be at most 25 characters") @NotEmpty(message = "User Name cannot be null") String username, @Size(max = 25,message = "The length of the password must be at most 25 characters") @NotEmpty(message = "Password cannot be null") @Pattern(regexp = "^(?=.?[A-Z])(?=.?[a-z])(?=.?[0-9])(?=.?[#?!@$%^&*-]).{8,}$",message = "The password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character (e.g., #?!@$%^&*-)") String password, @Size(max = 30,message = "The length of the user name must be at most 30 characters") @NotEmpty(message = "Name cannot be null") String name, @Positive(message = "Age cannot be null ") @Min(value = 18, message = "Age must be at least 18 ") @Max(value = 100, message = "Age cannot be more than 100 ") Integer age, @Size(max = 30,message = "The length of the city must be at most 30 characters") @NotEmpty(message = "City cannot be null") String city, @Size(max = 70,message = "The length of the health status must be at most 70 characters") @NotEmpty(message = "health status cannot be null") String healthStatus, @Size(max = 40,message = "The length of the city must be at most 40 characters") @NotEmpty(message = "City cannot be null") @Email(message = "Invalid email format") String email, @NotEmpty(message = "Gender cannot be empty") @Pattern(regexp = "^(Male|Female)$", message = "Gender must be either Male or Female") String gender, String role, @NotEmpty(message = "Phone number cannot be null") @Pattern(regexp = "^05[0-9]{8}$", message = "Phone number must start with 05 and be 10 digits") String phoneNumber, String photoURL) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.age = age;
        this.city = city;
        this.healthStatus = healthStatus;
        this.role = role;
        this.email = email;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.photoURL = photoURL;
    }

    public MyUser(String username1, String user123) {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return Collections.singleton(new SimpleGrantedAuthority(this.role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
