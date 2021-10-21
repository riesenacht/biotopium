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

package p2p

import (
	"bufio"
	"context"
	"github.com/libp2p/go-libp2p-core/host"
	"github.com/libp2p/go-libp2p-core/network"
	"github.com/libp2p/go-libp2p-core/peer"
	"github.com/libp2p/go-libp2p-core/protocol"
	"log"
	"riesenacht.ch/biotopium/network/gop2p/check"
)

const StreamBufSize = 128

// Represents a stream.
type Stream struct {
	Messages chan Message // Message input channel

	protocolID protocol.ID // Protocol ID
}

// listenProtocol listens to a protocol of the given name.
// The host and the protocol name must be given.
// A pointer to a new stream is returned
func listenProtocol(h host.Host, protocolName string) *Stream {
	protocolID := protocol.ID(protocolName)
	stream := &Stream{
		Messages:   make(chan Message, StreamBufSize),
		protocolID: protocolID,
	}
	h.SetStreamHandler(protocolID, func(s network.Stream) {
		buf := bufio.NewReader(s)
		str, err := buf.ReadString('\n')
		if err != nil {
			err = s.Reset()
			check.Err(err)
		}

		peerID, err := s.Conn().RemotePeer().MarshalText()
		check.Err(err)
		log.Printf("received " + str + "from " + string(peerID))
		stream.Messages <- []byte(str)
		err = s.Close()
		if err != nil {
			err = s.Reset()
			check.Err(err)
		}
	})

	return stream
}

// Send sends a message to a specific peer.
// The peer ID of the receiver and the serialized message has to be given.
func (s *Stream) Send(peerID peer.ID, serialized []byte) {
	stream, err := instance.Host.NewStream(context.Background(), peerID, instance.Stream.protocolID)
	check.Err(err)
	_, err = stream.Write(serialized)
	check.Err(err)
	err = stream.Close()
	if err != nil {
		err = stream.Reset()
		check.Err(err)
	}
}
