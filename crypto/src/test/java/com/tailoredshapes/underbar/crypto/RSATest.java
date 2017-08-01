package com.tailoredshapes.underbar.crypto;

import org.junit.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import static com.tailoredshapes.underbar.IO.file;
import static com.tailoredshapes.underbar.IO.resource;
import static com.tailoredshapes.underbar.IO.slurp;
import static com.tailoredshapes.underbar.crypto.RSA.*;
import static org.junit.Assert.*;

public class RSATest {
    @Test
    public void endToEnd() throws Exception {
        KeyPair bobPair = keypair();

        assertEquals( "Hi Bob!", decrypt(bobPair.getPrivate(), encrypt(bobPair.getPublic(), "Hi Bob!")));
    }

    @Test
    public void endToEndWithKeysFromFiles() throws Exception {
        String pubkey = slurp(file(resource("/id_rsa.pub")));
        String privKey = slurp(file(resource("/id_rsa")));

        PublicKey publicKey = sshPublicKey(pubkey);
        PrivateKey privateKey = sshPrivateKey(privKey);

        assertEquals( "Hi Bob!", decrypt(privateKey, encrypt(publicKey, "Hi Bob!")));
    }
}