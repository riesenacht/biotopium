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
	"unsafe"
)

// MemoryHandler is responsible for keeping track of C pointers
// and freeing memory.
type MemoryHandler struct {
	stringPointers []CString // string pointers
}

// AddString adds a CString to the memory handler.
// A CString has to be given.
func (mh *MemoryHandler) AddString(str CString) {
	mh.Free()
	mh.stringPointers = append(mh.stringPointers, str)
}

// Free frees all C pointers known by the memory handler.
func (mh *MemoryHandler) Free() {
	for _, str := range mh.stringPointers {
		C.free(unsafe.Pointer(str))
	}
	mh.stringPointers = make([]CString, 0, 1)
}

// The memory handler instance.
var memoryHandler = &MemoryHandler{
	make([]CString, 0, 1),
}
