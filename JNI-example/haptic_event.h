#pragma once

#ifdef __cplusplus
extern "C" {
#endif

struct HapticEvent {
    int joyDevice;
    int strong;
    int weak;
    int duration;
    void (*callback)(void);
};

#ifdef __cplusplus
}
#endif
