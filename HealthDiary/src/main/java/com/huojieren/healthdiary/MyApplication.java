package com.huojieren.healthdiary;

import android.app.Application;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

public class MyApplication extends Application {
    private static MyApplication mApp;
    public String foodIcon;

    public static MyApplication getInstance() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        // 初始化食物 icon
        foodIcon = "🍕🍔🍟🌭🍿🧂🥓🥚🍳🧇🥞🧈🫓🥖🥯🥨🥐🍞🧀🥗🥙🥪🌮🌯🍠🥩🍗🍖🥫🫔" +
                "🥟🥠🥡🍱🍘🍙🍤🍣🦪🍜🍛🍚🍥🥮🍢🧆🥘🍲🍧🍦🥧🥣🍝🫕🍨🍩🍪🎂🍰🧁🍯🍮🍡🍭🍬" +
                "🍫🍼🥛🧃☕🫖🍵🍹🍸🍷🍾🍶🧉🍺🍻🥂🥃🫗🧊🥄🍴🍽️🥢🧋🥤🏺🥝🥥🍇🍈🍉🥭🍍🍌🍋‍🟩" +
                "🍋🍊🍎🍏🍐🍑🍒🍓🌶️🌽🍆🫒🍅🫐🫑🍄🥑🥒🥬🥦🫚🌰🥕🧅🧄🥔🫛🍄‍🟫🥜🫘💐🌸🌷🌼🌻" +
                "🌺🌹🏵️🥀☘️🌱🪴🌲🍀🌿🌾🌵🌴🌳🍁🍂🍃🪹🪺";
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
