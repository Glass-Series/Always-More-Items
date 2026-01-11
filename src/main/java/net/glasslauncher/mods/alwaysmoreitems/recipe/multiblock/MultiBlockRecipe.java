package net.glasslauncher.mods.alwaysmoreitems.recipe.multiblock;

import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.util.Identifier;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiBlockRecipe {
    private final Identifier name;
    private final List<Object> description;
    private final String[][] layers;
    private final List<BlockPatternEntry> blockPatterns;

    public MultiBlockRecipe(Identifier name, List<Object> description, String[][] layers, List<BlockPatternEntry> blockPatterns){
        this.name = name;
        this.description = description;
        this.layers = layers;
        this.blockPatterns = blockPatterns;
    }

    public List<BlockPatternEntry> getBlockPatterns() {
        return blockPatterns;
    }

    public String getName(){
        return "multiblock." + this.name.namespace + "." + this.name.path;
    }

    public List<Object> getDescription(){
        return this.description;
    }

    public String[][] getLayers(){
        return layers;
    }

    private int getPatternCount(char pattern){
        int count = 0;
        for(String[] layer : layers){
            for(String section : layer){
                for(char currentPattern : section.toCharArray()){
                    if(currentPattern == pattern){
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private int getPatternCountForLayer(char pattern, int layer){
        int count = 0;
        for(String section : layers[layer]){
            for(char currentPattern : section.toCharArray()){
                if(currentPattern == pattern){
                    count++;
                }
            }
        }
        return count;
    }

    public Map<Integer, List<ItemStack>> getCostPerLayer(){
        Map<Integer, List<ItemStack>> costPerLayer = new HashMap<>();
        for(int layerIndex = -1; layerIndex < layers.length; layerIndex++){
            costPerLayer.put(layerIndex, getCost(layerIndex));
        }
        return costPerLayer;
    }

    public List<ItemStack> getCost(int layer){
        List<ItemStack> cost = new ArrayList<>();

        for(BlockPatternEntry entry : blockPatterns){
            if(entry.item() == null) continue;
            ItemStack stack = entry.item().copy();
            ItemStack existingStack = cost.stream()
                .filter(filterstack -> filterstack.isItemEqual(stack))
                .findFirst().orElse(null);

            if(existingStack != null){
                if(layer == -1){
                    existingStack.count += getPatternCount(entry.key());
                } else {
                    existingStack.count += getPatternCountForLayer(entry.key(), layer);
                }
                break;
            }

            if(layer == -1){
                stack.count = getPatternCount(entry.key());
            } else {
                stack.count = getPatternCountForLayer(entry.key(), layer);
            }
            if(stack.count > 0){
                cost.add(stack);
            }
        }
        cost.sort((a, b) -> Integer.compare(b.count, a.count));
        return cost;
    }

    @Nullable
    public BlockPatternEntry getEntryForPattern(char pattern){
        if(pattern == ' '){
            return new BlockPatternEntry(' ', States.AIR.get(), 0, null, null);
        }
        for(BlockPatternEntry patternEntry : blockPatterns){
            if(patternEntry.key() == pattern){
                return patternEntry;
            }
        }
        return null;
    }

    public List<ItemStack> getItems(){
        List<ItemStack> items = new ArrayList<>();
        for(BlockPatternEntry entry : blockPatterns){
            items.add(entry.item());
        }
        return items;
    }

    public int getStructureWidth(){
        int width = 0;
        for(String[] layer : layers){
            for(String section : layer){
                if(section.length() > width){
                    width = section.length();
                }
            }
        }
        return width;
    }

    public int getStructureHeight(){
        return layers.length;
    }

    public int getStructureDepth(){
        int depth = 0;
        for(String[] layer : layers){
            if(layer.length > depth){
                depth = layer.length;
            }
        }
        return depth;
    }
}
