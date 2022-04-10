/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.bukkit

import org.bukkit.*
import org.bukkit.advancement.Advancement
import org.bukkit.block.data.BlockData
import org.bukkit.boss.*
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.command.PluginCommand
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.generator.ChunkGenerator
import org.bukkit.help.HelpMap
import org.bukkit.inventory.*
import org.bukkit.loot.LootTable
import org.bukkit.map.MapView
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.ServicesManager
import org.bukkit.plugin.messaging.Messenger
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.scoreboard.ScoreboardManager
import org.bukkit.structure.StructureManager
import org.bukkit.util.CachedServerIcon
import org.karrat.Config
import org.karrat.Server
import org.karrat.bukkit.OfflinePlayer
import org.karrat.entity.getByNameOrNull
import org.karrat.entity.getByUuidOrNull
import org.karrat.server.broadcast
import org.karrat.struct.Uuid
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import java.util.function.Consumer
import java.util.logging.Logger

public object ServerDelegate : BukkitServer {
    
    override fun sendPluginMessage(source: Plugin, channel: String, message: ByteArray) {
        println("${source.name}: ${message.decodeToString()}")
    }
    
    // Client-server communication channels
    override fun getListeningPluginChannels(): MutableSet<String> = TODO()
    
    override fun getName(): String = "Karrat/Bukkit"
    
    override fun getVersion(): String = Config.versionName
    
    override fun getBukkitVersion(): String = Config.versionName
    
    override fun getOnlinePlayers(): MutableCollection<Player> =
        Server.players.map { it.delegate() }.toMutableList()
    
    override fun getMaxPlayers(): Int = Config.maxPlayers
    
    override fun getPort(): Int = Config.port
    
    override fun getViewDistance(): Int = Config.viewDistance
    
    override fun getSimulationDistance(): Int = Config.simulationDistance
    
    // Not sure what this wants...
    override fun getIp(): String {
        TODO("Not yet implemented")
    }
    
    override fun getWorldType(): String {
        TODO("Not yet implemented")
    }
    
