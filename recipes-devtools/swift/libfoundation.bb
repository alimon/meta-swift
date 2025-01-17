SUMMARY = "The Foundation framework defines a base layer of functionality that is required for almost all applications."
HOMEPAGE = "https://github.com/apple/swift-corelibs-foundation"

LICENSE = "Apache-2.0" 
LIC_FILES_CHKSUM = "file://LICENSE;md5=1cd73afe3fb82e8d5c899b9d926451d0"

require swift-version.inc
PV = "${SWIFT_VERSION}"

# tag swift-${PV}-RELEASE
SRC_URI = "git://github.com/apple/swift-corelibs-foundation.git;protocol=https;nobranch=1;rev=a87d185cecfc50086a592852bae223d5ec214cea"

S = "${WORKDIR}/git"

DEPENDS = "swift-stdlib libdispatch ncurses libxml2 icu curl"
RDEPENDS_${PN} += "swift-stdlib libdispatch"

inherit swift-cmake-base

TARGET_LDFLAGS += "-L${STAGING_DIR_TARGET}/usr/lib/swift/linux"

# Enable Swift parts
EXTRA_OECMAKE += "-DENABLE_SWIFT=YES"
EXTRA_OECMAKE += '-DCMAKE_Swift_FLAGS="${SWIFT_FLAGS}"'
EXTRA_OECMAKE += '-DCMAKE_VERBOSE_MAKEFILE=ON'
EXTRA_OECMAKE += '-DCF_DEPLOYMENT_SWIFT=ON'
lcl_maybe_fortify="-D_FORTIFY_SOURCE=0"

EXTRA_OECMAKE+= "-Ddispatch_DIR=${STAGING_DIR_TARGET}/usr/lib/swift/dispatch/cmake"

# Ensure the right CPU is targeted
cmake_do_generate_toolchain_file:append() {
    sed -i 's/set([ ]*CMAKE_SYSTEM_PROCESSOR .*[ ]*)/set(CMAKE_SYSTEM_PROCESSOR ${TARGET_CPU_NAME})/' ${WORKDIR}/toolchain.cmake
}

do_configure:append() {
    # Workaround Dispatch defined with cmake and module
    mkdir -p /tmp/dispatch
	cp -rf ${STAGING_DIR_TARGET}/usr/lib/swift/dispatch/module.modulemap /tmp/dispatch/module.modulemap
    rm -rf ${STAGING_DIR_TARGET}/usr/lib/swift/dispatch/module.modulemap
}

do_install:append() {
    # No need to install the plutil onto the target, so remove it for now
    rm ${D}${bindir}/plutil

    # Since plutil was the only thing in the bindir, remove the bindir as well
    rmdir ${D}${bindir}

    # Restore Dispatch
    cp -rf /tmp/dispatch/module.modulemap ${STAGING_DIR_TARGET}/usr/lib/swift/dispatch/module.modulemap
}

FILES:${PN} = "${libdir}/swift/*"
INSANE_SKIP:${PN} = "file-rdeps"
