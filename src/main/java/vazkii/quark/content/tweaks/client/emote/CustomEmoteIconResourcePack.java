package vazkii.quark.content.tweaks.client.emote;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;

import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.quark.base.Quark;
import vazkii.quark.content.tweaks.module.EmotesModule;

@OnlyIn(Dist.CLIENT)
public class CustomEmoteIconResourcePack extends AbstractPackResources {

	public CustomEmoteIconResourcePack() {
		super("quark-emotes", true);
	}

	@Nonnull
	@Override
	public Set<String> getNamespaces(@Nonnull PackType type) {
		if (type == PackType.CLIENT_RESOURCES)
			return ImmutableSet.of(EmoteHandler.CUSTOM_EMOTE_NAMESPACE);
		return ImmutableSet.of();
	}


	@Override
	@Nullable
	public IoSupplier<InputStream> getRootResource(String... p_252049_) {
		for(String name : p_252049_) {
			if(name.equals("pack.mcmeta"))
				return () -> Quark.class.getResourceAsStream("/proxypack.mcmeta");

			if(name.equals("pack.png"))
				return () -> Quark.class.getResourceAsStream("/proxypack.png");
		}
		 
		return null;
	}

	// TODO: 1.19.4 resource packs
	
	@Override
	@Nullable
	public IoSupplier<InputStream> getResource(PackType type, ResourceLocation res) {
		if(type == PackType.CLIENT_RESOURCES)
			return stream(getFile(res.getPath()));
			
		return null;
	}

	@Nonnull
	@Override
	public void listResources(@Nonnull PackType type, @Nonnull String pathIn, @Nonnull String idk, PackResources.ResourceOutput output) {
		File rootPath = new File(EmotesModule.emotesDir, type.getDirectory());

		for (String namespace : this.getNamespaces(type))
			this.crawl(new File(new File(rootPath, namespace), pathIn), 32, namespace, pathIn + "/", output);
	}

	private void crawl(File rootPath, int maxDepth, String namespace, String path, PackResources.ResourceOutput output) {
		File[] files = rootPath.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					if (maxDepth > 0)
						this.crawl(file, maxDepth - 1, namespace, path + file.getName() + "/", output);
				} else if (!file.getName().endsWith(".mcmeta")) {
					try {
						ResourceLocation res = new ResourceLocation(namespace, path + file.getName());
						output.accept(res, stream(file));
					} catch (ResourceLocationException e) {
						Quark.LOG.error(e.getMessage());
					}
				}
			}
		}
	}

	@Override
	public void close() {
		// NO-OP
	}

	private File getFile(String name) {
		String filename = name.substring(name.indexOf(":") + 1) + ".png";
		filename = filename.replaceAll("(.+/)+", "");

		return new File(EmotesModule.emotesDir, filename);
	}
	
	private IoSupplier<InputStream> stream(File file) {
		if(!file.exists())
			return null;
		return () -> new FileInputStream(file);
	}

	@Override
	public boolean isHidden() {
		return true;
	}

}
