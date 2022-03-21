# biotopium
[![Kotlin](https://img.shields.io/badge/Kotlin-1.6.10-%237F52FF?style=flat&logo=kotlin)](https://kotlinlang.org)
[![platform jvm](https://img.shields.io/badge/platform-jvm-red?style=flat)]()
[![platform browser](https://img.shields.io/badge/platform-js-yellow?style=flat)]()

[![project chat](https://img.shields.io/badge/zulip-join_chat-blue.svg?style=flat&logo=zulip)](https://biotopium.zulipchat.com)

## About
biotopium is a distributed, platform-independent multiplayer game running on a custom, dedicated blockchain.

## Implementation
The core of biotopium is implemented in Kotlin, 
a multi-purpose and multi-paradigmal programming language developed by JetBrains.

Leveraging the Kotlin Multiplatform feature as well as interoperability with Java and JavaScript, 
the project currently targets the platforms JS (Browser) and JVM.

## Development
Currently, this is rather a Proof-of-Concept for a distributed game implementation than a production-ready game.
We use a highly incremental approach for the development cycle.

Since this is a prototype and in favor of rapid development many breaking changes will occur in the early phase of development.

## Build Requirements
Besides JDK 11 and the Kotlin compiler the following tools are required to build this project:
- GCC
- Go 16

The current version uses go-libp2p for the JVM network component.
Therefore, the Go library has to be built as a cshared library using Go and GCC.

## Run the demo
Before running the demo, the `gop2p` module has to be built: run `goBuild`

In order to run the demo, two components are necessary:
1. A blocklord instance: run `runBlocklord`
2. A client instance: run `runJvm` or `runWebpack`

The current version is very limited, but we appreciate some feedback.

## Contributors
This project was originally initiated by
- Manuel Riesen
- Sandro Rüfenacht

## License
This project is released under the GPLv3 license.
For more information, see the COPYING file.