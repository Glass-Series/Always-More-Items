import java.net.URI

plugins {
	id("maven-publish")
	id("fabric-loom") version "1.10.5"
	id("babric-loom-extension") version "1.10.1"
	id("io.freefair.lombok") version "8.6"
}

project.java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

base.archivesName.set(project.properties["archives_base_name"] as String)
version = project.properties["mod_version"]!!
group = project.properties["maven_group"]!!

repositories {
	maven(url = "https://maven.glass-launcher.net/releases")
	maven(url = "https://maven.glass-launcher.net/snapshots")
	maven(url = "https://maven.minecraftforge.net/")
	maven(url = "https://jitpack.io")

	// Used for another StationAPI dependency
	exclusiveContent {
		forRepository {
			maven (
//				name = "Modrinth"
				url = "https://api.modrinth.com/maven"
			)
		}
		filter {
			includeGroup("maven.modrinth")
		}
	}
	mavenCentral()
}

loom {
	accessWidenerPath = file("src/main/resources/alwaysmoreitems.accesswidener")

	runs {
		register("testClient") {
			source("test")
			client()
		}
		register("testServer") {
			source("test")
			server()
		}
	}
}

dependencies {
	// To change the versions see the gradle.properties file
	minecraft("com.mojang:minecraft:${project.properties["minecraft_version"]}")
	mappings("net.glasslauncher:biny:${project.properties["yarn_mappings"]}:v2")
	modImplementation("net.fabricmc:fabric-loader:${project.properties["loader_version"]}")

	implementation("org.slf4j:slf4j-api:1.8.0-beta4")
	implementation("org.apache.logging.log4j:log4j-slf4j18-impl:2.17.2")

	modImplementation(transitiveImplementation("net.modificationstation:StationAPI:${project.properties["stapi_version"]}") as Dependency)

	modImplementation("net.glasslauncher.mods:ModMenu:${project.properties["modmenu_version"]}") {
		isTransitive = false
	}
	
	modImplementation("net.glasslauncher.mods:glass-networking:${project.properties["glass_networking_version"]}") {
		isTransitive = false
	}

	modImplementation("net.glasslauncher.mods:GlassConfigAPI:${project.properties["gcapi_version"]}") {
		isTransitive = false
	}

	// Needed despite GCAPI cause we directly use some yaml classes.
	implementation("me.carleslc:Simple-Yaml:1.8.4")
}

tasks.withType<ProcessResources> {
	inputs.property("version", project.properties["version"])

	filesMatching("fabric.mod.json") {
		expand(mapOf("version" to project.properties["version"]))
	}
}

// ensure that the encoding is set to UTF-8, no matter what the system default is
// this fixes some edge cases with special characters not displaying correctly
// see http://yodaconditions.net/blog/fix-for-java-file-encoding-problems-with-gradle.html
tasks.withType<JavaCompile> {
	// Loom and StAPI requires J17.
	options.release = 17
	options.encoding = "UTF-8"
}

java {
	// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
	// if it is present.
	// If you remove this line, sources will not be generated.
	withSourcesJar()
}

tasks.withType<Jar> {
	from("LICENSE") {
		rename { "${it}_${project.properties["archivesBaseName"]}" }
	}
}

// configure the maven publication
publishing {
	publications {
		register("mavenJava", MavenPublication::class) {
			from(components["java"])
		}
	}

	repositories {
		mavenLocal()
		if (project.hasProperty("glass_maven_username")) {
			maven {
				url = URI("https://maven.glass-launcher.net/releases")
				credentials {
					username = "${project.properties["glass_maven_username"]}"
					password = "${project.properties["glass_maven_password"]}"
				}
			}
		}
	}
}
