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

package ch.riesenacht.biotopium.core.action.contract

import ch.riesenacht.biotopium.core.action.exec.exec
import ch.riesenacht.biotopium.core.action.model.ActionType
import ch.riesenacht.biotopium.core.action.model.IntroductionAction
import ch.riesenacht.biotopium.core.action.rule.rules
import ch.riesenacht.biotopium.core.world.Player

/**
 * Action contract of the [IntroductionAction].
 */
val introductionContract = actionContract<IntroductionAction>(
    ActionType.INTRODUCTION,

    rules {

        // the player does not yet exist
        rule { action, origin, world ->
            world.players[origin.author] == null
        }

        // all introduction items are owned by the action author
        rule { action, origin, _ ->
            val gift = action.produce
            gift.seeds.all { it.owner == origin.author }
                    && gift.hoes.all { it.owner == origin.author }
                    && gift.realmClaimPaper.owner == origin.author
        }

    },
    exec { action, _, world ->

        val gift = action.produce
        val owner = gift.realmClaimPaper.owner

        val player = Player("player", owner)

        world.players[owner] = player

        player.addAllItems(gift.hoes)
        player.addAllItems(gift.seeds)
        player.addItem(gift.realmClaimPaper)
    }
)