package br.com.rodrigodev.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;
    
    @PostMapping(value = "/")
    public ResponseEntity create(@RequestBody UserModel userModel) {
        var user = this.userRepository.findByUsername(userModel.getUsername());
        
        if(user != null){
            // Mensagem e status code
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario já existe");
        }

        var passwordHashered = BCrypt.withDefaults()
            .hashToString(12, userModel.getPassword().toCharArray());


        userModel.setPassword(passwordHashered);

        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}
