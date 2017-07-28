package com.tailoredshapes.underbar.crypto;

import org.junit.Test;

import javax.crypto.SecretKey;

import static com.tailoredshapes.underbar.crypto.Crypto.*;
import static org.junit.Assert.*;

public class CryptoTest {

    @Test
    public void shouldEncryptAndDecryptAMessage() throws Exception {
        SecretKey key = aesKey();

        assertEquals("My message", decrypt(key, encrypt(key, "My message".getBytes())).toString());
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotDecryptWithWrongKey() throws Exception {
        SecretKey key = aesKey();
        SecretKey badKey = aesKey();

        decrypt(badKey, encrypt(key, "My message".getBytes()));
    }

    @Test
    public void shouldEncryptAndDecryptStrings() throws Exception {
        SecretKey key = aesKey();

        Payload payload = encrypt(key, "My message");
        Payload payloadFromString = new Payload(payload.asString(), payload.iv);
        assertEquals("My message", decrypt(key, payloadFromString).toString());
    }

    @Test
    public void shouldEncryptAndDecryptURLSafeStrings() throws Exception {
        SecretKey key = aesKey();
        Payload encrypted = encrypt(key, "My message");
        String urlSafeCipher = encrypted.asString(true);

        assertEquals("My message", decrypt(key, new Payload(urlSafeCipher, encrypted.iv, true)).toString());
    }
}