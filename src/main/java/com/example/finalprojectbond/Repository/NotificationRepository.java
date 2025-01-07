package com.example.finalprojectbond.Repository;

import com.example.finalprojectbond.Model.Experience;
import com.example.finalprojectbond.Model.Explorer;
import com.example.finalprojectbond.Model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    Notification findNotificationById(Integer id);
    List<Notification> findAllByExplorer(Explorer explorer);

    Notification findNotificationByExplorerAndExperience(Explorer explorer, Experience experience);

}
