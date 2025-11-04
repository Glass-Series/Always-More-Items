package net.glasslauncher.mods.alwaysmoreitems.plugins.multiblock;

import net.glasslauncher.mods.alwaysmoreitems.api.gui.AMIDrawable;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.gui.DrawableHelper;
import net.glasslauncher.mods.alwaysmoreitems.gui.RecipeLayout;
import net.glasslauncher.mods.alwaysmoreitems.gui.multiblock.InventoryBlockView;
import net.glasslauncher.mods.alwaysmoreitems.gui.multiblock.InventoryWorld;
import net.glasslauncher.mods.alwaysmoreitems.gui.screen.OverlayScreen;
import net.glasslauncher.mods.alwaysmoreitems.gui.screen.RecipesGui;
import net.glasslauncher.mods.alwaysmoreitems.recipe.multiblock.BlockPatternEntry;
import net.glasslauncher.mods.alwaysmoreitems.recipe.multiblock.MultiBlockRecipe;
import net.glasslauncher.mods.alwaysmoreitems.util.HoverChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.modificationstation.stationapi.api.block.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class MultiBlockRecipeWrapper implements RecipeWrapper {
    private static final int ARROW_Y = 13;

    private final AMIDrawable leftButton = DrawableHelper.createDrawable("/assets/alwaysmoreitems/stationapi/textures/gui/multiblock.png", 187, 0, 7, 11);
    private final AMIDrawable leftButtonHover = DrawableHelper.createDrawable("/assets/alwaysmoreitems/stationapi/textures/gui/multiblock.png", 187, 11, 7, 11);
    private final AMIDrawable rightButton = DrawableHelper.createDrawable("/assets/alwaysmoreitems/stationapi/textures/gui/multiblock.png", 194, 0, 7, 11);
    private final AMIDrawable rightButtonHover = DrawableHelper.createDrawable("/assets/alwaysmoreitems/stationapi/textures/gui/multiblock.png", 194, 11, 7, 11);

    private final MultiBlockRecipe recipe;

    private static final InventoryWorld world = new InventoryWorld();

    private HoverChecker leftButtonHoverChecker;
    private HoverChecker rightButtonHoverChecker;
    private int leftButtonX;

    float pitch = -45f;
    float yaw = -45f;
    float scale = -10f;

    boolean leftMouseDown = false;
    boolean rightMouseDown = false;

    int currentLayer = -1;
    public MultiBlockRecipeWrapper(MultiBlockRecipe recipe){
        this.recipe = recipe;
    }
    @Override
    public List<?> getInputs() {
        return recipe.getItems();
    }

    @Override
    public List<?> getOutputs() {
        return List.of();
    }

    private void loadRecipeStructure(InventoryWorld world, MultiBlockRecipe recipe){
        world.clear();
        List<String[]> layers = recipe.getLayers();
        int x = 0;
        int y = 0;
        int z = 0;
        for(String[] layer : layers){
            z = 0;
            for(String section : layer){
                x = 0;
                for(char key : section.toCharArray()){
                    BlockPatternEntry entry = recipe.getEntryForPattern(key);
                    if(entry != null){
                        world.setBlockStateWithMetadata(x, y, z, entry.blockstate(), entry.meta());
                    }
                    // TODO: warn for missing keys
                    else {

                    }
                    x++;
                }
                z++;
            }
            y++;
        }
    }

    @Override
    public void drawInfo(@NotNull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if(mouseX == 0 && mouseY == 0) return;
        if(!Mouse.isButtonDown(0) && leftMouseDown){
            leftMouseDown = false;
        }
        if(!Mouse.isButtonDown(1) && rightMouseDown){
            rightMouseDown = false;
        }
        if(leftMouseDown){
            pitch += Mouse.getDY();
            yaw += Mouse.getDX();
        }
        if(rightMouseDown){
            scale -= Mouse.getDY();
            scale = Math.min(-5f, scale);
        }

        if(leftButtonHoverChecker.isOver(mouseX, mouseY)){
            leftButtonHover.draw(minecraft, leftButtonX, ARROW_Y);
        }
        else {
            leftButton.draw(minecraft, leftButtonX, ARROW_Y);
        }

        if(rightButtonHoverChecker.isOver(mouseX, mouseY)){
            rightButtonHover.draw(minecraft, 162 - 7, ARROW_Y);
        }
        else {
            rightButton.draw(minecraft, 162 - 7, ARROW_Y);
        }
    }

    public List<ItemStack> getCost(){
        return recipe.getCost();
    }

    private String getLayerString(int layer){
        if(layer == -1) return "A";
        return String.valueOf(layer);
    }

    @Override
    public void drawAnimations(@NotNull Minecraft minecraft, int recipeWidth, int recipeHeight) {
        InventoryBlockView blockView = new InventoryBlockView(minecraft.world);
        BlockRenderManager blockRenderManager = new BlockRenderManager(blockView);

        RecipesGui recipesGui = OverlayScreen.INSTANCE.recipesGui;
        List<RecipeLayout> recipeLayouts = recipesGui.getRecipeLayouts();
        RecipeLayout recipeLayout = null;
        for(RecipeLayout r : recipeLayouts){
            if(r.getRecipeCategory() instanceof MultiBlockRecipeCategory){
                recipeLayout = r;
            }
        }

        float xScale = (float) minecraft.displayWidth / recipesGui.width;
        float yScale = (float) minecraft.displayHeight / recipesGui.height;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glPushMatrix();

        GL11.glTranslatef(81f,71f,180f);
        GL11.glRotatef(pitch, 1f, 0f, 0);
        GL11.glRotatef(yaw, 0f, 1f, 0);
        GL11.glScalef(scale, scale, scale);

        minecraft.textureManager.bindTexture(minecraft.textureManager.getTextureId("/terrain.png"));

        loadRecipeStructure(world, recipe);

        GL11.glScissor((int) ((recipeLayout.getPosX() + 1) * xScale), (int) (minecraft.displayHeight - ((recipeLayout.getPosY() + 128) * yScale)), (int)(160 * xScale), (int)(114 * yScale));
        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        Tessellator tessellator = Tessellator.INSTANCE;
        tessellator.startQuads();

        tessellator.setOffset(-(recipe.getStructureWidth() / 2f), -(recipe.getStructureHeight() / 2f), -(recipe.getStructureDepth() / 2f));

        world.setVisibleLayer(currentLayer);

        List<BlockPos> blockPositions = world.getBlockPositions();
        for(int renderLayer = 0; renderLayer < 2; renderLayer++){
            for(BlockPos blockPos : blockPositions){
                if(currentLayer == -1 || currentLayer == blockPos.y){
                    BlockState blockState = world.getBlockState(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                    if(blockState.getBlock().getRenderLayer() == renderLayer){
                        blockRenderManager.render(blockState.getBlock(), blockPos.getX(), blockPos.getY(), blockPos.getZ());
                    }
                }
            }
        }

        tessellator.draw();
        tessellator.setOffset(0.0, 0.0, 0.0);

        GL11.glPopMatrix();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        String layerText = "Layer: " + getLayerString(currentLayer);

        leftButtonX = 161 - 6 - 2 - minecraft.textRenderer.getWidth(layerText) - 7 - 2;
        leftButtonHoverChecker = new HoverChecker(ARROW_Y, 10 + ARROW_Y, leftButtonX, leftButtonX + 6);
        rightButtonHoverChecker = new HoverChecker(ARROW_Y, 10 + ARROW_Y, 161 - 6, 161);

        minecraft.textRenderer.drawWithShadow(layerText, 161 - 6 - 2 - minecraft.textRenderer.getWidth(layerText), 2 + ARROW_Y, 0xFFFFFF);
        minecraft.textRenderer.drawWithShadow(TranslationStorage.getInstance().get(recipe.getName()), 0, 3, 0xFFFFFF);
    }

    @Override
    public @Nullable ArrayList<Object> getTooltip(int i, int i1) {
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
            return (ArrayList<Object>) recipe.getDescription();
        }
        return null;
    }

    @Override
    public boolean handleClick(@NotNull Minecraft minecraft, int i, int i1, int i2) {
        if(i2 == 0){
            leftMouseDown = true;
        }
        if(i2 == 1){
            rightMouseDown = true;
        }

        if(leftButtonHoverChecker.isOver(i, i1)){
            currentLayer--;
            if(currentLayer < -1){
                currentLayer = recipe.getLayers().size() - 1;
            }
        }
        if(rightButtonHoverChecker.isOver(i, i1)){
            currentLayer++;
            if(currentLayer > recipe.getLayers().size() - 1){
                currentLayer = -1;
            }
        }
        return false;
    }
}
