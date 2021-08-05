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

package ch.riesenacht.biotopium.network.go2p;

import jnr.ffi.LibraryLoader;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * Wrapper for the gop2p library.
 * Uses JNR-FFI as native interface for accessing Go code.
 *
 * @author Manuel Riesen
 */
public class GoP2p {

    private static final String LIBRARY_NAME = System.mapLibraryName("gop2p");

    private static final GoP2pLibrary GO_P2P_LIBRARY;

    private final String topic;
    private final int port;
    private final String privateKeyBase64;

    static {
        String buildDirPath = new File(GoP2p.class.getProtectionDomain().getCodeSource().getLocation().getPath()).toPath().getParent().getParent().toAbsolutePath().toString();
        String path = new File(buildDirPath + File.separator + LIBRARY_NAME).getAbsolutePath();
        GO_P2P_LIBRARY = LibraryLoader.create(GoP2pLibrary.class).load(path);
    }

    private GoP2p(String topic, int port, String privateKeyBase64) {
        this.topic = topic;
        this.port = port;
        this.privateKeyBase64 = privateKeyBase64;
    }

    /**
     * The builder for the {@link GoP2p} class.
     */
    public static class Builder {

        private String topic;
        private int port = 0;
        private String privateKeyBase64;

        private Builder() { }

        /**
         * Sets the topic to use of the new {@link GoP2p} instance.
         * @param topic to use
         * @return builder
         */
        public Builder topic(String topic) {
            this.topic = topic;
            return this;
        }

        /**
         * Sets the port of the new {@link GoP2p} instance.
         * @param port port to listen
         * @return builder
         */
        public Builder port(int port) {
            this.port = port;
            return this;
        }

        /**
         * Sets the private key of the new {@link GoP2p} instance.
         * @param privateKeyBase64 private key in base64 format
         * @return builder
         */
        public Builder privateKeyBase64(String privateKeyBase64) {
            this.privateKeyBase64 = privateKeyBase64;
            return this;
        }

        /**
         * Finishes the building process.
         * @return new {@link GoP2p} instance
         */
        public GoP2p build() {
            return new GoP2p(topic, port, privateKeyBase64);
        }
    }

    /**
     * Provides the builder.
     * @return builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Starts the peer-to-peer server.
     */
    public void start() {
        Pointer privateKeyPtr = null;
        if(privateKeyBase64 != null) {
            privateKeyPtr = createPointerFromString(privateKeyBase64);
        }
        Pointer topicPtr = createPointerFromString(topic);
        GO_P2P_LIBRARY.StartServer(topicPtr, port, privateKeyPtr);
    }

    /**
     * Stops the peer-to-peer server.
     */
    public void stop() {
        GO_P2P_LIBRARY.StopServer();
    }

    /**
     * Listens to new messages.
     * This methods is blocking.
     * @return received message
     */
    public String listenBlocking() {
        Pointer result = GO_P2P_LIBRARY.ListenBlocking();
        return result.getString(0);
    }

    /**
     * Sends a message.
     * @param serialized serialized message
     */
    public void send(String serialized) {
        Pointer ptr = createPointerFromString(serialized);
        GO_P2P_LIBRARY.Send(ptr);
    }

    /**
     * Creates a pointer and puts the given string in it.
     * @param str string to put in pointer
     * @return pointer to string
     */
    private Pointer createPointerFromString(String str) {
        // correct C string size
        int size = str.length()+1;
        Pointer ptr =  Runtime.getSystemRuntime().getMemoryManager().allocateTemporary(size, true);
        ptr.putString(0, str, size, StandardCharsets.UTF_8);
        return ptr;
    }

    /**
     * Represents the gop2p library.
     */
    public interface GoP2pLibrary {
        void StartServer(Pointer topicPtr, int port, Pointer pkBase64Ptr);
        void StopServer();
        void Send(Pointer serialized);
        Pointer ListenBlocking();
    }

}
