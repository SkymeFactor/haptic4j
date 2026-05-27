
#include "rumble-test.h"
#include "wrapper.h"

extern "C" void rumble(HapticEvent* event) {
    auto& rumbler = get_rumbler();
    rumbler.rumble(*event);
}
