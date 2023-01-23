SUMMARY = "Swift toolchain for Linux"
HOMEPAGE = "https://swift.org/download/#releases"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${S}/usr/share/swift/LICENSE.txt;md5=f6c482a0548ea60d6c2e015776534035"

require swift-version.inc
PV = "${SWIFT_VERSION}"

SRC_DIR = "swift-${PV}-RELEASE-ubuntu20.04-aarch64"
SRC_URI = "https://download.swift.org/swift-${PV}-release/ubuntu2004-aarch64/swift-${PV}-RELEASE/swift-${PV}-RELEASE-ubuntu20.04-aarch64.tar.gz"
SRC_URI[sha256sum] = "af006a81d966fe08f5472c3cab81892d721a888189d4e11a1fe1336300c9345b"

DEPENDS = "curl"
RDEPENDS_${PN} = "libedit libxml2 ncurses sqlite3 util-linux-libuuid"

S = "${WORKDIR}/${SRC_DIR}"

do_install () {
    cp -r ${S}/usr ${D}/
    chown -R root:root ${D}/usr

    # XXX: fake symlinks to point ncurses5
    mkdir -p ${D}${base_libdir}
    ln -sf libncurses.so.5.9 ${D}${base_libdir}/libncurses.so.6
    ln -sf libtinfo.so.5.9 ${D}${base_libdir}/libtinfo.so.6

    mkdir -p ${D}${libdir}
    ln -sf libform.so.5.9 ${D}${libdir}/libform.so.6 
    ln -sf libpanel.so.5.9 ${D}${libdir}/libpanel.so.6
    ln -sf libedit.so.0.0.64 ${D}${libdir}/libedit.so.2

    # XXX: fix dev-elf check
    mv ${D}${libdir}/libswiftDemangle.so ${D}${libdir}/libswiftDemangle.so.1
    ln -sf libswiftDemangle.so.1 ${D}${libdir}/libswiftDemangle.so

    mv ${D}${libdir}/libsourcekitdInProc.so ${D}${libdir}/libsourcekitdInProc.so.1
    ln -sf libsourcekitdInProc.so.1 ${D}${libdir}/libsourcekitdInProc.so

}

FILES_${PN} += "${base_libdir} /usr"

COMPATIBLE_MACHINE = "(aarch64)"

INSANE_SKIP_${PN} = "file-rdeps staticdev dev-so"
