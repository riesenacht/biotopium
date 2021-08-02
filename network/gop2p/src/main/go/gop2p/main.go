/*
 * Copyright (c) 2021 The biotopium Authors.
 * This file is part of biotopium.
 *
 * biotopium is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * biotopium is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with biotopium.  If not, see <https://www.gnu.org/licenses/>.
 */

package main

//#include <stdlib.h>
import "C"
import (
	"riesenacht.ch/biotopium/network/gop2p/p2p"
	"riesenacht.ch/biotopium/network/gop2p/check"
	"encoding/base64"
)

// StartServer starts the peer-to-peer server.
//export StartServer
func StartServer(port int, pkBase64Ptr CString) {
    pkBase64 := C.GoString(pkBase64Ptr)
    var pkBytes []byte
    if len(pkBase64) > 0 {
        pkStr, err := base64.StdEncoding.DecodeString(string(pkBase64))
        check.Err(err)
        pkBytes = []byte(pkStr)
    }
	config := p2p.NewConfig("/biotopium/0.1.0", port, pkBytes)
	p2p.StartP2PServer(config)
}

// StopServer stops the peer-to-peer server.
//export StopServer
func StopServer() {
	p2p.StopP2PServer()
}

// ListenBlocking listens for new messages.
// This is a blocking function, waiting on a channel.
//export ListenBlocking
func ListenBlocking() CString {
	message := <-p2p.Instance().PubSub.Messages
	return NewCString(string(message))
}

// Send sends a message to all known peers.
// The serialized message as pointer to a C character (array) has to be given.
//export Send
func Send(serialized *C.char) {
	str := C.GoString(serialized)
	p2p.Instance().PubSub.Publish([]byte(str))
}

func main() {}
