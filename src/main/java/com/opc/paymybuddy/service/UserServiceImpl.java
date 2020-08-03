package com.opc.paymybuddy.service;

import com.opc.paymybuddy.dao.BankAccountDao;
import com.opc.paymybuddy.dao.UserDao;
import com.opc.paymybuddy.dto.UserDto;
import com.opc.paymybuddy.model.User;
import com.opc.paymybuddy.web.exceptions.DataMissingException;
import com.opc.paymybuddy.web.exceptions.DataAlreadyExistException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;
    @Autowired
    BankAccountDao bankAccountDao;
    @Autowired
    BankAccountService bankAccountService;

    // Encrypt password
    static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    // Pour le log4j2
    static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public long count() {
        return userDao.count();
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public boolean addUser(UserDto addUser) throws Exception {

        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        User user = new User();

       if (addUser.getEmail().isEmpty()) {
            logger.error("inscription : KO");
            throw new DataMissingException("Inscription failed : email is required !!");
        }
        if (addUser.getFirstName().isEmpty()) {
            logger.error("inscription : KO");
            throw new DataMissingException("Inscription failed : firstname is required !!");
        }
        if (addUser.getLastName().isEmpty()) {
            logger.error("inscription : KO");
            throw new DataMissingException("Inscription failed : lastname is required !!");
        }
        if (addUser.getPassword().isEmpty()) {
            logger.error("inscription : KO");
            throw new DataMissingException("Inscription failed : password is required !!");
        }

        if (userDao.existsByEmail(addUser.getEmail())) {

            String mess= String.format("Inscription failed : this mail %s is already exist !!", addUser.getEmail());

            logger.info(mess);

            throw new DataAlreadyExistException(mess);   // Ano : n'affiche pas le mess dans Postman

        }
        user.setEmail(addUser.getEmail());
        user.setFirstname(addUser.getFirstName());
        user.setLastname(addUser.getLastName());
        user.setPassword(encoder.encode(addUser.getPassword()));
        user.setBalance(BigDecimal.ZERO);

        user.setCreateDate(date);
        user.setId(0);

        userDao.save(user);
        logger.info("Add user OK " + addUser.toString());
        return true;
    }


/* public User addBuddy(User newBuddy) {
        return null;
    }
*/
}
