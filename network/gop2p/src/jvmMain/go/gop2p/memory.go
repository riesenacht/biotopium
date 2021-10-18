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
import "unsafe"

// MemoryHandler is responsible for keeping track of C pointers
// and freeing memory.
type MemoryHandler struct {
	stringPointers []CString // string pointers
	sweep map[CString]bool // markers for sweeping
}

// AddString adds a CString to the memory handler.
// A CString has to be given.
func (mh *MemoryHandler) AddString(str CString) {
	mh.Sweep()
	mh.stringPointers = append(mh.stringPointers, str)
}

// Sweep frees the memory of pointers which were marked for sweeping.
func (mh *MemoryHandler) Sweep() {
	for str, sweep := range mh.sweep {
		if sweep {
			C.free(unsafe.Pointer(str))
		}
	}
	mh.sweep = make(map[CString]bool)
}

// Mark marks a C pointer for sweeping.
// Once marked the memory of the C pointer will be freed at a given time.
func (mh *MemoryHandler) Mark(str CString) {
	mh.sweep[str] = true
}

// The memory handler instance.
var memoryHandler = &MemoryHandler{
	stringPointers: make([]CString, 0, 1),
	sweep: make(map[CString]bool),
}
