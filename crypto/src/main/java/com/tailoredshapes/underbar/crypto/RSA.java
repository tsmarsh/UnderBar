package com.tailoredshapes.underbar.crypto;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

import static com.tailoredshapes.underbar.Die.rethrow;
import static com.tailoredshapes.underbar.UnderBar.filter;
import static com.tailoredshapes.underbar.UnderBar.list;
import static com.tailoredshapes.underbar.UnderBar.map;

public interface RSA {
    static KeyPair keypair(){
        return keypair(4096);
    }

    static KeyPair keypair(int keysize){
        KeyPairGenerator generator = rethrow(()->KeyPairGenerator.getInstance("RSA"));
        generator.initialize(keysize);
        return generator.generateKeyPair();
    }

    static PrivateKey privateKey(String key){
        KeyFactory rsa = rethrow(() -> KeyFactory.getInstance("RSA"));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(key.getBytes());
        return rethrow(() -> rsa.generatePrivate(spec));
    }

    static PublicKey publicKey(String key){
        KeyFactory rsa = rethrow(() -> KeyFactory.getInstance("RSA"));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(key.getBytes());
        return rethrow(() -> rsa.generatePublic(spec));
    }

    static BigInteger readBigInteger(DataInputStream in) {
        assert rethrow(() -> in.read() == 2);

        int length = rethrow(() -> in.read());

        if (length >= 0x80) {
            byte[] extended = new byte[4];
            int bytesToRead = length & 0x7f;
            rethrow(() -> in.readFully(extended, 4 - bytesToRead, bytesToRead));
            length = new BigInteger(extended).intValue();
        }
        byte[] data = new byte[length];
        rethrow(() -> in.readFully(data));
        return new BigInteger(data);
    }

    static PrivateKey sshPrivateKey(String key){
        StringBuilder bob = new StringBuilder();
        filter(list(key.split("\n")), (line) -> !(line.contains("-") || line.contains(":"))).forEach(
                (line) -> {
                    bob.append(line);
                    bob.append("\n");
        });

        byte[] bytes = Base64.getDecoder().decode(bob.toString());

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));

        assert rethrow(() -> in.read()) == 48;
        assert rethrow(() -> in.read()) == 130;
        rethrow(() -> in.skipBytes(5));

        BigInteger modulo = readBigInteger(in);
        readBigInteger(in);
        BigInteger exponent = readBigInteger(in);

        RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(modulo, exponent);
        return rethrow(() -> KeyFactory.getInstance("RSA").generatePrivate(rsaPrivateKeySpec));
    }

    static PublicKey sshPublicKey(String key){
        String[] split = key.split(" ");
        assert split.length == 3;
        assert split[0].equals("ssh-rsa");

        byte[] keyBytes = Base64.getDecoder().decode(split[1]);
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(keyBytes));

        byte[] header = new byte[rethrow(in::readInt)];
        rethrow(() -> in.readFully(header));

        assert new String(header).equals("ssh-rsa");

        byte[] exponent = new byte[rethrow(in::readInt)];
        rethrow(() -> in.readFully(exponent));
        BigInteger e = new BigInteger(exponent);

        byte[] modulo = new byte[rethrow(in::readInt)];
        rethrow(() -> in.readFully(modulo));
        BigInteger m = new BigInteger(modulo);

        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(m, e);
        return rethrow(() -> KeyFactory.getInstance("RSA").generatePublic(rsaPublicKeySpec));
    }

    static byte[] encrypt(PublicKey key, byte[] message){
        Cipher rsa = rethrow( () -> Cipher.getInstance("RSA"));
        rethrow(() -> rsa.init(Cipher.ENCRYPT_MODE, key));
        return rethrow(() -> rsa.doFinal(message));
    }

    static byte[] decrypt(PrivateKey key, byte[] cipher){
        Cipher rsa = rethrow( () -> Cipher.getInstance("RSA"));
        rethrow(() -> rsa.init(Cipher.DECRYPT_MODE, key));
        return rethrow(() -> rsa.doFinal(cipher));
    }

    static String encrypt(PublicKey key, String message, boolean isUrlSafe){
        return isUrlSafe ? Base64.getUrlEncoder().encodeToString(encrypt(key, message.getBytes())) :
                Base64.getEncoder().encodeToString(encrypt(key, message.getBytes()));
    }

    static String encrypt(PublicKey key, String message){
        return encrypt(key, message, false);
    }

    static String decrypt(PrivateKey key, String message, boolean isUrlSafe){
        return isUrlSafe ? new String(decrypt(key, Base64.getUrlDecoder().decode(message))) :
                new String(decrypt(key, Base64.getDecoder().decode(message)));
    }

    static String decrypt(PrivateKey key, String message) {
        return decrypt(key, message, false);
    }
}
