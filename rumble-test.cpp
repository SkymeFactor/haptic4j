/* very simple test tool for vibrator strength
 *
 * License: LGPL 2.1
 * Copyright: Collabora Ltd.
 * Author: Sebastian Reichel <sebastian.reichel@collabora.co.uk>
 *
 */

#include <stdio.h>
#include <sys/ioctl.h>
#include <linux/input.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdlib.h>
#include <errno.h>

#include "rumble-test.h"


int main(int argc, char **argv) {
	// if (argc != 4) {
	// 	fprintf(stderr, "%s /dev/input/event<num> <strength> <length>\n", argv[0]);
	// 	return 1;
	// }
	constexpr auto timer = 1000;
	auto& rumbler = get_rumbler();
	rumbler.rumble({0, 0xffff, 0x0000, timer, []{ std::cout << "fuf\n"; }});
	rumbler.rumble({0, 0xffff, 0x0000, timer, []{ std::cout << "fuf\n"; }});
	std::this_thread::sleep_for(
            std::chrono::milliseconds(timer)
        );

	// const char * device_file_name = "";
	// unsigned int strength = 0x0001;
	// unsigned short length = 5000;

	// struct input_event event = {
	// 	.type = EV_FF,
	// };
	// struct ff_effect effect = {
	// 	.type = FF_RUMBLE,
	// 	.id = -1,
	// 	.u.rumble.strong_magnitude = 0x0000,
	// 	.u.rumble.weak_magnitude = 0x0000,
	// 	.replay.length = 5000,
	// 	.replay.delay = 0,
	// };
	// int fd = 0, err = 0;

	// device_file_name = argv[1];
	// strength = strtol(argv[2], NULL, 0);
	// length = strtoul(argv[3], NULL, 0);
	// printf("%d\n", length);

	// fd = open(device_file_name, O_RDWR);
	// if (fd == -1) {
	// 	fprintf(stderr, "could not open %s: %d\n", device_file_name, errno);
	// 	return 1;
	// }

	// effect.u.rumble.strong_magnitude = strength;
	// effect.replay.length = length;

	// printf("Upload rumble effect... ");
	// fflush(stdout);

	// err = ioctl(fd, EVIOCSFF, &effect);
	// if (err == -1) {
	// 	printf("failed\n");
	// 	fprintf(stderr, "ioctl error: %d\n", errno);
	// 	return 1;
	// }
	// printf("id=%d\n", effect.id);

	// event.code = effect.id;
	// event.value = 1;

	// err = write(fd, (const void*) &event, sizeof(event));
	// if (err == -1) {
	// 	fprintf(stderr, "failed to start rumble effect (err=%d)\n", errno);
	// 	return 1;
	// }

	// usleep(1000 * (long long)length);

	// event.value = 0;

	// err = write(fd, (const void*) &event, sizeof(event));
	// if (err == -1) {
	// 	fprintf(stderr, "failed to stop rumble effect (err=%d)\n", errno);
	// 	return 1;
	// }

	return 0;
}
