DESCRIPTION = "swift 5.6.1 test application"
LICENSE = "CLOSED"

SRC_URI = "file://Sources/hello-world/main.swift \
           file://Package.swift \
"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 */release/hello-world ${D}${bindir}
}

FILES:${PN} = "${bindir}"

inherit swift
