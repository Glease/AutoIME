// windows magic. don't ask
#pragma comment(lib, "imm32")
#include "net_glease_autoime_ImmUtilJNI.h"
#include <windows.h>
#include <imm.h>

static jfieldID fieldIdHWND, fieldIdHIMC;

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    fieldIdHWND = fieldIdHIMC = nullptr;
    return JNI_VERSION_1_2;
}

static void init(JNIEnv *env, jobject obj) {
    if (fieldIdHIMC) return;
    jclass clazz = env->GetObjectClass(obj);
    fieldIdHIMC = env->GetFieldID(clazz, "himc", "J");
    fieldIdHWND = env->GetFieldID(clazz, "hwnd", "J");
}

JNIEXPORT jlong JNICALL Java_net_glease_autoime_ImmUtilJNI_enable0
        (JNIEnv *env, jobject obj) {
    init(env, obj);
    HWND hwnd = (HWND) env->GetLongField(obj, fieldIdHWND);
    HIMC curCtx = ImmGetContext(hwnd);
    if (curCtx) return 0;
    HIMC toSet = (HIMC) env->GetLongField(obj, fieldIdHIMC);
    if (!toSet) {
        toSet = ImmCreateContext();
    }
    HIMC old = ImmAssociateContext(hwnd, toSet);
    env->SetLongField(obj, fieldIdHIMC, (jlong) old);
    return (jlong) old;
}

JNIEXPORT jlong JNICALL Java_net_glease_autoime_ImmUtilJNI_disable0
        (JNIEnv *env, jobject obj) {
    init(env, obj);
    HWND hwnd = (HWND) env->GetLongField(obj, fieldIdHWND);
    HIMC curCtx = ImmGetContext(hwnd);
    if (!curCtx) return 0;
    HIMC old = ImmAssociateContext(hwnd, nullptr);
    env->SetLongField(obj, fieldIdHIMC, (jlong) old);
    return (jlong) old;
}

JNIEXPORT void JNICALL Java_net_glease_autoime_ImmUtilJNI_destroyContext
  (JNIEnv *env, jclass klass, jlong himc) {
    ImmDestroyContext((HIMC) himc);
}
