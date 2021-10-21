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
	"context"
	"fmt"
	"github.com/libp2p/go-libp2p"
	"github.com/libp2p/go-libp2p-core/crypto"
	"github.com/libp2p/go-libp2p-core/host"
	"github.com/libp2p/go-libp2p-core/routing"
	dht "github.com/libp2p/go-libp2p-kad-dht"
	noise "github.com/libp2p/go-libp2p-noise"
	pubsub "github.com/libp2p/go-libp2p-pubsub"
	"github.com/libp2p/go-libp2p-core/peer"
	"log"
	"riesenacht.ch/biotopium/network/gop2p/check"
)

// server represents a peer-to-peer server.
type server struct {
	Host   host.Host          // P2P host
	Config *config            // configuration
	PubSub *PubSubTopic       // ps network
	Stream *Stream            // stream
	Cancel context.CancelFunc // running state
}

// The peer-to-peer server instance
var instance *server

// Instance provides access to the current p2p server instance.
// The instance if not nil is returned.
func Instance() *server {
	if instance == nil {
		log.Fatalln("peer-to-peer server is not created yet.")
		return nil
	}
	return instance
}

func PeerID() peer.ID {
	return instance.Host.ID()
}

// StartP2PServer starts the peer-to-peer server with a given configuration.
// A configuration has to be given.
func StartP2PServer(config *config) {
	instance = &server{
		Config: config,
	}
	ctx, cancel := context.WithCancel(context.Background())
	instance.Cancel = cancel

	var err error
	var privateKey crypto.PrivKey
	if config.PKBytes != nil {
		privateKey, err = crypto.UnmarshalPrivateKey(config.PKBytes)
		check.Err(err)
	} else {
		privateKey, _, err = crypto.GenerateKeyPair(
			crypto.Ed25519,
			-1,
		)
	}
	h, err := libp2p.New(ctx,
		libp2p.Identity(privateKey),
		libp2p.ListenAddrStrings(
			fmt.Sprintf("/ip4/0.0.0.0/tcp/%d/ws/", config.Port),
		),
		libp2p.NATPortMap(),      // Try to open ports over uPnP
		libp2p.EnableAutoRelay(), // Advertise node on relays
		libp2p.DefaultTransports, // default transports, includes WebSockets and TCP
		libp2p.Security(noise.ID, noise.New),
		libp2p.Routing(func(h host.Host) (routing.PeerRouting, error) {
			dhtInstance, err := dht.New(
				ctx,
				h,
			)
			return dhtInstance, err
		}),
	)
	check.Err(err)
	instance.Host = h

	ps, err := pubsub.NewGossipSub(ctx, h)
	check.Err(err)

	psNet := listenTopic(ctx, ps, h.ID())
	instance.PubSub = psNet

	stream := listenProtocol(h, config.ProtocolName)
	instance.Stream = stream

	log.Printf("P2P Server started with peer ID %s\n", h.ID())
}

// StopP2PServer stops the peer-to-peer instance.
func StopP2PServer() {
	defer instance.Cancel()
	err := instance.Host.Close()
	check.Err(err)
}
