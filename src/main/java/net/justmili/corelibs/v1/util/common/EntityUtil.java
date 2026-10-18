package net.justmili.corelibs.v1.util.common;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EntityUtil {

    public static void teleport(LivingEntity entity, ServerLevel level, double x, double y, double z, float rotY, float rotX, boolean resetCameraToBody) {
        entity.teleportTo(level, x, y, z, RelativeMovement.ALL, rotY, rotX);
    }

    public static void teleport(ServerPlayer player, double x, double y, double z, float rotY, float rotX) {
        player.connection.teleport(x, y, z, rotY, rotX);
    }

    public static void teleport(Player player, double x, double y, double z, float rotY, float rotX) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        teleport(serverPlayer, x, y, z, rotY, rotX);
    }

    public static void teleport(LivingEntity entity, Level level, double x, double y, double z, float rotY, float rotX, boolean resetCameraToBody) {
        teleport(entity, (ServerLevel) level, x, y, z, rotY, rotX, resetCameraToBody);
    }

    public static void teleport(LivingEntity entity, Level level, BlockPos pos, float rotY, float rotX, boolean resetCameraToBody) {
        teleport(entity, level, pos.getX(), pos.getY(), pos.getZ(), rotY, rotX, resetCameraToBody);
    }

    public static void teleport(LivingEntity entity, Level level, Vec3 pos, float rotY, float rotX, boolean resetCameraToBody) {
        teleport(entity, level, pos.x, pos.y, pos.z, rotY, rotX, resetCameraToBody);
    }

    public static void teleport(LivingEntity entity, ServerLevel level, double x, double y, double z, boolean resetCameraToBody) {
        teleport(entity, level, x, y, z, entity.getYRot(), entity.getXRot(), resetCameraToBody);
    }

    public static void teleport(LivingEntity entity, ServerLevel level, BlockPos pos, boolean resetCameraToBody) {
        teleport(entity, level, pos.getX(), pos.getY(), pos.getZ(), entity.getYRot(), entity.getXRot(), resetCameraToBody);
    }

    public static void teleport(LivingEntity entity, ServerLevel level, Vec3 pos, boolean resetCameraToBody) {
        teleport(entity, level, pos.x, pos.y, pos.z, entity.getYRot(), entity.getXRot(), resetCameraToBody);
    }

    public static void teleport(LivingEntity entity, Level level, double x, double y, double z, boolean resetCameraToBody) {
        teleport(entity, level, x, y, z, entity.getYRot(), entity.getXRot(), resetCameraToBody);
    }

    public static void teleport(LivingEntity entity, Level level, BlockPos pos, boolean resetCameraToBody) {
        teleport(entity, level, pos.getX(), pos.getY(), pos.getZ(), entity.getYRot(), entity.getXRot(), resetCameraToBody);
    }

    public static void teleport(LivingEntity entity, Level level, Vec3 pos, boolean resetCameraToBody) {
        teleport(entity, level, pos.x, pos.y, pos.z, entity.getYRot(), entity.getXRot(), resetCameraToBody);
    }

    public static void teleport(LivingEntity entity, ServerLevel level, double x, double y, double z, float rotY, float rotX) {
        teleport(entity, level, x, y, z, rotY, rotX, true);
    }

    public static void teleport(LivingEntity entity, ServerLevel level, BlockPos pos, float rotY, float rotX) {
        teleport(entity, level, pos.getX(), pos.getY(), pos.getZ(), rotY, rotX, true);
    }

    public static void teleport(LivingEntity entity, ServerLevel level, Vec3 pos, float rotY, float rotX) {
        teleport(entity, level, pos.x, pos.y, pos.z, rotY, rotX, true);
    }

    public static void teleport(LivingEntity entity, Level level, double x, double y, double z, float rotY, float rotX) {
        teleport(entity, level, x, y, z, rotY, rotX, true);
    }

    public static void teleport(LivingEntity entity, Level level, BlockPos pos, float rotY, float rotX) {
        teleport(entity, level, pos.getX(), pos.getY(), pos.getZ(), rotY, rotX, true);
    }

    public static void teleport(LivingEntity entity, Level level, Vec3 pos, float rotY, float rotX) {
        teleport(entity, level, pos.x, pos.y, pos.z, rotY, rotX, true);
    }

    public static void teleport(LivingEntity entity, ServerLevel level, double x, double y, double z) {
        teleport(entity, level, x, y, z, entity.getYRot(), entity.getXRot(), true);
    }

    public static void teleport(LivingEntity entity, ServerLevel level, BlockPos pos) {
        teleport(entity, level, pos.getX(), pos.getY(), pos.getZ(), entity.getYRot(), entity.getXRot(), true);
    }

    public static void teleport(LivingEntity entity, ServerLevel level, Vec3 pos) {
        teleport(entity, level, pos.x, pos.y, pos.z, entity.getYRot(), entity.getXRot(), true);
    }

    public static void teleport(LivingEntity entity, Level level, double x, double y, double z) {
        teleport(entity, level, x, y, z, entity.getYRot(), entity.getXRot(), true);
    }

    public static void teleport(LivingEntity entity, Level level, BlockPos pos) {
        teleport(entity, level, pos.getX(), pos.getY(), pos.getZ(), entity.getYRot(), entity.getXRot(), true);
    }

    public static void teleport(LivingEntity entity, Level level, Vec3 pos) {
        teleport(entity, level, pos.x, pos.y, pos.z, entity.getYRot(), entity.getXRot(), true);
    }

    public static void teleportToBedOrWorldSpawn(ServerPlayer player) {
        var respawnPos = player.getRespawnPosition();
        var respawnDim = player.getRespawnDimension();
        if (respawnPos == null) return;

        var targetLevel = player.server.getLevel(respawnDim);
        if (targetLevel == null) return;

        var bedPos = Player.findRespawnPositionAndUseSpawnBlock(targetLevel, respawnPos, 0, player.isRespawnForced(), false);
        if (bedPos.isPresent()) {
            teleport(player, targetLevel, bedPos.get(), 180, 0);
            return;
        }

        teleport(player, targetLevel, respawnPos.getX() + 0.5f, respawnPos.getY(), respawnPos.getZ() + 0.5f, 180, 0);
    }

    public static void applyEffect(LivingEntity entity, MobEffect effects, int duration, int power) {
        entity.addEffect(new MobEffectInstance(effects, duration, power, false, false, false));
    }

    public static boolean hasAdvancement(ServerPlayer player, ResourceLocation id) {
        var advancement = getAdvancement(player.server, id);
        if (advancement == null) return false;

        return getOrStartProgress(player, id).isDone();
    }

    public static void grantAdvancement(ServerPlayer player, ResourceLocation id) {
        var advancement = getAdvancement(player.server, id);
        if (advancement == null) return;

        var progress = getOrStartProgress(player, id);
        if (!progress.isDone()) for (String criteria : progress.getRemainingCriteria())
            player.getAdvancements().award(advancement, criteria);
    }

    public static void revokeAdvancement(ServerPlayer player, ResourceLocation id) {
        var advancement = getAdvancement(player.server, id);
        if (advancement == null) return;

        var progress = getOrStartProgress(player, id);
        if (progress.isDone()) for (String criteria : progress.getCompletedCriteria())
            player.getAdvancements().revoke(advancement, criteria);
    }

    private static Advancement getAdvancement(MinecraftServer server, ResourceLocation id) {
        return server.getAdvancements().getAdvancement(id);
    }

    private static AdvancementProgress getOrStartProgress(ServerPlayer player, ResourceLocation id) {
        return player.getAdvancements().getOrStartProgress(getAdvancement(player.server, id));
    }

    public static void useHeld(Player player, InteractionHand hand, int shrinkAmount) {
        if (!player.isCreative()) player.getItemInHand(hand).shrink(shrinkAmount);
    }

    public static void useHeld(Player player, InteractionHand hand) {
        useHeld(player, hand, 1);
    }

    public static void useHeldWithResult(Player player, InteractionHand hand, ItemLike result, boolean shrinkStack, int shrinkAmount) {
        var item = new ItemStack(result);

        if (shrinkStack) useHeld(player, hand, shrinkAmount);
        if (player.getItemInHand(hand).isEmpty()) {
            player.setItemInHand(hand, item);
        } else if (!player.getInventory().add(item)) {
            player.drop(item, false);
        }
    }

    public static void useHeldWithResult(Player player, InteractionHand hand, ItemLike result) {
        useHeldWithResult(player, hand, result, true, 1);
    }

    public static boolean isTouchingWall(Entity entity) {
        var boundingBox = entity.getBoundingBox();
        return hasBlockCollision(entity, boundingBox, 0.01, 0.0, 0.01)
            || hasBlockCollision(entity, boundingBox, -0.01, 0.0, -0.01);
    }

    public static boolean isTouchingCeiling(Entity entity) {
        return hasBlockCollision(entity, entity.getBoundingBox(), 0, 0.01, 0);
    }

    private static boolean hasBlockCollision(Entity entity, AABB boundingBox, double offsetX, double offsetY, double offsetZ) {
        var bbOffset = boundingBox.move(
            offsetX * boundingBox.getXsize(),
            offsetY * boundingBox.getYsize(),
            offsetZ * boundingBox.getZsize()
        );

        return entity.level().getBlockCollisions(entity, bbOffset).iterator().hasNext();
    }

    public record MobData(Class<?> entityClass, double range, double runSpeed) { }

    public static <T extends Mob> List<T> getNearby(ServerPlayer player, Class<T> mob, double radius) {
        return player.level().getEntitiesOfClass(mob, player.getBoundingBox().inflate(radius));
    }

    // For a list of entities within an area of a player
    public static <T extends Mob> void executeForNearby(ServerPlayer player, List<MobData> dataList, BiConsumer<T, MobData> action) {
        //noinspection unchecked
        dataList.forEach(data ->
            getNearby(player, data.entityClass().asSubclass(Mob.class), data.range()).forEach(mob -> action.accept((T) mob, data))
        );
    }

    // For a single entity
    public static <T extends Mob> void executeForNearby(ServerPlayer player, Class<?> entityClass, double range, Consumer<T> action) {
        //noinspection unchecked
        getNearby(player, entityClass.asSubclass(Mob.class), range).forEach(mob -> action.accept((T) mob));
    }

    public static <T extends Mob> void executeForNearby(ServerPlayer player, Class<?> entityClass, double range, double speed, BiConsumer<T, Double> action) {
        //noinspection unchecked
        getNearby(player, entityClass.asSubclass(Mob.class), range).forEach(mob -> action.accept((T) mob, speed));
    }
}