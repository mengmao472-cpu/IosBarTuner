package com.iosbar.tuner;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.android.systemui")) return;

        try {
            String className = "com.oplus.systemui.navigationbar.gesture.sidegesture.OplusNavigationHandle";
            Class<?> clazz = XposedHelpers.findClass(className, lpparam.classLoader);

            XposedHelpers.findAndHookMethod(clazz, "onDraw", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Object handle = param.thisObject;

                    // 🛠️ 1. 底部边距 (像素值，8-14之间比较好看)
                    XposedHelpers.setIntField(handle, "mHandleBottom", 12); 

                    // 🛠️ 2. 高度 (22是默认值，不要改)
                    XposedHelpers.setIntField(handle, "mHeight", 22);

                    // 🛠️ 3. 宽度 (解决“变短”的核心！630是原版，750、800会明显变长)
                    XposedHelpers.setIntField(handle, "mWidth", 780);
                    
                    // 🛠️ 4. 圆角
                    XposedHelpers.setIntField(handle, "mRadius", 11);
                }
            });
        } catch (Throwable t) {
            XposedBridge.log("IosBarTuner Error: " + t.getMessage());
        }
    }
}
