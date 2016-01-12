package com.okeydokey.accounts;

/**
 * @author okeydokey
 */

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class AccountService {
    @Autowired
    private AccountRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    public Account createAccount(AccountDto.Create dto) {
//        Account account = new Account();
//        account.setUsername(dto.getUsername());
//        account.setPassword(dto.getPassword());

//        Account account = new Account();
//        BeanUtils.copyProperties(dto, Account.class);

        Account account = modelMapper.map(dto, Account.class);
        Date now = new Date();
        account.setJoined(now);
        account.setUpdated(now);

        return repository.save(account);

    }
}
