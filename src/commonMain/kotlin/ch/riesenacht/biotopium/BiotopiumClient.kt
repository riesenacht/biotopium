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

package ch.riesenacht.biotopium

import ch.riesenacht.biotopium.core.Biotopium
import ch.riesenacht.biotopium.core.action.ActionManager
import ch.riesenacht.biotopium.core.action.model.IntroductionAction
import ch.riesenacht.biotopium.core.blockchain.BlockchainManager
import ch.riesenacht.biotopium.core.blockchain.KeyManager
import ch.riesenacht.biotopium.core.world.model.item.Hoe
import ch.riesenacht.biotopium.core.world.model.item.IntroductionGift
import ch.riesenacht.biotopium.core.world.model.item.RealmClaimPaper
import ch.riesenacht.biotopium.core.world.model.item.Seed
import ch.riesenacht.biotopium.core.world.model.plant.PlantType
import ch.riesenacht.biotopium.network.MessageHandler
import ch.riesenacht.biotopium.network.model.message.blockchain.ChainFwdMessage
import ch.riesenacht.biotopium.network.model.message.blockchain.ChainReqMessage

/**
 * Represents a biotopium instance in client-mode.
 */
object BiotopiumClient : Biotopium(biotopiumClientConfig) {

    /**
     * Updates the blockchain and calls the [callback] function.
     */
    fun updateChain(callback: () -> Unit) {
        this.networkManager.send(blocklordPeerIds.random(), ChainReqMessage(BlockchainManager.maxHeight))
        var handler: MessageHandler<ChainFwdMessage>? = null
        handler = MessageHandler { _, _ ->
            this.networkManager.removeMessageHandler(ChainFwdMessage::class, handler!!)
            callback.invoke()
        }
        this.networkManager.registerMessageHandler(ChainFwdMessage::class, handler)
    }

    /**
     * Creates the introduction action for a new player.
     */
    fun createIntroductionAction() {
        val address = KeyManager.address
        val numHoes = 8
        val numWheatSeeds = 4
        val numCornSeeds = 4
        val realmClaimPaper = RealmClaimPaper(address)
        val hoes = (1..numHoes).map { Hoe(address) }.toList()
        val wheatSeeds = (1..numWheatSeeds).map { Seed(address, PlantType.WHEAT) }.toList()
        val cornSeeds = (1..numCornSeeds).map { Seed(address, PlantType.CORN) }.toList()
        val introductionGift = IntroductionGift(realmClaimPaper, hoes, wheatSeeds + cornSeeds)
        ActionManager.createAction(IntroductionAction(introductionGift))
    }
}
