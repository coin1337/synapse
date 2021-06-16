package com.ragemachine.moon.impl.client.modules.misc;

import com.ragemachine.moon.api.event.events.PacketEvent;
import com.ragemachine.moon.impl.client.command.Command;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.TextUtil;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketEnchantItem;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlaceRecipe;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketRecipeInfo;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.client.CPacketSeenAdvancements;
import net.minecraft.network.play.client.CPacketSpectate;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketAdvancementInfo;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketCamera;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.network.play.server.SPacketCombatEvent;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketCooldown;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.network.play.server.SPacketDisplayObjective;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.network.play.server.SPacketEntityHeadLook;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketKeepAlive;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketPlaceGhostRecipe;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRecipeBook;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.network.play.server.SPacketResourcePackSend;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketScoreboardObjective;
import net.minecraft.network.play.server.SPacketSelectAdvancementsTab;
import net.minecraft.network.play.server.SPacketServerDifficulty;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketSignEditorOpen;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.network.play.server.SPacketStatistics;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.network.play.server.SPacketUnloadChunk;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.network.play.server.SPacketUpdateScore;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.network.play.server.SPacketWindowProperty;
import net.minecraft.network.play.server.SPacketWorldBorder;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PacketCanceller extends Module {

    private Value<Mode> mode = addSetting(new Value("Packets", Mode.CLIENT));
    private Value<Integer> page = addSetting(new Value("SPackets",1, 1, 10, v -> mode.getValue() == Mode.SERVER));
    private Value<Integer> pages = addSetting(new Value("CPackets", 1, 1, 4, v -> mode.getValue() == Mode.CLIENT));

    private Value<Boolean> AdvancementInfo = addSetting(new Value("AdvancementInfo", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 1));
    private Value<Boolean> Animation = addSetting(new Value("Animation", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 1));
    private Value<Boolean> BlockAction = addSetting(new Value("BlockAction", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 1));
    private Value<Boolean> BlockBreakAnim = addSetting(new Value("BlockBreakAnim", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 1));
    private Value<Boolean> BlockChange = addSetting(new Value("BlockChange", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 1));
    private Value<Boolean> Camera = addSetting(new Value("Camera", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 1));
    private Value<Boolean> ChangeGameState = addSetting(new Value("ChangeGameState", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 1));
    private Value<Boolean> Chat = addSetting(new Value("Chat", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 1));

    private Value<Boolean> ChunkData = addSetting(new Value("ChunkData", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 2));
    private Value<Boolean> CloseWindow = addSetting(new Value("CloseWindow", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 2));
    private Value<Boolean> CollectItem = addSetting(new Value("CollectItem", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 2));
    private Value<Boolean> CombatEvent = addSetting(new Value("Combatevent", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 2));
    private Value<Boolean> ConfirmTransaction = addSetting(new Value("ConfirmTransaction", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 2));
    private Value<Boolean> Cooldown = addSetting(new Value("Cooldown", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 2));
    private Value<Boolean> CustomPayload = addSetting(new Value("CustomPayload", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 2));
    private Value<Boolean> CustomSound = addSetting(new Value("CustomSound", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 2));

    private Value<Boolean> DestroyEntities = addSetting(new Value("DestroyEntities", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 3));
    private Value<Boolean> Disconnect = addSetting(new Value("Disconnect", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 3));
    private Value<Boolean> DisplayObjective = addSetting(new Value("DisplayObjective", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 3));
    private Value<Boolean> Effect = addSetting(new Value("Effect", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 3));
    private Value<Boolean> Entity = addSetting(new Value("Entity", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 3));
    private Value<Boolean> EntityAttach = addSetting(new Value("EntityAttach", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 3));
    private Value<Boolean> EntityEffect = addSetting(new Value("EntityEffect", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 3));
    private Value<Boolean> EntityEquipment = addSetting(new Value("EntityEquipment", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 3));

    private Value<Boolean> EntityHeadLook = addSetting(new Value("EntityHeadLook", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 4));
    private Value<Boolean> EntityMetadata = addSetting(new Value("EntityMetadata", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 4));
    private Value<Boolean> EntityProperties = addSetting(new Value("EntityProperties", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 4));
    private Value<Boolean> EntityStatus = addSetting(new Value("EntityStatus", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 4));
    private Value<Boolean> EntityTeleport = addSetting(new Value("EntityTeleport", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 4));
    private Value<Boolean> EntityVelocity = addSetting(new Value("EntityVelocity", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 4));
    private Value<Boolean> Explosion = addSetting(new Value("Explosion", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 4));
    private Value<Boolean> HeldItemChange = addSetting(new Value("HeldItemChange", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 4));

    private Value<Boolean> JoinGame = addSetting(new Value("JoinGame", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 5));
    private Value<Boolean> KeepAlive = addSetting(new Value("KeepAlive", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 5));
    private Value<Boolean> Maps = addSetting(new Value("Maps", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 5));
    private Value<Boolean> MoveVehicle = addSetting(new Value("MoveVehicle", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 5));
    private Value<Boolean> MultiBlockChange = addSetting(new Value("MultiBlockChange", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 5));
    private Value<Boolean> OpenWindow = addSetting(new Value("OpenWindow", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 5));
    private Value<Boolean> Particles = addSetting(new Value("Particles", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 5));
    private Value<Boolean> PlaceGhostRecipe = addSetting(new Value("PlaceGhostRecipe", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 5));

    private Value<Boolean> PlayerAbilities = addSetting(new Value("PlayerAbilities", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 6));
    private Value<Boolean> PlayerListHeaderFooter = addSetting(new Value("PlayerListHeaderFooter", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 6));
    private Value<Boolean> PlayerListItem = addSetting(new Value("PlayerListItem", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 6));
    private Value<Boolean> PlayerPosLook = addSetting(new Value("PlayerPosLook", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 6));
    private Value<Boolean> RecipeBook = addSetting(new Value("RecipeBook", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 6));
    private Value<Boolean> RemoveEntityEffect = addSetting(new Value("RemoveEntityEffect", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 6));
    private Value<Boolean> ResourcePackSend = addSetting(new Value("ResourcePackSend", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 6));
    private Value<Boolean> Respawn = addSetting(new Value("Respawn", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 6));

    private Value<Boolean> ScoreboardObjective = addSetting(new Value("ScoreboardObjective", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 7));
    private Value<Boolean> SelectAdvancementsTab = addSetting(new Value("SelectAdvancementsTab", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 7));
    private Value<Boolean> ServerDifficulty = addSetting(new Value("ServerDifficulty", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 7));
    private Value<Boolean> SetExperience = addSetting(new Value("SetExperience", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 7));
    private Value<Boolean> SetPassengers = addSetting(new Value("SetPassengers", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 7));
    private Value<Boolean> SetSlot = addSetting(new Value("SetSlot", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 7));
    private Value<Boolean> SignEditorOpen = addSetting(new Value("SignEditorOpen", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 7));
    private Value<Boolean> SoundEffect = addSetting(new Value("SoundEffect", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 7));

    private Value<Boolean> SpawnExperienceOrb = addSetting(new Value("SpawnExperienceOrb", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 8));
    private Value<Boolean> SpawnGlobalEntity = addSetting(new Value("SpawnGlobalEntity", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 8));
    private Value<Boolean> SpawnMob = addSetting(new Value("SpawnMob", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 8));
    private Value<Boolean> SpawnObject = addSetting(new Value("SpawnObject", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 8));
    private Value<Boolean> SpawnPainting = addSetting(new Value("SpawnPainting", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 8));
    private Value<Boolean> SpawnPlayer = addSetting(new Value("SpawnPlayer", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 8));
    private Value<Boolean> SpawnPosition = addSetting(new Value("SpawnPosition", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 8));
    private Value<Boolean> Statistics = addSetting(new Value("Statistics", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 8));

    private Value<Boolean> TabComplete = addSetting(new Value("TabComplete", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 9));
    private Value<Boolean> Teams = addSetting(new Value("Teams", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 9));
    private Value<Boolean> TimeUpdate = addSetting(new Value("TimeUpdate", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 9));
    private Value<Boolean> Title = addSetting(new Value("Title", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 9));
    private Value<Boolean> UnloadChunk = addSetting(new Value("UnloadChunk", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 9));
    private Value<Boolean> UpdateBossInfo = addSetting(new Value("UpdateBossInfo", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 9));
    private Value<Boolean> UpdateHealth = addSetting(new Value("UpdateHealth", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 9));
    private Value<Boolean> UpdateScore = addSetting(new Value("UpdateScore", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 9));

    private Value<Boolean> UpdateTileEntity = addSetting(new Value("UpdateTileEntity", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 10));
    private Value<Boolean> UseBed = addSetting(new Value("UseBed", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 10));
    private Value<Boolean> WindowItems = addSetting(new Value("WindowItems", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 10));
    private Value<Boolean> WindowProperty = addSetting(new Value("WindowProperty", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 10));
    private Value<Boolean> WorldBorder = addSetting(new Value("WorldBorder", false, v -> mode.getValue() == Mode.SERVER && page.getValue() == 10));

    private Value<Boolean> Animations = addSetting(new Value("Animations", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 1));
    private Value<Boolean> ChatMessage = addSetting(new Value("ChatMessage", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 1));
    private Value<Boolean> ClickWindow = addSetting(new Value("ClickWindow", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 1));
    private Value<Boolean> clientSettings = addSetting(new Value("clientSettings", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 1));
    private Value<Boolean> ClientStatus = addSetting(new Value("ClientStatus", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 1));
    private Value<Boolean> CloseWindows = addSetting(new Value("CloseWindows", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 1));
    private Value<Boolean> ConfirmTeleport = addSetting(new Value("ConfirmTeleport", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 1));
    private Value<Boolean> ConfirmTransactions = addSetting(new Value("ConfirmTransactions", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 1));

    private Value<Boolean> CreativeInventoryAction = addSetting(new Value("CreativeInventoryAction", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 2));
    private Value<Boolean> CustomPayloads = addSetting(new Value("CustomPayloads", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 2));
    private Value<Boolean> EnchantItem = addSetting(new Value("EnchantItem", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 2));
    private Value<Boolean> EntityAction = addSetting(new Value("EntityAction", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 2));
    private Value<Boolean> HeldItemChanges = addSetting(new Value("HeldItemChanges", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 2));
    private Value<Boolean> Input = addSetting(new Value("Input", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 2));
    private Value<Boolean> KeepAlives = addSetting(new Value("KeepAlives", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 2));
    private Value<Boolean> PlaceRecipe = addSetting(new Value("PlaceRecipe", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 2));

    private Value<Boolean> Player = addSetting(new Value("Player", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 3));
    private Value<Boolean> PlayerAbility = addSetting(new Value("PlayerAbility", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 3));
    private Value<Boolean> PlayerDigging = addSetting(new Value("PlayerDigging", false, v -> mode.getValue() == Mode.CLIENT && page.getValue() == 3));
    private Value<Boolean> PlayerTryUseItem = addSetting(new Value("PlayerTryUseItem", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 3));
    private Value<Boolean> PlayerTryUseItemOnBlock = addSetting(new Value("TryUseItemOnBlock", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 3));
    private Value<Boolean> RecipeInfo = addSetting(new Value("RecipeInfo", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 3));
    private Value<Boolean> ResourcePackStatus = addSetting(new Value("ResourcePackStatus", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 3));
    private Value<Boolean> SeenAdvancements = addSetting(new Value("SeenAdvancements", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 3));

    private Value<Boolean> PlayerPackets = addSetting(new Value("PlayerPackets", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 4));
    private Value<Boolean> Spectate = addSetting(new Value("Spectate", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 4));
    private Value<Boolean> SteerBoat = addSetting(new Value("SteerBoat", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 4));
    private Value<Boolean> TabCompletion = addSetting(new Value("TabCompletion", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 4));
    private Value<Boolean> UpdateSign = addSetting(new Value("UpdateSign", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 4));
    private Value<Boolean> UseEntity = addSetting(new Value("UseEntity", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 4));
    private Value<Boolean> VehicleMove = addSetting(new Value("VehicleMove", false, v -> mode.getValue() == Mode.CLIENT && pages.getValue() == 4));

    private int hudAmount = 0;

    public PacketCanceller() {
        super("PacketCanceller", "Blocks Packets", Category.MISC, true, false, false);
    }

    public enum Mode {
        CLIENT, SERVER
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {

        if (!this.isEnabled()) {
            return;
        }

        if (event.getPacket() instanceof CPacketAnimation && Animations.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketChatMessage && ChatMessage.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketClickWindow && ClickWindow.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketClientSettings && clientSettings.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketClientStatus && ClientStatus.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketCloseWindow && CloseWindows.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketConfirmTeleport && ConfirmTeleport.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketConfirmTransaction && ConfirmTransactions.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketCreativeInventoryAction && CreativeInventoryAction.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketCustomPayload && CustomPayloads.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketEnchantItem && EnchantItem.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketEntityAction && EntityAction.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketHeldItemChange && HeldItemChanges.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketInput && Input.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketKeepAlive && KeepAlives.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlaceRecipe && PlaceRecipe.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlayer && Player.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlayerAbilities && PlayerAbility.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlayerDigging && PlayerDigging.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlayerTryUseItem && PlayerTryUseItem.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && PlayerTryUseItemOnBlock.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketRecipeInfo && RecipeInfo.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketResourcePackStatus && ResourcePackStatus.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketSeenAdvancements && SeenAdvancements.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketSpectate && Spectate.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketSteerBoat && SteerBoat.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketTabComplete && TabCompletion.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketUpdateSign && UpdateSign.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketUseEntity && UseEntity.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketVehicleMove && VehicleMove.getValue()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {

        if (!this.isEnabled()) {
            return;
        }

        if (event.getPacket() instanceof SPacketAdvancementInfo && AdvancementInfo.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketAnimation && Animation.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketBlockAction && BlockAction.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketBlockBreakAnim && BlockBreakAnim.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketBlockChange && BlockChange.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCamera && Camera.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketChangeGameState && ChangeGameState.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketChat && Chat.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketChunkData && ChunkData.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCloseWindow && CloseWindow.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCollectItem && CollectItem.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCombatEvent && CombatEvent.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketConfirmTransaction && ConfirmTransaction.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCooldown && Cooldown.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCustomPayload && CustomPayload.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCustomSound && CustomSound.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketDestroyEntities && DestroyEntities.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketDisconnect && Disconnect.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketChunkData && ChunkData.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCloseWindow && CloseWindow.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCollectItem && CollectItem.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketDisplayObjective && DisplayObjective.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEffect && Effect.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntity && Entity.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityAttach && EntityAttach.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityEffect && EntityEffect.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityEquipment && EntityEquipment.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityHeadLook && EntityHeadLook.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityMetadata && EntityMetadata.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityProperties && EntityProperties.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityStatus && EntityStatus.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityTeleport && EntityTeleport.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityVelocity && EntityVelocity.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketExplosion && Explosion.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketHeldItemChange && HeldItemChange.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketJoinGame && JoinGame.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketKeepAlive && KeepAlive.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketMaps && Maps.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketMoveVehicle && MoveVehicle.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketMultiBlockChange && MultiBlockChange.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketOpenWindow && OpenWindow.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketParticles && Particles.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketPlaceGhostRecipe && PlaceGhostRecipe.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketPlayerAbilities && PlayerAbilities.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketPlayerListHeaderFooter && PlayerListHeaderFooter.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketPlayerListItem && PlayerListItem.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook && PlayerPosLook.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketRecipeBook && RecipeBook.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketRemoveEntityEffect && RemoveEntityEffect.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketResourcePackSend && ResourcePackSend.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketRespawn && Respawn.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketScoreboardObjective && ScoreboardObjective.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSelectAdvancementsTab && SelectAdvancementsTab.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketServerDifficulty && ServerDifficulty.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSetExperience && SetExperience.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSetPassengers && SetPassengers.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSetSlot && SetSlot.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSignEditorOpen && SignEditorOpen.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSoundEffect && SoundEffect.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnExperienceOrb && SpawnExperienceOrb.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnGlobalEntity && SpawnGlobalEntity.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnMob && SpawnMob.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnObject && SpawnObject.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnPainting && SpawnPainting.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnPlayer && SpawnPlayer.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnPosition && SpawnPosition.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketStatistics && Statistics.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketTabComplete && TabComplete.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketTeams && Teams.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketTimeUpdate && TimeUpdate.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketTitle && Title.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketUnloadChunk && UnloadChunk.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketUpdateBossInfo && UpdateBossInfo.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketUpdateHealth && UpdateHealth.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketUpdateScore && UpdateScore.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketUpdateTileEntity && UpdateTileEntity.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketUseBed && UseBed.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketWindowItems && WindowItems.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketWindowProperty && WindowProperty.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketWorldBorder && WorldBorder.getValue()) {
            event.setCanceled(true);
        }
    }

    @Override
    public void onEnable() {
        String standart = TextUtil.GREEN + "PacketCanceller On!" + TextUtil.WHITE + " Cancelled Packets: ";
        StringBuilder text = new StringBuilder(standart);
        if (!this.values.isEmpty()) {
            for (Value value : this.values) {
                if(!(value.getValue() instanceof Boolean) || !((boolean) value.getValue()) || value.getName().equalsIgnoreCase("Enabled") || value.getName().equalsIgnoreCase("drawn")) {
                    continue;
                }
                String name = value.getName();
                text.append(name).append(", ");
            }
        }

        if(text.toString().equals(standart)) {
            Command.sendMessage(TextUtil.GREEN + "PacketCanceller On!" + TextUtil.WHITE + " Currently not cancelling any Packets.");
        } else {
            String output = removeLastChar(removeLastChar(text.toString()));
            Command.sendMessage(output);
        }
    }

    @Override
    public void onUpdate() {
        int amount = 0;
        if (!this.values.isEmpty()) {
            for (Value value : this.values) {
                if (!(value.getValue() instanceof Boolean) || !((boolean) value.getValue()) || value.getName().equalsIgnoreCase("Enabled") || value.getName().equalsIgnoreCase("drawn")) {
                    continue;
                }
                amount++;
            }
        }
        hudAmount = amount;
    }

    @Override
    public String getDisplayInfo() {
        if (hudAmount == 0) {
            return "";
        }
        return hudAmount + (hudAmount == 1 ? " Packet" : " Packets");
    }

    public String removeLastChar(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
