package com.tailoredshapes.underbar.crypto;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.SecureRandom;
import java.util.Base64;

import static com.tailoredshapes.underbar.Die.rethrow;
import static javax.crypto.KeyGenerator.getInstance;

import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

public interface Crypto {

    class Payload {
        public IvParameterSpec iv;
        public byte[] cipher;

        public Payload(byte[] cipher, IvParameterSpec iv){
            this.cipher = cipher;
            this.iv = iv;
        }

        public Payload(String cipher, IvParameterSpec iv){
            this.cipher = fromString(cipher, false);
            this.iv = iv;
        }

        public Payload(String cipher, IvParameterSpec iv, boolean isUrlSafe){
            this.cipher = fromString(cipher, isUrlSafe);
            this.iv = iv;
        }

        public byte[] fromString(String cipher, boolean isUrlSafe){
            return this.cipher = isUrlSafe ? Base64.getUrlDecoder().decode(cipher) :
                    Base64.getDecoder().decode(cipher);
        }
        public String asString(){
           return asString(false);
        }

        public String asString(boolean isUrlSafe){
            return isUrlSafe ? Base64.getUrlEncoder().encodeToString(cipher)
                    :  Base64.getEncoder().encodeToString(cipher);
        }

        public String toString(){
            return new String(cipher);
        }
    }

    static SecretKey aesKey(){
        KeyGenerator keyGenerator = rethrow(()->getInstance("AES"));
        SecureRandom random = new SecureRandom();
        keyGenerator.init(random);
        return keyGenerator.generateKey();
    }

    static IvParameterSpec iv(){
        SecureRandom rnd = new SecureRandom();
        byte[] iv = new byte[16];
        rnd.nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    static byte[] pad(byte[] cipher){
        int paddingRequired = cipher.length % 16;

        if(0 != paddingRequired){
            byte[] dest = new byte[cipher.length + paddingRequired];
            System.arraycopy(cipher, 0, dest, 0, cipher.length);
            return  dest;
        }
        return cipher;
    }

    static Payload encrypt(SecretKey key, byte[] message){
        return aes(key, new Payload(message, iv()), ENCRYPT_MODE);
    }

    static Payload encrypt(SecretKey key, String message){
        return encrypt(key, message.getBytes());
    }

    static Payload decrypt(SecretKey key, Payload payload){
        return aes(key, payload, DECRYPT_MODE);
    }

    static Payload aes(SecretKey key, Payload payload, int mode){
        Cipher aes = rethrow(() -> Cipher.getInstance("AES/CBC/PKCS5PADDING"));
        rethrow(() -> aes.init(mode, key, payload.iv));
        return new Payload(rethrow(() -> aes.doFinal(payload.cipher)), payload.iv);
    }
}