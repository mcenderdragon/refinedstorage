package com.raoulvdberge.refinedstorage.tile.grid;

import com.raoulvdberge.refinedstorage.RSTiles;
import com.raoulvdberge.refinedstorage.api.network.grid.IGrid;
import com.raoulvdberge.refinedstorage.apiimpl.network.node.NetworkNodeGrid;
import com.raoulvdberge.refinedstorage.screen.BaseScreen;
import com.raoulvdberge.refinedstorage.screen.grid.GuiGrid;
import com.raoulvdberge.refinedstorage.tile.TileNode;
import com.raoulvdberge.refinedstorage.tile.config.IType;
import com.raoulvdberge.refinedstorage.tile.data.TileDataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class TileGrid extends TileNode<NetworkNodeGrid> {
    public static final TileDataParameter<Integer, TileGrid> VIEW_TYPE = new TileDataParameter<>(DataSerializers.VARINT, 0, t -> t.getNode().getViewType(), (t, v) -> {
        if (IGrid.isValidViewType(v)) {
            t.getNode().setViewType(v);
            t.getNode().markDirty();
        }
    }, (initial, p) -> trySortGrid(initial));
    public static final TileDataParameter<Integer, TileGrid> SORTING_DIRECTION = new TileDataParameter<>(DataSerializers.VARINT, 0, t -> t.getNode().getSortingDirection(), (t, v) -> {
        if (IGrid.isValidSortingDirection(v)) {
            t.getNode().setSortingDirection(v);
            t.getNode().markDirty();
        }
    }, (initial, p) -> trySortGrid(initial));
    public static final TileDataParameter<Integer, TileGrid> SORTING_TYPE = new TileDataParameter<>(DataSerializers.VARINT, 0, t -> t.getNode().getSortingType(), (t, v) -> {
        if (IGrid.isValidSortingType(v)) {
            t.getNode().setSortingType(v);
            t.getNode().markDirty();
        }
    }, (initial, p) -> trySortGrid(initial));
    public static final TileDataParameter<Integer, TileGrid> SEARCH_BOX_MODE = new TileDataParameter<>(DataSerializers.VARINT, 0, t -> t.getNode().getSearchBoxMode(), (t, v) -> {
        if (IGrid.isValidSearchBoxMode(v)) {
            t.getNode().setSearchBoxMode(v);
            t.getNode().markDirty();
        }
    }, (initial, p) -> BaseScreen.executeLater(GuiGrid.class, grid -> grid.getSearchField().setMode(p)));
    public static final TileDataParameter<Integer, TileGrid> SIZE = new TileDataParameter<>(DataSerializers.VARINT, 0, t -> t.getNode().getSize(), (t, v) -> {
        if (IGrid.isValidSize(v)) {
            t.getNode().setSize(v);
            t.getNode().markDirty();
        }
    }, (initial, p) -> BaseScreen.executeLater(GuiGrid.class, BaseScreen::init));
    public static final TileDataParameter<Integer, TileGrid> TAB_SELECTED = new TileDataParameter<>(DataSerializers.VARINT, 0, t -> t.getNode().getTabSelected(), (t, v) -> {
        t.getNode().setTabSelected(v == t.getNode().getTabSelected() ? -1 : v);
        t.getNode().markDirty();
    }, (initial, p) -> BaseScreen.executeLater(GuiGrid.class, grid -> grid.getView().sort()));
    public static final TileDataParameter<Integer, TileGrid> TAB_PAGE = new TileDataParameter<>(DataSerializers.VARINT, 0, t -> t.getNode().getTabPage(), (t, v) -> {
        if (v >= 0 && v <= t.getNode().getTotalTabPages()) {
            t.getNode().setTabPage(v);
            t.getNode().markDirty();
        }
    });
    public static final TileDataParameter<Boolean, TileGrid> OREDICT_PATTERN = new TileDataParameter<>(DataSerializers.BOOLEAN, false, t -> t.getNode().isOredictPattern(), (t, v) -> {
        t.getNode().setOredictPattern(v);
        t.getNode().markDirty();
    }, (initial, p) -> BaseScreen.executeLater(GuiGrid.class, grid -> grid.updateOredictPattern(p)));
    public static final TileDataParameter<Boolean, TileGrid> PROCESSING_PATTERN = new TileDataParameter<>(DataSerializers.BOOLEAN, false, t -> t.getNode().isProcessingPattern(), (t, v) -> {
        t.getNode().setProcessingPattern(v);
        t.getNode().clearMatrix();
        t.getNode().markDirty();
    }, (initial, p) -> BaseScreen.executeLater(GuiGrid.class, BaseScreen::init));
    public static final TileDataParameter<Integer, TileGrid> PROCESSING_TYPE = IType.createParameter((initial, p) -> BaseScreen.executeLater(GuiGrid.class, BaseScreen::init));

    public static void trySortGrid(boolean initial) {
        if (!initial) {
            BaseScreen.executeLater(GuiGrid.class, grid -> grid.getView().sort());
        }
    }

    public TileGrid() {
        super(RSTiles.GRID);

        dataManager.addWatchedParameter(VIEW_TYPE);
        dataManager.addWatchedParameter(SORTING_DIRECTION);
        dataManager.addWatchedParameter(SORTING_TYPE);
        dataManager.addWatchedParameter(SEARCH_BOX_MODE);
        dataManager.addWatchedParameter(SIZE);
        dataManager.addWatchedParameter(TAB_SELECTED);
        dataManager.addWatchedParameter(TAB_PAGE);
        dataManager.addWatchedParameter(OREDICT_PATTERN);
        dataManager.addWatchedParameter(PROCESSING_PATTERN);
        dataManager.addWatchedParameter(PROCESSING_TYPE);
    }

    @Override
    @Nonnull
    public NetworkNodeGrid createNode(World world, BlockPos pos) {
        return new NetworkNodeGrid(world, pos);
    }

    @Override
    public String getNodeId() {
        return NetworkNodeGrid.ID;
    }

    /* TODO
    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable Direction side) {
        return (getNode().getGridType() == GridType.PATTERN && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) || super.hasCapability(capability, side);
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
        if (getNode().getGridType() == GridType.PATTERN && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getNode().getPatterns());
        }

        return super.getCapability(capability, side);
    }*/
}
