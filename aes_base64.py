#!/usr/bin/env python
# -*- coding:utf-8 -*-
'''
### How to run:
pip install pycrypto
python3 aes_base64.py
'''


from Crypto.Cipher import AES
import base64


class AESEncrypto:
    '''
    AES/CBC/PKCS5Padding
    '''

    def __init__(self, key, iv):
        self.BLOCK_SIZE = 16
        self.raw_key = base64.b64decode(key)
        self.iv = iv

    def pkcs5_pad(self, s):
        return s + (self.BLOCK_SIZE - len(s) % self.BLOCK_SIZE) * \
            chr(self.BLOCK_SIZE - len(s) % self.BLOCK_SIZE)

    def pkcs5_unpad(self, s):
        return s[0:-ord(s[-1])]

    def encrypt(self, value):
        '''
        input str, return base64 string
        '''
        cipher = AES.new(self.raw_key, AES.MODE_CBC, self.iv)
        value = self.pkcs5_pad(value)
        crypted = base64.b64encode(cipher.encrypt(value)).decode()
        return crypted

    def decrypt(self, value):
        '''
        input base64 string, return bytes
        '''
        cipher = AES.new(self.raw_key, AES.MODE_CBC, self.iv)
        _value = base64.b64decode(value)
        crypted = cipher.decrypt(_value)
        if type(crypted) is bytes:
            crypted = crypted.decode('raw_unicode_escape')
        return self.pkcs5_unpad(crypted)


if __name__ == '__main__':
    # init values
    content_str = "this is a test string"
    aes_key = "cTFXd1RySmQ5VzZHa0NoeQ=="
    iv_str = "1234567890864210"
    print("Origin: %s" % content_str)

    # init obj
    aes = AESEncrypto(aes_key, iv_str)
    # encryption
    _en_str = aes.encrypt(content_str)
    # decryption
    print("Encrypt: %s" % _en_str)
    print("Decrypt: %s" % aes.decrypt(_en_str))
