/*
* @Author: zeeler
* @Date:   2019-10-01
* How to run
* 1) Make sure you have golang installed on your operation system
* go version
* 2) run code
* go run aes_base64.go
 */

package main

import (
	"bytes"
	"crypto/aes"
	"crypto/cipher"
	"encoding/base64"
	"fmt"
)

//AESBase64 main struct
type AESBase64 struct {
	SecretKey []byte
	IvSpec    []byte
}

func (a *AESBase64) encrypt(contentStr string) ([]byte, error) {
	// encryption
	block, err := aes.NewCipher(a.SecretKey)
	if err != nil {
		return nil, err
	}
	blockSize := block.BlockSize()
	origData := pkCS5Padding([]byte(contentStr), blockSize)
	blockMode := cipher.NewCBCEncrypter(block, a.IvSpec)
	crypted := make([]byte, len(origData))
	blockMode.CryptBlocks(crypted, origData)
	// base64 encode
	ret := base64.StdEncoding.EncodeToString(crypted)
	return []byte(ret), nil
}

func (a *AESBase64) decrypt(crypted []byte) ([]byte, error) {
	// decode base64 string
	toDecrypt, err := base64.StdEncoding.DecodeString(string(crypted))
	if err != nil {
		return nil, err
	}
	// decryption
	block, err := aes.NewCipher(a.SecretKey)
	if err != nil {
		return nil, err
	}
	blockMode := cipher.NewCBCDecrypter(block, a.IvSpec)
	origData := make([]byte, len(toDecrypt))
	blockMode.CryptBlocks(origData, toDecrypt)
	origData = pkCS5UnPadding(origData)
	return origData, nil
}

func pkCS5Padding(ciphertext []byte, blockSize int) []byte {
	padding := blockSize - len(ciphertext)%blockSize
	padtext := bytes.Repeat([]byte{byte(padding)}, padding)
	return append(ciphertext, padtext...)
}

func pkCS5UnPadding(origData []byte) []byte {
	length := len(origData)
	// remove the last byte
	unpadding := int(origData[length-1])
	return origData[:(length - unpadding)]
}

func main() {
	// init
	contentStr := "this is a test string"
	key := "cTFXd1RySmQ5VzZHa0NoeQ=="
	iv := []byte("1234567890864210")
	fmt.Println("Origin:", contentStr)

	// decode base64 key
	deKey, err := base64.StdEncoding.DecodeString(key)
	if err != nil {
		fmt.Println(err)
		return
	}

	// init obj
	aesObj := AESBase64{
		SecretKey: deKey,
		IvSpec:    iv,
	}

	// encryption
	res, err := aesObj.encrypt(contentStr)
	if err != nil {
		fmt.Println(err)
		return
	}
	fmt.Println("Encrypt:", string(res))

	// decryption
	res, err = aesObj.decrypt(res)
	if err != nil {
		fmt.Println(err)
		return
	}
	fmt.Println("Decrypt:", string(res))
}
