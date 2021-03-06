package com.okeydokey.accounts;

import com.okeydokey.commons.config.ErrorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author okeydokey
 */
@RestController
public class AccountController {
    @Autowired
    private AccountService service;

    @Autowired
    private AccountRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    // TODO HATEOAS study. example twitch ?..
    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    public ResponseEntity createAccount(@RequestBody @Valid AccountDto.Create account,
                                        BindingResult result){
        if(result.hasErrors()){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("잘못된 요청입니다.");
            errorResponse.setCode("bad.request");
            // TODO BindingResult 의 에러 정보 사용
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }

        Account newAccount = service.createAccount(account);

        return new ResponseEntity(modelMapper.map(newAccount, AccountDto.Response.class), HttpStatus.CREATED);
    }

    @ExceptionHandler(UserDuplicatedException.class)
    public ResponseEntity handleUserDuplicateException(UserDuplicatedException e){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("["+e.getUsername()+"] 중복된 username 입니다.");
        errorResponse.setCode("duplicated.username.exception");
        return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/accounts", method =RequestMethod.GET)
    public ResponseEntity  getAccounts(Pageable pageable){
        Page<Account> page = repository.findAll(pageable);
        List<AccountDto.Response> content = page.getContent().stream().map(account -> modelMapper.map(account, AccountDto.Response.class))
        .collect(Collectors.toList());

        PageImpl<AccountDto.Response> result = new PageImpl<AccountDto.Response>(content, pageable, page.getTotalElements());

        return new ResponseEntity(result, HttpStatus.OK);
    }
}
