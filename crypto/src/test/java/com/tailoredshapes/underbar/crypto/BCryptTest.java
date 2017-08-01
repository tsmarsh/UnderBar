package com.tailoredshapes.underbar.crypto;

import org.junit.Test;

import static com.tailoredshapes.underbar.crypto.BCrypt.checkPassword;
import static com.tailoredshapes.underbar.crypto.BCrypt.genSalt;
import static com.tailoredshapes.underbar.crypto.BCrypt.hashPassword;
import static org.junit.Assert.*;

public class BCryptTest {

    @Test
    public void shouldEncryptAPassword() throws Exception {
        String hash = hashPassword("s3cur3P@assW0rd!", genSalt());

        assertTrue(checkPassword("s3cur3P@assW0rd!", hash));
    }
}