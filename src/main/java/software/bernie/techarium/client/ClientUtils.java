package software.bernie.techarium.client;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.client.tile.model.MagneticCoilModel;
import software.bernie.techarium.client.tile.render.ArboretumRenderer;
import software.bernie.techarium.client.tile.render.BotariumRenderer;
import software.bernie.techarium.client.tile.render.ExchangeStationRenderer
import software.bernie.techarium.client.tile.render.GravMagnetRenderer;
import software.bernie.techarium.client.tile.render.MagneticCoilRenderer;
import software.bernie.techarium.client.tile.render.VoltaicPileRenderer;
import software.bernie.techarium.client.tile.render.ExchangeStationRenderer;
import software.bernie.techarium.registry.BlockRegistry;

@UtilityClass
@Mod.EventBusSubscriber(modid = Techarium.ModID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientUtils {
	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		registerTileRenderers();
	}

	public static void registerTileRenderers() {
		ClientRegistry.bindTileEntityRenderer(BlockRegistry.BOTARIUM.getTileEntityType(), BotariumRenderer::new);
		ClientRegistry.bindTileEntityRenderer(BlockRegistry.ARBORETUM.getTileEntityType(), ArboretumRenderer::new);
		ClientRegistry.bindTileEntityRenderer(BlockRegistry.EXCHANGE_STATION.getTileEntityType(), ExchangeStationRenderer::new);
		ClientRegistry.bindTileEntityRenderer(BlockRegistry.GRAVMAGNET.getTileEntityType(), GravMagnetRenderer::new);
		ClientRegistry.bindTileEntityRenderer(BlockRegistry.MAGNETIC_COIL.getTileEntityType(), MagneticCoilRenderer::new);
		ClientRegistry.bindTileEntityRenderer(BlockRegistry.VOLTAIC_PILE.getTileEntityType(), VoltaicPileRenderer::new);
	}

	@SubscribeEvent
	public static void modelBakeEvent(ModelBakeEvent event) {

	}

	public static boolean isShift() {
		return InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 340)
			|| InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 344);
	}

	public static boolean isAdvancedItem() {
		return Minecraft.getInstance().options.advancedItemTooltips;
	}
}
