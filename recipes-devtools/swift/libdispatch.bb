
SUMMARY = "Libdispatch"
HOMEPAGE = "https://github.com/apple/swift-corelibs-libdispatch"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=1cd73afe3fb82e8d5c899b9d926451d0"

require swift-version.inc
PV = "${SWIFT_VERSION}"

# tag swift-${PV}-RELEASE
SRC_URI = "git://github.com/apple/swift-corelibs-libdispatch.git;protocol=https;nobranch=1;rev=b602cbb26c5cee1aac51021aa2cd6a30a03b1bd3"

DEPENDS = "swift-stdlib ncurses"

S = "${WORKDIR}/git"

inherit swift-cmake-base

TARGET_LDFLAGS += "-L${STAGING_DIR_TARGET}/usr/lib/swift/linux"

# Enable Swift parts
EXTRA_OECMAKE += "-DENABLE_SWIFT=YES"

# Ensure the right CPU is targeted
cmake_do_generate_toolchain_file:append() {
    sed -i 's/set([ ]*CMAKE_SYSTEM_PROCESSOR .*[ ]*)/set(CMAKE_SYSTEM_PROCESSOR ${TARGET_CPU_NAME})/' ${WORKDIR}/toolchain.cmake
}

do_install:append() {
    # Copy cmake build modules
    mkdir -p ${D}${libdir}/swift/dispatch/cmake
    cp -rf ${WORKDIR}/build/cmake/modules/* ${D}${libdir}/swift/dispatch/cmake/
}

FILES:${PN} = "${libdir}/swift/*"
INSANE_SKIP:${PN} = "file-rdeps"