    override fun getGenerateStructures(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun getMaxWorldSize(): Int {
        TODO("Not yet implemented")
    }
    
    override fun getAllowEnd(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun getAllowNether(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun hasWhitelist(): Boolean = Config.isWhitelistOnly
    
    override fun setWhitelist(value: Boolean) {
        Config.isWhitelistOnly = value
    }
    
    override fun isWhitelistEnforced(): Boolean = Config.isWhitelistOnly
    
    override fun setWhitelistEnforced(value: Boolean) {
        Config.isWhitelistOnly = value
    }
    
    override fun getWhitelistedPlayers(): MutableSet<BukkitOfflinePlayer> =
        Config.whitelist.map { OfflinePlayer(it) }.toMutableSet()
    
    override fun reloadWhitelist() { /* Ignore */ }
    
    override fun broadcastMessage(message: String): Int {
        Server.broadcast(message)
        return Server.players.size
    }
    
    override fun getUpdateFolder(): String {
        TODO("Not yet implemented")
    }
    
    override fun getUpdateFolderFile(): File {
        TODO("Not yet implemented")
    }
    
    override fun getConnectionThrottle(): Long =
        Config.connectionThrottle.inWholeMilliseconds
    
    override fun getTicksPerAnimalSpawns(): Int {
        TODO("Not yet implemented")
    }
    
    override fun getTicksPerMonsterSpawns(): Int {
        TODO("Not yet implemented")
    }
    
    override fun getTicksPerWaterSpawns(): Int {
        TODO("Not yet implemented")
    }
    
    override fun getTicksPerWaterAmbientSpawns(): Int {
        TODO("Not yet implemented")
    }
    
    override fun getTicksPerWaterUndergroundCreatureSpawns(): Int {
        TODO("Not yet implemented")
    }
    
    override fun getTicksPerAmbientSpawns(): Int {
        TODO("Not yet implemented")
    }
    
    override fun getPlayer(name: String): BukkitPlayer? =
        Server.players.getByNameOrNull(name)?.delegate()
    
    override fun getPlayer(id: UUID): BukkitPlayer? =
        Server.players.getByUuidOrNull(Uuid.from(id))?.delegate()
    
    override fun getPlayerExact(name: String): BukkitPlayer? =
        Server.players.firstOrNull { it.name == name }?.delegate()
    
    override fun matchPlayer(name: String): MutableList<BukkitPlayer> {
        TODO("Not yet implemented")
    }
    
    override fun getPluginManager(): PluginManager {
        TODO("Not yet implemented")
    }
    
    override fun getScheduler(): BukkitScheduler {
        TODO("Not yet implemented")
    }
    
    override fun getServicesManager(): ServicesManager {
        TODO("Not yet implemented")
    }
    
    override fun getWorlds(): MutableList<BukkitWorld> {
        TODO("Not yet implemented")
    }
    
    override fun createWorld(creator: WorldCreator): BukkitWorld? {
        TODO("Not yet implemented")
    }
    
    override fun unloadWorld(name: String, save: Boolean): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun unloadWorld(world: BukkitWorld, save: Boolean): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun getWorld(name: String): BukkitWorld? =
        Server.worlds.firstOrNull { it.name == name }?.delegate()
    
    override fun getWorld(uid: UUID): BukkitWorld? =
        TODO("World id?")
    
    override fun getMap(id: Int): MapView? {
        TODO("Not yet implemented")
    }
    
    override fun createMap(world: BukkitWorld): MapView {
        TODO("Not yet implemented")
    }
    
    override fun createExplorerMap(world: BukkitWorld, location: Location, structureType: StructureType): ItemStack {
        TODO("Not yet implemented")
    }
    
    override fun createExplorerMap(
        world: BukkitWorld,
        location: Location,
        structureType: StructureType,
        radius: Int,
        findUnexplored: Boolean
    ): ItemStack {
        TODO("Not yet implemented")
    }
    
    override fun reload() {
        TODO("Not yet implemented")
    }
    
    override fun reloadData() {
        TODO("Not yet implemented")
    }
    
    override fun getLogger(): Logger {
        TODO("Not yet implemented")
    }
    
    override fun getPluginCommand(name: String): PluginCommand? {
        TODO("Not yet implemented")
    }
    
    override fun savePlayers() {
        TODO("Not yet implemented")
    }
    
    override fun dispatchCommand(sender: CommandSender, commandLine: String): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun addRecipe(recipe: Recipe?): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun getRecipesFor(result: ItemStack): MutableList<Recipe> {
        TODO("Not yet implemented")
    }
    
    override fun getRecipe(recipeKey: NamespacedKey): Recipe? {
        TODO("Not yet implemented")
    }
    
    override fun getCraftingRecipe(craftingMatrix: Array<out ItemStack>, world: BukkitWorld): Recipe? {
        TODO("Not yet implemented")
    }
    
    override fun craftItem(craftingMatrix: Array<out ItemStack>, world: BukkitWorld, player: Player): ItemStack {
        TODO("Not yet implemented")
    }
    
    override fun recipeIterator(): MutableIterator<Recipe> {
        TODO("Not yet implemented")
    }
    
    override fun clearRecipes() {
        TODO("Not yet implemented")
    }
    
    override fun resetRecipes() {
        TODO("Not yet implemented")
    }
    
    override fun removeRecipe(key: NamespacedKey): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun getCommandAliases(): MutableMap<String, Array<String>> {
        TODO("Not yet implemented")
    }
    
    override fun getSpawnRadius(): Int {
        TODO("Not yet implemented")
    }
    
    override fun setSpawnRadius(value: Int) {
        TODO("Not yet implemented")
    }
    
    override fun getHideOnlinePlayers(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun getOnlineMode(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun getAllowFlight(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun isHardcore(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun shutdown(): Unit =
        Server.stop()
    
    override fun broadcast(message: String, permission: String): Int {
        TODO("Not yet implemented")
    }
    
    override fun getOfflinePlayer(name: String): OfflinePlayer {
        TODO("Not yet implemented")
    }
    
    override fun getOfflinePlayer(id: UUID): OfflinePlayer {
        TODO("Not yet implemented")
    }
    
    override fun getIPBans(): MutableSet<String> {
        TODO("Not yet implemented")
    }
    
    override fun banIP(address: String) {
        TODO("Not yet implemented")
    }
    
    override fun unbanIP(address: String) {
        TODO("Not yet implemented")
    }
    
    override fun getBannedPlayers(): MutableSet<OfflinePlayer> {
        TODO("Not yet implemented")
    }
    
    override fun getBanList(type: BanList.Type): BanList {
        TODO("Not yet implemented")
    }
    
    override fun getOperators(): MutableSet<OfflinePlayer> {
        TODO("Not yet implemented")
    }
    
    override fun getDefaultGameMode(): GameMode {
        TODO("Not yet implemented")
    }
    
    override fun setDefaultGameMode(mode: GameMode) {
        TODO("Not yet implemented")
    }
    
    override fun getConsoleSender(): ConsoleCommandSender {
        TODO("Not yet implemented")
    }
    
    override fun getWorldContainer(): File {
        TODO("Not yet implemented")
    }
    
    override fun getOfflinePlayers(): Array<OfflinePlayer> {
        TODO("Not yet implemented")
    }
    
    override fun getMessenger(): Messenger {
        TODO("Not yet implemented")
    }
    
    override fun getHelpMap(): HelpMap {
        TODO("Not yet implemented")
    }
    
    override fun createInventory(owner: InventoryHolder?, type: InventoryType): Inventory {
        TODO("Not yet implemented")
    }
    
    override fun createInventory(owner: InventoryHolder?, type: InventoryType, title: String): Inventory {
        TODO("Not yet implemented")
    }
    
    override fun createInventory(owner: InventoryHolder?, size: Int): Inventory {
        TODO("Not yet implemented")
    }
    
    override fun createInventory(owner: InventoryHolder?, size: Int, title: String): Inventory {
        TODO("Not yet implemented")
    }
    
    override fun createMerchant(title: String?): Merchant {
        TODO("Not yet implemented")
    }
    
    override fun getMonsterSpawnLimit(): Int {
        TODO("Not yet implemented")
    }
    
    override fun getAnimalSpawnLimit(): Int {
        TODO("Not yet implemented")
    }
    
    override fun getWaterAnimalSpawnLimit(): Int {
        TODO("Not yet implemented")
    }
    
    override fun getWaterAmbientSpawnLimit(): Int {
        TODO("Not yet implemented")
    }
    
    override fun getWaterUndergroundCreatureSpawnLimit(): Int {
        TODO("Not yet implemented")
    }
    
    override fun getAmbientSpawnLimit(): Int {
        TODO("Not yet implemented")
    }
    
    override fun isPrimaryThread(): Boolean =
        Thread.currentThread().id == Config.mainThreadId
    
    override fun getMotd(): String = Config.motd
    
    override fun getShutdownMessage(): String? {
        TODO("Not yet implemented")
    }
    
    override fun getWarningState(): Warning.WarningState {
        TODO("Not yet implemented")
    }
    
    override fun getItemFactory(): ItemFactory {
        TODO("Not yet implemented")
    }
    
    override fun getScoreboardManager(): ScoreboardManager? {
        TODO("Not yet implemented")
    }
    
    override fun getServerIcon(): CachedServerIcon? {
        TODO("Not yet implemented")
    }
    
    override fun loadServerIcon(file: File): CachedServerIcon {
        TODO("Not yet implemented")
    }
    
    override fun loadServerIcon(image: BufferedImage): CachedServerIcon {
        TODO("Not yet implemented")
    }
    
    override fun setIdleTimeout(threshold: Int) {
        TODO("Not yet implemented")
    }
    
    override fun getIdleTimeout(): Int {
        TODO("Not yet implemented")
    }
    
    override fun createChunkData(world: BukkitWorld): ChunkGenerator.ChunkData {
        TODO("Not yet implemented")
    }
    
    override fun createBossBar(title: String?, color: BarColor, style: BarStyle, vararg flags: BarFlag?): BossBar {
        TODO("Not yet implemented")
    }
    
    override fun createBossBar(
        key: NamespacedKey,
        title: String?,
        color: BarColor,
        style: BarStyle,
        vararg flags: BarFlag?
    ): KeyedBossBar {
        TODO("Not yet implemented")
    }
    
    override fun getBossBars(): MutableIterator<KeyedBossBar> {
        TODO("Not yet implemented")
    }
    
    override fun getBossBar(key: NamespacedKey): KeyedBossBar? {
        TODO("Not yet implemented")
    }
    
    override fun removeBossBar(key: NamespacedKey): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun getEntity(uuid: UUID): Entity? {
        TODO("Not yet implemented")
    }
    
    override fun getAdvancement(key: NamespacedKey): Advancement? {
        TODO("Not yet implemented")
    }
    
    override fun advancementIterator(): MutableIterator<Advancement> {
        TODO("Not yet implemented")
    }
    
    override fun createBlockData(material: Material): BlockData {
        TODO("Not yet implemented")
    }
    
    override fun createBlockData(material: Material, consumer: Consumer<BlockData>?): BlockData {
        TODO("Not yet implemented")
    }
    
    override fun createBlockData(data: String): BlockData {
        TODO("Not yet implemented")
    }
    
    override fun createBlockData(material: Material?, data: String?): BlockData {
        TODO("Not yet implemented")
    }
    
    override fun <T : Keyed?> getTag(registry: String, tag: NamespacedKey, clazz: Class<T>): Tag<T>? {
        TODO("Not yet implemented")
    }
    
    override fun <T : Keyed?> getTags(registry: String, clazz: Class<T>): MutableIterable<Tag<T>> {
        TODO("Not yet implemented")
    }
    
    override fun getLootTable(key: NamespacedKey): LootTable? {
        TODO("Not yet implemented")
    }
    
    override fun selectEntities(sender: CommandSender, selector: String): MutableList<Entity> {
        TODO("Not yet implemented")
    }
    
    override fun getStructureManager(): StructureManager {
        TODO("Not yet implemented")
    }
    
    override fun getUnsafe(): UnsafeValues {
        TODO("Not yet implemented")
    }
    
    override fun spigot(): BukkitServerSpigot {
        TODO("Not yet implemented")
    }
    
}