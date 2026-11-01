import net.justmili.dslprops.modId
import net.justmili.dslprops.modVersion

plugins {
    id("dev.kikugie.stonecutter")
    id("dslprops-props")
}

stonecutter active "1.20.1"

// See https://stonecutter.kikugie.dev/wiki/config/params
stonecutter parameters {
    swaps["mod_version"] = "\"$modVersion\";"
    swaps["minecraft"] = "\"${node.metadata.version}\";"
    constants["release"] = modId != "template"
    dependencies["fapi"] = node.project.property("deps.fabric_api") as String

    replacements {
        string(current.parsed >= "1.21.11") {
            replace("ResourceLocation", "Identifier")
        }

        string(current.parsed >= "26.1") {
            replace("accessWidener v2 named", "accessWidener v2 official")
        }
    }
}