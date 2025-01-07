package com.example.finalprojectbond;

import com.example.finalprojectbond.InDTO.ExplorerInDTO;
import com.example.finalprojectbond.Model.Explorer;
import com.example.finalprojectbond.Model.MyUser;
import com.example.finalprojectbond.OutDTO.ExplorerOutDTO;
import com.example.finalprojectbond.Repository.AuthRepository;
import com.example.finalprojectbond.Repository.ExplorerRepository;
import com.example.finalprojectbond.Service.ExplorerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ExplorerServiceTest {

    @Mock
    private final AuthRepository authRepository = mock(AuthRepository.class);
    @Mock
    private final ExplorerRepository explorerRepository = mock(ExplorerRepository.class);
    @Mock
    private final ExplorerService explorerService = new ExplorerService(authRepository, explorerRepository, null, null, null);

    @Test
    void testGetAllExplorers() {
        // Arrange
        Integer userId = 1;
        MyUser myUser1 = new MyUser("username1", "user123");
        MyUser myUser2 = new MyUser("username2", "user123");

        Explorer explorer1 = new Explorer();
        explorer1.setMyUser(myUser1);
        Explorer explorer2 = new Explorer();
        explorer2.setMyUser(myUser2);

        when(explorerRepository.findAll()).thenReturn(List.of(explorer1, explorer2));

        // Act
        List<ExplorerOutDTO> result = explorerService.getAllExplorers(userId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testRegisterExplorer() {
        // Arrange
        ExplorerInDTO explorerInDTO = new ExplorerInDTO();
        explorerInDTO.setUsername("username1");
        explorerInDTO.setPassword("password123");
        explorerInDTO.setName("Explorer One");
        explorerInDTO.setAge(25);
        explorerInDTO.setCity("New York");
        explorerInDTO.setHealthStatus("Healthy");
        explorerInDTO.setEmail("explorer1@example.com");
        explorerInDTO.setGender("Male");
        explorerInDTO.setPhoneNumber("1234567890");
        explorerInDTO.setPhotoURL("url/to/photo");

        // Act
        explorerService.registerExplorer(explorerInDTO);

        // Assert
        verify(authRepository, times(1)).save(any(MyUser.class));  // Verify if the user is saved
        verify(explorerRepository, times(1)).save(any(Explorer.class));  // Verify if the explorer is saved
    }

    @Test
    void testDeleteExplorer() {
        // Arrange
        Integer myUserId = 1;
        Explorer explorer = new Explorer();
        MyUser myUser = new MyUser();
        myUser.setId(myUserId);
        explorer.setMyUser(myUser);

        when(explorerRepository.findExplorerById(myUserId)).thenReturn(explorer);
        when(authRepository.findMyUserById(myUserId)).thenReturn(myUser);

        // Act
        explorerService.deleteExplorer(myUserId);

        // Assert
        verify(explorerRepository, times(1)).delete(explorer);  // Ensure explorer is deleted
        verify(authRepository, times(1)).delete(myUser);  // Ensure user is deleted
    }

}


