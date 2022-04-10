/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.bukkit

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Statistic
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.karrat.struct.Uuid
import java.util.*

public class OfflinePlayer(
    public val uuid: Uuid
) : BukkitOfflinePlayer {
    
    override fun isOp(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun setOp(value: Boolean) {
        TODO("Not yet implemented")
    }
    
    override fun getName(): String? {
        TODO("Not yet implemented")
    }
    
    override fun getUniqueId(): UUID =
        UUID(uuid.mostSignificantBits, uuid.leastSignificantBits)
    
    override fun serialize(): MutableMap<String, Any> {
        TODO("Not yet implemented")
    }
    
    override fun isOnline(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun isBanned(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun isWhitelisted(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun setWhitelisted(value: Boolean) {
        TODO("Not yet implemented")
    }
    
    override fun getPlayer(): Player? {
        TODO("Not yet implemented")
    }
    
    override fun getFirstPlayed(): Long {
        TODO("Not yet implemented")
    }
    
    override fun getLastPlayed(): Long {
        TODO("Not yet implemented")
    }
    
    override fun hasPlayedBefore(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun getBedSpawnLocation(): Location? {
        TODO("Not yet implemented")
    }
    
    override fun incrementStatistic(statistic: Statistic) {
        TODO("Not yet implemented")
    }
    
    override fun incrementStatistic(statistic: Statistic, amount: Int) {
        TODO("Not yet implemented")
    }
    
    override fun incrementStatistic(statistic: Statistic, material: Material) {
        TODO("Not yet implemented")
    }
    
    override fun incrementStatistic(statistic: Statistic, material: Material, amount: Int) {
        TODO("Not yet implemented")
    }
    
    override fun incrementStatistic(statistic: Statistic, entityType: EntityType) {
        TODO("Not yet implemented")
    }
    
    override fun incrementStatistic(statistic: Statistic, entityType: EntityType, amount: Int) {
        TODO("Not yet implemented")
    }
    
    override fun decrementStatistic(statistic: Statistic) {
        TODO("Not yet implemented")
    }
    
    override fun decrementStatistic(statistic: Statistic, amount: Int) {
        TODO("Not yet implemented")
    }
    
    override fun decrementStatistic(statistic: Statistic, material: Material) {
        TODO("Not yet implemented")
    }
    
    override fun decrementStatistic(statistic: Statistic, material: Material, amount: Int) {
        TODO("Not yet implemented")
    }
    
    override fun decrementStatistic(statistic: Statistic, entityType: EntityType) {
        TODO("Not yet implemented")
    }
    
    override fun decrementStatistic(statistic: Statistic, entityType: EntityType, amount: Int) {
        TODO("Not yet implemented")
    }
    
    override fun setStatistic(statistic: Statistic, newValue: Int) {
        TODO("Not yet implemented")
    }
    
    override fun setStatistic(statistic: Statistic, material: Material, newValue: Int) {
        TODO("Not yet implemented")
    }
    
    override fun setStatistic(statistic: Statistic, entityType: EntityType, newValue: Int) {
        TODO("Not yet implemented")
    }
    
    override fun getStatistic(statistic: Statistic): Int {
        TODO("Not yet implemented")
    }
    
    override fun getStatistic(statistic: Statistic, material: Material): Int {
        TODO("Not yet implemented")
    }
    
    override fun getStatistic(statistic: Statistic, entityType: EntityType): Int {
        TODO("Not yet implemented")
    }
    
}