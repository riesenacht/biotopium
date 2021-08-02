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
import jnr.ffi.Memory;
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

    static {
        String buildDirPath = new File(GoP2p.class.getProtectionDomain().getCodeSource().getLocation().getPath()).toPath().getParent().getParent().toAbsolutePath().toString();
        String path = new File(buildDirPath + File.separator + LIBRARY_NAME).getAbsolutePath();
        GO_P2P_LIBRARY = LibraryLoader.create(GoP2pLibrary.class).load(path);
    }

    /**
     * Starts the peer-to-peer server.
     */
    public void start() {
        GO_P2P_LIBRARY.StartServer();
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
        // correct C string size
        int size = serialized.length()+1;
        Pointer ptr =  Runtime.getSystemRuntime().getMemoryManager().allocateTemporary(size, true);
        ptr.putString(0, serialized, size, StandardCharsets.UTF_8);
        GO_P2P_LIBRARY.Send(ptr);
    }

    /**
     * Represents the gop2p library.
     */
    public interface GoP2pLibrary {
        void StartServer();
        void StopServer();
        void Send(Pointer serialized);
        Pointer ListenBlocking();
    }

}
