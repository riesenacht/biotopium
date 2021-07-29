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
)

// StartServer starts the peer-to-peer server.
//export StartServer
func StartServer() {
	pkBytes := []byte{0x8, 0x1, 0x12, 0x40, 0xac, 0x44, 0x9, 0x4, 0x2e, 0x4b, 0x7, 0x7b, 0x81, 0x51, 0x1, 0x0, 0xfb, 0x7d, 0x4b, 0xb1, 0xab, 0xc9, 0x8b, 0xdd, 0x44, 0xd6, 0x12, 0x91, 0x20, 0xc4, 0x95, 0x82, 0x52, 0x3c, 0x52, 0xc6, 0x4f, 0xc6, 0x41,
		0x5e, 0xb5, 0x11, 0x5a, 0xb2, 0xae, 0x77, 0xdd, 0x5a, 0x9f, 0x1a, 0xc1, 0x13, 0x56, 0x26, 0xd1, 0xb5, 0x98, 0x89, 0x2, 0x67, 0x36, 0xae, 0xe6, 0x54, 0xd1, 0xda, 0x8c, 0xd}

	config := p2p.NewConfig("/biotopium/0.1.0", 5558, pkBytes)
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
