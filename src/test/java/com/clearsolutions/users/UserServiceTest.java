package com.clearsolutions.users;

import com.clearsolutions.users.advice.exceptions.BadRequestException;
import com.clearsolutions.users.advice.exceptions.UserNotFoundException;
import com.clearsolutions.users.user.model.entity.Address;
import com.clearsolutions.users.user.model.entity.User;
import com.clearsolutions.users.user.model.properties.UserProperties;
import com.clearsolutions.users.user.model.request.UserCreateRequest;
import com.clearsolutions.users.user.model.request.UserUpdateRequest;
import com.clearsolutions.users.user.repository.UserRepository;
import com.clearsolutions.users.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProperties userProperties;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        Address address = Address.builder()
                .country("Ukraine")
                .city("Kyiv")
                .street("Shevchenka")
                .houseNumber(23L)
                .apartmentNumber(22L)
                .build();

        user = User.builder()
                .id(1L)
                .email("johndoe@domain.com")
                .firstName("John")
                .lastName("Doe")
                .birthday(LocalDate.of(1999, 2, 22))
                .address(address)
                .phone("+380765432105")
                .build();
    }

    @Test
    public void testCreateUser() {
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .email("johndoe@domain.com")
                .firstName("John")
                .lastName("Doe")
                .birthday(LocalDate.of(1999, 2, 22))
                .country("Ukraine")
                .city("Kyiv")
                .street("Shevchenka")
                .houseNumber(23L)
                .apartmentNumber(22L)
                .phone("+380765432105")
                .build();

        when(userProperties.getUserEmailRegex()).thenReturn("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        when(userProperties.getUserMinAge()).thenReturn(18);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        String result = userService.createUser(userCreateRequest);

        assertEquals("User was created successfully", result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testCreateUser_WithEmailTaken() {
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .email("johndoe@domain.com")
                .firstName("John")
                .lastName("Doe")
                .birthday(LocalDate.of(1999, 2, 22))
                .country("Ukraine")
                .city("Kyiv")
                .street("Shevchenka")
                .houseNumber(23L)
                .apartmentNumber(22L)
                .phone("+380765432105")
                .build();

        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        when(userProperties.getUserEmailRegex()).thenReturn("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

        assertThrows(BadRequestException.class, () -> userService.createUser(userCreateRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testCreateUser_WithUserCreateRequestIsNull() {
        assertThrows(BadRequestException.class, () -> userService.createUser(null));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testCreateUser_WithInvalidEmail() {
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .email("johndoe")
                .firstName("John")
                .lastName("Doe")
                .birthday(LocalDate.of(1999, 2, 22))
                .country("Ukraine")
                .city("Kyiv")
                .street("Shevchenka")
                .houseNumber(23L)
                .apartmentNumber(22L)
                .phone("+380765432105")
                .build();


        when(userProperties.getUserEmailRegex()).thenReturn("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

        assertThrows(BadRequestException.class, () -> userService.createUser(userCreateRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testCreateUser_firstNameIsNull() {
        UserCreateRequest userCreateRequest1 = UserCreateRequest.builder()
                .email("johndoe@domain.com")
                .firstName(null)
                .lastName("Doe")
                .birthday(LocalDate.now().minusYears(17))
                .country("Ukraine")
                .city("Kyiv")
                .street("Shevchenka")
                .houseNumber(23L)
                .apartmentNumber(22L)
                .phone("+380765432105")
                .build();
        when(userProperties.getUserEmailRegex()).thenReturn("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

        assertThrows(BadRequestException.class, () -> userService.createUser(userCreateRequest1));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testCreateUser_lastNameIsNull() {
        UserCreateRequest userCreateRequest1 = UserCreateRequest.builder()
                .email("johndoe@domain.com")
                .firstName("John")
                .lastName(null)
                .birthday(LocalDate.now().minusYears(17))
                .country("Ukraine")
                .city("Kyiv")
                .street("Shevchenka")
                .houseNumber(23L)
                .apartmentNumber(22L)
                .phone("+380765432105")
                .build();
        when(userProperties.getUserEmailRegex()).thenReturn("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

        assertThrows(BadRequestException.class, () -> userService.createUser(userCreateRequest1));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testCreateUser_birthdayIsNull() {
        UserCreateRequest userCreateRequest1 = UserCreateRequest.builder()
                .email("johndoe@domain.com")
                .firstName("John")
                .lastName("Doe")
                .birthday(null)
                .country("Ukraine")
                .city("Kyiv")
                .street("Shevchenka")
                .houseNumber(23L)
                .apartmentNumber(22L)
                .phone("+380765432105")
                .build();
        when(userProperties.getUserEmailRegex()).thenReturn("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

        assertThrows(BadRequestException.class, () -> userService.createUser(userCreateRequest1));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testCreateUser_IsNotAdult() {
        UserCreateRequest userCreateRequest1 = UserCreateRequest.builder()
                .email("johndoe@domain.com")
                .firstName("John")
                .lastName("Doe")
                .birthday(LocalDate.now().minusYears(17))
                .country("Ukraine")
                .city("Kyiv")
                .street("Shevchenka")
                .houseNumber(23L)
                .apartmentNumber(22L)
                .phone("+380765432105")
                .build();
        when(userProperties.getUserEmailRegex()).thenReturn("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        when(userProperties.getUserMinAge()).thenReturn(18);

        assertThrows(BadRequestException.class, () -> userService.createUser(userCreateRequest1));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testUpdateUser() {
        Long userId = 1L;

        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .email("new-email@example.com")
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .birthday(LocalDate.of(2000, 1, 1))
                .country("USA")
                .city("New York")
                .street("Main St")
                .houseNumber(2L)
                .apartmentNumber(2L)
                .phone("+1234567890")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userProperties.getUserEmailRegex()).thenReturn("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        when(userProperties.getUserMinAge()).thenReturn(18);

        String result = userService.updateUser(userId, userUpdateRequest);

        assertEquals("User was updated successfully", result);
        assertEquals(userUpdateRequest.email(), user.getEmail());
        assertEquals(userUpdateRequest.firstName(), user.getFirstName());
        assertEquals(userUpdateRequest.lastName(), user.getLastName());
        assertEquals(userUpdateRequest.birthday(), user.getBirthday());
        assertEquals(userUpdateRequest.country(), user.getAddress().getCountry());
        assertEquals(userUpdateRequest.city(), user.getAddress().getCity());
        assertEquals(userUpdateRequest.street(), user.getAddress().getStreet());
        assertEquals(userUpdateRequest.houseNumber(), user.getAddress().getHouseNumber());
        assertEquals(userUpdateRequest.apartmentNumber(), user.getAddress().getApartmentNumber());
        assertEquals(userUpdateRequest.phone(), user.getPhone());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdate_UserNotFound() {
        Long userId = 1L;

        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .email("new-email@example.com")
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .birthday(LocalDate.of(2000, 1, 1))
                .country("USA")
                .city("New York")
                .street("Main St")
                .houseNumber(2L)
                .apartmentNumber(2L)
                .phone("+1234567890")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userId, userUpdateRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testUpdateUser_WithUserUpdateRequestIsNull() {
        Long userId = 1L;


        assertThrows(BadRequestException.class, () -> userService.updateUser(userId, null));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testUpdateUser_WithInvalidEmail() {
        Long userId = 1L;

        UserUpdateRequest userCreateRequest = UserUpdateRequest.builder()
                .email("johndoe")
                .firstName("John")
                .lastName("Doe")
                .birthday(LocalDate.of(1999, 2, 22))
                .country("Ukraine")
                .city("Kyiv")
                .street("Shevchenka")
                .houseNumber(23L)
                .apartmentNumber(22L)
                .phone("+380765432105")
                .build();


        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userProperties.getUserEmailRegex()).thenReturn("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

        assertThrows(BadRequestException.class, () -> userService.updateUser(userId, userCreateRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testUpdateUser_firstNameIsNull() {
        Long userId = 1L;

        UserUpdateRequest userCreateRequest = UserUpdateRequest.builder()
                .email("johndoe@domain.com")
                .firstName(null)
                .lastName("Doe")
                .birthday(LocalDate.now().minusYears(17))
                .country("Ukraine")
                .city("Kyiv")
                .street("Shevchenka")
                .houseNumber(23L)
                .apartmentNumber(22L)
                .phone("+380765432105")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userProperties.getUserEmailRegex()).thenReturn("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

        assertThrows(BadRequestException.class, () -> userService.updateUser(userId, userCreateRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testUpdateUser_lastNameIsNull() {
        Long userId = 1L;

        UserUpdateRequest userCreateRequest = UserUpdateRequest.builder()
                .email("johndoe@domain.com")
                .firstName("John")
                .lastName(null)
                .birthday(LocalDate.now().minusYears(17))
                .country("Ukraine")
                .city("Kyiv")
                .street("Shevchenka")
                .houseNumber(23L)
                .apartmentNumber(22L)
                .phone("+380765432105")
                .build();
        when(userProperties.getUserEmailRegex()).thenReturn("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> userService.updateUser(userId, userCreateRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testUpdateUser_birthdayIsNull() {
        Long userId = 1L;

        UserUpdateRequest userCreateRequest = UserUpdateRequest.builder()
                .email("johndoe@domain.com")
                .firstName("John")
                .lastName("Doe")
                .birthday(null)
                .country("Ukraine")
                .city("Kyiv")
                .street("Shevchenka")
                .houseNumber(23L)
                .apartmentNumber(22L)
                .phone("+380765432105")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userProperties.getUserEmailRegex()).thenReturn("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

        assertThrows(BadRequestException.class, () -> userService.updateUser(userId, userCreateRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testUpdateUser_IsNotAdult() {
        Long userId = 1L;

        UserUpdateRequest userCreateRequest = UserUpdateRequest.builder()
                .email("johndoe@domain.com")
                .firstName("John")
                .lastName("Doe")
                .birthday(LocalDate.now().minusYears(17))
                .country("Ukraine")
                .city("Kyiv")
                .street("Shevchenka")
                .houseNumber(23L)
                .apartmentNumber(22L)
                .phone("+380765432105")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userProperties.getUserEmailRegex()).thenReturn("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        when(userProperties.getUserMinAge()).thenReturn(18);

        assertThrows(BadRequestException.class, () -> userService.updateUser(userId, userCreateRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public  void testDeleteUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        String actualResponse = userService.deleteUser(user.getId());

        assertEquals("User was deleted successfully", actualResponse);

        verify(userRepository).findById(user.getId());
        verify(userRepository).deleteById(user.getId());
    }

    @Test
    public  void testDeleteUser_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        String actualResponse = userService.deleteUser(user.getId());

        assertEquals("User was deleted successfully", actualResponse);

        verify(userRepository).findById(user.getId());
        verify(userRepository).deleteById(user.getId());
    }

    @Test
    public void testGetUsersByAgeRange_ValidRange() {
        int from = 19;
        int to = 30;

        List<User> mockUsers = new ArrayList<>();
        mockUsers.add(User.builder().id(1L).birthday(LocalDate.now().minusYears(31)).build());
        mockUsers.add(User.builder().id(2L).birthday(LocalDate.now().minusYears(30)).build());
        mockUsers.add(User.builder().id(3L).birthday(LocalDate.now().minusYears(22)).build());
        mockUsers.add(User.builder().id(4L).birthday(LocalDate.now().minusYears(35)).build());
        mockUsers.add(User.builder().id(5L).birthday(LocalDate.now().minusYears(18)).build());
        mockUsers.add(User.builder().id(6L).birthday(LocalDate.now().minusYears(19)).build());

        when(userRepository.findAll()).thenReturn(mockUsers);

        List<User> usersInAgeRange = userService.getUsersByAgeRange(from, to);

        assertEquals(3, usersInAgeRange.size());
        assertEquals(2L, usersInAgeRange.get(0).getId());
        assertEquals(3L, usersInAgeRange.get(1).getId());
        assertEquals(6L, usersInAgeRange.get(2).getId());
    }

    @Test
    public void testGetUsersByAgeRange_InvalidRange() {
        int from = 30;
        int to = 18;

        assertThrows(BadRequestException.class, () -> userService.getUsersByAgeRange(from, to));
        verify(userRepository, never()).findAll();
    }

}