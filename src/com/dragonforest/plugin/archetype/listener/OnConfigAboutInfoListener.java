package com.dragonforest.plugin.archetype.listener;

import com.dragonforest.plugin.archetype.model.AboutModel;

public interface OnConfigAboutInfoListener {
    void onFinish(AboutModel aboutModel);
    void onPrevious();
}
