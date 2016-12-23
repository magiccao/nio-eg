# nio-eg
one java nio server example based sdk

one java nio server example based netty

***
## 组成
server：接收client的请求，并回显； 基于java sdk nio开发

netty-server：逻辑和server相同；基于netty开发

client：向server发送4个request，终端打印相应的响应； golang开发

***
## server和client 通信协议
+ request
```
  +-------+------------+------+-------------+
  | type |   length   |  op  |     body     |
  | 0x01 | 0x0000AC02 | 0x00 | (300 bytes)  |
  +------+------------+------+--------------+
```
+ response
```
  +-------+------------+------+-------------+
  | type |   length   |  op  |     body     |
  | 0x01 | 0x0000AC02 | 0x00 | (300 bytes)  |
  +------+------------+------+--------------+
```

***
## 协议释义
+ len：body的长度，4字节，大端；
+ type：body序列化协议，1字节

## 运行
client：go run client.go
