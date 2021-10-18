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

import (
	"errors"
	"strings"
)

// StrSeparator represents the used seperator for a string bundle.
const StrSeparator = "~"

// StrBundle creates a bundle out of given strings.
// The strings are concatenated using a defined separator.
func StrBundle(arr ...string) (string, error) {
	bundle := ""
	for i, str := range arr {
		if strings.Contains(str, StrSeparator) {
			return "", errors.New("Failed constructing string bundle: separator occurs in string")
		}
		if i > 0 {
			bundle += StrSeparator
		}
		bundle += str
	}
	return bundle, nil
}
