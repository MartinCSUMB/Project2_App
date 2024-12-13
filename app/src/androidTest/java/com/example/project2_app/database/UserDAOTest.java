package com.example.project2_app.database;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.project2_app.database.entities.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UserDAOTest {

    private InventoryManagementDatabase database;
    private UserDAO userDAO;

    @Before
    public void setUp() {
        database = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                InventoryManagementDatabase.class
        ).allowMainThreadQueries().build();

        userDAO = database.userDAO();
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void testInsertUser() {
        User user = new User("testUser", "password123");
        userDAO.insert(user);

        List<User> users = userDAO.getAllUsers();
        assertEquals(1, users.size());
        assertEquals("testUser", users.get(0).getUsername());
    }

    @Test
    public void testUpdateUser() {
        User user = new User("testUser", "password123");
        userDAO.insert(user);

        List<User> users = userDAO.getAllUsers();
        User insertedUser = users.get(0);
        insertedUser.setPassword("newPassword123");
        userDAO.update(insertedUser);

        List<User> updatedUsers = userDAO.getAllUsers();
        assertEquals("newPassword123", updatedUsers.get(0).getPassword());
    }

    @Test
    public void testDeleteUser() {
        User user = new User("testUser", "password123");
        userDAO.insert(user);

        List<User> users = userDAO.getAllUsers();
        assertEquals(1, users.size());

        userDAO.delete(users.get(0));
        List<User> remainingUsers = userDAO.getAllUsers();
        assertTrue(remainingUsers.isEmpty());
    }
}
