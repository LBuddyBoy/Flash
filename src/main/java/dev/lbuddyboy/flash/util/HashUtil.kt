package dev.lbuddyboy.flash.util

import org.bson.internal.Base64
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class HashUtil(key: SecretKey?) {
    private val ecipher: Cipher
    private val dcipher: Cipher

    init {
        ecipher = Cipher.getInstance("AES")
        dcipher = Cipher.getInstance("AES")
        ecipher.init(Cipher.ENCRYPT_MODE, key)
        dcipher.init(Cipher.DECRYPT_MODE, key)
    }

    @Throws(Exception::class)
    fun encrypt(str: String): String {
        val utf8 = str.toByteArray(StandardCharsets.UTF_8)
        val enc = ecipher.doFinal(utf8)
        return Base64.encode(enc)
    }

    @Throws(Exception::class)
    fun decrypt(str: String?): String {
        val dec = Base64.decode(str)
        val utf8 = dcipher.doFinal(dec)
        return kotlin.String(utf8, StandardCharsets.UTF_8)
    }

    companion object {
        private const val key = "bcD@g@s3%92B&#Zq"
        fun encryptUsingKey(str: String): String? {
            val secretKey: SecretKey = SecretKeySpec(key.toByteArray(), "AES")
            val encrypter: HashUtil
            encrypter = try {
                HashUtil(secretKey)
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
            val encrypted: String
            encrypted = try {
                encrypter.encrypt(str)
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
            return encrypted
        }

        fun decryptUsingKey(str: String?): String? {
            val secretKey: SecretKey = SecretKeySpec(key.toByteArray(), "AES")
            val encrypter: HashUtil
            encrypter = try {
                HashUtil(secretKey)
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
            return try {
                encrypter.decrypt(str)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } // Hash and Salt - Use this!
        //    public static byte[] getSalt() {
        //        return key.getBytes();
        //    }
        //
        //    public static byte[] getSaltedHash512(String input, byte[] salt) {
        //        try {
        //            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        //            messageDigest.update(salt);
        //            byte[] byteData = messageDigest.digest(input.getBytes());
        //            messageDigest.reset();
        //
        //            return byteData;
        //        }catch (Exception e) {
        //            e.printStackTrace();
        //            return null;
        //        }
        //    }
        //
        //    public static byte[] getSaltedHash(String input) {
        //        return getSaltedHash512(input, getSalt());
        //    }
    }
}