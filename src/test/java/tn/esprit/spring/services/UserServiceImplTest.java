package tn.esprit.spring.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tn.esprit.spring.entities.Role;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.repository.UserRepository;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private AutoCloseable closeable;

    private User mockUser;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        mockUser = new User(1L, "John", "Doe", new Date(), Role.USER);
    }

    @Test
    void testAddUser() {
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User savedUser = userService.addUser(mockUser);

        assertNotNull(savedUser);
        assertEquals("John", savedUser.getFirstName());
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void testRetrieveAllUsers() {
        List<User> userList = Arrays.asList(
                new User(1L, "John", "Doe", new Date(), Role.USER),
                new User(2L, "Jane", "Smith", new Date(), Role.ADMIN)
        );

        when(userRepository.findAll()).thenReturn(userList);

        List<User> result = userService.retrieveAllUsers();
        assertEquals(2, result.size());
    }

    @Test
    void testUpdateUser() {
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        User updated = userService.updateUser(mockUser);
        assertNotNull(updated);
        assertEquals("John", updated.getFirstName());
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        assertDoesNotThrow(() -> userService.deleteUser("1"));
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testRetrieveUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        User found = userService.retrieveUser("1");
        assertNotNull(found);
        assertEquals("Doe", found.getLastName());
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
