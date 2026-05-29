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

#include "haptic4j.h"


int main(int argc, char **argv) {
	constexpr auto timer = 1000;

	auto& rumbler = get_rumbler();

	rumbler.rumble({0, 0xffff, 0x0000, timer, []{ std::cout << "fuf\n"; }});
	rumbler.rumble({0, 0xffff, 0x0000, timer, []{ std::cout << "fuf\n"; }});
	
	std::this_thread::sleep_for(
		std::chrono::milliseconds(timer)
	);

	return 0;
}
