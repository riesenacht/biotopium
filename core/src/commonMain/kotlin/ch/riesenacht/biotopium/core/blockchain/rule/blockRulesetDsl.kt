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

package ch.riesenacht.biotopium.core.blockchain.rule

/**
 * The builder class for constructing a block ruleset.
 *
 * @author Manuel Riesen
 */
class BlockRuleSetBuilder {

    /**
     * All block rules.
     */
    val rules: MutableList<BlockRule> = mutableListOf()

    /**
     * Creates a block rule with the given [predicate]
     * and adds it to the current ruleset on construction.
     */
    fun rule(predicate: BlockRule) {
        rules.add(predicate)
    }

    /**
     * Includes a [ruleSet].
     * Adds all elements of the given [ruleSet] to the one which is currently built.
     */
    fun include(ruleSet: BlockRuleSet) {
        rules.addAll(ruleSet)
    }
}

/**
 * Creates a block ruleset and applies the [init] function.
 */
fun blockRuleset(init: BlockRuleSetBuilder.() -> Unit): BlockRuleSet {
    val ruleSetBuilder = BlockRuleSetBuilder()
    ruleSetBuilder.init()
    return ruleSetBuilder.rules.toTypedArray()
}