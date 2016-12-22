# nio-eg
one java nio example

***
## 组成
server：接收client的请求，并回显； java开发
client：向server发送4个request，终端打印相应的响应； golang开发

***
## server和client 通信协议
header(6B)： type(1B) | len(4B) | op(1B)

***
## 协议释义
+ len：body的长度，4字节，大端；
+ type：body序列化协议，1字节

## 运行
client：go run client.go
