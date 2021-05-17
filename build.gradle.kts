import dev.nokee.platform.jni.JniLibraryDependencies

plugins {
    java
    id("dev.nokee.jni-library")
    id("dev.nokee.objective-cpp-language")
}

fun MinimalExternalModuleDependency.dependencyNotation() =
    "${module.group}:${module.name}:${versionConstraint.requiredVersion}"

fun JniLibraryDependencies.jvmLibImplementation(notation: Provider<MinimalExternalModuleDependency>) {
    jvmImplementation(notation.map { it.dependencyNotation() }.get())
}

fun JniLibraryDependencies.nativeLibImplementation(notation: Provider<MinimalExternalModuleDependency>) {
    nativeImplementation(notation.map { it.dependencyNotation() }.get())
}

fun JniLibraryDependencies.nativeLibImplementation(
    notation: Provider<MinimalExternalModuleDependency>,
    action: Action<in ModuleDependency>
) {
    nativeImplementation(notation.map { it.dependencyNotation() }.get(), action)
}

fun ModuleDependencyCapabilitiesHandler.requireLibCapability(notation: Provider<MinimalExternalModuleDependency>) {
    requireCapabilities(notation.get().dependencyNotation())
}

library {
    targetMachines.addAll(machines.macOS.x86_64)
    dependencies {
        jvmLibImplementation(libs.nullabilityAnnotations)
        nativeLibImplementation(libs.macos.appKit)
        nativeLibImplementation(libs.macos.cocoa)
        nativeLibImplementation(libs.macos.javaVM.base) {
            capabilities {
                requireLibCapability(libs.macos.javaVM.capability.javaNativeFoundation)
            }
        }
    }
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
