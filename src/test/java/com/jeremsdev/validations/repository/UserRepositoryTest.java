package com.jeremsdev.validations.repository;

import com.jeremsdev.validations.service.impl.UserServiceImpl;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false) // Disabled Filter
public class UserRepositoryTest {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private UserRepository userRepository;
}
