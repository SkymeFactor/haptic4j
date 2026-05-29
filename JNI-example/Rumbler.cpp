#include <functional>

#include "Rumbler.h"
#include "haptic_event.h"
#include "haptic4j.h"

JavaVM* g_vm = nullptr;
jclass g_cls = nullptr;

std::function<void()> g_callback = nullptr;

void JNI_OnUnload(JavaVM* vm, void* reserved) {
    JNIEnv* env = nullptr;
    if (JNI_OK != vm->AttachCurrentThread((void**)&env, nullptr)) {
        std::cerr << "Failed to attach to JVM" << std::endl;
        return;
    }
    env->DeleteGlobalRef(g_cls);
}


JNIEXPORT void JNICALL Java_Rumbler_rumble(JNIEnv *env, jclass cls,
                                           jint joyDevice, jint strong,
                                           jint weak, jint duration) {
    auto& rumbler = get_rumbler();

    if (nullptr == g_callback) {
        env->GetJavaVM(&g_vm);
        g_cls = (jclass) env->NewGlobalRef(cls);
        std::stringstream ss;
        ss << "device " << joyDevice << " rumbled for " << duration << " ms";
        std::string msg = ss.str();

        g_callback = [msg] {
            JNIEnv* env = nullptr;
            if (JNI_OK != g_vm->AttachCurrentThread((void**)&env, nullptr)) {
                std::cerr << "Failed to attach to JVM" << std::endl;
                return;
            }
            jmethodID cb = env->GetStaticMethodID(g_cls, "callback", "(Ljava/lang/String;)V");
            if (nullptr == cb) {
                std::cerr << "Failed to find Rumbler.callback method" << std::endl;
                return;
            }
            jstring msg_str = env->NewStringUTF(msg.c_str());
            env->CallStaticVoidMethod(g_cls, cb, msg_str);
            env->DeleteLocalRef(msg_str);
        };
    }

    HapticEvent event{joyDevice, strong, weak, duration, []{g_callback();}};
    rumbler.rumble(event);
}
