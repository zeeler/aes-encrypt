## AES encryption code examples

### Go example

#### 1. Make sure go is installed on your system

```shell
go version
```

* better if you installed version newer than 1.10

#### 2. Run directly

```shell
go run aes_base64.go
```

#### 3. Compile and run by binary file

```shell
go build aes_base64.go
./aes_base64
```

### Python example

#### 1. Make sure python3 is installed on your system

```shell
python3 -VV
```

#### 2. Install crypto module

```shell
pip install pycrypto
```

#### 3. Run

```shell
python3 aes_base64.py
```

### Java example

#### 1. Make sure java jdk is installed on your system

```shell
javac -version
```

#### 2. Compile

```shell
javac -cp .:org-apache-commons-codec.jar AESBase64.java
```

#### 3. Run

```shell
java -cp .:org-apache-commons-codec.jar AESBase64
```