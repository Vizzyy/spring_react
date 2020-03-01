package vizzyy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vizzyy.domain.User;
import vizzyy.domain.UserRepository;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User createUser(String CN, String role){
        int size = getUsers().size() + 1;
        User newUser = new User((long) size, CN, role);
        return userRepository.save(newUser);
    }

    public User deleteUser(){
        return null;
    }

    public User getUser(){
        return null;
    }

}
