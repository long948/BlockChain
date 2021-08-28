package com.block.xjfkchain.data;

import com.block.xjfkchain.adapter.InComeListAdapter;
import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class Level1Item extends AbstractExpandableItem implements MultiItemEntity {
    public InComeEntity mInComeEntity;

    public Level1Item( InComeEntity mInComeEntity) {
        this.mInComeEntity = mInComeEntity;
    }

    @Override
    public int getItemType() {
        return InComeListAdapter.TYPE_LEVEL_1;
    }

    @Override
    public int getLevel() {
        return 1;
    }

}
