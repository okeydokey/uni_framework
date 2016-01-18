package com.okeydokey.accounts;

/**
 * @author okeydokey
 */

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@Slf4j
public class AccountService {
    @Autowired
    private AccountRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    public Account createAccount(AccountDto.Create dto) {
//        Account account = new Account();
//        BeanUtils.copyProperties(dto, Account.class);

        Account account = modelMapper.map(dto, Account.class);

        String username = dto.getUsername();
        if(repository.findByUsername(username)  != null ){
            log.error("user duplicated exception. {}", username);
            throw new UserDuplicatedException(username);
        }

        Date now = new Date();
        account.setJoined(now);
        account.setUpdated(now);

        return repository.save(account);

    }
}
