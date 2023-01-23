SUMMARY = "Swift toolchain for Linux"
HOMEPAGE = "https://swift.org/download/#releases"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${S}/usr/share/swift/LICENSE.txt;md5=f6c482a0548ea60d6c2e015776534035"

require swift-version.inc
PV = "${SWIFT_VERSION}"

SRC_DIR = "swift-${PV}-RELEASE-ubuntu20.04"
SRC_URI = "https://download.swift.org/swift-${PV}-release/ubuntu2004/swift-${PV}-RELEASE/swift-${PV}-RELEASE-ubuntu20.04.tar.gz"
SRC_URI[sha256sum] = "583b7a26bae1fb7c00d046be1dd31a43ae7062fd9384e947fc8c69654daa9411"

DEPENDS = "curl"
RDEPENDS_${PN} = "ncurses-native"
 
S = "${WORKDIR}/${SRC_DIR}"

inherit native

########################################################################
# This informs bitbake that we want to install a non-default directory #
# in the native sysroot.                                               #
#                                                                      #
# We install the swift toolchain into opt to avoid conflicts with      #
# other packages when installing. Ex:                                  #
#                                                                      #
#   The file /usr/include/unicode/sortkey.h is installed by both       #
#   swift-native and icu-native, aborting                              #
#                                                                      #
########################################################################
SYSROOT_DIRS_NATIVE += "${base_prefix}/opt"

do_install_append () {
    install -d ${D}${base_prefix}/opt/usr/bin/
    cp -r ${S}/usr/bin/* ${D}${base_prefix}/opt/usr/bin/

    install -d ${D}${bindir}/
    ln -s ../../opt/usr/bin ${D}${bindir}/swift-tools

    install -d ${D}${base_prefix}/opt/usr/lib/
    cp -r ${S}/usr/lib/* ${D}${base_prefix}/opt/usr/lib/

    install -d ${D}${base_prefix}/opt/usr/include/
    cp -r ${S}/usr/include/* ${D}${base_prefix}/opt/usr/include/

    install -d ${D}${base_prefix}/opt/usr/share/
    cp -r ${S}/usr/share/* ${D}${base_prefix}/opt/usr/share/
}

FILES_${PN} += "${base_prefix}/opt/*"
