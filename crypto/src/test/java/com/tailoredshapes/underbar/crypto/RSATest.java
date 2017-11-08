package com.tailoredshapes.underbar.crypto;

import org.junit.Test;

import javax.crypto.SecretKey;
import java.io.File;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import static com.tailoredshapes.underbar.IO.file;
import static com.tailoredshapes.underbar.IO.resource;
import static com.tailoredshapes.underbar.IO.slurp;
import static com.tailoredshapes.underbar.crypto.RSA.*;
import static org.junit.Assert.*;

public class RSATest {
    KeyPair bobPair = keypair(512); //for speed

    @Test
    public void endToEnd() throws Exception {
        assertEquals( "Hi Bob!", decrypt(bobPair.getPrivate(), encrypt(bobPair.getPublic(), "Hi Bob!")));
    }

    @Test(expected = RuntimeException.class)
    public void endToEndLargeTextFails() throws Exception {

        String lorem = slurp(new File(String.join(File.separator, "src", "test", "resources", "/lorem.txt")));

        encrypt(bobPair.getPublic(), lorem);
    }

    @Test
    public void isVeryGoodAtEncryptingAESKeys() throws Exception {
        SecretKey secretKey = AES.aesKey();

        assertArrayEquals(secretKey.getEncoded(),
                decrypt(bobPair.getPrivate(), encrypt(bobPair.getPublic(), secretKey.getEncoded())));
    }

    @Test
    public void endToEndWithKeysFromFiles() throws Exception {

        String pubkey = slurp(new File(String.join(File.separator, "src", "test", "resources", "id_rsa.pub")));
        String privKey = slurp(new File(String.join(File.separator, "src", "test", "resources", "id_rsa")));

        PublicKey publicKey = sshPublicKey(pubkey);
        PrivateKey privateKey = sshPrivateKey(privKey);

        assertEquals( "Hi Bob!", decrypt(privateKey, encrypt(publicKey, "Hi Bob!")));
    }
}