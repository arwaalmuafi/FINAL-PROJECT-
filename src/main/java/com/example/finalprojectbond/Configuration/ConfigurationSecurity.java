package com.example.finalprojectbond.Configuration;

import com.example.finalprojectbond.Service.MyUserDetailsService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
//@Data
public class ConfigurationSecurity {


    private final MyUserDetailsService myUserDetailsService;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(myUserDetailsService);
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)

                .and()

                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests()

                .requestMatchers("/api/v1/organizer/register-organizer",
                        "/api/v1/organizer/get-organizer-by-rating-desc",
                        "/api/v1/organizer/get-organizer-by-rating-asc",
                        "/api/v1/organizer/get-organizer-by-city/",
                        "/api/v1/explorer/register-explorer",
                        "/api/v1/tag/get-all-tags"
                ).permitAll()

                .requestMatchers("/api/v1/task/get-all",
                        "/api/v1/user/approve-organizer/",
                        "/api/v1/experience/get-all-experience",
                        "/api/v1/explorer/get-all-explorer",
                        "/api/v1/experience-photo/get-experience-photos/"
                ).hasAuthority("ADMIN")

                .requestMatchers(

                        "/api/v1/organizer/**",

                        "/api/v1/task/create-task/*",
                        "/api/v1/task/update-task/*",
                        "/api/v1/task/delete-task/*",
                        "/api/v1/task/view-task-progress/*",
                        "/api/v1/task/get-task-by-experience/*",

                        "/api/v1/review-explorer/create-review-explorer/*",
                        "/api/v1/review-explorer/update-review-explorer/*",
                        "/api/v1/review-explorer/delete-review-explorer/*",
                        "/api/v1/review-explorer/get-all-review-by-organizer/*",

                        "/api/v1/review-experience/get-explorer-reviews-filtered-by-high-to-low/",
                        "/api/v1/review-experience/get-explorer-reviews-filtered-by-low-to-high/",
                        "/api/v1/review-experience/get-experience-reviews-filtered-by-date/",
                        "/api/v1/review-experience/all-explorer-reviews-for-Experiences/*",
                        "/api/v1/review-experience/get-all-review-experience/*",

                        "/api/v1/tag/assign-tags-to-experience",

                        "/api/v1/application/accept-application/*",
                        "/api/v1/application/reject-application/*",
                        "/api/v1/application/get-applications-by-experience/*",
                        "/api/v1/application/get-completed-applications/*",
                        "/api/v1/application/get-pending-applications/*",

                        "/api/v1/meeting-zone/get-my-meeting-zone/*",
                        "/api/v1/meeting-zone/create-meeting-zone/*",
                        "/api/v1/meeting-zone/update-meeting-zone/*",
                        "/api/v1/meeting-zone/delete-meeting-zone/*",

                        "/api/v1/notification/notification-one-explorer/*",
                        "/api/v1/notification/update-notification/*",
                        "/api/v1/notification/delete-notification/*",
                        "/api/v1/notification/notify-all-explorer-to-confirm-meeting-zone/",


                        "/api/v1/experience/create-experience",
                        "/api/v1/experience/update/*",
                        "/api/v1/experience/delete/*",
                        "/api/v1/experience/fully-booked/*",
                        "/api/v1/experience/remove-explorer/*",
                        "/api/v1/experience/get-non-confirmed/*",
                        "/api/v1/experience/get-accepted/*",
                        "/api/v1/experience/remove-non-confirmed/*",
                        "/api/v1/experience/complete/*",
                        "/api/v1/experience/cancel/*",
                        "/api/v1/experience/change-status-to-in-progress/*",

                        "/api/v1/experience-photo/add-photo",
                        "/api/v1/experience-photo/delete-photo/*",
                        "/api/v1/experience-photo/get-experience-photos/*"

                ).hasAuthority("ORGANIZER")


                .requestMatchers(
                        "/api/v1/task/get-task-by-explorer",
                        "/api/v1/task/get-in-complete-tasks",
                        "/api/v1/task/mark-completed/*",


                        "/api/v1/review-explorer/get-all-reviews-by-explorer",
                        "/api/v1/review-explorer/get-explorer-reviews-asc",
                        "/api/v1/review-explorer/get-explorer-reviews-Desc",


                        "/api/v1/review-experience/create-review-experience/*",
                        "/api/v1/review-experience/update-review-experience/*",
                        "/api/v1/review-experience/delete-review-experience/*",

                        "/api/v1/application/get-my-applications",
                        "/api/v1/application/create-application/*",
                        "/api/v1/application/cancel-application/*",
                        "/api/v1/application/update-application/*",
                        "/api/v1/application/search-applications-by-experience-title/*",


                        "/api/v1/meeting-zone/get-meeting-zone-for-explorer-experience/*",

                        "/api/v1/notification/get-my-notifications",

                        "/api/v1/explorer/update-explorer/*",
                        "/api/v1/explorer/delete-explorer/*",
                        "/api/v1/explorer/confirm-meeting-zone/*",
                        "/api/v1/explorer/get-my-experiences",


                        "/api/v1/experience-photo/get-experience-photos"

                ).hasAuthority("EXPLORER")


                .anyRequest().authenticated()

                .and()

                .logout().logoutUrl("/api/v1/logout").permitAll()
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)

                .and()

                .httpBasic();
        return http.build();

    }


}
