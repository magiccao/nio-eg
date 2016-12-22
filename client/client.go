package main

import (
	"encoding/binary"
	"fmt"
	"io"
	"log"
	"net"
)

func main() {
	conn, err := net.Dial("tcp", "127.0.0.1:8080")
	if err != nil {
		log.Fatal(err)
	}

	conn.(*net.TCPConn).SetNoDelay(true)
	for i := 0; i < 2; i++ {
		writeRequest(conn)
		readResponse(conn)
	}

	writeRequest(conn)
	writeRequest(conn)
	readResponse(conn)
	readResponse(conn)

	conn.Close()
}

func writeRequest(conn net.Conn) {
	writeAll(conn, []byte{'9'}) // type

	bodyL := make([]byte, 4)
	binary.BigEndian.PutUint32(bodyL, 26)
	writeAll(conn, bodyL) // len

	writeAll(conn, []byte{'1'}) // op

	body := make([]byte, 26)
	for i := 0; i < 26; i++ {
		body[i] = byte('A' + i)
	}
	writeAll(conn, body)
}

func readResponse(conn net.Conn) {
	bodyL := make([]byte, 4)
	readAll(conn, bodyL)
	len := binary.BigEndian.Uint32(bodyL)

	buf := make([]byte, len)
	readAll(conn, buf)
	fmt.Println(string(buf))
}

func readAll(conn net.Conn, buf []byte) (n int, err error) {
	size := len(buf)
	for n < size {
		nn, e := conn.Read(buf)
		n += nn
		if e != nil {
			if e != io.EOF {
				err = e
			}
			break
		}
		buf = buf[nn:]
	}
	return
}

func writeAll(conn net.Conn, buf []byte) (n int, err error) {
	size := len(buf)
	for n < size {
		nn, e := conn.Write(buf)
		n += nn
		if e != nil {
			if e != io.EOF {
				err = e
			}
			break
		}
		buf = buf[nn:]
	}
	return
}
