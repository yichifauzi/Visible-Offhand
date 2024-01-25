package com.movtery.visibleoffhand.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.movtery.visibleoffhand.VisibleOffhand.getConfig;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
    @Shadow
    protected abstract void renderPlayerArm(PoseStack pPoseStack, MultiBufferSource pBuffer, int pCombinedLight, float pEquippedProgress, float pSwingProgress, HumanoidArm pSide);

    @Inject(method = "renderArmWithItem", at = @At("HEAD"))
    private void doubleHands(AbstractClientPlayer pPlayer, float pPartialTicks, float pPitch, InteractionHand pHand, float pSwingProgress, ItemStack pStack, float pEquippedProgress, PoseStack pPoseStack, MultiBufferSource pBuffer, int pCombinedLight, CallbackInfo ci) {
        if (getConfig().getOptions().doubleHands) {
            boolean mainHand = pHand == InteractionHand.MAIN_HAND;
            HumanoidArm offArm = mainHand ? pPlayer.getMainArm() : pPlayer.getMainArm().getOpposite();
            if (pStack.isEmpty()) {
                if (!mainHand && !pPlayer.isInvisible()) {
                    this.renderPlayerArm(pPoseStack, pBuffer, pCombinedLight, pEquippedProgress, pSwingProgress, offArm);
                }
            }
        }
    }
}