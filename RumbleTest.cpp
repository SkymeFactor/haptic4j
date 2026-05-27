#include "RumbleTest.h"
#include "rumble-test.h"


JNIEXPORT void JNICALL Java_RumbleTest_rumble(JNIEnv *, jclass,
                                              jint joyDevice, jint strong,
                                              jint weak, jint duration) {
    auto& rumbler = get_rumbler();
    rumbler.rumble(joyDevice, strong, weak, duration);
}
