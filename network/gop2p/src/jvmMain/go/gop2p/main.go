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
	"encoding/base64"
	"github.com/libp2p/go-libp2p-core/peer"
	"riesenacht.ch/biotopium/network/gop2p/check"
	"riesenacht.ch/biotopium/network/gop2p/p2p"
	"strings"
)

// The delimiter for string bundles.
const stringBundleDelimiter = ";"

// StartServer starts the peer-to-peer server.
//export StartServer
func StartServer(topicPtr CString, protocolNamePtr CString, port int, bootstrapPeerBundlePtr CString, pkBase64Ptr CString) {
	pkBase64 := C.GoString(pkBase64Ptr)
	var pkBytes []byte
	if len(pkBase64) > 0 {
		pkStr, err := base64.StdEncoding.DecodeString(string(pkBase64))
		check.Err(err)
		pkBytes = pkStr
	}
	topic := C.GoString(topicPtr)
	protocolName := C.GoString(protocolNamePtr)
	bootstrapPeerBundle := C.GoString(bootstrapPeerBundlePtr)
	var bootstrapPeers []string
	if len(bootstrapPeerBundle) != 0 {
		bootstrapPeers = strings.Split(bootstrapPeerBundle, stringBundleDelimiter)
	} else {
		bootstrapPeers = make([]string, 0, 0)
	}

	config := p2p.NewConfig(topic, protocolName, port, bootstrapPeers, pkBytes)
	p2p.StartP2PServer(config)
}

// StopServer stops the peer-to-peer server.
//export StopServer
func StopServer() {
	p2p.StopP2PServer()
}

// PeerID returns the ID of the local peer.
//export PeerID
func PeerID() CString {
	peerID := p2p.PeerID()
	idBytes, err := peerID.MarshalText()
	check.Err(err)
	return NewCStringOnce(string(idBytes))
}

// ListenPubSubBlocking listens for new messages.
// This is a blocking function, waiting on a channel.
//export ListenPubSubBlocking
func ListenPubSubBlocking() CString {
	message := <-p2p.Instance().PubSub.Messages
	return NewCStringOnce(string(message))
}

// ListenStreamBlocking listens for new messages on the stream.
// This is a blocking function, waiting on a channel.
//export ListenStreamBlocking
func ListenStreamBlocking() CString {
	message := <-p2p.Instance().Stream.Messages
	return NewCStringOnce(string(message))
}


// SendPubSub sends a message to all known peers.
// The serialized message as pointer to a C character (array) has to be given.
//export SendPubSub
func SendPubSub(serialized *C.char) {
	str := C.GoString(serialized)
	println(str)
	p2p.Instance().PubSub.Publish([]byte(str))
}

// SendStream sends a message to a specific peer.
// The peer ID and the serialized message as pointer to a C character (array) must be given.
//export SendStream
func SendStream(peerIdPtr, serializedPtr *C.char) {
	encodedPeerID := C.GoString(peerIdPtr)
	peerID, err := peer.Decode(encodedPeerID)
	check.Err(err)
	str := C.GoString(serializedPtr)
	p2p.Instance().Stream.Send(peerID, []byte(str))
}

func main() {}
