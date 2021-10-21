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
	"github.com/libp2p/go-libp2p-core/peer"
	pubsub "github.com/libp2p/go-libp2p-pubsub"
	"riesenacht.ch/biotopium/network/gop2p/check"
)

// PubSubBufSize is the buffer size for pubsub
const PubSubBufSize = 128

// PubSubTopic represents a pubsub topic.
type PubSubTopic struct {
	Messages chan Message // Message input channel

	ctx    context.Context      // Context
	ps     *pubsub.PubSub       // PubSub instance
	topic  *pubsub.Topic        // Topic
	sub    *pubsub.Subscription // Subscription
	peerID peer.ID              // Peer ID
}

// listenTopic starts to listen to a topic.
// A context, a pubsub, and a peer ID have to be given.
// A ps topic is returned.
func listenTopic(ctx context.Context, ps *pubsub.PubSub, peerID peer.ID) *PubSubTopic {

	topic, err := ps.Join(instance.Config.Topic)
	check.Err(err)

	sub, err := topic.Subscribe()
	check.Err(err)

	t := &PubSubTopic{
		ctx:      ctx,
		ps:       ps,
		topic:    topic,
		sub:      sub,
		peerID:   peerID,
		Messages: make(chan Message, PubSubBufSize),
	}

	go t.listen()
	return t
}

// listen listens to incoming messages of a pubsub topic.
func (t *PubSubTopic) listen() {
	for {
		msg, err := t.sub.Next(t.ctx)
		if err != nil {
			close(t.Messages)
			return
		}
		// exclude current peer ID
		if msg.ReceivedFrom == t.peerID {
			continue
		}
		t.Messages <- msg.Data
	}
}

// Publish publishes a message to the pubsub topic.
// A message has to be given.
func (t *PubSubTopic) Publish(message []byte) {
	err := t.topic.Publish(t.ctx, message)
	check.Err(err)
}
