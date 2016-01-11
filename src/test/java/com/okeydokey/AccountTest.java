package com.okeydokey;

import com.okeydokey.account.model.Account;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by yunki on 2016-01-11.
 */

public class AccountTest {
    @Test
    public void getterSetter(){
        Account account = new Account();
        account.setLoginId("okeydokey");
        account.setPassword("1228");
        assertThat(account.getLoginId(), is("okeydokey"));

    }
}
