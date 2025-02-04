/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.bukkit

import org.bukkit.EntityEffect
import org.bukkit.Location
import org.bukkit.Server
import org.bukkit.World
import org.bukkit.block.BlockFace
import org.bukkit.block.PistonMoveReaction
import org.bukkit.entity.EntityType
import org.bukkit.entity.Pose
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.metadata.MetadataValue
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionAttachment
import org.bukkit.permissions.PermissionAttachmentInfo
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.plugin.Plugin
import org.bukkit.util.BoundingBox
import org.bukkit.util.Vector
import org.karrat.entity.Entity
import java.util.*

public open class EntityDelegate(public val underlyingEntity: Entity) : BukkitEntity {
    
    override fun setMetadata(metadataKey: String, newMetadataValue: MetadataValue) {
        TODO("Not yet implemented")
    }
    
    override fun getMetadata(metadataKey: String): MutableList<MetadataValue> {
        TODO("Not yet implemented")
    }
    
    override fun hasMetadata(metadataKey: String): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun removeMetadata(metadataKey: String, owningPlugin: Plugin) {
        TODO("Not yet implemented")
    }
    
    override fun isOp(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun setOp(value: Boolean) {
        TODO("Not yet implemented")
    }
    
    override fun isPermissionSet(name: String): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun isPermissionSet(perm: Permission): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun hasPermission(name: String): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun hasPermission(perm: Permission): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun addAttachment(plugin: Plugin, name: String, value: Boolean): PermissionAttachment {
        TODO("Not yet implemented")
    }
    
    override fun addAttachment(plugin: Plugin): PermissionAttachment {
        TODO("Not yet implemented")
    }
    
    override fun addAttachment(plugin: Plugin, name: String, value: Boolean, ticks: Int): PermissionAttachment? {
        TODO("Not yet implemented")
    }
    
    override fun addAttachment(plugin: Plugin, ticks: Int): PermissionAttachment? {
        TODO("Not yet implemented")
    }
    
    override fun removeAttachment(attachment: PermissionAttachment) {
        TODO("Not yet implemented")
    }
    
    override fun recalculatePermissions() {
        TODO("Not yet implemented")
    }
    
    override fun getEffectivePermissions(): MutableSet<PermissionAttachmentInfo> {
        TODO("Not yet implemented")
    }
    
    override fun sendMessage(message: String) {
        TODO("Not yet implemented")
    }
    
    override fun sendMessage(vararg messages: String?) {
        TODO("Not yet implemented")
    }
    
    override fun sendMessage(sender: UUID?, message: String) {
        TODO("Not yet implemented")
    }
    
    override fun sendMessage(sender: UUID?, vararg messages: String?) {
        TODO("Not yet implemented")
    }
    
    override fun getServer(): Server {
        TODO("Not yet implemented")
    }
    
    override fun getName(): String {
        TODO("Not yet implemented")
    }
    
    override fun spigot(): BukkitEntitySpigot {
        TODO("Not yet implemented")
    }
    
    override fun getCustomName(): String? {
        TODO("Not yet implemented")
    }
    
    override fun setCustomName(name: String?) {
        TODO("Not yet implemented")
    }
    
    override fun getPersistentDataContainer(): PersistentDataContainer {
        TODO("Not yet implemented")
    }
    
    override fun getLocation(): Location {
        TODO("Not yet implemented")
    }
    
    override fun getLocation(loc: Location?): Location? {
        TODO("Not yet implemented")
    }
    
    override fun setVelocity(velocity: Vector) {
        TODO("Not yet implemented")
    }
    
    override fun getVelocity(): Vector {
        TODO("Not yet implemented")
    }
    
    override fun getHeight(): Double {
        TODO("Not yet implemented")
    }
    
    override fun getWidth(): Double {
        TODO("Not yet implemented")
    }
    
    override fun getBoundingBox(): BoundingBox {
        TODO("Not yet implemented")
    }
    
    override fun isOnGround(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun isInWater(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun getWorld(): World {
        TODO("Not yet implemented")
    }
    
    override fun setRotation(yaw: Float, pitch: Float) {
        TODO("Not yet implemented")
    }
    
    override fun teleport(location: Location): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun teleport(location: Location, cause: PlayerTeleportEvent.TeleportCause): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun teleport(destination: BukkitEntity): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun teleport(destination: BukkitEntity, cause: PlayerTeleportEvent.TeleportCause): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun getNearbyEntities(x: Double, y: Double, z: Double): MutableList<BukkitEntity> {
        TODO("Not yet implemented")
    }
    
    override fun getEntityId(): Int {
        TODO("Not yet implemented")
    }
    
    override fun getFireTicks(): Int {
        TODO("Not yet implemented")
    }
    
    override fun getMaxFireTicks(): Int {
        TODO("Not yet implemented")
    }
    
    override fun setFireTicks(ticks: Int) {
        TODO("Not yet implemented")
    }
    
    override fun setVisualFire(fire: Boolean) {
        TODO("Not yet implemented")
    }
    
    override fun isVisualFire(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun getFreezeTicks(): Int {
        TODO("Not yet implemented")
    }
    
    override fun getMaxFreezeTicks(): Int {
        TODO("Not yet implemented")
    }
    
    override fun setFreezeTicks(ticks: Int) {
        TODO("Not yet implemented")
    }
    
    override fun isFrozen(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun remove() {
        TODO("Not yet implemented")
    }
    
    override fun isDead(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun isValid(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun isPersistent(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun setPersistent(persistent: Boolean) {
        TODO("Not yet implemented")
    }
    
    override fun getPassenger(): BukkitEntity? {
        TODO("Not yet implemented")
    }
    
    override fun setPassenger(passenger: BukkitEntity): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun getPassengers(): MutableList<BukkitEntity> {
        TODO("Not yet implemented")
    }
    
    override fun addPassenger(passenger: BukkitEntity): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun removePassenger(passenger: BukkitEntity): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun eject(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun getFallDistance(): Float {
        TODO("Not yet implemented")
    }
    
    override fun setFallDistance(distance: Float) {
        TODO("Not yet implemented")
    }
    
    override fun setLastDamageCause(event: EntityDamageEvent?) {
        TODO("Not yet implemented")
    }
    
    override fun getLastDamageCause(): EntityDamageEvent? {
        TODO("Not yet implemented")
    }
    
    override fun getUniqueId(): UUID {
        TODO("Not yet implemented")
    }
    
    override fun getTicksLived(): Int {
        TODO("Not yet implemented")
    }
    
    override fun setTicksLived(value: Int) {
        TODO("Not yet implemented")
    }
    
    override fun playEffect(type: EntityEffect) {
        TODO("Not yet implemented")
    }
    
    override fun getType(): EntityType {
        TODO("Not yet implemented")
    }
    
    override fun isInsideVehicle(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun leaveVehicle(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun getVehicle(): BukkitEntity? {
        TODO("Not yet implemented")
    }
    
    override fun setCustomNameVisible(flag: Boolean) {
        TODO("Not yet implemented")
    }
    
    override fun isCustomNameVisible(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun setGlowing(flag: Boolean) {
        TODO("Not yet implemented")
    }
    
    override fun isGlowing(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun setInvulnerable(flag: Boolean) {
        TODO("Not yet implemented")
    }
    
    override fun isInvulnerable(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun isSilent(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun setSilent(flag: Boolean) {
        TODO("Not yet implemented")
    }
    
    override fun hasGravity(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun setGravity(gravity: Boolean) {
        TODO("Not yet implemented")
    }
    
    override fun getPortalCooldown(): Int {
        TODO("Not yet implemented")
    }
    
    override fun setPortalCooldown(cooldown: Int) {
        TODO("Not yet implemented")
    }
    
    override fun getScoreboardTags(): MutableSet<String> {
        TODO("Not yet implemented")
    }
    
    override fun addScoreboardTag(tag: String): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun removeScoreboardTag(tag: String): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun getPistonMoveReaction(): PistonMoveReaction {
        TODO("Not yet implemented")
    }
    
    override fun getFacing(): BlockFace {
        TODO("Not yet implemented")
    }
    
    override fun getPose(): Pose {
        TODO("Not yet implemented")
    }
    
}