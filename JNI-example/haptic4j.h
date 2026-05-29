#pragma once

#include <sys/ioctl.h>
#include <linux/input.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

#include <cstdio>
#include <cstdlib>
#include <cerrno>
#include <iostream>
#include <thread>
#include <condition_variable>
#include <mutex>
#include <optional>
#include <functional>
#include <cstring>
#include <filesystem>

#include "haptic_event.h"



std::optional<std::string> findEventPathByJsNumber(int jsNumber) {
    namespace fs = std::filesystem;
    const std::string jsName = "js" + std::to_string(jsNumber);

    for (const auto& entry : fs::directory_iterator("/sys/class/input")) {
        if (!entry.is_directory()) { continue; }

        const std::string entryName = entry.path().filename();
        if (entryName != jsName) { continue; }

        fs::path devicePath = entry.path() / "device";
        if (!fs::exists(devicePath)) {
            return std::nullopt;
        }

        for (const auto& deviceEntry : fs::directory_iterator(devicePath)) {
            const std::string childName = deviceEntry.path().filename();

            if (childName.starts_with("event")) {
                return "/dev/input/" + childName;
            }
        }
    }

    return std::nullopt;
}

class Rumbler {
    std::jthread thr;
    std::mutex mutex;
    std::condition_variable_any cv;
    std::optional<HapticEvent> pendingRequest;

    void run(std::stop_token stopToken) {
        while (!stopToken.stop_requested()) {
            std::optional<HapticEvent> request;
            {
                std::unique_lock lock(mutex);
                cv.wait(lock, stopToken, [&]() {
                    return pendingRequest.has_value();
                });

                if (stopToken.stop_requested()) {
                    return;
                }

                request = std::move(pendingRequest);
                pendingRequest.reset();
            }

            execute(*request);
        }
    }

    void execute(const HapticEvent& req) {
        struct input_event event {};
        event.type = EV_FF;

        struct ff_effect effect {};
        effect.type = FF_RUMBLE;
        effect.id = -1;
        effect.u.rumble.strong_magnitude = req.strong;
        effect.u.rumble.weak_magnitude = req.weak;
        effect.replay.length = req.duration;
        effect.replay.delay = 0;
        auto callback = req.callback; // might change when we awake

        // auto device_file_name = findEventPathByJsNumber(req.joyDevice);
        // if (!device_file_name.has_value()) {
        //     std::cerr << "could not find events for device "
        //               << req.joyDevice
        //               << std::endl;
        //     return;
        // }

        // int fd = open(device_file_name->c_str(), O_RDWR);
        // if (fd == -1) {
        //     std::cerr << "could not open "
        //               << *device_file_name
        //               << ": "
        //               << strerror(errno)
        //               << std::endl;
        //     return;
        // }
        
        // std::cout << "Uploading effect..." << std::endl;

        // if (ioctl(fd, EVIOCSFF, &effect) == -1) {
        //     std::cerr << "EVIOCSFF failed: "
        //               << strerror(errno)
        //               << std::endl;
        //     close(fd);
        //     return;
        // }
        // std::cout << "id=" << effect.id << '\n';

        // event.code = effect.id;
        // event.value = 1;

        // if (write(fd, &event, sizeof(event)) == -1) {
        //     std::cerr << "Failed to start rumble: "
        //               << strerror(errno)
        //               << std::endl;
        //     ioctl(fd, EVIOCRMFF, effect.id);
        //     close(fd);
        //     return;
        // }

        std::this_thread::sleep_for(
            std::chrono::milliseconds(req.duration)
        );

        event.value = 0;

        // if (write(fd, &event, sizeof(event)) == -1) {
        //     std::cerr << "Failed to stop rumble: "
        //               << strerror(errno)
        //               << std::endl;
        // }

        // ioctl(fd, EVIOCRMFF, effect.id);
        // close(fd);
        if (nullptr != callback) {
            callback();
        }
    }

public:

    Rumbler() : thr([this](std::stop_token st) { run(st); }) {}
    
    ~Rumbler() {
        thr.request_stop();
        cv.notify_all();
    }

    void rumble(const HapticEvent& event) {
        {
            std::lock_guard lock(mutex);
            pendingRequest = event;
        }
        cv.notify_one();
    }
};

Rumbler& get_rumbler() {
	static Rumbler r{};
	return r;
}
