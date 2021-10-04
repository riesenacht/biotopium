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
import ch.riesenacht.biotopium.core.action.model.ClaimRealmAction
import ch.riesenacht.biotopium.core.action.rule.rules

/**
 * Action contract for the [ClaimRealmAction].
 */
val claimRealmContract = actionContract<ClaimRealmAction>(
    ActionType.CLAIM_REALM,

    rules {
        // the realm does not exist
        rule { action, _, world ->
            val realm = action.produce

            !world.realms.containsKey(realm.ix to realm.iy)
        }

        // the author of the block equals the owner of the claim paper,
        // the player owns the claim paper
        rule { action, block, world ->
            val claimPaper = action.consume

            claimPaper.owner == block.author
                    && world.players[claimPaper.owner]?.items?.contains(claimPaper) ?: false
        }
    },

    exec { action, _, world ->

        val claimPaper = action.consume
        val realm = action.produce
        val owner = claimPaper.owner

        // remove realm claim paper from the player's inventory
        world.players[owner]!!.removeItem(claimPaper)

        // add the new realm to the world
        world.realms[realm.ix to realm.iy] = realm
    }
)