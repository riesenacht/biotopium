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

// config represent the configuration of a peer-to-peer instance.
type config struct {
	Topic string // topic to listen to
	Port int // port to listen on
	PKBytes []byte // bytes for private key
}

// NewConfig is the factory function of the config struct.
// A topic, a port and private key bytes have to be given.
// A pointer to a new P2PConfig is returned.
func NewConfig(topic string, port int, pkByte []byte) *config {
	return &config{
		Topic: topic,
		Port: port,
		PKBytes: pkByte,
	}
}
